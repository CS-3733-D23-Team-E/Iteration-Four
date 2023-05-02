package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.FlowerRequestData;
import edu.wpi.teame.entities.ServiceRequestData;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.junit.jupiter.api.Test;

public class FlowerDAOTest {
  @Test
  public void testGetAddDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    List<FlowerRequestData> flower = SQLRepo.INSTANCE.getFlowerRequestsList();

    FlowerRequestData frd =
        new FlowerRequestData(
            1,
            "joseph",
            "HallNode",
            "2023-04-07",
            "12pm-1pm",
            "Diyar",
            "6",
            "8",
            "4",
            "2",
            "0",
            "0",
            "Testing",
            ServiceRequestData.Status.PENDING);
    SQLRepo.INSTANCE.addServiceRequest(frd);
    //
    //    List<FlowerRequestData> flowerRequestAdded = SQLRepo.INSTANCE.getFlowerRequestsList();
    //    assertEquals(flower.size() + 1, flowerRequestAdded.size());
    //
    //    SQLRepo.INSTANCE.deleteServiceRequest(frd);
    //
    //    List<FlowerRequestData> flowerRequestDeleted = SQLRepo.INSTANCE.getFlowerRequestsList();
    //    assertEquals(flower.size(), flowerRequestDeleted.size());

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  //  @Test
  //  public void testUpdate() {
  //    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);
  //
  //    FlowerRequestData flowerRequest =
  //        new FlowerRequestData(
  //            1,
  //            "joseph",
  //            "Cafe",
  //            "2023-04-07",
  //            "3:12PM",
  //            "Joseph",
  //            "rose",
  //            "2",
  //            "yes",
  //            "i love you babe",
  //            "no package",
  //            FlowerRequestData.Status.IN_PROGRESS);
  //    SQLRepo.INSTANCE.addServiceRequest(flowerRequest);
  //    SQLRepo.INSTANCE.updateServiceRequest(flowerRequest, "status", "DONE");
  //    SQLRepo.INSTANCE.deleteServiceRequest(flowerRequest);
  //
  //    SQLRepo.INSTANCE.exitDatabaseProgram();
  //  }

  @Test
  public void testImportExport() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.FLOWER_REQUESTS, desktopPath, "FlowerService");
    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.FLOWER_REQUESTS, desktopPath + "\\FlowerService");

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
