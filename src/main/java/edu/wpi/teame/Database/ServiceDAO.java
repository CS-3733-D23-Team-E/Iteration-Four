package edu.wpi.teame.Database;

import edu.wpi.teame.entities.ServiceRequestData;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public abstract class ServiceDAO<E> extends DAO<E> {

  public ServiceDAO(Connection c, String tableName) {
    activeConnection = c;
    table = tableName;
    localCache = new LinkedList<>();
    listenerDAO = new TableListenerDAO(this);
  }

  @Override
  public List<E> getLocalCache() {
    listenerDAO.checkAndInvalidate();

    return localCache;
  }

  abstract List<E> get();

  abstract void add(E obj);

  @Override
  void update(E obj, String attribute, String value) {
    ServiceRequestData srd = (ServiceRequestData) obj;
    int requestID = srd.getRequestID();
    String sql = "";

    try {
      Statement stmt = activeConnection.createStatement();
      sql =
          "UPDATE "
              + table
              + " "
              + "SET \""
              + attribute
              + "\" = '"
              + value
              + "' WHERE \"requestID\" = "
              + requestID
              + ";";
      int result = stmt.executeUpdate(sql);
      if (result < 1)
        System.out.println(
            "There was a problem updating the " + attribute + " of that ServiceRequest");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  void delete(E obj) {
    ServiceRequestData srd = (ServiceRequestData) obj;
    try {
      Statement stmt = activeConnection.createStatement();
      int deleteID = srd.getRequestID();

      String sql =
          "DELETE FROM " + table + " WHERE " + table + ".\"requestID\" = " + deleteID + ";";

      int result = stmt.executeUpdate(sql);

      if (result < 1) System.out.println("There was a problem deleting the ServiceRequest");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
