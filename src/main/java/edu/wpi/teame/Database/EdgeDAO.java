package edu.wpi.teame.Database;

import edu.wpi.teame.map.HospitalEdge;
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

public class EdgeDAO<E> extends DAO<HospitalEdge> {

  public EdgeDAO(Connection c) {
    activeConnection = c;
    table = "teame.\"Edge\"";
    localCache = new LinkedList<>();
    listenerDAO = new TableListenerDAO(this);
  }

  @Override
  public List<HospitalEdge> getLocalCache() {
    listenerDAO.checkAndInvalidate();

    return localCache;
  }

  @Override
  List<HospitalEdge> get() {
    localCache = new LinkedList<>();

    try {
      Statement stmt = activeConnection.createStatement();

      String sql = "SELECT \"startNode\", \"endNode\" FROM " + table + ";";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        localCache.add(new HospitalEdge(rs.getString("startNode"), rs.getString("endNode")));
      }
      return localCache;
    } catch (SQLException e) {
      throw new RuntimeException("Something went wrong");
    }
  }

  @Override
  void update(HospitalEdge obj, String attribute, String value) {
    String startNode = obj.getNodeOneID();
    String endNode = obj.getNodeTwoID();
    String sqlUpdate =
        "UPDATE "
            + table
            + " SET \""
            + attribute
            + "\" = "
            + value
            + " WHERE \"endNode\" = "
            + endNode
            + " AND \"startNode\" = "
            + startNode
            + ";";

    try {
      Statement stmt = activeConnection.createStatement();
      stmt.executeUpdate(sqlUpdate);
      stmt.close();
    } catch (SQLException e) {
      System.out.println(
          "Exception: Cannot duplicate two set of the same edges, start and end nodes have to exist (cannot create more ids)");
    }
  }

  @Override
  void delete(HospitalEdge edge) {
    String startNode = edge.getNodeOneID();
    String endNode = edge.getNodeTwoID();
    String sqlDelete =
        "DELETE FROM "
            + table
            + " WHERE \"startNode\" = "
            + startNode
            + " AND \"endNode\" = '"
            + endNode
            + "';";

    try {
      Statement stmt = activeConnection.createStatement();
      stmt.executeUpdate(sqlDelete);
      stmt.close();
    } catch (SQLException e) {
      System.out.println("error deleting");
    }
  }

  @Override
  void add(HospitalEdge edge) {
    String startNode = edge.getNodeOneID();
    String endNode = edge.getNodeTwoID();
    String sqlAdd = "INSERT INTO " + table + " VALUES('" + startNode + "','" + endNode + "');";
    try {
      Statement stmt = activeConnection.createStatement();
      stmt.executeUpdate(sqlAdd);
      stmt.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  void importFromCSV(String filePath, String tableName) {
    try {
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
                + "VALUES ("
                + splitL1[0]
                + ","
                + splitL1[1]
                + "); ";
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
