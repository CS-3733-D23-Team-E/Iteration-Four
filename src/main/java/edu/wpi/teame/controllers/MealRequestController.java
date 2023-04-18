package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.ServiceRequestData;
import edu.wpi.teame.map.LocationName;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.controlsfx.control.SearchableComboBox;
import org.json.JSONObject;

public class MealRequestController implements IRequestController {
  @FXML MFXButton returnButtonMealRequest;
  @FXML MFXButton cancelButton;
  @FXML MFXButton submitButton;
  @FXML TextField notes;
  @FXML TextField recipientName;
  @FXML SearchableComboBox<String> roomName;
  @FXML SearchableComboBox<String> deliveryTime;
  @FXML DatePicker deliveryDate;
  @FXML SearchableComboBox<String> mainCourse;
  @FXML SearchableComboBox<String> sideCourse;
  @FXML SearchableComboBox<String> drinkChoice;

  @FXML TextField allergies;
  @FXML SearchableComboBox<String> assignedStaff;
  @FXML MFXButton resetButton;

  ObservableList<String> deliveryTimes =
      FXCollections.observableArrayList(
          "10am - 11am", "11am - 12pm", "12pm - 1pm", "1pm - 2pm", "2pm - 3pm", "3pm - 4pm");

  ObservableList<String> mainCourses =
      FXCollections.observableArrayList(
          "Hamburger", "Cheeseburger", "Grilled Cheese", "Chicken Nuggets");
  ObservableList<String> sideCourses =
      FXCollections.observableArrayList("Fries", "Apple Slices", "Tater Tots", "Carrots");

  ObservableList<String> drinks =
      FXCollections.observableArrayList("Water", "Apple Juice", "Orange Juice", "Coffee", "Tea");

  @FXML
  public void initialize() {
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
    roomName.setItems(names);
    mainCourse.setItems(mainCourses);
    sideCourse.setItems(sideCourses);
    drinkChoice.setItems(drinks);
    deliveryTime.setItems(deliveryTimes);
    cancelButton.setOnMouseClicked(event -> cancelRequest());
    submitButton.setOnMouseClicked(event -> sendRequest());
    resetButton.setOnMouseClicked(event -> clearForm());
  }

  public ServiceRequestData sendRequest() {

    // Create the json to store the values
    JSONObject requestData = new JSONObject();
    requestData.put("recipientName", recipientName.getText());
    requestData.put("roomName", roomName.getValue());
    requestData.put("deliveryTime", deliveryTime.getValue());
    requestData.put("mainCourse", mainCourse.getValue());
    requestData.put("sideCourse", sideCourse.getValue());
    requestData.put("drinkChoice", drinkChoice.getValue());
    requestData.put("deliveryDate", deliveryDate.getValue());
    requestData.put("notes", notes.getText());

    // Create the service request data
    ServiceRequestData mealRequestData =
        new ServiceRequestData(
            ServiceRequestData.RequestType.MEALDELIVERY,
            requestData,
            ServiceRequestData.Status.PENDING,
            assignedStaff.getValue());

    // Return to home screen
    Navigation.navigate(Screen.HOME);

    SQLRepo.INSTANCE.addServiceRequest(mealRequestData);

    return mealRequestData;
  }

  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }

  // Clears the current service request fields
  public void clearForm() {
    recipientName.clear();
    roomName.setValue(null);
    deliveryTime.setValue(null);
    mainCourse.setValue(null);
    sideCourse.setValue(null);
    drinkChoice.setValue(null);
    deliveryDate.setValue(null);
    notes.clear();
    assignedStaff.setValue(null);
  }
}
