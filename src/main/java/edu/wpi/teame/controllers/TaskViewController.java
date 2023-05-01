package edu.wpi.teame.controllers;

import static edu.wpi.teame.entities.ServiceRequestData.Status.DONE;
import static edu.wpi.teame.entities.ServiceRequestData.Status.IN_PROGRESS;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.entities.ServiceRequestData;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class TaskViewController {
  @FXML Label pendingRequestText;

  @FXML Label inProgressRequestText;

  @FXML Label completedRequestText;

  @FXML Text inProgressRequestTitleText;
  @FXML Text pendingRequestsTitleText;
  @FXML Text completedRequestTitleText;
  @FXML Label nonCompletedTitleText;
  @FXML ListView<ServiceRequestData> outgoingRequestsList;

  @FXML
  public void initialize() {
    setupFactories();
    fillServiceRequestsFields();
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

    if (Employee.activeEmployee.getPermission().equals("STAFF")) {
      // filter by employee
      requests =
          requests.stream()
              .filter(
                  request ->
                      request
                          .getAssignedStaff()
                          .equalsIgnoreCase(Employee.activeEmployee.getUsername()))
              .toList();
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
                System.out.println("open the damn pathfinding page!");
              });
          MenuItem detailsItem = new MenuItem();
          detailsItem.textProperty().set("View details");
          detailsItem.setOnAction(
              event -> {
                System.out.println("open the damn service request page!");
              });
          MenuItem statusItem = new MenuItem();
          statusItem
              .textProperty()
              .set(cell.getItem() != null ? getUpdatedStatusText(cell.getItem()) : "");
          statusItem.setOnAction(
              event -> {
                System.out.println("edited the damn database!");
                upgradeStatus(cell.getItem());
              });
          contextMenu.getItems().addAll(pathfindingItem, detailsItem, statusItem);

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
}
