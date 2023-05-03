package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.OfficeSuppliesData;
import edu.wpi.teame.entities.RoomCleanupData;
import java.util.List;
import org.junit.jupiter.api.Test;

public class RoomCleanupDAOTest {

  @Test
  public void testGetAddDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    List<RoomCleanupData> RoomCleanup = SQLRepo.INSTANCE.getRoomCleanupList();

    RoomCleanupData ofd =
        new RoomCleanupData(
            1,
            "HallNode",
            "2023-04-07",
            "12pm-1pm",
            "Diyar",
            "8",
            "Paper Towels",
            "Tissues",
            OfficeSuppliesData.Status.PENDING);

    SQLRepo.INSTANCE.addServiceRequest(ofd);
    List<RoomCleanupData> RoomCleanupadded = SQLRepo.INSTANCE.getRoomCleanupList();
    assertEquals(RoomCleanup.size() + 1, RoomCleanupadded.size());

    SQLRepo.INSTANCE.deleteServiceRequest(ofd);
    List<OfficeSuppliesData> RoomCleanupDeleted = SQLRepo.INSTANCE.getOfficeSupplyList();
    assertEquals(RoomCleanupDeleted.size(), RoomCleanup.size());

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    RoomCleanupData ofd =
        new RoomCleanupData(
            1,
            "HallNode",
            "2023-04-07",
            "12pm-1pm",
            "Diyar",
            "8",
            "Paper Towels",
            "Tissues",
            OfficeSuppliesData.Status.PENDING);

    SQLRepo.INSTANCE.addServiceRequest(ofd);
    SQLRepo.INSTANCE.updateServiceRequest(ofd, "status", "DONE");
    SQLRepo.INSTANCE.deleteServiceRequest(ofd);

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
  //    String tableName = "RoomCleanup";
  //
  //    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.ROOMCLEANUP, desktopPath, tableName);
  //    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.ROOMCLEANUP, desktopPath + "\\" + tableName);
  //
  //    SQLRepo.INSTANCE.exitDatabaseProgram();
  //  }
}
