package edu.wpi.teame.Database;

import edu.wpi.teame.entities.MedicalSuppliesData;
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

public class MedicalSuppliesDAO<E> extends ServiceDAO<MedicalSuppliesData> {
  public MedicalSuppliesDAO(Connection c) {
    super(c, "teame.\"MedicalSupplies\"");
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
                + " VALUES ('"
                + splitL1[0]
                + "','"
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
                + "',"
                + splitL1[8]
                + ",'"
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
  List<MedicalSuppliesData> get() {
    localCache = new LinkedList<>();

    try {
      Statement stmt = activeConnection.createStatement();

      String sql = "SELECT * FROM " + table + ";";

      ResultSet rs = stmt.executeQuery(sql);

      int oldID = -1;
      MedicalSuppliesData msd = null;
      while (rs.next()) {
        if (rs.getInt("requestID") == oldID) {

          String medicalSupply = rs.getString("medicalSupply");

          switch (medicalSupply) {
            case "bandaids":
              msd.setBandaids(rs.getString("quantity"));
              break;
            case "surgicalGloves":
              msd.setGloves(rs.getString("quantity"));
              break;
            case "firstAid":
              msd.setFirstAid(rs.getString("quantity"));
              break;
            case "stethoscope":
              msd.setStethoscope(rs.getString("quantity"));
              break;
            case "scalpel":
              msd.setScalpel(rs.getString("quantity"));
              break;
            case "syringe":
              msd.setSyringe(rs.getString("quantity"));
              break;
            default:
              System.out.println("not a valid medicalSupply");
              break;
          }
        } else {
          oldID = rs.getInt("requestID");
          msd =
              new MedicalSuppliesData(
                  oldID,
                  rs.getString("name"),
                  rs.getString("room"),
                  rs.getString("deliveryDate"),
                  rs.getString("deliveryTime"),
                  rs.getString("assignedStaff"),
                  rs.getString("notes"),
                  ServiceRequestData.Status.stringToStatus(rs.getString("status")));

          String medicalSupply = rs.getString("medicalSupply");

          switch (medicalSupply) {
            case "bandaids":
              msd.setBandaids(rs.getString("quantity"));
              break;
            case "surgicalGloves":
              msd.setGloves(rs.getString("quantity"));
              break;
            case "firstAid":
              msd.setFirstAid(rs.getString("quantity"));
              break;
            case "stethoscope":
              msd.setStethoscope(rs.getString("quantity"));
              break;
            case "scalpel":
              msd.setScalpel(rs.getString("quantity"));
              break;
            case "syringe":
              msd.setSyringe(rs.getString("quantity"));
              break;
            default:
              System.out.println("not a valid medicalSupply");
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
  void add(MedicalSuppliesData obj) {
    // RequestID auto Generated
    String name = obj.getName();
    String room = obj.getRoom();
    String deliveryDate = obj.getDeliveryDate();
    ServiceRequestData.Status requestStatus = obj.getRequestStatus();
    String deliveryTime = obj.getDeliveryTime();
    String notes = obj.getNotes();
    String staff = obj.getAssignedStaff();

    String sql = "SELECT * FROM nextval('serial');";

    if (!obj.getBandaids().equals("0")) {
      sql +=
          "INSERT INTO "
              + table
              + " VALUES('"
              + name
              + "','"
              + room
              + "','"
              + deliveryDate
              + "','"
              + deliveryTime
              + "','"
              + staff
              + "','bandaids','"
              + obj.getBandaids()
              + "','"
              + notes
              + "', currval('serial')"
              + ",'"
              + requestStatus
              + "');";
    }
    if (!obj.getSyringe().equals("0")) {
      sql +=
          "INSERT INTO "
              + table
              + " VALUES('"
              + name
              + "','"
              + room
              + "','"
              + deliveryDate
              + "','"
              + deliveryTime
              + "','"
              + staff
              + "','syringe','"
              + obj.getSyringe()
              + "','"
              + notes
              + "', currval('serial')"
              + ",'"
              + requestStatus
              + "');";
    }
    if (!obj.getGloves().equals("0")) {
      sql +=
          "INSERT INTO "
              + table
              + " VALUES('"
              + name
              + "','"
              + room
              + "','"
              + deliveryDate
              + "','"
              + deliveryTime
              + "','"
              + staff
              + "','surgicalGloves','"
              + obj.getGloves()
              + "','"
              + notes
              + "', currval('serial')"
              + ",'"
              + requestStatus
              + "');";
    }
    if (!obj.getScalpel().equals("0")) {
      sql +=
          "INSERT INTO "
              + table
              + " VALUES('"
              + name
              + "','"
              + room
              + "','"
              + deliveryDate
              + "','"
              + deliveryTime
              + "','"
              + staff
              + "','syringe','"
              + obj.getScalpel()
              + "','"
              + notes
              + "', currval('serial')"
              + ",'"
              + requestStatus
              + "');";
    }
    if (!obj.getFirstAid().equals("0")) {
      sql +=
          "INSERT INTO "
              + table
              + " VALUES('"
              + name
              + "','"
              + room
              + "','"
              + deliveryDate
              + "','"
              + deliveryTime
              + "','"
              + staff
              + "','firstAid','"
              + obj.getFirstAid()
              + "','"
              + notes
              + "', currval('serial')"
              + ",'"
              + requestStatus
              + "');";
    }
    if (!obj.getStethoscope().equals("0")) {
      sql +=
          "INSERT INTO "
              + table
              + " VALUES('"
              + name
              + "','"
              + room
              + "','"
              + deliveryDate
              + "','"
              + deliveryTime
              + "','"
              + staff
              + "','stethoscope','"
              + obj.getStethoscope()
              + "','"
              + notes
              + "', currval('serial')"
              + ",'"
              + requestStatus
              + "');";
    }

    Statement stmt;
    try {
      stmt = activeConnection.createStatement();
      stmt.executeUpdate(sql);
      obj.setRequestID(super.returnNewestRequestID());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
