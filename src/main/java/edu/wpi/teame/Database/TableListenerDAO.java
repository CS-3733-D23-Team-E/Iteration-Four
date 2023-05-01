package edu.wpi.teame.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import lombok.SneakyThrows;
import org.postgresql.PGConnection;
import org.postgresql.util.PSQLException;

public class TableListenerDAO<E> {
  private DAO<E> dao;

  Connection listenerConnection;
  String tableName;

  @SneakyThrows
  TableListenerDAO(DAO<E> newDAO) {
    this.dao = newDAO;

    listenerConnection = SQLRepo.INSTANCE.connect();
    // listenerConnection = this.dao.getActiveConnection();
    tableName = dao.getTable();

    String table = tableName.substring(6).replaceAll("\"", "");

    listenerConnection
        .prepareStatement(
            "CREATE OR REPLACE FUNCTION notify"
                + table
                + "() RETURNS TRIGGER AS $"
                + table
                + "$"
                + "BEGIN "
                + "NOTIFY "
                + table
                + ";"
                + "RETURN NULL;"
                + // Return value ignored
                "END;$"
                + table
                + "$ language plpgsql")
        .execute();

    listenerConnection
        .prepareStatement(
            "CREATE OR REPLACE TRIGGER "
                + table
                + "Update AFTER UPDATE OR INSERT OR DELETE ON "
                + tableName
                + " FOR EACH STATEMENT EXECUTE FUNCTION notify"
                + table
                + "()")
        .execute();

    // Begin listening on the person table. This means our connection will get notified any time the
    // above
    // trigger is executed
    reListen();

    invalidateTable(); // Load the initial cache state
  }

  @SneakyThrows
  private void reListen() {
    String table = tableName.substring(6).replaceAll("\"", "");
    listenerConnection.prepareStatement("LISTEN " + table).execute();
  }

  private void invalidateTable() {
    // Get all of the people in the DB
    List<E> data = dao.get();

    dao.setLocalCache(new LinkedList<>());

    dao.setLocalCache(data); // Clear all people in the cache
  }

  /** Checks if invalidation is necessary, and invalidates the table if necessary */
  @SneakyThrows
  void checkAndInvalidate() {
    // Get the base driver to check for notifications
    PGConnection driver = listenerConnection.unwrap(PGConnection.class);

    String table = tableName.substring(6).replaceAll("\"", "");

    // Try getting notifications
    // && notifs[0].getName().equals(table.toLowerCase())
    try {
      if (driver.getNotifications().length > 0) {
        invalidateTable();
        System.out.println("The " + table + " has updated");
      }
      // If we get an error, we have timed out, and we MUST re-build the table
      // and connection, as we may have missed an update message
    } catch (PSQLException error) {
      listenerConnection = SQLRepo.INSTANCE.connect();
      reListen();
      invalidateTable();
    }
  }

  public void close() {
    try {
      listenerConnection.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
