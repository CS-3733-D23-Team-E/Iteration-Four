package edu.wpi.teame;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.map.HospitalNode;
import edu.wpi.teame.map.LocationName;

public class Main {

  public static void main(String[] args) {
    SQLRepo.INSTANCE.connectToDatabase("nb", "Nick", SQLRepo.DB.WPI);
    HospitalNode.processNodeList(SQLRepo.INSTANCE.getNodeList());
    HospitalNode.processEdgeList(SQLRepo.INSTANCE.getEdgeList());
    LocationName.processLocationList(SQLRepo.INSTANCE.getLocationList());
    System.out.println(LocationName.allLocations.keySet());
    SQLRepo.INSTANCE.exitDatabaseProgram();
    App.launch(App.class, args);
  }
  // shortcut: psvm
}
