package edu.wpi.teame.Database;

import static java.lang.Integer.parseInt;

import edu.wpi.teame.entities.FlowerRequestData;
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

public class FlowerDAO<E> extends ServiceDAO<FlowerRequestData> {
  public FlowerDAO(Connection c) {
    super(c, "teame.\"FlowerRequest\"");
  }

  @Override
  List<FlowerRequestData> get() {
    localCache = new LinkedList<>();

    try {
      Statement stmt = activeConnection.createStatement();

      String sql = "SELECT * FROM " + table + ";";

      ResultSet rs = stmt.executeQuery(sql);
      int oldID = -1;
      FlowerRequestData frd = null;
      while (rs.next()) {
        if (rs.getInt("requestID") == oldID) {

          String flowerType = rs.getString("flowerType");

          switch (flowerType) {
            case "rose":
              frd.setRose(rs.getString("quantity"));
              break;
            case "tulip":
              frd.setTulip(rs.getString("quantity"));
              break;
            case "sunflower":
              frd.setSunflower(rs.getString("quantity"));
              break;
            case "muscari":
              frd.setMuscari(rs.getString("quantity"));
              break;
            case "lily":
              frd.setLily(rs.getString("quantity"));
              break;
            case "callalily":
              frd.setCallalily(rs.getString("quantity"));
              break;
            default:
              System.out.println("not a valid flower type");
              break;
          }
        } else {
          oldID = rs.getInt("requestID");
          frd =
              new FlowerRequestData(
                  oldID,
                  rs.getString("name"),
                  rs.getString("room"),
                  rs.getString("deliveryDate"),
                  rs.getString("deliveryTime"),
                  rs.getString("assignedStaff"),
                  rs.getString("notes"),
                  ServiceRequestData.Status.stringToStatus(rs.getString("status")));

          String flowerType = rs.getString("flowerType");

          switch (flowerType) {
            case "rose":
              frd.setRose(rs.getString("quantity"));
              break;
            case "tulip":
              frd.setTulip(rs.getString("quantity"));
              break;
            case "sunflower":
              frd.setSunflower(rs.getString("quantity"));
              break;
            case "muscari":
              frd.setMuscari(rs.getString("quantity"));
              break;
            case "lily":
              frd.setLily(rs.getString("quantity"));
              break;
            case "callilily":
              frd.setCallalily(rs.getString("quantity"));
              break;
            default:
              System.out.println("not a valid flower type");
              break;
          }
        }
        if (!localCache.contains(frd)) {
          localCache.add(frd);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return localCache;
  }

  @Override
  void add(FlowerRequestData obj) {
    // RequestID auto Generated
    String name = obj.getName();
    String room = obj.getRoom();
    String deliveryDate = obj.getDeliveryDate();
    ServiceRequestData.Status requestStatus = obj.getRequestStatus();
    String deliveryTime = obj.getDeliveryTime();
    String notes = obj.getNotes();
    String staff = obj.getAssignedStaff();

    String sql = "SELECT * FROM nextval('serial');";

    if (!obj.getRose().equals("0")) {
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
              + "','rose','"
              + obj.getRose()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getTulip().equals("0")) {
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
              + "','tulip','"
              + obj.getTulip()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getSunflower().equals("0")) {
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
              + "','sunflower','"
              + obj.getSunflower()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getMuscari().equals("0")) {
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
              + "','muscari','"
              + obj.getMuscari()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getLily().equals("0")) {
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
              + "','lily','"
              + obj.getLily()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getCallalily().equals("0")) {
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
              + "','callalily','"
              + obj.getCallalily()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    Statement stmt;
    try {
      stmt = activeConnection.createStatement();
      obj.setRequestID(this.returnNewestRequestID());
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
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
}
