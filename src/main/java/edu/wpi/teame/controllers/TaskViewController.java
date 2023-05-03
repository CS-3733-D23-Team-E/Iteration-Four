package edu.wpi.teame.controllers;

import static edu.wpi.teame.entities.ServiceRequestData.Status.DONE;
import static edu.wpi.teame.entities.ServiceRequestData.Status.IN_PROGRESS;

import edu.wpi.teame.App;
import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.*;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TaskViewController {
  @FXML Label pendingRequestText;

  @FXML Label inProgressRequestText;

  @FXML Label completedRequestText;

  @FXML Text inProgressRequestTitleText;
  @FXML Text pendingRequestsTitleText;
  @FXML Text completedRequestTitleText;
  @FXML Label nonCompletedTitleText;
  @FXML ListView<ServiceRequestData> outgoingRequestsList;

  // Elements for screen mode
  @FXML Rectangle inProgressRectangle;
  @FXML Rectangle completedRectangle;
  @FXML Rectangle pendingRectangle;

  @FXML
  public void initialize() {
    setupFactories();
    fillServiceRequestsFields();

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

                  if (Settings.INSTANCE.getScreenMode() == Settings.ScreenMode.DARK_MODE) {
                    darkMode();
                  } else if (Settings.INSTANCE.getScreenMode() == Settings.ScreenMode.LIGHT_MODE) {
                    lightMode();
                  }
                }));

    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    // Page Language Translation Code
    if (Settings.INSTANCE.getLanguage() == Settings.Language.ENGLISH) {
      translateToEnglish();
    } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
      translateToSpanish();
    } else // throw error for language not being a valid language
    {
      // throw some sort of error here at some point
    }
  }

  private void fillServiceRequestsFields() {
    List<ServiceRequestData> requests =
        new java.util.ArrayList<>(
            SQLRepo.INSTANCE.getFlowerRequestsList().stream()
                .map(request -> (ServiceRequestData) request)
                .toList());
    requests.addAll(
        SQLRepo.INSTANCE.getFurnitureRequestsList().stream()
            .map(request -> (ServiceRequestData) request)
            .toList());
    requests.addAll(
        SQLRepo.INSTANCE.getMealRequestsList().stream()
            .map(request -> (ServiceRequestData) request)
            .toList());
    requests.addAll(
        SQLRepo.INSTANCE.getOfficeSupplyList().stream()
            .map(request -> (ServiceRequestData) request)
            .toList());
    requests.addAll(
        SQLRepo.INSTANCE.getConfList().stream()
            .map(request -> (ServiceRequestData) request)
            .toList());
    requests.addAll(
        SQLRepo.INSTANCE.getMedicalSuppliesList().stream()
            .map(request -> (ServiceRequestData) request)
            .toList());
    requests.addAll(
        SQLRepo.INSTANCE.getRoomCleanupList().stream()
            .map(request -> (ServiceRequestData) request)
            .toList());

    List<ServiceRequestData> pendingRequests =
        requests.stream()
            .filter(request -> request.getRequestStatus().equals(ServiceRequestData.Status.PENDING))
            .toList();

    List<ServiceRequestData> inProgressRequests =
        requests.stream()
            .filter(request -> request.getRequestStatus().equals(IN_PROGRESS))
            .toList();
    List<ServiceRequestData> completedRequests =
        requests.stream().filter(request -> request.getRequestStatus().equals(DONE)).toList();

    inProgressRequestText.setText(inProgressRequests.size() + "");
    pendingRequestText.setText(pendingRequests.size() + "");
    completedRequestText.setText(completedRequests.size() + "");

    if (Employee.activeEmployee.getPermission() == Employee.Permission.STAFF) {
      // filter by employee
      requests =
          requests.stream()
              .filter(
                  request ->
                      (request.getAssignedStaff() != null
                          && request
                              .getAssignedStaff()
                              .equalsIgnoreCase(Employee.activeEmployee.getUsername())))
              .toList();
      System.out.println(Employee.activeEmployee.getUsername());
      nonCompletedTitleText.setText("Your Non-completed requests:");
    } else {
      nonCompletedTitleText.setText("All Non-completed requests:");
    }
    List<ServiceRequestData> nonCompleteRequests =
        requests.stream().filter(request -> !request.getRequestStatus().equals(DONE)).toList();

    outgoingRequestsList.setItems(FXCollections.observableList(nonCompleteRequests));
  }

  private void setupFactories() {
    outgoingRequestsList.setCellFactory(
        lv -> {
          ListCell<ServiceRequestData> cell = new ListCell<>();
          //
          // App.getRootPane().getStylesheets().add("edu/wpi/teame/styles/eStyleSheet.css");
          ContextMenu contextMenu = new ContextMenu();

          MenuItem pathfindingItem = new MenuItem();
          pathfindingItem.textProperty().set("View directions");
          pathfindingItem.setOnAction(
              event -> {
                System.out.println("open the pathfinding page!");
                sendPath(getLocFromRequest(cell.getItem()));
                Navigation.navigate(Screen.MAP);
              });
          MenuItem detailsItem = new MenuItem();
          detailsItem.textProperty().set("View details");
          detailsItem.setOnAction(
              event -> {
                System.out.println("open the service request page!");
              });
          MenuItem statusItem = new MenuItem();
          statusItem
              .textProperty()
              .set(cell.getItem() != null ? getUpdatedStatusText(cell.getItem()) : "");
          statusItem.setOnAction(
              event -> {
                upgradeStatus(cell.getItem());
              });
          contextMenu.getItems().addAll(pathfindingItem, /*detailsItem, */ statusItem);

          // adds the context menu to now-filled cells (if you are an admin)
          cell.emptyProperty()
              .addListener(
                  (obs, wasEmpty, isNowEmpty) -> {
                    if (!isNowEmpty) {
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
                        .setValue(
                            cell.getItem() != null ? formatServiceRequest(cell.getItem()) : "");
                    statusItem
                        .textProperty()
                        .set(cell.getItem() != null ? getUpdatedStatusText(cell.getItem()) : "");
                  });

          return cell;
        });
  }

  private String formatServiceRequest(ServiceRequestData request) {
    return request.getRequestType()
        + " request, ID "
        + request.getRequestID()
        + ": "
        + request.getRequestStatus();
  }

  private String getUpdatedStatusText(ServiceRequestData request) {
    switch (request.getRequestStatus()) {
      case PENDING:
        return "Switch status to IN_PROGRESS";
      case IN_PROGRESS:
        return "Switch status to DONE";
      case DONE:
        return "Delete Request";
      default:
        return "";
    }
  }

  private void upgradeStatus(ServiceRequestData request) {
    switch (request.getRequestStatus()) {
      case PENDING:
        SQLRepo.INSTANCE.updateServiceRequest(request, "status", "IN_PROGRESS");
        break;
      case IN_PROGRESS:
        SQLRepo.INSTANCE.updateServiceRequest(request, "status", "DONE");
        break;
      case DONE:
        SQLRepo.INSTANCE.deleteServiceRequest(request);
        break;
    }
    fillServiceRequestsFields();
  }

  public void translateToSpanish() {
    pendingRequestsTitleText.setText("Pendiente"); // Pending
    inProgressRequestTitleText.setText("En Curso"); // In Progress
    completedRequestTitleText.setText("Completo"); // Completed
    if (Employee.activeEmployee.getPermission().equals(Employee.Permission.ADMIN)) {
      nonCompletedTitleText.setText(
          "Todas Las Solicitudes No Completadas"); // Non-Completed Requests
    } else {
      nonCompletedTitleText.setText("Sus Solicitudes No Completadas"); // Non-Completed Requests
    }
    outgoingRequestsList.setPlaceholder(new Label("Sin solicitudes de servicio"));
  }

  public void translateToEnglish() {
    pendingRequestsTitleText.setText("Pending"); // Pending
    inProgressRequestTitleText.setText("In Progress"); // In Progress
    completedRequestTitleText.setText("Completed"); // Completed
    if (Employee.activeEmployee.getPermission().equals(Employee.Permission.ADMIN)) {
      nonCompletedTitleText.setText("All Non-Completed Requests"); // Non-Completed Requests
    } else {
      nonCompletedTitleText.setText("Your Non-Completed Requests"); // Non-Completed Requests
    }
    outgoingRequestsList.setPlaceholder(new Label("No service requests"));
  }

  public void darkMode() {
    pendingRequestsTitleText.setFill(Color.web("#f1f1f1"));
    pendingRequestText.setTextFill(Color.web("#f1f1f1"));
    inProgressRequestText.setTextFill(Color.web("#f1f1f1"));
    inProgressRequestTitleText.setFill(Color.web("#f1f1f1"));
    completedRequestText.setTextFill(Color.web("#f1f1f1"));
    completedRequestTitleText.setFill(Color.web("#f1f1f1"));
    nonCompletedTitleText.setTextFill(Color.web("f1f1f1"));
    inProgressRectangle.setFill(Color.web("#5C5C5C"));
    completedRectangle.setFill(Color.web("#5C5C5C"));
    pendingRectangle.setFill(Color.web("#5C5C5C"));
  }

  public void lightMode() {
    pendingRequestsTitleText.setFill(Color.web("#1f1f1f"));
    pendingRequestText.setTextFill(Color.web("#1f1f1f"));
    inProgressRequestText.setTextFill(Color.web("#1f1f1f"));
    inProgressRequestTitleText.setFill(Color.web("#1f1f1f"));
    completedRequestText.setTextFill(Color.web("#1f1f1f"));
    completedRequestTitleText.setFill(Color.web("#1f1f1f"));
    nonCompletedTitleText.setTextFill(Color.web("1f1f1f"));
    inProgressRectangle.setFill(Color.web("#f1f1f1"));
    completedRectangle.setFill(Color.web("#f1f1f1"));
    pendingRectangle.setFill(Color.web("#f1f1f1"));
  }

  private void sendPath(String loc) {
    App.getPrimaryStage().setUserData(loc);
  }

  private String getLocFromRequest(ServiceRequestData request) {
    switch (request.getRequestType()) {
      case MEALDELIVERY:
        return ((MealRequestData) request).getRoom();
        // break;
      case ROOMCLEANING:
        return ((RoomCleanupData) request).getRoom();
        // break;
      case CONFERENCEROOM:
        return ((ConferenceRequestData) request).getRoom();
        // break;
      case FLOWERDELIVERY:
        return ((FlowerRequestData) request).getRoom();
        // break;
      case FURNITUREDELIVERY:
        return ((FurnitureRequestData) request).getRoom();
        // break;
      case OFFICESUPPLIESDELIVERY:
        return ((OfficeSuppliesData) request).getRoom();
        // break;
      case MEDICALSUPPLIESDELIVERY:
        return ((MedicalSuppliesData) request).getRoom();
        // break;
      default:
        return null;
    }
  }
}
