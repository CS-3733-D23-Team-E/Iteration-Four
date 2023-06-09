package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.ConferenceRequestData;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ConferenceRoomDAOTest {
  @Test
  public void testGetAddDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    List<ConferenceRequestData> conference = SQLRepo.INSTANCE.getConfList();

    ConferenceRequestData crd =
        new ConferenceRequestData(
            0,
            "joseph",
            "Cafe",
            "2023-04-07",
            "3:12PM",
            "Joseph",
            "Conference Room L1",
            "for 2 hours",
            ConferenceRequestData.Status.DONE);

    SQLRepo.INSTANCE.addServiceRequest(crd);
    List<ConferenceRequestData> conferenceRequestAdded = SQLRepo.INSTANCE.getConfList();

    assertEquals(conference.size() + 1, conferenceRequestAdded.size());

    SQLRepo.INSTANCE.deleteServiceRequest(crd);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    ConferenceRequestData conferenceRequest =
        new ConferenceRequestData(
            0,
            "joseph",
            "Cafe",
            "2023-04-07",
            "3:12PM",
            "Joseph",
            "Conference Room L1",
            "for 2 hours",
            ConferenceRequestData.Status.DONE);
    SQLRepo.INSTANCE.addServiceRequest(conferenceRequest);
    SQLRepo.INSTANCE.updateServiceRequest(conferenceRequest, "status", "PENDING");
    SQLRepo.INSTANCE.deleteServiceRequest(conferenceRequest);
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
  //    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.CONFERENCE_ROOM, desktopPath, "ConfRoomService");
  //
  //    SQLRepo.INSTANCE.importFromCSV(
  //        SQLRepo.Table.CONFERENCE_ROOM, desktopPath + "\\ConfRoomService");
  //
  //    SQLRepo.INSTANCE.exitDatabaseProgram();
  //  }
}
