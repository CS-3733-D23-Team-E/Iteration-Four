package edu.wpi.teame.controllers;

import edu.wpi.teame.App;
import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.controllers.DatabaseEditor.MovePreviewController;
import edu.wpi.teame.entities.*;
import edu.wpi.teame.map.HospitalNode;
import edu.wpi.teame.utilities.MoveUtilities;
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
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomePageController {
  @FXML Text dateText;
  @FXML Text timeText;
  @FXML Text todayIsText;
  @FXML Text alertText;

  @FXML HBox alertHBox;
  @FXML MFXButton alertSubmitButton;
  @FXML MFXTextField alertTextBox;

  @FXML Label helloText;

  Boolean loggedIn;
  String language = "english";

  // Elements for screen mode

  @FXML Rectangle taskBox;
  @FXML Rectangle alertsBox;
  @FXML Rectangle nameBox;

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

    AtomicReference<String> announcementString = new AtomicReference<>("");

    if (Settings.INSTANCE.getLanguage() == Settings.Language.ENGLISH) {
      helloText.setText("Hello, " + Employee.activeEmployee.getFullName());
      alertList.setPlaceholder(new Label("No alerts"));
    } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
      helloText.setText("Hola, " + Employee.activeEmployee.getFullName());
      alertList.setPlaceholder(new Label("Sin alertas"));
    } else // throw error for language not being a valid language
    {
      // throw some sort of error here at some point
    }

    loggedIn = false;

    alertSubmitButton.setOnMouseClicked(
        event -> {
          setAlert();
          alertTextBox.clear();
        });

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

                  if (Settings.INSTANCE.getScreenMode() == Settings.ScreenMode.LIGHT_MODE) {
                    lightMode();
                  } else if (Settings.INSTANCE.getScreenMode() == Settings.ScreenMode.DARK_MODE) {
                    darkMode();
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

    if (Employee.activeEmployee.getPermission() != Employee.Permission.ADMIN) {
      alertHBox.setVisible(false);
      alertHBox.setManaged(false);
    }
  }

  public void translateToSpanish() {
    // Change language variable
    language = "spanish";

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
    //    logoutButton.setText("Cerrar Sesi" + aO + "n"); // Logout
    Font spanishLogout = new Font("Roboto", 13);
    //    logoutButton.setFont(spanishLogout);
  }

  public void translateToEnglish() {
    // Change language variable
    language = "english";

    // Date Bar
    todayIsText.setText("Today is..."); // Keep in English

    // Announcements Bar
    alertText.setText("Alerts"); // Keep in English

    // announcementButton.setText("Submit"); // Keep in English

    alertSubmitButton.setText("Submit"); // Submit
    alertTextBox.setPromptText("Alert Text Here"); // Alert Text Here

    // Logout Button
    //    logoutButton.setText("Logout"); // Keep in English
    Font englishLogout = new Font("Roboto", 18);
    //    logoutButton.setFont(englishLogout);
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
                    if (!isNowEmpty
                        && Employee.activeEmployee
                            .getPermission()
                            .equals(Employee.Permission.ADMIN)) {
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

  public void darkMode() {

    taskBox.setFill(Color.web("#292929"));
    alertsBox.setFill(Color.web("#292929"));
    nameBox.setFill(Color.web("#292929"));
    dateText.setFill(Color.web("#f1f1f1"));
    timeText.setFill(Color.web("#f1f1f1"));
    todayIsText.setFill(Color.web("#f1f1f1"));
    alertText.setFill(Color.web("#f1f1f1"));
    helloText.setTextFill(Color.web("#f1f1f1"));
  }

  public void lightMode() {

    taskBox.setFill(Color.web("#f1f1f1"));
    alertsBox.setFill(Color.web("#f1f1f1"));
    nameBox.setFill(Color.web("#f1f1f1"));
    dateText.setFill(Color.web("#1f1f1f"));
    timeText.setFill(Color.web("#1f1f1f"));
    todayIsText.setFill(Color.web("#1f1f1f"));
    alertText.setFill(Color.web("#1f1f1f"));
    helloText.setTextFill(Color.web("#1f1f1f"));
  }

  private void openPreview(List<Object> instructions) {
    var resource = App.class.getResource("views/DatabaseEditor/MovePreview.fxml");

    FXMLLoader loader = new FXMLLoader(resource);

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
