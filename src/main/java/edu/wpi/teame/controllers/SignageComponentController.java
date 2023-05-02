package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.SignageComponentData;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
  List<SignageComponentData> sg = SQLRepo.INSTANCE.getSignageList();;
  List<SearchableComboBox> allKioskLocations = new LinkedList<>();

  private void fillSGListAndKioskLocation() {
    List<String> temp =
        sg.stream().map(SignageComponentData::getKiosk_location).distinct().toList();
    kioskName.setItems(FXCollections.observableArrayList(temp));
  }

  private void updateKioskLocation(String removedLocation) {}

  ObservableList<String> signageDirections =
      FXCollections.observableArrayList(
          SignageComponentData.ArrowDirections.getAllDirections().stream()
              .map(dir -> SignageComponentData.ArrowDirections.directionToString(dir))
              .toList());

  @FXML
  public void initialize() {
    // formSubmitted.setVisible(false);
    cancelButton.setOnMouseClicked(event -> cancelRequest());
    resetButton.setOnMouseClicked(event -> clearForm());
    fillSGListAndKioskLocation();
    /*close.setOnMouseClicked(
    event -> {
      clearForm();
      Navigation.navigate(Screen.SIGNAGE_EDITOR_PAGE);
      // formSubmitted.setVisible(false);
    });*/

    kioskName.setOnAction(
        event -> {
          if (kioskName.getValue() != null) {
            List<String> temp =
                sg.stream()
                    .filter(item -> item.getKiosk_location().equals(kioskName.getValue()))
                    .map(SignageComponentData::getLocationNames)
                    .toList();
            kioskLocations0.setItems(FXCollections.observableArrayList(temp));
            kioskLocations1.setItems(FXCollections.observableArrayList(temp));
            kioskLocations2.setItems(FXCollections.observableArrayList(temp));
            kioskLocations3.setItems(FXCollections.observableArrayList(temp));
            kioskLocations4.setItems(FXCollections.observableArrayList(temp));
            kioskLocations5.setItems(FXCollections.observableArrayList(temp));
            kioskLocations6.setItems(FXCollections.observableArrayList(temp));
            kioskLocations7.setItems(FXCollections.observableArrayList(temp));
            kioskLocations8.setItems(FXCollections.observableArrayList(temp));

            allKioskLocations.add(kioskLocations0);
            allKioskLocations.add(kioskLocations1);
            allKioskLocations.add(kioskLocations2);
            allKioskLocations.add(kioskLocations3);
            allKioskLocations.add(kioskLocations4);
            allKioskLocations.add(kioskLocations5);
            allKioskLocations.add(kioskLocations6);
            allKioskLocations.add(kioskLocations7);
            allKioskLocations.add(kioskLocations8);
          }
        });

    // directions.setItems(signageDirections);

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

  public SignageComponentData submitForm() throws SQLException {
    System.out.println("send signage component change");

    for (SearchableComboBox kioskLocation : allKioskLocations) {
      if (kioskLocation.getValue() != null) {

        SignageComponentData.ArrowDirections get = SignageComponentData.ArrowDirections.RIGHT;

        // Create the service request data
        SignageComponentData requestData =
            new SignageComponentData(
                date.getValue().toString(), kioskLocations1.getValue(), locations.getValue(), get);

        SQLRepo.INSTANCE.updateSignage(requestData, "arrowDirection", directions.getValue());
      }
    }

    SignageComponentData.ArrowDirections get =
        SQLRepo.INSTANCE.getDirectionFromPKeyL(
            date.getValue().toString(), kioskLocations1.getValue(), locations.getValue());

    // Create the service request data
    SignageComponentData requestData =
        new SignageComponentData(
            date.getValue().toString(), kioskLocations1.getValue(), locations.getValue(), get);

    SQLRepo.INSTANCE.updateSignage(requestData, "arrowDirection", directions.getValue());

    return requestData;
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
