package edu.wpi.teame.Database;

import static java.lang.Integer.parseInt;

import edu.wpi.teame.entities.OfficeSuppliesData;
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

public class OfficeSuppliesDAO<E> extends ServiceDAO<OfficeSuppliesData> {

  public OfficeSuppliesDAO(Connection c) {
    super(c, "teame.\"OfficeSupplies\"");
  }

  @Override
  List<OfficeSuppliesData> get() {
    localCache = new LinkedList<>();

    try {
      Statement stmt = activeConnection.createStatement();

      String sql = "SELECT * FROM " + table + ";";

      ResultSet rs = stmt.executeQuery(sql);
      int oldID = -1;
      OfficeSuppliesData mrd = null;
      while (rs.next()) {
        if (rs.getInt("requestID") == oldID) {

          String officeSupply = rs.getString("officeSupply");

          switch (officeSupply) {
            case "pen":
              mrd.setPen(rs.getString("quantity"));
              break;
            case "pencil":
              mrd.setPencil(rs.getString("quantity"));
              break;
            case "ruler":
              mrd.setRuler(rs.getString("quantity"));
              break;
            case "tape":
              mrd.setTape(rs.getString("quantity"));
              break;
            case "holepuncher":
              mrd.setHolepuncher(rs.getString("quantity"));
              break;
            case "stapler":
              mrd.setStapler(rs.getString("quantity"));
              break;
            default:
              System.out.println("not a valid office supply item");
              break;
          }
        } else {
          oldID = rs.getInt("requestID");
          mrd =
              new OfficeSuppliesData(
                  oldID,
                  rs.getString("name"),
                  rs.getString("room"),
                  rs.getString("deliveryDate"),
                  rs.getString("deliveryTime"),
                  rs.getString("assignedStaff"),
                  rs.getString("notes"),
                  ServiceRequestData.Status.stringToStatus(rs.getString("status")));

          String officeSupply = rs.getString("officeSupply");

          switch (officeSupply) {
            case "pen":
              mrd.setPen(rs.getString("quantity"));
              break;
            case "pencil":
              mrd.setPencil(rs.getString("quantity"));
              break;
            case "ruler":
              mrd.setRuler(rs.getString("quantity"));
              break;
            case "tape":
              mrd.setTape(rs.getString("quantity"));
              break;
            case "holepuncher":
              mrd.setHolepuncher(rs.getString("quantity"));
              break;
            case "stapler":
              mrd.setStapler(rs.getString("quantity"));
              break;
            default:
              System.out.println("not a valid office supply item");
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

  void add(OfficeSuppliesData obj) {
    // RequestID auto Generated
    String name = obj.getName();
    String room = obj.getRoom();
    String deliveryDate = obj.getDeliveryDate();
    ServiceRequestData.Status requestStatus = obj.getRequestStatus();
    String deliveryTime = obj.getDeliveryTime();
    String notes = obj.getNotes();
    String staff = obj.getAssignedStaff();

    String sql = "SELECT * FROM nextval('serial');";

    if (!obj.getPen().equals("0")) {
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
              + "','pen','"
              + obj.getPen()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getPencil().equals("0")) {
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
              + "','pencil','"
              + obj.getPencil()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getRuler().equals("0")) {
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
              + "','ruler','"
              + obj.getRuler()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getTape().equals("0")) {
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
              + "','tape','"
              + obj.getTape()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getHolepuncher().equals("0")) {
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
              + "','holepuncher','"
              + obj.getHolepuncher()
              + "','"
              + notes
              + "','"
              + requestStatus
              + "');";
    }
    if (!obj.getStapler().equals("0")) {
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
              + "','stapler','"
              + obj.getStapler()
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
