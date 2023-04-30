package edu.wpi.teame.controllers.DatabaseEditor;

import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.utilities.ButtonUtilities;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class DatabaseEditorController {

  @FXML DatabaseMapViewController mapViewController;
  @FXML DatabaseTableViewController tableViewController;
  @FXML DatabaseServiceRequestViewController serviceRequestViewController;
  @FXML MoveComponentController moveComponentController;

  @FXML AnchorPane tableView;
  @FXML AnchorPane moveView;
  @FXML AnchorPane serviceView;
  @FXML AnchorPane mapView;
  @FXML MFXButton menuButton;
  @FXML MFXButton menuBarHome;
  @FXML MFXButton menuBarServices;
  @FXML MFXButton menuBarMaps;

  @FXML MFXButton menuBarAbout;
  @FXML MFXButton menuBarDatabase;

  @FXML MFXButton menuBarSignage;
  @FXML MFXButton menuBarBlank;
  @FXML MFXButton menuBarExit;
  @FXML VBox menuBar;
  @FXML AnchorPane employeeView;

  @FXML MFXButton tableEditorSwapButton;
  @FXML MFXButton mapEditorSwapButton;
  @FXML MFXButton moveEditorSwapButton;
  @FXML MFXButton requestsEditorSwapButton;
  @FXML MFXButton employeeEditorSwapButton;
  @FXML VBox importExportZone;

  @FXML MFXButton importButton;
  @FXML MFXButton exportButton;

  @FXML MFXButton backButton;
  @FXML Label editorTitle;
  @FXML ImageView aboutI;
  @FXML ImageView servicesI;
  @FXML ImageView signageI;
  @FXML ImageView pathfindingI;
  @FXML ImageView databaseI;
  @FXML ImageView settingsI;
  @FXML ImageView exitI;
  @FXML ImageView homeI;

  Boolean menuVisibilty = false;

  String nyay = "\u00F1"; // ?
  String aA = "\u0301"; // ?
  String aE = "\u00E9"; // ?
  String aI = "\u00ED"; // ?
  String aO = "\u00F3"; // ?
  String aU = "\u00FA"; // ?
  String aQuestion = "\u00BF"; // Upside down question mark

  @FXML
  public void initialize() {
    onlyVisible(tableView);
    importExportZone.setVisible(true);
    onlyDisable(tableEditorSwapButton);
    Font englishLabel = new Font("Roboto", 36);
    Font spanishLabel = new Font("Roboto", 26);
    if (Settings.INSTANCE.getLanguage() == Settings.Language.ENGLISH) {
      editorTitle.setFont(englishLabel);
      editorTitle.setText("Table Editor");
    } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
      editorTitle.setFont(spanishLabel);
      editorTitle.setText("Editor de Tablas");
    } else {
      // put other languages here
    }
    // Initially set the menu bar to invisible
    menuBarVisible(false);

    // When the menu button is clicked, invert the value of menuVisibility and set the menu bar to
    // that value
    // (so each time the menu button is clicked it changes the visibility of menu bar back and
    // forth)
    menuButton.setOnMouseClicked(
        event -> {
          menuVisibilty = !menuVisibilty;
          menuBarVisible(menuVisibilty);
        });

    // Navigation controls for the button in the menu bar
    menuBarHome.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.HOME);
          menuVisibilty = !menuVisibilty;
        });

    // makes the menu bar buttons get highlighted when the mouse hovers over them
    ButtonUtilities.mouseSetupMenuBar(
        menuBarHome,
        "baseline-left",
        homeI,
        "images/house-blank.png",
        "images/house-blank-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarServices,
        "baseline-left",
        servicesI,
        "images/hand-holding-medical.png",
        "images/hand-holding-medical-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarSignage,
        "baseline-left",
        signageI,
        "images/diamond-turn-right.png",
        "images/diamond-turn-right-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarMaps, "baseline-left", pathfindingI, "images/marker.png", "images/marker-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarDatabase,
        "baseline-left",
        databaseI,
        "images/folder-tree.png",
        "images/folder-tree-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarAbout, "baseline-left", aboutI, "images/abouticon.png", "images/abouticon-blue.png");

    ButtonUtilities.mouseSetupMenuBar(
        menuBarExit,
        "baseline-center",
        exitI,
        "images/sign-out-alt.png",
        "images/sign-out-alt-blue.png");

    initButtons();

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

  private void initButtons() {
    Font englishLabel = new Font("Roboto", 36);
    Font spanishLabel = new Font("Roboto", 26);
    tableEditorSwapButton.setOnAction(
        event -> {
          onlyVisible(tableView);
          importExportZone.setVisible(true);
          onlyDisable(tableEditorSwapButton);

          if (Settings.INSTANCE.getLanguage() == Settings.Language.ENGLISH) {
            editorTitle.setFont(englishLabel);
            editorTitle.setText("Table Editor");
          } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
            editorTitle.setFont(spanishLabel);
            editorTitle.setText("Editor de Tablas");
          } else {
            // put other languages here
          }
        });
    mapEditorSwapButton.setOnAction(
        event -> {
          onlyVisible(mapView);
          importExportZone.setVisible(false);
          onlyDisable(mapEditorSwapButton);
          if (Settings.INSTANCE.getLanguage() == Settings.Language.ENGLISH) {
            editorTitle.setFont(englishLabel);
            editorTitle.setText("Map Editor");
          } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
            editorTitle.setFont(spanishLabel);
            editorTitle.setText("Editor de Mapas");
          } else {
            // put other languages here
          }
        });
    moveEditorSwapButton.setOnAction(
        event -> {
          onlyVisible(moveView);
          importExportZone.setVisible(false);
          onlyDisable(moveEditorSwapButton);
          if (Settings.INSTANCE.getLanguage() == Settings.Language.ENGLISH) {
            editorTitle.setFont(englishLabel);
            editorTitle.setText("Move Editor");
          } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
            editorTitle.setFont(spanishLabel);
            editorTitle.setText("Editor de Movimiento");
          } else {
            // put other languages here
          }
        });
    requestsEditorSwapButton.setOnAction(
        event -> {
          onlyVisible(serviceView);
          importExportZone.setVisible(false);
          onlyDisable(requestsEditorSwapButton);
          if (Settings.INSTANCE.getLanguage() == Settings.Language.ENGLISH) {
            editorTitle.setFont(englishLabel);
            editorTitle.setText("Request Editor");
          } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
            editorTitle.setFont(spanishLabel);
            editorTitle.setText("Editor de Solicitudes");
          } else {
            // put other languages here
          }
        });
    employeeEditorSwapButton.setOnAction(
        event -> {
          onlyVisible(employeeView);
          importExportZone.setVisible(false);
          onlyDisable(employeeEditorSwapButton);
          if (Settings.INSTANCE.getLanguage() == Settings.Language.ENGLISH) {
            editorTitle.setFont(englishLabel);
            editorTitle.setText("Employee editor");
          } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
            editorTitle.setFont(spanishLabel);
            editorTitle.setText("Editor de Empleados");
          } else {
            // put other languages here
          }
        });

    importButton.setOnAction(event -> tableViewController.importTable());
    exportButton.setOnAction(event -> tableViewController.exportTable());

    backButton.setOnAction(event -> Navigation.navigate(Screen.HOME));
  }

  private void onlyDisable(MFXButton btn) {
    tableEditorSwapButton.setDisable(false);
    mapEditorSwapButton.setDisable(false);
    moveEditorSwapButton.setDisable(false);
    requestsEditorSwapButton.setDisable(false);
    employeeEditorSwapButton.setDisable(false);
    btn.setDisable(true);
  }

  private void onlyVisible(AnchorPane pane) {
    tableView.setVisible(false);
    moveView.setVisible(false);
    serviceView.setVisible(false);
    mapView.setVisible(false);
    employeeView.setVisible(false);
    pane.setVisible(true);
  }

  public void menuBarVisible(boolean bool) {
    menuBar.setVisible(bool);
  }

  public void translateToSpanish() {
    // Left Side Buttons
    Font spanishButtons = new Font("Roboto", 13);
    tableEditorSwapButton.setText("Editor de Tablas"); // Table Editor
    mapEditorSwapButton.setText("Editor de Mapas"); // Map Editor
    moveEditorSwapButton.setFont(spanishButtons);
    requestsEditorSwapButton.setFont(spanishButtons);
    employeeEditorSwapButton.setFont(spanishButtons);
    moveEditorSwapButton.setText("Editor de Movimiento"); // Move Editor
    requestsEditorSwapButton.setText("Editor de Solicitudes"); // Requests Editor
    employeeEditorSwapButton.setText("Editor de Empleados"); // Employee Editor
    importButton.setText("Importaci" + aO + "n"); // Import
    exportButton.setText("Exportar"); // Export
    backButton.setText("Volver a Principal"); // Back to Home
  }

  public void translateToEnglish() {
    // Left Side Buttons
    Font englishButtons = new Font("Roboto", 16);
    tableEditorSwapButton.setText("Table Editor"); // Keep in English
    mapEditorSwapButton.setText("Map Editor"); // Keep in English
    moveEditorSwapButton.setFont(englishButtons);
    requestsEditorSwapButton.setFont(englishButtons);
    employeeEditorSwapButton.setFont(englishButtons);
    moveEditorSwapButton.setText("Move Editor"); // Keep in English
    requestsEditorSwapButton.setText("Requests Editor"); // Keep in English
    employeeEditorSwapButton.setText("Employee Editor"); // Keep in English
    importButton.setText("Import"); // Keep in English
    exportButton.setText("Export"); // Keep in English
    backButton.setText("Back to Home"); // Keep in English
  }
}
