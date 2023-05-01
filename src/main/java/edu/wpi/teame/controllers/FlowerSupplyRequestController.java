package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.entities.MedicalSupplyData;
import edu.wpi.teame.entities.ServiceRequestData;
import edu.wpi.teame.map.LocationName;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.*;
import java.util.List;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class FlowerSupplyRequestController {

  @FXML Button clear;
  @FXML Button cancel;
  @FXML Button submit;
  @FXML TextField recipientsName;
  @FXML MFXFilterComboBox locationName;
  @FXML MFXDatePicker date;
  @FXML MFXFilterComboBox hours;
  @FXML MFXFilterComboBox minutes;
  @FXML MFXFilterComboBox ampm;
  @FXML MFXFilterComboBox staffAssigned;
  @FXML TextField notes;
  @FXML MFXButton item1Minus;
  @FXML MFXButton item1Plus;
  @FXML TextField item1Quantity;
  @FXML MFXButton item2Minus;
  @FXML MFXButton item2Plus;
  @FXML TextField item2Quantity;
  @FXML MFXButton item3Minus;
  @FXML MFXButton item3Plus;
  @FXML TextField item3Quantity;
  @FXML MFXButton item4Minus;
  @FXML MFXButton item4Plus;
  @FXML TextField item4Quantity;
  @FXML MFXButton item5Minus;
  @FXML MFXButton item5Plus;
  @FXML TextField item5Quantity;
  @FXML MFXButton item6Minus;
  @FXML MFXButton item6Plus;
  @FXML TextField item6Quantity;
  @FXML ImageView item1Img;
  @FXML ImageView item2Img;
  @FXML ImageView item3Img;
  @FXML ImageView item4Img;
  @FXML ImageView item5Img;
  @FXML ImageView item6Img;

  @FXML AnchorPane RSS;
  ObservableList<String> staffMembers = FXCollections.observableArrayList();

  ObservableList<String> hoursList =
      FXCollections.observableArrayList(
          "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
  ObservableList<String> minutesList = FXCollections.observableArrayList("00", "15", "30", "45");
  ObservableList<String> ampmList = FXCollections.observableArrayList("A.M.", "P.M.");
  int item1 = 0;
  int item2 = 0;

  int item3 = 0;

  int item4 = 0;
  int item5 = 0;

  int item6 = 0;

  public void initializeButtons() {

    // Item 1
    this.item1Minus.setOnAction(
        event -> {
          item1--;
          if (item1 < 0) {
            item1 = 0;
          }

          this.item1Quantity.setText(Integer.toString(item1));
        });
    this.item1Plus.setOnAction(
        event -> {
          item1++;
          this.item1Quantity.setText(Integer.toString(item1));
        });

    // Item 2

    this.item2Minus.setOnAction(
        event -> {
          item2--;
          if (item2 < 0) {
            item2 = 0;
          }

          this.item2Quantity.setText(Integer.toString(item2));
        });
    this.item2Plus.setOnAction(
        event -> {
          item2++;
          this.item2Quantity.setText(Integer.toString(item2));
        });

    // Item 3
    this.item3Quantity.setText(Integer.toString(item3));

    this.item3Minus.setOnAction(
        event -> {
          item3--;
          if (item3 < 0) {
            item3 = 0;
          }

          this.item3Quantity.setText(Integer.toString(item3));
        });
    this.item3Plus.setOnAction(
        event -> {
          item3++;
          this.item3Quantity.setText(Integer.toString(item3));
        });
    // Item 4
    this.item4Quantity.setText(Integer.toString(item4));

    this.item4Minus.setOnAction(
        event -> {
          item4--;
          if (item4 < 0) {
            item4 = 0;
          }

          this.item4Quantity.setText(Integer.toString(item4));
        });
    this.item4Plus.setOnAction(
        event -> {
          item4++;
          this.item4Quantity.setText(Integer.toString(item4));
        });
    // Item 5
    this.item5Quantity.setText(Integer.toString(item5));

    this.item5Minus.setOnAction(
        event -> {
          item5--;
          if (item5 < 0) {
            item5 = 0;
          }

          this.item5Quantity.setText(Integer.toString(item5));
        });
    this.item5Plus.setOnAction(
        event -> {
          item5++;
          this.item5Quantity.setText(Integer.toString(item5));
        });
    // Item 6
    this.item6Quantity.setText(Integer.toString(item6));

    this.item6Minus.setOnAction(
        event -> {
          item6--;
          if (item6 < 0) {
            item6 = 0;
          }

          this.item6Quantity.setText(Integer.toString(item6));
        });
    this.item6Plus.setOnAction(
        event -> {
          item6++;
          this.item6Quantity.setText(Integer.toString(item6));
        });
  }

  public void initialize() {
    initializeButtons();

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

    List<Employee> employeeList = SQLRepo.INSTANCE.getEmployeeList();
    for (Employee emp : employeeList) {
      staffMembers.add(emp.getUsername());
    }
    staffAssigned.setItems(FXCollections.observableArrayList(staffMembers));
    locationName.setItems(names);

    cancel.setOnMouseClicked(event -> cancelRequest());
    submit.setOnMouseClicked(event -> sendRequest());
    clear.setOnMouseClicked(event -> clearForm());

    hours.setItems(hoursList);
    minutes.setItems(minutesList);
    ampm.setItems(ampmList);
    clearForm();
  }

  public MedicalSupplyData sendRequest() {

    String time = hours.getText() + ":" + minutes.getText() + " " + ampm.getText();

    MedicalSupplyData md =
        new MedicalSupplyData(
            0,
            recipientsName.getText(),
            locationName.getText(),
            date.getValue().toString(),
            time,
            staffAssigned.getText(),
            notes.getText(),
            item1Quantity.getText(),
            item2Quantity.getText(),
            item3Quantity.getText(),
            item4Quantity.getText(),
            item5Quantity.getText(),
            item6Quantity.getText(),
            ServiceRequestData.Status.PENDING);

    SQLRepo.INSTANCE.addServiceRequest(md);
    clearForm();
    return md;
  }

  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }

  public void clearForm() {
    recipientsName.clear();
    locationName.setValue(null);
    date.setValue(null);
    notes.clear();
    staffAssigned.setValue(null);
    hours.setValue(null);
    minutes.setValue(null);
    ampm.setValue(null);
    item1Quantity.setText("0");
    item2Quantity.setText("0");
    item3Quantity.setText("0");
    item4Quantity.setText("0");
    item5Quantity.setText("0");
    item6Quantity.setText("0");
  }
}
