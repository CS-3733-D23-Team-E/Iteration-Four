package edu.wpi.teame.Database;

import edu.wpi.teame.entities.FurnitureRequestData;
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

public class FurnitureDAO<E> extends ServiceDAO<FurnitureRequestData> {
  public FurnitureDAO(Connection c) {
    super(c, "teame.\"FurnitureRequest\"");
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
                + splitL1[0]
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
                + "','"
                + splitL1[9]
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
  List<FurnitureRequestData> get() {
    localCache = new LinkedList<>();

    try {
      Statement stmt = activeConnection.createStatement();

      String sql = "SELECT * FROM " + table + ";";

      ResultSet rs = stmt.executeQuery(sql);

      int oldID = -1;
      FurnitureRequestData msd = null;
      while (rs.next()) {
        if (rs.getInt("requestID") == oldID) {

          String furnitureSupply = rs.getString("furnitureType");

          switch (furnitureSupply) {
            case "deskChair":
              msd.setDeskChair(rs.getString("quantity"));
              break;
            case "stool":
              msd.setStool(rs.getString("quantity"));
              break;
            case "cot":
              msd.setCot(rs.getString("quantity"));
              break;
            case "filingCabinet":
              msd.setFilingCabinet(rs.getString("quantity"));
              break;
            case "table":
              msd.setTable(rs.getString("quantity"));
              break;
            case "bed":
              msd.setBed(rs.getString("quantity"));
              break;
            default:
              System.out.println("not a valid furnitureType");
              break;
          }
        } else {
          oldID = rs.getInt("requestID");
          msd =
              new FurnitureRequestData(
                  oldID,
                  rs.getString("name"),
                  rs.getString("room"),
                  rs.getString("deliveryDate"),
                  rs.getString("deliveryTime"),
                  rs.getString("assignedStaff"),
                  rs.getString("notes"),
                  ServiceRequestData.Status.stringToStatus(rs.getString("status")));

          String furnitureSupply = rs.getString("furnitureType");

          switch (furnitureSupply) {
            case "deskChair":
              msd.setDeskChair(rs.getString("quantity"));
              break;
            case "stool":
              msd.setStool(rs.getString("quantity"));
              break;
            case "cot":
              msd.setCot(rs.getString("quantity"));
              break;
            case "filingCabinet":
              msd.setFilingCabinet(rs.getString("quantity"));
              break;
            case "table":
              msd.setTable(rs.getString("quantity"));
              break;
            case "bed":
              msd.setBed(rs.getString("quantity"));
              break;
            default:
              System.out.println("not a valid furnitureType");
              break;
          }
        }
        if (!localCache.contains(msd)) {
          localCache.add(msd);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return localCache;
  }

  @Override
  void add(FurnitureRequestData obj) {
    // RequestID auto Generated
    String name = obj.getName();
    String room = obj.getRoom();
    String deliveryDate = obj.getDeliveryDate();
    ServiceRequestData.Status requestStatus = obj.getRequestStatus();
    String deliveryTime = obj.getDeliveryTime();
    String notes = obj.getNotes();
    String staff = obj.getAssignedStaff();

    String sql = "SELECT * FROM nextval('serial');";

    if (!obj.getDeskChair().equals("0")) {
      sql +=
          "INSERT INTO "
              + table
              + " VALUES("
              + "currval('serial'), '"
              + name
              + "','"
              + room
              + "','"
              + deliveryDate
              + "','"
              + deliveryTime
              + "','"
              + staff
              + "','deskChair','"
              + obj.getDeskChair()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getStool().equals("0")) {
      sql +=
          "INSERT INTO "
              + table
              + " VALUES("
              + "currval('serial'), '"
              + name
              + "','"
              + room
              + "','"
              + deliveryDate
              + "','"
              + deliveryTime
              + "','"
              + staff
              + "','stool','"
              + obj.getStool()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getCot().equals("0")) {
      sql +=
          "INSERT INTO "
              + table
              + " VALUES("
              + "currval('serial'), '"
              + name
              + "','"
              + room
              + "','"
              + deliveryDate
              + "','"
              + deliveryTime
              + "','"
              + staff
              + "','cot','"
              + obj.getCot()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getFilingCabinet().equals("0")) {
      sql +=
          "INSERT INTO "
              + table
              + " VALUES("
              + "currval('serial'), '"
              + name
              + "','"
              + room
              + "','"
              + deliveryDate
              + "','"
              + deliveryTime
              + "','"
              + staff
              + "','filingCabinet','"
              + obj.getFilingCabinet()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getTable().equals("0")) {
      sql +=
          "INSERT INTO "
              + table
              + " VALUES("
              + "currval('serial'), '"
              + name
              + "','"
              + room
              + "','"
              + deliveryDate
              + "','"
              + deliveryTime
              + "','"
              + staff
              + "','table','"
              + obj.getTable()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getBed().equals("0")) {
      sql +=
          "INSERT INTO "
              + table
              + " VALUES("
              + "currval('serial'), '"
              + name
              + "','"
              + room
              + "','"
              + deliveryDate
              + "','"
              + deliveryTime
              + "','"
              + staff
              + "','bed','"
              + obj.getBed()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }

    Statement stmt;
    try {
      stmt = activeConnection.createStatement();
      obj.setRequestID(super.returnNewestRequestID());
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
