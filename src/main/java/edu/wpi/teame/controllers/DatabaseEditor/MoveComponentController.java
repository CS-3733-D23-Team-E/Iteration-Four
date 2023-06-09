package edu.wpi.teame.controllers.DatabaseEditor;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.AlertData;
import edu.wpi.teame.map.HospitalNode;
import edu.wpi.teame.map.MoveAttribute;
import edu.wpi.teame.utilities.MoveUtilities;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.SearchableComboBox;

public class MoveComponentController {

  @FXML DatabaseEditorController databaseEditorController;

  @FXML SearchableComboBox<String> departmentMoveSelector;
  @FXML SearchableComboBox<Integer> newNodeSelector;
  @FXML SearchableComboBox<String> departmentOneSelector;
  @FXML SearchableComboBox<String> departmentTwoSelector;
  @FXML DatePicker moveDateSelector;
  @FXML Tab moveTab;
  @FXML Tab swapTab;
  @FXML MFXButton confirmButton;
  @FXML MFXButton resetButton;
  @FXML Label todayIsLabel;
  @FXML Label moveCountText;
  @FXML ListView<String> currentMoveList;
  @FXML TableView<MoveAttribute> futureMoveTable;
  @FXML TableColumn<MoveAttribute, String> nodeIDCol;
  @FXML TableColumn<MoveAttribute, String> nameCol;
  @FXML TableColumn<MoveAttribute, String> dateCol;
  @FXML AnchorPane previewPane; // the new image place
  @FXML AnchorPane movePreview;
  @FXML MovePreviewController movePreviewController;

  MoveUtilities movUtil;
  List<AlertData> alertsList;

  @FXML
  public void initialize() {

    databaseEditorController.setOnlySelected(databaseEditorController.moveEditorSwapButton);

    movUtil = new MoveUtilities();
    todayIsLabel.setText(todayIsLabel.getText() + movUtil.formatToday());
    refreshFields();
    initTableAndList();
    initButtons();
  }

  private void initButtons() {
    swapTab.setOnSelectionChanged(
        event -> {
          if (swapTab.isSelected()) {
            confirmButton.setOnAction(
                e -> {
                  swapDepartments();
                });
          }
          enablePreviewCondition();
        });
    moveTab.setOnSelectionChanged(
        event -> {
          if (moveTab.isSelected()) {
            confirmButton.setOnAction(e -> moveToNewNode());
          }
          enablePreviewCondition();
        });
    resetButton.setOnAction(
        event -> {
          resetFieldSelections();
          movePreviewController.unsetNode1();
          movePreviewController.unsetNode2();
        });
    confirmButton.setOnAction(
        e -> {
          moveToNewNode();
        });
    moveDateSelector.setOnAction(
        e -> {
          enablePreviewCondition();
        });

    departmentMoveSelector.setOnAction(event -> enablePreviewCondition());
    departmentOneSelector.setOnAction(event -> enablePreviewCondition());
    departmentTwoSelector.setOnAction(event -> enablePreviewCondition());
    newNodeSelector.setOnAction(event -> enablePreviewCondition());
    System.out.println(alertsList);
  }

  private void refreshFields() {

    ObservableList<String> availableLocations =
        FXCollections.observableList(
            movUtil.getMovesForDepartments().stream()
                .map(move -> move.getLongName())
                .sorted()
                .distinct()
                .toList());

    // List of node IDs that only contains the node IDs of departments
    List<Integer> nodeIDs =
        movUtil.getMovesForDepartments().stream().map(MoveAttribute::getNodeID).distinct().toList();
    newNodeSelector.setItems(FXCollections.observableList(nodeIDs));

    departmentMoveSelector.setItems(availableLocations);
    departmentOneSelector.setItems(availableLocations);
    departmentTwoSelector.setItems(availableLocations);

    moveDateSelector.setValue(LocalDate.now());
  }

  private void swapDepartments() {
    if ((departmentOneSelector.getValue() != null)
        && (departmentTwoSelector.getValue() != null)
        && (moveDateSelector.getValue() != null)) {
      // MoveAttribute moveOne = findMoveAttribute(departmentOneSelector.getValue());
      MoveAttribute moveOne =
          movUtil.findMostRecentMoveByDate(
              departmentOneSelector.getValue(),
              movUtil.toDateFromLocal(moveDateSelector.getValue()));
      //      MoveAttribute moveTwo = findMoveAttribute(departmentTwoSelector.getValue());
      MoveAttribute moveTwo =
          movUtil.findMostRecentMoveByDate(
              departmentTwoSelector.getValue(),
              movUtil.toDateFromLocal(moveDateSelector.getValue()));

      // make sure the current moves aren't on the same day as the suggested move
      if (movUtil.afterDate(moveOne, moveDateSelector.getValue()) != 0
          && movUtil.afterDate(moveTwo, moveDateSelector.getValue()) != 0) {
        MoveAttribute swaping1With2 =
            new MoveAttribute(
                moveOne.getNodeID(), moveTwo.getLongName(), moveDateSelector.getValue().toString());
        MoveAttribute swaping2With1 =
            new MoveAttribute(
                moveTwo.getNodeID(), moveOne.getLongName(), moveDateSelector.getValue().toString());

        SQLRepo.INSTANCE.addMove(swaping1With2);
        SQLRepo.INSTANCE.addMove(swaping2With1);
        //        AlertData alert =
        //            new AlertData(
        //                1,
        //                departmentOneSelector.getValue()
        //                    + " is swapping location with "
        //                    + departmentTwoSelector.getValue()
        //                    + " on "
        //                    + moveDateSelector.getValue().toString());
        AlertData alert = movUtil.alertFromSwap(swaping1With2, swaping2With1);

        SQLRepo.INSTANCE.addAlert(alert);
        initTableAndList();
        resetFieldSelections();
      } else {
        // Throw an error in a popup or around the text box
        System.out.println("The move you tried to add is too close to another move!");
      }
    }
  }

  private void moveToNewNode() {
    if ((departmentMoveSelector.getValue() != null)
        && (newNodeSelector.getValue() != null)
        && (moveDateSelector.getValue() != null)) {

      MoveAttribute toBeMoved =
          movUtil.findMostRecentMoveByDate(
              departmentMoveSelector.getValue(),
              movUtil.toDateFromLocal(moveDateSelector.getValue()));
      MoveAttribute newMove =
          new MoveAttribute(
              newNodeSelector.getValue(),
              toBeMoved.getLongName(),
              moveDateSelector.getValue().toString());
      SQLRepo.INSTANCE.addMove(newMove);
      //      AlertData alert =
      //          new AlertData(
      //              1,
      //              departmentMoveSelector.getValue()
      //                  + " is moving to Node "
      //                  + newNodeSelector.getValue()
      //                  + " on "
      //                  + toBeMoved.getDate());
      AlertData alert = movUtil.alertFromMove(newMove);
      SQLRepo.INSTANCE.addAlert(alert);
      initTableAndList();
      resetFieldSelections();
    }
  }

  private void resetFieldSelections() {
    departmentMoveSelector.setValue(null);
    departmentOneSelector.setValue(null);
    departmentTwoSelector.setValue(null);
    moveDateSelector.setValue(LocalDate.now());
    newNodeSelector.setValue(null);
  }

  private void initTableAndList() {
    nodeIDCol.setCellValueFactory(new PropertyValueFactory<MoveAttribute, String>("nodeID"));
    nameCol.setCellValueFactory(new PropertyValueFactory<MoveAttribute, String>("longName"));
    dateCol.setCellValueFactory(new PropertyValueFactory<MoveAttribute, String>("date"));

    futureMoveTable.setItems(FXCollections.observableList(movUtil.getFutureMoves()));

    currentMoveList.setItems(FXCollections.observableList(movUtil.getCurrentMoveMessages()));

    moveCountText.setText(currentMoveList.getItems().size() + " Move(s) Today: ");
  }

  private void enablePreviewCondition() {
    if (swapTab.isSelected()) {
      movePreviewController.setBidirectional(true);
      if (departmentTwoSelector.getValue() != null) {
        movePreviewController.setNode2(
            HospitalNode.allNodes.get(
                movUtil
                        .findMostRecentMoveByDate(
                            departmentTwoSelector.getValue(),
                            movUtil.toDateFromLocal(moveDateSelector.getValue()))
                        .getNodeID()
                    + ""),
            departmentTwoSelector.getValue());
      } else {
        movePreviewController.unsetNode2();
      }
      if (departmentOneSelector.getValue() != null) {
        movePreviewController.setNode1(
            HospitalNode.allNodes.get(
                movUtil
                        .findMostRecentMoveByDate(
                            departmentOneSelector.getValue(),
                            movUtil.toDateFromLocal(moveDateSelector.getValue()))
                        .getNodeID()
                    + ""),
            departmentOneSelector.getValue());
      } else {
        movePreviewController.unsetNode1();
      }
    } else {
      movePreviewController.setBidirectional(false);
      if (departmentMoveSelector.getValue() != null) {
        movePreviewController.setNode1(
            HospitalNode.allNodes.get(
                movUtil
                        .findMostRecentMoveByDate(
                            departmentMoveSelector.getValue(),
                            movUtil.toDateFromLocal(moveDateSelector.getValue()))
                        .getNodeID()
                    + ""),
            departmentMoveSelector.getValue());
      } else {
        movePreviewController.unsetNode1();
      }
      if (newNodeSelector.getValue() != null) {
        movePreviewController.setNode2(
            HospitalNode.allNodes.get(newNodeSelector.getValue() + ""), "New Location");
      } else {
        movePreviewController.unsetNode2();
      }
      //
    }
  }
}
