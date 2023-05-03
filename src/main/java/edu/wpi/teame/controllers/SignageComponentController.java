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
import javafx.beans.binding.Bindings;
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
  LocalDate lastDate = null;
  SignageUtilities signageUtilities = new SignageUtilities();
  List<SignageComponentData> sg = SQLRepo.INSTANCE.getSignageList();

  // List of all the pickers on screen
  List<SignageDirectionPicker> allLocationPickers = new LinkedList<>();
  List<String> temp = sg.stream().map(SignageComponentData::getKiosk_location).distinct().toList();
  ObservableList<String> kioskLocations = FXCollections.observableArrayList(temp);

  @FXML
  public void initialize() {
    // formSubmitted.setVisible(false);
    cancelButton.setOnMouseClicked(event -> cancelRequest());
    resetButton.setOnMouseClicked(event -> clearForm());
    date.setValue(LocalDate.now());
    kioskName.setValue(Settings.INSTANCE.getCurrentKiosk());
    updatePickers();

    addButton
        .disableProperty()
        .bind(
            Bindings.or(
                Bindings.size(signagePane.getChildren()).greaterThan(7),
                Bindings.or(
                    date.valueProperty().isNull(),
                    Bindings.or(
                        kioskName.valueProperty().isNull(),
                        addLocationCombo.valueProperty().isNull()))));
    submitButton.disableProperty().bind(Bindings.size(signagePane.getChildren()).isEqualTo(0));
    addKioskButton
        .disableProperty()
        .bind(
            Bindings.or(
                addKioskText.textProperty().isEmpty(), addButton.textProperty().isEqualTo("")));

    // On kiosk change update the pickers displayed
    kioskName.setOnAction(
        event -> {
          String currentKiosk = kioskName.getValue();
          if (currentKiosk != null && date.getValue() != null) {
            updatePickers();
          }
        });

    // On date change update the pickers displayed
    date.setOnAction(
        event -> {
          LocalDate currentDate = date.getValue();
          if (currentDate != null && kioskName.getValue() != null) {
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
            fillSGListAndKioskLocation();
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

  private void fillSGListAndKioskLocation() {

    kioskName.setItems(kioskLocations);

    ObservableList<String> floorLocations =
        FXCollections.observableArrayList(
            LocationName.allLocations.values().stream()
                .filter(
                    (location) -> {
                      if (location == null) {
                        return false;
                      }

                      return location.getNodeType() != LocationName.NodeType.HALL
                          && !SQLRepo.INSTANCE.getSignageList().stream()
                              .filter(
                                  (signageComponentData) ->
                                      signageComponentData
                                              .getDate()
                                              .equals(date.getValue().toString())
                                          && signageComponentData
                                              .getKiosk_location()
                                              .equals(Settings.INSTANCE.getCurrentKiosk()))
                              .map(
                                  (signageComponentData) -> signageComponentData.getLocationNames())
                              .toList()
                              .contains(location.getLongName());
                    })
                .map((location) -> location.getLongName())
                .sorted() // Sort alphabetically
                .toList());

    addLocationCombo.setItems(floorLocations);
  }

  public void submitForm() throws SQLException {
    // Delete the old data
    signageUtilities.deleteAllForASpecificDayInTheDatabase(date.getValue(), kioskName.getValue());
    // Add or Update the database
    for (Node child : signagePane.getChildren()) {
      SignageDirectionPicker signagePicker = (SignageDirectionPicker) child;
      // Add the new data
      SQLRepo.INSTANCE.addSignage(signagePicker.getComponentData());
    }
  }

  public void updatePickers() {
    // Clear the flow pane and set lastKiosk
    signagePane.getChildren().clear();
    // Get the component data for only the locations of the selected kiosk
    List<SignageComponentData> temp =
        signageUtilities.findAllDirectionsOnDateAtKiosk(kioskName.getValue(), date.getValue());
    // Create a SignageDirectionPicker for each location using the signage data
    if (temp != null) {
      for (SignageComponentData compData : temp) {
        SignageDirectionPicker newPicker = new SignageDirectionPicker(compData);
        signagePane.getChildren().add(newPicker);
      }
    }
    fillSGListAndKioskLocation();
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
}
