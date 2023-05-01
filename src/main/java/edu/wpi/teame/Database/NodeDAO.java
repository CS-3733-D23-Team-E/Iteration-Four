package edu.wpi.teame.Database;

import edu.wpi.teame.map.Floor;
import edu.wpi.teame.map.HospitalNode;
import edu.wpi.teame.map.NodeInitializer;
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

public class NodeDAO<E> extends DAO<HospitalNode> {

  public NodeDAO(Connection c) {
    this.activeConnection = c;
    table = "teame.\"Node\"";
    localCache = new LinkedList<>();
    listenerDAO = new TableListenerDAO(this);
  }

  @Override
  public List<HospitalNode> getLocalCache() {
    listenerDAO.checkAndInvalidate();

    return localCache;
  }

  @Override
  List<HospitalNode> get() {
    localCache = new LinkedList<>();

    try {
      Statement stmt = activeConnection.createStatement();
      String sql = "SELECT * FROM " + table + ";";

      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        localCache.add(
            new HospitalNode(
                new NodeInitializer(
                    rs.getInt("nodeID") + "",
                    rs.getInt("xcoord"),
                    rs.getInt("ycoord"),
                    rs.getString("floor"),
                    rs.getString("building"))));
      }
      if (localCache.isEmpty()) System.out.println("There was a problem returning the nodes");
    } catch (SQLException e) {
      throw new RuntimeException("There was a problem retrieving the nodes");
    }
    return localCache;
  }

  @Override
  void update(HospitalNode obj, String attribute, String value) {
    String nodeID = obj.getNodeID();
    String sql = "";

    try {
      Statement stmt = activeConnection.createStatement();

      if (attribute.equals("nodeID") || attribute.equals("xcoord") || attribute.equals("ycoord")) {
        sql =
            "UPDATE "
                + table
                + " SET \""
                + attribute
                + "\" = "
                + value
                + " WHERE \"nodeID\" = "
                + nodeID
                + ";";
      } else {
        sql =
            "UPDATE "
                + table
                + " SET \""
                + attribute
                + "\" = '"
                + value
                + "' WHERE \"nodeID\" = "
                + nodeID
                + ";";
      }

      int result = stmt.executeUpdate(sql);
      if (result < 1) System.out.println("There was a problem updating that value of the node");
    } catch (SQLException e) {
      throw new RuntimeException("There was a problem updating that value of the node");
    }
  }

  @Override
  public void delete(HospitalNode obj) {
    try {
      Statement stmt = activeConnection.createStatement();
      int deletionNode = Integer.parseInt(obj.getNodeID());

      String sql = "DELETE FROM " + table + " WHERE \"nodeID\" = " + deletionNode + ";";

      int result = stmt.executeUpdate(sql);

      if (result < 1) System.out.println("There was a problem deleting the node");
    } catch (SQLException e) {
      throw new RuntimeException("There was a problem deleting the node");
    }
  }

  @Override
  public void add(HospitalNode obj) {
    try {
      String nodeID = obj.getNodeID();
      int xcoord = obj.getXCoord();
      int ycoord = obj.getYCoord();
      String floor = Floor.floorToString(obj.getFloor());
      String building = obj.getBuilding();

      Statement stmt = activeConnection.createStatement();
      String sql =
          "INSERT INTO "
              + table
              + " VALUES("
              + nodeID
              + ","
              + xcoord
              + ","
              + ycoord
              + ",'"
              + floor
              + "','"
              + building
              + "');";
      int result = stmt.executeUpdate(sql);
      if (result < 1) {
        System.out.println("There was a problem inserting the node");
      }
    } catch (SQLException e) {
      throw new RuntimeException("There was a problem inserting the node");
    }
  }

  @Override
  public void importFromCSV(String filePath, String tableName) {
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
                + ","
                + splitL1[1]
                + ","
                + splitL1[2]
                + ",'"
                + splitL1[3]
                + "','"
                + splitL1[4]
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
