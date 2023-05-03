package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.Employee;
import org.junit.jupiter.api.Test;

public class EmployeeDAOTest {

  @Test
  public void testlogIn() throws RuntimeException {
    SQLRepo.INSTANCE.connectToDatabase("staff", "staff", SQLRepo.DB.WPI);
    SQLRepo.INSTANCE.exitDatabaseProgram();

    SQLRepo.INSTANCE.connectToDatabase("admin", "admin", SQLRepo.DB.WPI);
    SQLRepo.INSTANCE.exitDatabaseProgram();

    Employee failure = SQLRepo.INSTANCE.connectToDatabase("test", "fail", SQLRepo.DB.WPI);
    assertNull(failure);
    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testGetAddandDelete() {
    SQLRepo.INSTANCE.connectToDatabase("staff", "staff", SQLRepo.DB.WPI);
    int numEmployees = SQLRepo.INSTANCE.getEmployeeList().size();

    Employee Jamie = new Employee("Jamie Rapal", "JRapal", "password", "ADMIN");
    SQLRepo.INSTANCE.addEmployee(Jamie);
    int numEmployees2 = SQLRepo.INSTANCE.getEmployeeList().size();
    assertEquals(numEmployees + 1, numEmployees2);

    SQLRepo.INSTANCE.deleteEmployee(Jamie);
    int numEmployees3 = SQLRepo.INSTANCE.getEmployeeList().size();
    assertEquals(numEmployees, numEmployees3);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  /*
    @Test
    public void addEmployeesBack() {
      SQLRepo.INSTANCE.connectToDatabase("staff", "staff", SQLRepo.DB.WPI);
      SQLRepo.INSTANCE.addEmployee(new Employee("adminAccount", "Admin", "Admin", "ADMIN"));
      SQLRepo.INSTANCE.addEmployee(new Employee("staffAccount", "Staff", "Staff", "STAFF"));
      SQLRepo.INSTANCE.addEmployee(new Employee("teame", "teame", "teame50", "ADMIN"));
      SQLRepo.INSTANCE.addEmployee(new Employee("Mark Wahlberg", "Mark", "Mark", "STAFF"));
      SQLRepo.INSTANCE.addEmployee(new Employee("Kim Jong Un", "KJU", "KJU", "STAFF"));
      SQLRepo.INSTANCE.addEmployee(new Employee("Joseph Thesmar", "Joseph", "Joseph", "ADMIN"));
      SQLRepo.INSTANCE.addEmployee(new Employee("Braeden Swain", "Braeden", "Braeden", "ADMIN"));
      SQLRepo.INSTANCE.addEmployee(new Employee("Donald Trump", "DTummy", "Trump", "STAFF"));
      SQLRepo.INSTANCE.addEmployee(new Employee("Lionel Messi", "LMessi", "Messi", "STAFF"));
      SQLRepo.INSTANCE.addEmployee(new Employee("Aarsh Zadaphiya", "Aarsh", "Aarsh", "ADMIN"));
    }
  */

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50", SQLRepo.DB.WPI);

    Employee Jamie = new Employee("Jamie Rapal", "JRapal", "password", "ADMIN");
    SQLRepo.INSTANCE.addEmployee(Jamie);
    SQLRepo.INSTANCE.updateEmployee(Jamie, "fullName", "Jamie R");
    Jamie.setFullName("Jamie R");

    SQLRepo.INSTANCE.updateEmployee(Jamie, "username", "JamieRapal");
    Jamie.setUsername("JamieRapal");

    SQLRepo.INSTANCE.updateEmployee(Jamie, "password", "pass");
    SQLRepo.INSTANCE.updateEmployee(Jamie, "permission", "STAFF");

    SQLRepo.INSTANCE.deleteEmployee(Jamie);

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
  //    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.EMPLOYEE, desktopPath, "Employee");
  //    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.EMPLOYEE, desktopPath + "\\Employee");
  //
  //    SQLRepo.INSTANCE.exitDatabaseProgram();
  //  }
}
