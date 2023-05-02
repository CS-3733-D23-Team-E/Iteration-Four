package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.entities.SignageComponentData;
import edu.wpi.teame.entities.SignageDirectionPicker;
import edu.wpi.teame.map.LocationName;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import org.controlsfx.control.SearchableComboBox;

public class SignageComponentController {
  @FXML MFXButton submitButton;
  @FXML MFXButton cancelButton;
  @FXML MFXButton resetButton;
  @FXML MFXButton addButton;
  @FXML MFXDatePicker date;
  @FXML SearchableComboBox<String> kioskName;
  @FXML SearchableComboBox<String> kioskPreset;
  @FXML SearchableComboBox<String> addLocationCombo;
  @FXML FlowPane signagePane;
  String lastKiosk = null;
  LocalDate lastDate = null;
  List<SignageComponentData> sg = SQLRepo.INSTANCE.getSignageList();;
  List<SignageDirectionPicker> allKioskLocations = new LinkedList<>();

  @FXML
  public void initialize() {
    // formSubmitted.setVisible(false);
    cancelButton.setOnMouseClicked(event -> cancelRequest());
    resetButton.setOnMouseClicked(event -> clearForm());
    date.setValue(LocalDate.now());
    kioskPreset.setValue(Settings.INSTANCE.getDefaultLocation());
    fillSGListAndKioskLocation();

    // When changing the selected kiosk name
    kioskName.setOnAction(
        event -> {
          String currentKiosk = kioskName.getValue();
          if (currentKiosk != null && currentKiosk != lastKiosk && date.getValue() != null) {
            lastKiosk = currentKiosk;
            updatePickers();
          }
        });
    date.setOnAction(
        event -> {
          LocalDate currentDate = date.getValue();
          if (currentDate != null && currentDate != lastDate && kioskName.getValue() != null) {
            lastDate = currentDate;
            updatePickers();
          }
        });

    submitButton.setOnMouseClicked(
        event -> {
          try {
            submitForm();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
          clearForm();
          // formSubmitted.setVisible(true);
        });

    addButton.setOnMouseClicked(
        event -> {
          String location = addLocationCombo.getValue();
          // Create a new picker
          if (location != null && date.getValue() != null && kioskName.getValue() != null) {
            SignageDirectionPicker newPick =
                new SignageDirectionPicker(
                    new SignageComponentData(
                        date.getValue().toString(),
                        kioskName.getValue(),
                        addLocationCombo.getValue(),
                        SignageComponentData.ArrowDirections.RIGHT));
            allKioskLocations.add(newPick);
            signagePane.getChildren().add(newPick);
          }
        });
  }

  private void fillSGListAndKioskLocation() {
    List<String> temp =
        sg.stream().map(SignageComponentData::getKiosk_location).distinct().toList();
    kioskName.setItems(FXCollections.observableArrayList(temp));
    kioskPreset.setItems((FXCollections.observableArrayList(temp)));

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
    for (Node child : signagePane.getChildren()) {
      SignageDirectionPicker signagePicker = (SignageDirectionPicker) child;
      // if a location is not selected ignore
      if (signagePicker.getComboBox().getValue() != null) {
        // Update the signage for SQLRepo
        System.out.println(
            "Update Signage for "
                + date.getValue()
                + ": "
                + signagePicker.getComponentData().getLocationNames()
                + ": "
                + signagePicker.getComponentData().getArrowDirections());

        SQLRepo.INSTANCE.updateSignage(
            signagePicker.getComponentData(),
            "arrowDirection",
            signagePicker.getComponentData().getArrowDirections().toString());
      }
    }

    //    SignageComponentData.ArrowDirections get =
    //        SQLRepo.INSTANCE.getDirectionFromPKeyL(
    //            date.getValue().toString(), kioskLocations1.getValue(), locations.getValue());
    //
    //    // Create the service request data
    //    SignageComponentData requestData =
    //        new SignageComponentData(
    //            date.getValue().toString(), kioskLocations1.getValue(), locations.getValue(),
    // get);
    //
    //    SQLRepo.INSTANCE.updateSignage(requestData, "arrowDirection", directions.getValue());
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
    // locations.setValue(null);
    // directions.setValue(null);
  }

  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }
}
