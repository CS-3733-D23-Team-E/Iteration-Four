package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.FurnitureRequestData;
import edu.wpi.teame.entities.ServiceRequestData;
import java.util.List;
import org.junit.jupiter.api.Test;

public class FurnitureDAOTest {
  @Test
  public void testGetAddDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    List<FurnitureRequestData> furniture = SQLRepo.INSTANCE.getFurnitureRequestsList();

    FurnitureRequestData ofd =
        new FurnitureRequestData(
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
            "1",
            "Testing",
            ServiceRequestData.Status.PENDING);
    SQLRepo.INSTANCE.addServiceRequest(ofd);

    //    List<FurnitureRequestData> furnitureRequestAdded =
    // SQLRepo.INSTANCE.getFurnitureRequestsList();
    //    assertEquals(furniture.size() + 1, furnitureRequestAdded.size());
    //
    //    SQLRepo.INSTANCE.deleteServiceRequest(frd);
    //    List<FurnitureRequestData> furnitureRequestDeleted =
    //        SQLRepo.INSTANCE.getFurnitureRequestsList();
    //    assertEquals(furnitureRequestDeleted.size(), furniture.size());

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    FurnitureRequestData ofd =
        new FurnitureRequestData(
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
            "1",
            "Testing",
            ServiceRequestData.Status.PENDING);

    SQLRepo.INSTANCE.addServiceRequest(ofd);
    SQLRepo.INSTANCE.updateServiceRequest(ofd, "status", "DONE");
    // SQLRepo.INSTANCE.deleteServiceRequest(furnitureRequest);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  //  @Test
  //  public void testImportExport() {
  //    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);
  //
  //    FileSystemView view = FileSystemView.getFileSystemView();
  //    File file = view.getHomeDirectory();
  //    String desktopPath = file.getPath();
  //
  //    String tableName = "FurnitureService";
  //
  //    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.FURNITURE_REQUESTS, desktopPath, tableName);
  //    SQLRepo.INSTANCE.importFromCSV(
  //        SQLRepo.Table.FURNITURE_REQUESTS, desktopPath + "\\" + tableName);
  //
  //    SQLRepo.INSTANCE.exitDatabaseProgram();
  //  }
}
