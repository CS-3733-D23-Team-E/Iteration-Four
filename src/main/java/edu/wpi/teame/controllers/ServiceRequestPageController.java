package edu.wpi.teame.controllers;

import static edu.wpi.teame.entities.ServiceRequestData.Status.*;

import edu.wpi.teame.entities.Settings;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

public class ServiceRequestPageController {

  //  @FXML MFXButton menuButton;
  //  @FXML MFXButton menuBarHome;
  //  @FXML MFXButton menuBarServices;
  //  @FXML MFXButton menuBarSignage;
  //  @FXML MFXButton menuBarMaps;
  //  @FXML MFXButton menuBarDatabase;
  //  @FXML MFXButton menuBarAbout;
  //  @FXML MFXButton menuBarBlank;
  //  @FXML MFXButton menuBarExit;
  //
  //  @FXML MFXButton menuBarHelp;
  //  @FXML MFXButton menuBarSettings;
  //  @FXML MFXButton userButton;
  //  @FXML VBox menuBar;
  @FXML ImageView homeI;
  @FXML ImageView servicesI;
  @FXML ImageView signageI;
  @FXML ImageView pathfindingI;
  @FXML ImageView databaseI;

  @FXML ImageView aboutI;

  @FXML ImageView exitI;

  @FXML Label pendingRequestText;

  @FXML Label inProgressRequestText;

  @FXML Label completedRequestText;

  @FXML Label nonCompletedText;
  @FXML MFXButton spanishButton;
  @FXML MFXButton englishButton;

  //  @FXML Text inProgressRequestTitleText;
  //  @FXML Text pendingRequestsTitleText;
  //  @FXML Text completedRequestTitleText;
  //  @FXML Label nonCompletedTitleText;
  @FXML Tab flowerRequestTab;

  @FXML Tab medicalRequestTab;
  @FXML Tab mealRequestTab;
  @FXML Tab officeSuppliesTab;
  @FXML Tab conferenceRoomTab;
  @FXML Tab furnitureDeliveryTab;

  @FXML Tab roomCleanupTab;

  //  @FXML ListView<String> outgoingRequestsList;

  //  @FXML VBox logoutBox;
  //  @FXML MFXButton logoutButton;

  //  boolean menuVisibilty = false;
  //  boolean logoutVisible = false;

  String language = "english";
  String nyay = "\u00F1"; // ñ
  String aA = "\u0301"; // á
  String aE = "\u00E9"; // é
  String aI = "\u00ED"; // í
  String aO = "\u00F3"; // ó
  String aU = "\u00FA"; // ù
  String aQuestion = "\u00BF"; // Upside down question mark

  @FXML
  public void initialize() {
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

  public void translateToSpanish() {
    // Change language variable
    language = "spanish";

    // Service Request Tabs
    flowerRequestTab.setText("Solicitud de Flores"); // Flower Request
    mealRequestTab.setText("Solicitud de Comida"); // Meal Request
    officeSuppliesTab.setText("Suministros de Oficina"); // Office Supplies
    conferenceRoomTab.setText("Sala de Conferencias"); // Conference Room
    furnitureDeliveryTab.setText("Entrega de Muebles"); // Furniture Delivery
  }

  public void translateToEnglish() {
    // Change language variable
    language = "english";

    // Service Request Tabs
    flowerRequestTab.setText("Flower Request"); // Flower Request
    mealRequestTab.setText("Meal Request"); // Meal Request
    officeSuppliesTab.setText("Office Supplies"); // Office Supplies
    conferenceRoomTab.setText("Conference Room"); // Conference Room
    furnitureDeliveryTab.setText("Furniture Delivery"); // Furniture Delivery
  }
}
