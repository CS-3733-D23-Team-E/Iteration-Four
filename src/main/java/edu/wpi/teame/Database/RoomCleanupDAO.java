package edu.wpi.teame.Database;

import static java.lang.Integer.parseInt;

import edu.wpi.teame.entities.RoomCleanupData;
import edu.wpi.teame.entities.ServiceRequestData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RoomCleanupDAO<E> extends ServiceDAO<RoomCleanupData> {

  public RoomCleanupDAO(Connection c) {
    super(c, "teame.\"RoomCleanup\"");
  }

  @Override
  void importFromCSV(String filePath, String tableName) {
    try {
      BufferedReader ireader = new BufferedReader(new FileReader(filePath));
      String line;
      List<String> rows = new ArrayList<>();
      while ((line = ireader.readLine()) != null) {
        rows.add(line);
      }
      rows.remove(0);
      ireader.close();
      Statement stmt = activeConnection.createStatement();

      String sqlDelete = "DELETE FROM teame.\"" + tableName + "\";";
      stmt.execute(sqlDelete);

      for (String l1 : rows) {
        String[] splitL1 = l1.split(",");
        String sql =
            "INSERT INTO "
                + "teame.\""
                + tableName
                + "\""
                + " VALUES ("
                + parseInt(splitL1[0])
                + ",'"
                + splitL1[1]
                + "','"
                + splitL1[2]
                + "','"
                + splitL1[3]
                + "','"
                + splitL1[4]
                + "','"
                + splitL1[5]
                + "','"
                + splitL1[6]
                + "','"
                + splitL1[7]
                + "','"
                + splitL1[8]
                + "'); ";
        stmt.execute(sql);
      }

      System.out.println(
          "Imported " + (rows.size()) + " rows from " + filePath + " to " + tableName);

    } catch (IOException | SQLException e) {
      System.err.println("Error importing from " + filePath + " to " + tableName);
      e.printStackTrace();
    }
  }

  @Override
  List<RoomCleanupData> get() {
    localCache = new LinkedList<>();

    try {
      Statement stmt = activeConnection.createStatement();

      String sql = "SELECT * FROM " + table + ";";

      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        localCache.add(
            new RoomCleanupData(
                rs.getInt("requestID"),
                rs.getString("room"),
                rs.getString("deliveryDate"),
                rs.getString("deliverytime"),
                rs.getString("assignedStaff"),
                rs.getString("severityLevel"),
                rs.getString("cleaningSupplies"),
                rs.getString("restockSupplies"),
                ServiceRequestData.Status.stringToStatus(rs.getString("status"))));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return localCache;
  }

  @Override
  void add(RoomCleanupData obj) {
    // RequestID auto Generated
    String room = obj.getRoom();
    String deliveryDate = obj.getDeliveryDate();
    String severityLevel = obj.getSeverityLevel();
    String cleaningSupplies = obj.getCleaningSupplies();
    String restockSupplies = obj.getRestockSupplies();
    String assignedStaff = obj.getAssignedStaff();
    ServiceRequestData.Status requestStatus = obj.getRequestStatus();
    String deliveryTime = obj.getDeliveryTime();

    String sqlAdd =
        "INSERT INTO "
            + table
            + " VALUES(nextval('serial'), '"
            + room
            + "','"
            + deliveryDate
            + "','"
            + severityLevel
            + "','"
            + cleaningSupplies
            + "','"
            + restockSupplies
            + "','"
            + assignedStaff
            + "','"
            + requestStatus
            + "','"
            + deliveryTime
            + "');";

    Statement stmt;
    try {
      stmt = activeConnection.createStatement();
      stmt.executeUpdate(sqlAdd);
      obj.setRequestID(this.returnNewestRequestID());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
