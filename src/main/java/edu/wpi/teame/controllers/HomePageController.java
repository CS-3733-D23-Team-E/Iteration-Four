package edu.wpi.teame.controllers;

import edu.wpi.teame.App;
import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.controllers.DatabaseEditor.MovePreviewController;
import edu.wpi.teame.entities.AlertData;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.entities.LoginData;
import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.map.HospitalNode;
import edu.wpi.teame.utilities.ButtonUtilities;
import edu.wpi.teame.utilities.MoveUtilities;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomePageController {
  //  @FXML MFXButton serviceRequestButton;
  //  @FXML MFXButton editSignageButton;
  //  @FXML MFXButton databaseButton;
  //  @FXML MFXButton pathfindingButton;
  @FXML MFXButton loginButton;
  @FXML TextField username;
  @FXML TextField password;

  @FXML MFXButton menuBarHome;
  @FXML MFXButton menuBarServices;
  @FXML MFXButton menuBarMaps;

  @FXML MFXButton menuBarAbout;
  @FXML MFXButton menuBarDatabase;

  @FXML MFXButton menuBarSignage;
  @FXML MFXButton menuBarBlank;
  @FXML MFXButton menuBarExit;
  @FXML Text dateText;
  @FXML Text timeText;
  @FXML VBox menuBar;

  // @FXML MFXButton announcementButton;
  // @FXML Text announcementText;
  // @FXML MFXTextField announcementTextBox;
  @FXML Text todayIsText;
  @FXML Text alertText;

  @FXML MFXButton alertSubmitButton;

  @FXML MFXTextField alertTextBox;

  @FXML VBox logoutBox;
  @FXML MFXButton logoutButton;
  @FXML MFXButton userButton;
  @FXML ImageView homeI;
  @FXML ImageView aboutI;
  @FXML ImageView servicesI;
  @FXML ImageView signageI;
  @FXML ImageView pathfindingI;
  @FXML ImageView databaseI;
  @FXML ImageView settingsI;
  @FXML ImageView exitI;
  @FXML MFXButton menuBarSettings;

  @FXML Label helloText;
  @FXML Label staffName;

  Boolean loggedIn;
  String language = "english";
  boolean menuVisibilty = false;
  boolean logoutVisible = false;

  String nyay = "\u00F1"; // ñ
  String aA = "\u0301"; // á
  String aE = "\u00E9"; // é
  String aI = "\u00ED"; // í
  String aO = "\u00F3"; // ó
  String aU = "\u00FA"; // ù
  String aQuestion = "\u00BF"; // Upside down question mark

  @FXML ListView<AlertData> alertList;
  @FXML MovePreviewController movePreviewController = new MovePreviewController();

  List<AlertData> alerts;

  MoveUtilities movUtil = new MoveUtilities();

  public void initialize() {

    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(10);
    dropShadow.setSpread(.71);
    dropShadow.setWidth(21);
    dropShadow.setHeight(50);
    Color paint = new Color(0.0, 0.6175, 0.65, 0.5);

    LocalTime currentTime = LocalTime.now();
    LocalDate currentDate = LocalDate.now();

    // Format current date as a string
    DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    String currentDateString = currentDate.format(format);
    // Format the current time as a string
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    String currentTimeString = currentTime.format(formatter);

    // Print the current time as a string
    timeText.setText(currentTimeString);
    dateText.setText(currentDateString);

    //    serviceRequestButton.setOnMouseClicked(event ->
    // Navigation.navigate(Screen.SERVICE_REQUESTS));
    //
    //    editSignageButton.setOnMouseClicked(event ->
    // Navigation.navigate(Screen.SIGNAGE_EDITOR_PAGE));
    //    databaseButton.setOnMouseClicked(event -> Navigation.navigate(Screen.DATABASE_EDITOR));
    //
    //    pathfindingButton.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));

    menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_EDITOR_PAGE));
    menuBarServices.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));
    menuBarHome.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
    menuBarSettings.setOnMouseClicked(event -> Navigation.navigate(Screen.SETTINGSPAGE));
    menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate((Screen.DATABASE_TABLEVIEW)));
    menuBarExit.setOnMouseClicked(event -> Platform.exit());

    loggedIn = false;
    logoutButton.setOnMouseClicked(event -> attemptLogin());
    AtomicReference<String> announcementString = new AtomicReference<>("");
    helloText.setText("Hello, " + Employee.activeEmployee.getFullName());
    staffName.setText(Employee.activeEmployee.getFullName());

    /*announcementButton.setOnMouseClicked(
       event -> {
         String announcement = announcementTextBox.getText();
         announcementText.setText(announcement);
         announcementString.set(announcement);
       });

    */

    menuBarAbout.setOnMouseClicked(event -> Navigation.navigate((Screen.ABOUT)));

    menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate((Screen.DATABASE_EDITOR)));

    menuBarExit.setOnMouseClicked(event -> Platform.exit());

    loggedIn = false;
    logoutButton.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.SIGNAGE_TEXT);
          SQLRepo.INSTANCE.exitDatabaseProgram();
        });

    alertSubmitButton.setOnMouseClicked(
        event -> {
          setAlert();
          alertTextBox.clear();
        });

    logoutPopup(false);

    // Navigation controls for the button in the menu bar
    menuBarHome.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.HOME);
          menuVisibilty = !menuVisibilty;
        });

    userButton.setOnMouseClicked(
        event -> {
          logoutVisible = !logoutVisible;
          logoutPopup(logoutVisible);
        });

    logoutButton.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.SIGNAGE_TEXT);
          SQLRepo.INSTANCE.exitDatabaseProgram();
        });

    // makes the menu bar buttons get highlighted when the mouse hovers over them
    ButtonUtilities.mouseSetupMenuBar(
        menuBarHome,
        "baseline-left",
        homeI,
        "images/house-blank.png",
        "images/house-blank-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarServices,
        "baseline-left",
        servicesI,
        "images/hand-holding-medical.png",
        "images/hand-holding-medical-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarSignage,
        "baseline-left",
        signageI,
        "images/diamond-turn-right.png",
        "images/diamond-turn-right-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarMaps, "baseline-left", pathfindingI, "images/marker.png", "images/marker-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarDatabase,
        "baseline-left",
        databaseI,
        "images/folder-tree.png",
        "images/folder-tree-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarAbout, "baseline-left", aboutI, "images/abouticon.png", "images/abouticon-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarSettings,
        "baseline-left",
        settingsI,
        "images/settingsicon.png",
        "images/settingsicon-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarExit,
        "baseline-center",
        exitI,
        "images/sign-out-alt.png",
        "images/sign-out-alt-blue.png");

    // makes the buttons highlight when they are hovered over
    //    ButtonUtilities.mouseSetup(serviceRequestButton);
    //    ButtonUtilities.mouseSetup(editSignageButton);
    //    ButtonUtilities.mouseSetup(pathfindingButton);
    //    ButtonUtilities.mouseSetup(databaseButton);
    ButtonUtilities.mouseSetup(logoutButton);

    initAlertList();
    fillAlertList();

    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  LocalTime now = LocalTime.now();
                  String formattedTime = now.format(formatter);
                  timeText.setText(formattedTime);
                  // fillAlertList();
                  if (Settings.INSTANCE.getLanguage() == Settings.Language.ENGLISH) {
                    // translateToEnglish(String.valueOf(announcementString));
                    translateToEnglish();
                  } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
                    // translateToSpanish(String.valueOf(announcementString));
                    translateToSpanish();
                  }
                }));

    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    // Page Language Translation Code (commented out until connected to the instance)
    /*englishButton.setOnMouseClicked(
        event -> {
          translateToEnglish(String.valueOf(announcementString));
        });
    spanishButton.setOnMouseClicked(
        event -> {
          translateToSpanish(String.valueOf(announcementString));
        });*/

    // englishButton.setOnMouseClicked(
    //     event -> {
    //       Settings.INSTANCE.setLanguage(Settings.Language.ENGLISH);
    //       englishButton.setEffect(dropShadow);
    //       spanishButton.setEffect(null);
    //     });
    // spanishButton.setOnMouseClicked(
    //     event -> {
    //       Settings.INSTANCE.setLanguage(Settings.Language.SPANISH);
    //       spanishButton.setEffect(dropShadow);
    //       englishButton.setEffect(null);
    //     });
    // throw error for language not being a valid language
    // Page Language Translation Code
    if (Settings.INSTANCE.getLanguage() == Settings.Language.ENGLISH) {
      translateToEnglish();
    } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
      translateToSpanish();
    } else // throw error for language not being a valid language
    {
      // throw some sort of error here at some point
    }
  }

  public void attemptLogin() {
    // Get the input login info
    LoginData login = new LoginData(username.getText(), password.getText());

    // If the login was successful
    if (login.attemptLogin()) {
      // Hide text fields and button
      password.setVisible(false);
      username.setVisible(false);
      loginButton.setVisible(false);
      // Set loggedIn as true
      loggedIn = true;

    } else {
      // Clear the fields
      password.clear();
      username.clear();
    }
  }

  public void logoutPopup(boolean bool) {
    logoutBox.setVisible(bool);
  }

  public void menuBarVisible(boolean bool) {
    menuBarHome.setVisible(bool);
    menuBarServices.setVisible(bool);
    menuBarSignage.setVisible(bool);
    menuBarMaps.setVisible(bool);
    menuBarDatabase.setVisible(bool);

    menuBarAbout.setVisible(bool);
    menuBarSettings.setVisible(bool);
    menuBarExit.setVisible(bool);
    menuBarBlank.setVisible(bool);
    menuBar.setVisible(bool);
  }

  public void translateToSpanish() {
    // Change language variable
    language = "spanish";

    // Menu Bar
    menuBarHome.setText("Principal"); // Home
    menuBarServices.setText("Servicios"); // Services
    menuBarSignage.setText("Se" + nyay + "alizaci" + aO + "n"); // Signage
    menuBarMaps.setText("Navegaci" + aO + "n"); // Pathfinding
    menuBarDatabase.setText("Base de Datos"); // Database
    menuBarExit.setText(("Salida")); // Exit

    // Home Page Buttons
    //    editSignageButton.setText("Se" + nyay + "alizaci" + aO + "n"); // Signage
    //    serviceRequestButton.setText("Servicios"); // Services
    //    pathfindingButton.setText("Navegaci" + aO + "n"); // Pathfinding
    //    databaseButton.setText("Base de Datos"); // Database

    // Date Bar
    todayIsText.setText("Hoy es..."); // Today is...

    // Announcements Bar
    alertText.setText("Alertas"); // Alerts
    /*if (announcmentString.equals("")) { // Do this if there are currently no announcements
      announcementText.setText("No hay nuevos anuncios."); // No new announcements.
    }
    announcementTextBox.setPromptText("Texto del Anuncio Aqu" + aI); // Announcement Text Here

     */
    // announcementButton.setText("Presentar"); // Submit

    alertSubmitButton.setText("Presentar"); // Submit
    alertTextBox.setPromptText("Texto de Alerta Aqu" + aI); // Alert Text Here

    // Logout Button
    logoutButton.setText("Cerrar Sesi" + aO + "n"); // Logout
    Font spanishLogout = new Font("Roboto", 13);
    logoutButton.setFont(spanishLogout);
  }

  public void translateToEnglish() {
    // Change language variable
    language = "english";

    // Menu Bar
    menuBarHome.setText("Home"); // Keep in English
    menuBarServices.setText("Services"); // Keep in English
    menuBarSignage.setText("Signage"); // Keep in English
    menuBarMaps.setText("Pathfinding"); // Keep in English
    menuBarDatabase.setText("Database"); // Keep in English
    menuBarExit.setText(("Exit")); // Keep in English

    // Home Page Buttons
    //    editSignageButton.setText("Signage"); // Keep in English
    //    serviceRequestButton.setText("Services"); // Keep in English
    //    pathfindingButton.setText("Pathfinding"); // Keep in English
    //    databaseButton.setText("Database"); // Keep in English

    // Date Bar
    todayIsText.setText("Today is..."); // Keep in English

    // Announcements Bar
    alertText.setText("Alerts"); // Keep in English

    // announcementButton.setText("Submit"); // Keep in English

    alertSubmitButton.setText("Submit"); // Submit
    alertTextBox.setPromptText("Alert Text Here"); // Alert Text Here

    // Logout Button
    logoutButton.setText("Logout"); // Keep in English
    Font englishLogout = new Font("Roboto", 18);
    logoutButton.setFont(englishLogout);
  }

  public AlertData setAlert() {
    System.out.println("alert sent");
    AlertData alertData;
    if (alerts.size() > 0) {
      alertData = new AlertData(alerts.get(0).getAlertID() + 1, alertTextBox.getText());
    } else {
      alertData = new AlertData(1, alertTextBox.getText());
    }

    SQLRepo.INSTANCE.addAlert(alertData);
    fillAlertList();

    return alertData;
  }

  private void fillAlertList() {
    alerts =
        new java.util.ArrayList<>(
            SQLRepo.INSTANCE.getAlertList().stream()
                .sorted(
                    new Comparator<AlertData>() {
                      @Override
                      public int compare(AlertData o1, AlertData o2) {
                        return o2.getTime().compareTo(o1.getTime());
                      }
                    })
                .toList());

    alertList.setItems(FXCollections.observableList(alerts));
  }

  private void initAlertList() {
    alertList.setCellFactory(
        lv -> {
          ListCell<AlertData> cell = new ListCell<>();
          ContextMenu contextMenu = new ContextMenu();

          MenuItem deleteItem = new MenuItem();
          deleteItem.textProperty().set("Delete alert");
          deleteItem.setOnAction(
              event -> {
                SQLRepo.INSTANCE.deleteAlert(cell.getItem());
                fillAlertList();
              });
          contextMenu.getItems().add(deleteItem);

          MenuItem moveItem = new MenuItem();
          //          if (cell.getItem() != null && movUtil.moveFromAlert(cell.getItem()) != null) {
          //            moveItem.textProperty().set("View move");
          //          } else {
          //            moveItem.textProperty().set("No move preview available");
          //          }
          moveItem.setOnAction(
              event -> {
                List<Object> instructions = movUtil.moveFromAlert(cell.getItem());
                if (instructions != null) {
                  openPreview(instructions);
                }
              });
          contextMenu.getItems().add(moveItem);

          // adds the context menu to now-filled cells (if you are an admin)
          cell.emptyProperty()
              .addListener(
                  (obs, wasEmpty, isNowEmpty) -> {
                    if (!isNowEmpty && Employee.activeEmployee.getPermission().equals("ADMIN")) {
                      cell.setContextMenu(contextMenu);
                    } else {
                      cell.setContextMenu(null);
                    }
                  });
          // update the item when the item changes
          cell.itemProperty()
              .addListener(
                  (event) -> {
                    cell.textProperty()
                        .setValue(cell.getItem() != null ? cell.getItem().toString() : "");
                    if (cell.getItem() != null) {
                      if (movUtil.moveFromAlert(cell.getItem()) != null) {
                        moveItem.textProperty().set("View move");
                        moveItem.setDisable(false);
                      } else {
                        moveItem.textProperty().set("No move preview available");
                        moveItem.setDisable(true);
                      }
                    }
                  });
          cell.getStyleClass().add("alert-cell");
          return cell;
        });
  }

  private void openPreview(List<Object> instructions) {
    var resource = App.class.getResource("views/DatabaseEditor/MovePreview.fxml");

    FXMLLoader loader = new FXMLLoader(resource);

    // loader.setController(movePreviewController); // NOTE: replaces this line in the FXML:
    // fx:controller="edu.wpi.teame.controllers.DatabaseEditor.MovePreviewController"

    AnchorPane previewLayout;
    try {
      previewLayout = loader.load();
    } catch (IOException e) {
      previewLayout = new AnchorPane();
    }

    Scene newScene = new Scene(previewLayout);

    System.out.println(movePreviewController);
    movePreviewController = loader.getController();
    System.out.println(movePreviewController);

    Stage newStage = new Stage();
    newStage.setTitle("Move Preview");
    newStage.setScene(newScene);
    movePreviewController.setBidirectional((boolean) instructions.get(4));
    movePreviewController.setNode1(
        (HospitalNode) instructions.get(0), (String) instructions.get(1));
    movePreviewController.setNode2(
        (HospitalNode) instructions.get(2), (String) instructions.get(3));
    newStage.showAndWait();
  }
}
