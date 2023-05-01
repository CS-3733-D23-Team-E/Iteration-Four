package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.SignageComponentData;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class SignageComponentController {
  @FXML HBox finalButtonsHBox;
  @FXML VBox signageFormVBox;
  @FXML MFXButton submitButton;
  @FXML MFXButton cancelButton;
  @FXML MFXButton resetButton;
  @FXML MFXDatePicker date;
  @FXML SearchableComboBox<String> kioskLocations;
  @FXML SearchableComboBox<String> locations;
  @FXML SearchableComboBox<String> directions;
  @FXML MFXButton close;
  //  @FXML VBox formSubmitted;
  @FXML ListView<String> signageComponent;
  List<SignageComponentData> sg = SQLRepo.INSTANCE.getSignageList();;

  private void fillSGListAndKioskLocation() {
    List<String> temp =
        sg.stream().map(SignageComponentData::getKiosk_location).distinct().toList();
    kioskLocations.setItems(FXCollections.observableArrayList(temp));
  }

  ObservableList<String> allLocations = FXCollections.observableArrayList();

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

    kioskLocations.setOnAction(
        event -> {
          if (kioskLocations.getValue() != null) {
            List<String> temp =
                sg.stream()
                    .filter(item -> item.getKiosk_location().equals(kioskLocations.getValue()))
                    .map(SignageComponentData::getLocationNames)
                    .toList();
            signageComponent.setItems(FXCollections.observableArrayList(temp));
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
    // signageFormVBox.setVisible(false);
    finalButtonsHBox.setVisible(false);
    System.out.println("send signage component change");

    SignageComponentData.ArrowDirections get =
        SQLRepo.INSTANCE.getDirectionFromPKeyL(
            date.getValue().toString(), kioskLocations.getValue(), locations.getValue());

    // Create the service request data
    SignageComponentData requestData =
        new SignageComponentData(
            date.getValue().toString(), kioskLocations.getValue(), locations.getValue(), get);

    SQLRepo.INSTANCE.updateSignage(requestData, "arrowDirection", directions.getValue());

    return requestData;
  }

  public void clearForm() {
    signageFormVBox.setVisible(true);
    finalButtonsHBox.setVisible(true);
    date.setValue(null);
    kioskLocations.setValue(null);
    // locations.setValue(null);
    // directions.setValue(null);
  }

  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }
}
