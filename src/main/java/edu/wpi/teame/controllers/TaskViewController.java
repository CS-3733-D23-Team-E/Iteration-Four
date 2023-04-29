package edu.wpi.teame.controllers;

import static edu.wpi.teame.entities.ServiceRequestData.Status.DONE;
import static edu.wpi.teame.entities.ServiceRequestData.Status.IN_PROGRESS;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.entities.ServiceRequestData;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class TaskViewController {
  @FXML Label pendingRequestText;

  @FXML Label inProgressRequestText;

  @FXML Label completedRequestText;

  @FXML Text inProgressRequestTitleText;
  @FXML Text pendingRequestsTitleText;
  @FXML Text completedRequestTitleText;
  @FXML Label nonCompletedTitleText;
  @FXML ListView<String> outgoingRequestsList;

  @FXML
  public void initialize() {
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

    List<String> requestTexts =
        nonCompleteRequests.stream()
            .map(
                request ->
                    (request.getRequestType()
                        + " request, ID "
                        + request.getRequestID()
                        + ": "
                        + request.getRequestStatus()))
            .toList();
    outgoingRequestsList.setItems(FXCollections.observableList(requestTexts));
  }
}
