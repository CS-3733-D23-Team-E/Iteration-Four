package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.MedicalSuppliesData;
import edu.wpi.teame.entities.ServiceRequestData;
import java.util.List;
import org.junit.jupiter.api.Test;

public class MedicalSuppliesDAOTest {

  @Test
  public void testGetAddDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    List<MedicalSuppliesData> medicalSuppliesData = SQLRepo.INSTANCE.getMedicalSuppliesList();

    MedicalSuppliesData ofd =
        new MedicalSuppliesData(
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
    SQLRepo.INSTANCE.addServiceRequest(ofd);

    //    List<MedicalSuppliesData> medicalSuppliesAdded =
    // SQLRepo.INSTANCE.getMedicalSuppliesList();
    //    //assertEquals(medicalSuppliesData.size() + 1, medicalSuppliesAdded.size());
    //
    //    SQLRepo.INSTANCE.deleteServiceRequest(ofd);
    //    List<MedicalSuppliesData> medicalSupplyRequestDeleted =
    //        SQLRepo.INSTANCE.getMedicalSuppliesList();
    //    assertEquals(medicalSupplyRequestDeleted.size(), medicalSuppliesData.size());

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  //  @Test
  //  public void testUpdate() {
  //    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);
  //
  //    List<MedicalSuppliesData> medicalSuppliesData = SQLRepo.INSTANCE.getMedicalSuppliesList();
  //
  //    MedicalSuppliesData ofd =
  //        new MedicalSuppliesData(
  //            1,
  //            "joseph",
  //            "Cafe",
  //            "2023-04-07",
  //            "3:12PM",
  //            "Joseph",
  //            "Needle",
  //            "2",
  //            "fast",
  //            ServiceRequestData.Status.PENDING);
  //    SQLRepo.INSTANCE.addServiceRequest(ofd);
  //
  //    SQLRepo.INSTANCE.updateServiceRequest(ofd, "status", "DONE");
  //    // SQLRepo.INSTANCE.deleteServiceRequest(ofd);
  //
  //    SQLRepo.INSTANCE.exitDatabaseProgram();
  //  }

  //  @Test
  //  public void testImportExport() {
  //    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);
  //
  //    FileSystemView view = FileSystemView.getFileSystemView();
  //    File file = view.getHomeDirectory();
  //    String desktopPath = file.getPath();
  //
  //    String tableName = "MedicalSupplies";
  //
  //    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.MEDICAL_SUPPLIES, desktopPath, tableName);
  //    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.MEDICAL_SUPPLIES, desktopPath + "\\" +
  // tableName);
  //
  //    SQLRepo.INSTANCE.exitDatabaseProgram();
  //  }
}
