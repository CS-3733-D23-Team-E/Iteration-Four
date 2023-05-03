package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.MealRequestData;
import edu.wpi.teame.entities.ServiceRequestData;
import java.util.List;
import org.junit.jupiter.api.Test;

public class MealDAOTest {
  @Test
  public void testGetAddDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    List<MealRequestData> flower = SQLRepo.INSTANCE.getMealRequestsList();

    MealRequestData mrd =
        new MealRequestData(
            1,
            "joseph",
            "HallNode",
            "2023-04-07",
            "12pm-1pm",
            "Diyar",
            "2",
            "0",
            "0",
            "0",
            "1",
            "1",
            "Testing Testing Test",
            ServiceRequestData.Status.PENDING);
    SQLRepo.INSTANCE.addServiceRequest(mrd);
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

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    MealRequestData mrd =
        new MealRequestData(
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

    SQLRepo.INSTANCE.addServiceRequest(mrd);
    System.out.println(mrd.getRequestID());
    SQLRepo.INSTANCE.updateServiceRequest(mrd, "status", "DONE");
    // SQLRepo.INSTANCE.deleteServiceRequest(mrd);

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
  //    String tableName = "MealService";
  //
  //    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.MEAL_REQUESTS, desktopPath, tableName);
  //    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.MEAL_REQUESTS, desktopPath + "\\" + tableName);
  //
  //    SQLRepo.INSTANCE.exitDatabaseProgram();
  //  }
}
