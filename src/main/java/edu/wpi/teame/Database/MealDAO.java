package edu.wpi.teame.Database;

import static java.lang.Integer.parseInt;

import edu.wpi.teame.entities.MealRequestData;
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

public class MealDAO<E> extends ServiceDAO<MealRequestData> {
  public MealDAO(Connection c) {
    super(c, "teame.\"MealService\"");
  }

  @Override
  List<MealRequestData> get() {
    localCache = new LinkedList<>();

    try {
      Statement stmt = activeConnection.createStatement();

      String sql = "SELECT * FROM " + table + ";";

      ResultSet rs = stmt.executeQuery(sql);
      int oldID = -1;
      MealRequestData mrd = null;
      while (rs.next()) {
        if (rs.getInt("requestID") == oldID) {

          String item = rs.getString("item");

          switch (item) {
            case "cheeseburger":
              mrd.setCheeseburgers(rs.getString("quantity"));
              break;
            case "sandwich":
              mrd.setSandwich(rs.getString("quantity"));
              break;
            case "salad":
              mrd.setSalad(rs.getString("quantity"));
              break;
            case "nuggets":
              mrd.setNuggets(rs.getString("quantity"));
              break;
            case "orangejuice":
              mrd.setOrangejuice(rs.getString("quantity"));
              break;
            case "water":
              mrd.setWater(rs.getString("quantity"));
              break;
            default:
              System.out.println("not a valid meal item");
              break;
          }
        } else {
          oldID = rs.getInt("requestID");
          mrd =
              new MealRequestData(
                  oldID,
                  rs.getString("name"),
                  rs.getString("room"),
                  rs.getString("deliveryDate"),
                  rs.getString("deliveryTime"),
                  rs.getString("assignedStaff"),
                  rs.getString("notes"),
                  ServiceRequestData.Status.stringToStatus(rs.getString("status")));

          String item = rs.getString("item");

          switch (item) {
            case "cheeseburger":
              mrd.setCheeseburgers(rs.getString("quantity"));
              break;
            case "sandwich":
              mrd.setSandwich(rs.getString("quantity"));
              break;
            case "salad":
              mrd.setSalad(rs.getString("quantity"));
              break;
            case "nuggets":
              mrd.setNuggets(rs.getString("quantity"));
              break;
            case "orangejuice":
              mrd.setOrangejuice(rs.getString("quantity"));
              break;
            case "water":
              mrd.setWater(rs.getString("quantity"));
              break;
            default:
              System.out.println("not a valid meal item");
              break;
          }
        }
        if (!localCache.contains(mrd)) {
          localCache.add(mrd);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return localCache;
  }

  @Override
  void add(MealRequestData obj) {
    String name = obj.getName();
    String room = obj.getRoom();
    String deliveryDate = obj.getDeliveryDate();
    ServiceRequestData.Status requestStatus = obj.getRequestStatus();
    String deliveryTime = obj.getDeliveryTime();
    String notes = obj.getNotes();
    String staff = obj.getAssignedStaff();

    String sql = "SELECT * FROM nextval('serial');";

    if (!obj.getCheeseburgers().equals("0")) {
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
              + "','cheeseburger','"
              + obj.getCheeseburgers()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getSandwich().equals("0")) {
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
              + "','sandwich','"
              + obj.getSandwich()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getSalad().equals("0")) {
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
              + "','salad','"
              + obj.getSalad()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getNuggets().equals("0")) {
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
              + "','nuggets','"
              + obj.getNuggets()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getOrangejuice().equals("0")) {
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
              + "','orangejuice','"
              + obj.getOrangejuice()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getWater().equals("0")) {
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
              + "','water','"
              + obj.getWater()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    Statement stmt;
    try {
      stmt = activeConnection.createStatement();
      stmt.executeUpdate(sql);
      obj.setRequestID(this.returnNewestRequestID());
    } catch (SQLException e) {
      System.out.println("error adding");
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
