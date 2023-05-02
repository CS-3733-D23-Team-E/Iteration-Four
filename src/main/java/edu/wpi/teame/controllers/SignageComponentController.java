package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.SignageComponentData;
import edu.wpi.teame.entities.SignageDirectionPicker;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import org.controlsfx.control.SearchableComboBox;

public class SignageComponentController {
  @FXML MFXButton submitButton;
  @FXML MFXButton cancelButton;
  @FXML MFXButton resetButton;
  @FXML MFXDatePicker date;
  @FXML SearchableComboBox<String> kioskName;
  @FXML SearchableComboBox<String> kioskLocations0;
  @FXML SearchableComboBox<String> kioskLocations1;
  @FXML SearchableComboBox<String> kioskLocations2;
  @FXML SearchableComboBox<String> kioskLocations3;
  @FXML SearchableComboBox<String> kioskLocations4;
  @FXML SearchableComboBox<String> kioskLocations5;
  @FXML SearchableComboBox<String> kioskLocations6;
  @FXML SearchableComboBox<String> kioskLocations7;
  @FXML SearchableComboBox<String> kioskLocations8;
  @FXML SearchableComboBox<String> locations;
  @FXML SearchableComboBox<String> directions;
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
    fillSGListAndKioskLocation();

    // When changing the selected kiosk name
    kioskName.setOnAction(
        event -> {
          String currentKiosk = kioskName.getValue();
          if (currentKiosk != null && currentKiosk != lastKiosk) {
            lastKiosk = currentKiosk;
            updatePickers();
          }
        });
    date.setOnAction(
        event -> {
          LocalDate currentDate = date.getValue();
          if (currentDate != null && currentDate != lastDate) {
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
  }

  private void fillSGListAndKioskLocation() {
    List<String> temp =
        sg.stream().map(SignageComponentData::getKiosk_location).distinct().toList();
    kioskName.setItems(FXCollections.observableArrayList(temp));
  }

  public void submitForm() throws SQLException {

    for (SignageDirectionPicker signagePicker : allKioskLocations) {
      // if a location is not selected ignore
      if (signagePicker.getComboBox().getValue() != null) {
        // Update the signage for SQLRepo
        System.out.println(
            "Update Signage: "
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
    // Get the component data for only the locations of the selected kiosk
    List<SignageComponentData> temp =
        sg.stream()
            .filter(
                item ->
                    item.getKiosk_location().equals(currentKiosk)
                        && item.getDate().equals(currentDate)) //
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
    kioskLocations1.setValue(null);
    kioskLocations2.setValue(null);
    kioskLocations3.setValue(null);
    kioskLocations4.setValue(null);
    kioskLocations5.setValue(null);
    kioskLocations6.setValue(null);
    kioskLocations7.setValue(null);
    kioskLocations8.setValue(null);
    // locations.setValue(null);
    // directions.setValue(null);
  }

  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }
}
