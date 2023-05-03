package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.entities.SignageComponentData;
import edu.wpi.teame.entities.SignageDirectionPicker;
import edu.wpi.teame.map.LocationName;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import edu.wpi.teame.utilities.SignageUtilities;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import org.controlsfx.control.SearchableComboBox;

public class SignageComponentController {
  @FXML MFXButton submitButton;
  @FXML MFXButton cancelButton;
  @FXML MFXButton resetButton;
  @FXML MFXButton addButton;
  @FXML MFXDatePicker date;
  @FXML SearchableComboBox<String> kioskName;
  @FXML SearchableComboBox<String> addLocationCombo;
  @FXML FlowPane signagePane;
  @FXML TextField addKioskText;
  @FXML MFXButton addKioskButton;
  String lastKiosk = null;
  LocalDate lastDate = null;
  SignageUtilities signageUtilities = new SignageUtilities();
  List<SignageComponentData> sg = SQLRepo.INSTANCE.getSignageList();

  // List of all the pickers on screen
  List<SignageDirectionPicker> allLocationPickers = new LinkedList<>();

  ObservableList<String> kioskLocations;

  @FXML
  public void initialize() {
    // formSubmitted.setVisible(false);
    cancelButton.setOnMouseClicked(event -> cancelRequest());
    resetButton.setOnMouseClicked(event -> clearForm());
    date.setValue(LocalDate.now());
    fillSGListAndKioskLocation();

    // On kiosk change update the pickers displayed
    kioskName.setOnAction(
        event -> {
          String currentKiosk = kioskName.getValue();
          if (currentKiosk != null && currentKiosk != lastKiosk && date.getValue() != null) {
            lastKiosk = currentKiosk;
            updatePickers();
          }
        });

    // On date change update the pickers displayed
    date.setOnAction(
        event -> {
          LocalDate currentDate = date.getValue();
          if (currentDate != null && currentDate != lastDate && kioskName.getValue() != null) {
            lastDate = currentDate;
            updatePickers();
          }
        });

    // Submit the current pickers orientation to the database
    addKioskButton.setOnMouseClicked(
        event -> {
          String kioskLocation = addKioskText.getText();
          if (kioskLocation != null) {
            kioskLocations.add(kioskLocation);
          }
          addKioskText.setText(null);
        });

    submitButton.setOnMouseClicked(
        event -> {
          try {
            submitForm();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
          Alert popup = new Alert(Alert.AlertType.CONFIRMATION, "Signage Submitted");
          popup.show();
        });

    // Add a location
    addButton.setOnMouseClicked(
        event -> {
          String location = addLocationCombo.getValue();

          // Create a new picker
          if (location != null && date.getValue() != null && kioskName.getValue() != null) {
            String kLoc = kioskName.getValue();
            String d = date.getValue().toString();

            // Create a new picker for the added location
            SignageDirectionPicker newPick =
                new SignageDirectionPicker(
                    new SignageComponentData(
                        date.getValue().toString(),
                        kioskName.getValue(),
                        addLocationCombo.getValue(),
                        SignageComponentData.ArrowDirections.RIGHT));
            allLocationPickers.add(newPick);
            addLocationCombo.getItems().remove(addLocationCombo.getValue());
            addLocationCombo.setValue(null);
            signagePane.getChildren().add(newPick);
          }
        });
  }

  private void addNewKioskToDB(
      String d, String kloc, String location, SignageComponentData.ArrowDirections ad) {
    SQLRepo.INSTANCE.addSignage(new SignageComponentData(d, kloc, location, ad));
  }

  private void fillSGListAndKioskLocation() {

    List<String> temp =
        sg.stream().map(SignageComponentData::getKiosk_location).distinct().toList();
    kioskLocations = FXCollections.observableArrayList(temp);
    kioskName.setItems(kioskLocations);

    ObservableList<String> floorLocations =
        FXCollections.observableArrayList(
            LocationName.allLocations.values().stream()
                .filter(
                    (location) -> // Filter out hallways and long names with no corresponding
                        // LocationName
                        location == null
                            ? false
                            : location.getNodeType() != LocationName.NodeType.HALL
                                && location.getNodeType() != LocationName.NodeType.STAI
                                && location.getNodeType() != LocationName.NodeType.ELEV
                                && location.getNodeType() != LocationName.NodeType.REST)
                .map((location) -> location.getLongName())
                .sorted() // Sort alphabetically
                .toList());
    addLocationCombo.setItems(floorLocations);
  }

  public void submitForm() throws SQLException {
    // Add or Update the database
    for (Node child : signagePane.getChildren()) {
      SignageDirectionPicker signagePicker = (SignageDirectionPicker) child;
      // if a location is not selected ignore
      if (signagePicker.isKioskLocationInDB()) {
        SQLRepo.INSTANCE.updateSignage(
            signagePicker.getComponentData(),
            "arrowDirection",
            signagePicker.getComponentData().getArrowDirections().toString());
      } else {
        SQLRepo.INSTANCE.addSignage(signagePicker.getComponentData());
      }
    }
  }

  public void updatePickers() {
    // Clear the flow pane and set lastKiosk
    signagePane.getChildren().clear();
    String currentKiosk = kioskName.getValue();
    LocalDate currentDate = date.getValue();
    System.out.println(currentDate.toString());
    // Get the component data for only the locations of the selected kiosk
    List<SignageComponentData> temp =
        sg.stream()
            .filter(
                item ->
                    item.getKiosk_location().equals(currentKiosk)
                        && item.getDate().equals(currentDate.toString())) //
            // FILTERS ONLY THE DATA FOR SELECTED DAY
            .toList();

    // Create a SignageDirectionPicker for each location using the signage data
    for (SignageComponentData compData : temp) {
      SignageDirectionPicker newPicker = new SignageDirectionPicker(compData);
      signagePane.getChildren().add(newPicker);
    }
  }

  public void clearForm() {
    date.setValue(null);
    addLocationCombo.setValue(null);
    signagePane.getChildren().clear();
    kioskName.setValue(null);
    // locations.setValue(null);
    // directions.setValue(null);
  }

  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }

  public void clearPickers() {
    allLocationPickers.clear();
    signagePane.getChildren().clear();
  }
}
