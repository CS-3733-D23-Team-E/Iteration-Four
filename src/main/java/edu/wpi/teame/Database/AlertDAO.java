package edu.wpi.teame.Database;

import edu.wpi.teame.entities.AlertData;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AlertDAO<E> extends DAO<AlertData> {

  public AlertDAO(Connection c) {
    activeConnection = c;
    table = "teame.\"Alert\"";
    localCache = new LinkedList<>();
    listenerDAO = new TableListenerDAO(this);
  }

  @Override
  public List<AlertData> getLocalCache() {
    listenerDAO.checkAndInvalidate();

    return super.getLocalCache();
  }

  @Override
  List<AlertData> get() {
    localCache.clear();

    try {
      Statement stmt = activeConnection.createStatement();
      String sql = "SELECT * FROM " + table + ";";

      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        localCache.add(
            new AlertData(
                rs.getInt("alertID"), rs.getString("message"), rs.getString("timestamp")));
      }
      if (localCache.isEmpty()) System.out.println("No Alerts return");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return localCache;
  }

  @Override
  void update(AlertData obj, String attribute, String value) {
    int alertID = obj.getAlertID();
    String sqlUpdate =
        "UPDATE "
            + table
            + " SET \""
            + attribute
            + "\" = '"
            + value
            + "' WHERE \"alertID\" = "
            + alertID
            + ";";
    try {
      Statement stmt = activeConnection.createStatement();
      stmt.executeUpdate(sqlUpdate);
      stmt.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  void delete(AlertData obj) {
    int alertID = obj.getAlertID();
    String sql = "DELETE FROM " + table + " WHERE \"alertID\" = " + alertID + ";";
    try {
      Statement stmt = activeConnection.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  void add(AlertData obj) {
    try {
      String message = obj.getMessage();
      String timestamp = obj.getTimestamp();

      Statement stmt = activeConnection.createStatement();
      String sql =
          "INSERT INTO "
              + table
              + " VALUES(nextval('serial'), '"
              + message
              + "', '"
              + timestamp
              + "');";
      int result = stmt.executeUpdate(sql);
      if (result < 1) {
        System.out.println("There was a problem inserting the alert");
      }
      obj.setAlertID(returnNewestRequestID());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  void importFromCSV(String filePath, String tableName) {
    try {
      // Load CSV file
      BufferedReader reader = new BufferedReader(new FileReader(filePath));
      String line;
      List<String> rows = new ArrayList<>();
      while ((line = reader.readLine()) != null) {
        rows.add(line);
      }
      rows.remove(0);
      reader.close();
      Statement stmt = activeConnection.createStatement();

      String sqlDelete = "DELETE FROM teame.\"" + tableName + "\";";
      stmt.execute(sqlDelete);

      for (String l1 : rows) {
        String[] splitL1 = l1.split(",");
        String sql =
            "INSERT INTO teame.\""
                + tableName
                + "\""
                + " VALUES ("
                + splitL1[0]
                + ", '"
                + splitL1[1]
                + "', '"
                + splitL1[2]
                + "'); ";
        try {
          stmt.execute(sql);
        } catch (SQLException e) {
          System.out.println("Could not import nodeID " + splitL1[0]);
        }
      }

      System.out.println(
          "Imported " + (rows.size()) + " rows from " + filePath + " to " + tableName);

    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
