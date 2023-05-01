package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.RoomCleanupData;
import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.map.LocationName;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.stream.Stream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.SearchableComboBox;

public class RoomCleanupRequestController {

  @FXML MFXButton submitButton;
  @FXML MFXButton cancelButton;
  @FXML MFXButton resetButton;

  @FXML SearchableComboBox<String> roomNameChoice;
  @FXML DatePicker deliveryDateChoice;
  @FXML SearchableComboBox<String> deliveryTimeChoice;
  @FXML SearchableComboBox<String> assignedStaffChoice;
  @FXML SearchableComboBox<String> severityLevelChoice;
  @FXML TextField cleaningSuppliesField;
  @FXML TextField restockSuppliesField;

  @FXML MFXButton closeButton;
  @FXML VBox requestSubmittedBox;

  @FXML Text roomText;
  @FXML Text deliveryDateText;
  @FXML Text deliveryTimeText;
  @FXML Text assignedStaffText;
  @FXML Text severityLevelText;
  @FXML Text cleaningSuppliesText;
  @FXML Text restockSuppliesText;

  String language = "english";
  String nyay = "\u00F1"; // ñ
  String aA = "\u0301"; // á
  String aE = "\u00E9"; // é
  String aI = "\u00ED"; // í
  String aO = "\u00F3"; // ó
  String aU = "\u00FA"; // ù
  String aQuestion = "\u00BF"; // Upside down question mark
  ObservableList<String> deliveryTimes =
      FXCollections.observableArrayList(
          "10am - 11am", "11am - 12pm", "12pm - 1pm", "1pm - 2pm", "2pm - 3pm", "3pm - 4pm");

  ObservableList<String> severityLevel =
      FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

  @FXML
  public void initialize() {
    requestSubmittedBox.setVisible(false);

    Stream<LocationName> locationStream = LocationName.allLocations.values().stream();
    ObservableList<String> names =
        FXCollections.observableArrayList(
            locationStream
                .filter(
                    (locationName) -> {
                      return locationName.getNodeType() != LocationName.NodeType.HALL
                          && locationName.getNodeType() != LocationName.NodeType.STAI
                          && locationName.getNodeType() != LocationName.NodeType.REST
                          && locationName.getNodeType() != LocationName.NodeType.ELEV;
                    })
                .map(
                    (locationName) -> {
                      return locationName.getLongName();
                    })
                .sorted()
                .toList());

    assignedStaffChoice.setItems(
        FXCollections.observableList(
            SQLRepo.INSTANCE.getEmployeeList().stream()
                .filter(employee -> employee.getPermission().equals("STAFF"))
                .map(employee -> employee.getUsername())
                .toList()));

    roomNameChoice.setItems(names);
    deliveryTimeChoice.setItems(deliveryTimes);
    severityLevelChoice.setItems(severityLevel);

    submitButton.setOnMouseClicked(event -> sendRequest());
    cancelButton.setOnMouseClicked(event -> cancelRequest());
    resetButton.setOnMouseClicked(event -> clearForm());

    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  if (Settings.INSTANCE.getLanguage() == Settings.Language.ENGLISH) {
                    translateToEnglish();
                  } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
                    translateToSpanish();
                  }
                }));

    submitButton.setOnMouseClicked(
        event -> {
          // sendRequest();
          requestSubmittedBox.setVisible(true);
          clearForm();
        });
    closeButton.setOnMouseClicked(event -> requestSubmittedBox.setVisible(false));
  }

  private void clearForm() {
    roomNameChoice.setValue(null);
    deliveryTimeChoice.setValue(null);
    severityLevelChoice.setValue(null);
    cleaningSuppliesField.clear();
    assignedStaffChoice.setValue(null);
    restockSuppliesField.clear();
  }

  public RoomCleanupData sendRequest() {
    RoomCleanupData requestData =
        new RoomCleanupData(
            0,
            roomNameChoice.getValue(),
            deliveryDateChoice.getValue().toString(),
            deliveryTimeChoice.getValue(),
            assignedStaffChoice.getValue(),
            severityLevelChoice.getValue(),
            cleaningSuppliesField.getText(),
            restockSuppliesField.getText(),
            RoomCleanupData.Status.PENDING);

    SQLRepo.INSTANCE.addServiceRequest(requestData);
    return requestData;
  }

  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }

  public void translateToSpanish() {
    // Input Fields
    roomText.setText("Cuarto"); // Room
    deliveryDateText.setText("Fecha de Entrega"); // Delivery Date
    deliveryTimeText.setText("Tiempo de Entrega"); // Delivery Time
    assignedStaffText.setText("Empleado"); // Staff
    severityLevelText.setText("Nivel de Severidad"); // Severity Level
    cleaningSuppliesText.setText("Limpiando Suministros"); // Cleaning Supplies
    restockSuppliesText.setText("Suministros de Reabastecimiento"); // Restock Supplies

    // Buttons
    cancelButton.setText("Cancelar"); // Cancel
    resetButton.setText("Poner a Cero"); // Reset
    submitButton.setText("Presentar"); // Submit
  }

  public void translateToEnglish() {
    roomText.setText("Room"); // Keep in English
    deliveryDateText.setText("Delivery Date"); // Keep in English
    deliveryTimeText.setText("Delivery Time"); // Keep in English
    assignedStaffText.setText("Staff"); // Keep in English
    severityLevelText.setText("Severity Level"); // Keep in English
    cleaningSuppliesText.setText("Cleaning Supplies"); // Keep in English
    restockSuppliesText.setText("Restock Supplies"); // Keep in English

    // Buttons
    cancelButton.setText("Cancel"); // Keep in English
    resetButton.setText("Reset"); // Keep in English
    submitButton.setText("Submit"); // Keep in English
  }
}
