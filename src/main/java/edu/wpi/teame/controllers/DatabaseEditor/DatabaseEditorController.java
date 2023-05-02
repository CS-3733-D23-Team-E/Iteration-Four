package edu.wpi.teame.controllers.DatabaseEditor;

import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class DatabaseEditorController {

  @FXML DatabaseTableViewController tableViewController;
  @FXML ToggleGroup editorGroup;
  @FXML ToggleButton tableEditorSwapButton;
  @FXML ToggleButton mapEditorSwapButton;
  @FXML ToggleButton moveEditorSwapButton;
  @FXML ToggleButton requestsEditorSwapButton;
  @FXML ToggleButton employeeEditorSwapButton;
  @FXML VBox importExportZone;
  @FXML Button importButton;
  @FXML Button exportButton;
  @FXML Button backButton;

  String nyay = "\u00F1"; // �
  String aA = "\u0301"; // �
  String aE = "\u00E9"; // �
  String aI = "\u00ED"; // �
  String aO = "\u00F3"; // �
  String aU = "\u00FA"; // �
  String aQuestion = "\u00BF"; // Upside down question mark

  @FXML
  public void initialize() {

    editorGroup
        .selectedToggleProperty()
        .addListener(
            ((observable, oldToggle, newToggle) -> {
              if (newToggle == null) {
                oldToggle.setSelected(true);
                return;
              }

              importExportZone.setVisible(newToggle.equals(tableEditorSwapButton));
            }));

    importExportZone.setVisible(true);

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
    tableEditorSwapButton.setOnAction(
        event -> {
          Navigation.navigate(Screen.DATABASE_TABLEVIEW);
        });
    mapEditorSwapButton.setOnAction(
        event -> {
          Navigation.navigate(Screen.DATABASE_MAPVIEW);
        });
    moveEditorSwapButton.setOnAction(
        event -> {
          Navigation.navigate(Screen.MOVE_COMPONENT);
        });
    requestsEditorSwapButton.setOnAction(
        event -> {
          Navigation.navigate(Screen.DATABASE_SERVICEVIEW);
        });
    employeeEditorSwapButton.setOnAction(
        event -> {
          Navigation.navigate(Screen.DATABASE_EMPLOYEEVIEW);
        });

    importButton.setOnAction(event -> tableViewController.importTable());
    exportButton.setOnAction(event -> tableViewController.exportTable());

    backButton.setOnAction(event -> Navigation.navigate(Screen.HOME));
  }

  private void resetToggleButtons() {
    tableEditorSwapButton.setSelected(false);
    mapEditorSwapButton.setSelected(false);
    moveEditorSwapButton.setSelected(false);
    employeeEditorSwapButton.setSelected(false);
    requestsEditorSwapButton.setSelected(false);
  }

  public void setOnlySelected(ToggleButton button) {
    resetToggleButtons();
    button.setSelected(true);
  }

  public void translateToSpanish() {
    // Left Side Buttons
    Font spanishButtons = new Font("Roboto", 12);
    tableEditorSwapButton.setText("Editor de Tablas"); // Table Editor
    mapEditorSwapButton.setText("Editor de Mapas"); // Map Editor

    moveEditorSwapButton.setFont(spanishButtons);
    moveEditorSwapButton.setText("Editor de Movimiento"); // Move Editor
    requestsEditorSwapButton.setFont(spanishButtons);
    requestsEditorSwapButton.setText("Editor de Solicitudes"); // Requests Editor
    employeeEditorSwapButton.setFont(spanishButtons);
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
