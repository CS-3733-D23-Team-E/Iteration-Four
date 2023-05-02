package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.OfficeSuppliesData;
import edu.wpi.teame.entities.ServiceRequestData;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.junit.jupiter.api.Test;

public class OfficeSuppliesDAOTest {
  @Test
  public void testGetAddDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    List<OfficeSuppliesData> officeSupply = SQLRepo.INSTANCE.getOfficeSupplyList();

    OfficeSuppliesData ofd =
        new OfficeSuppliesData(
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

    //    List<OfficeSuppliesData> officeSupplyRequestAdded =
    // SQLRepo.INSTANCE.getOfficeSupplyList();
    //    assertEquals(officeSupplyRequestAdded.size(), officeSupply.size() + 1);
    //
    //    SQLRepo.INSTANCE.deleteServiceRequest(ofd);
    //    List<OfficeSuppliesData> officeSupplyRequestDeleted =
    // SQLRepo.INSTANCE.getOfficeSupplyList();
    //    assertEquals(officeSupplyRequestDeleted.size(), officeSupply.size());

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    OfficeSuppliesData ofd =
        new OfficeSuppliesData(
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
    SQLRepo.INSTANCE.deleteServiceRequest(ofd);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testImportExport() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    String tableName = "OfficeSupplies";

    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.OFFICE_SUPPLY, desktopPath, tableName);
    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.OFFICE_SUPPLY, desktopPath + "\\" + tableName);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
