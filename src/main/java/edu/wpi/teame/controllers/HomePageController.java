package edu.wpi.teame.controllers;

import edu.wpi.teame.entities.LoginData;
import edu.wpi.teame.utilities.ButtonUtilities;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class HomePageController {
  @FXML MFXButton serviceRequestButton;
  @FXML MFXButton editSignageButton;
  @FXML MFXButton databaseButton;
  @FXML MFXButton pathfindingButton;
  @FXML MFXButton loginButton;
  @FXML TextField username;
  @FXML TextField password;
  @FXML MFXButton menuButton;
  @FXML MFXButton menuBarHome;
  @FXML MFXButton menuBarServices;
  @FXML MFXButton menuBarMaps;
  @FXML MFXButton menuBarDatabase;
  @FXML MFXButton menuBarSignage;
  @FXML MFXButton menuBarBlank;
  @FXML MFXButton menuBarExit;
  @FXML Text dateText;
  @FXML Text timeText;
  @FXML VBox menuBar;
  @FXML MFXButton announcementButton;
  @FXML Text announcementText;
  @FXML MFXTextField announcementTextBox;
  @FXML VBox logoutBox;
  @FXML MFXButton logoutButton;
  @FXML MFXButton userButton;
  @FXML ImageView homeI;
  @FXML ImageView servicesI;
  @FXML ImageView signageI;
  @FXML ImageView pathfindingI;
  @FXML ImageView databaseI;
  @FXML ImageView exitI;
  @FXML MFXButton spanishButton;
  @FXML MFXButton englishButton;
  @FXML Text todayIsText;
  @FXML Text announcementsText;

  Boolean loggedIn;
  String language = "english";
  boolean menuVisibilty = false;
  boolean logoutVisible = false;

  String nyay = "\u00F1"; // ñ
  String aA = "\u0301"; // á
  String aE = "\u00E9"; // é
  String aI = "\u00ED"; // í
  String aO = "\u00F3"; // ó
  String aU = "\u00F9"; // ù
  String aQuestion = "\u00BF"; // Upside down question mark

  public void initialize() {
    LocalTime currentTime = LocalTime.now();
    LocalDate currentDate = LocalDate.now();

    // Format current date as a string
    DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    String currentDateString = currentDate.format(format);
    // Format the current time as a string
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    String currentTimeString = currentTime.format(formatter);

    // Print the current time as a string
    timeText.setText(currentTimeString);
    dateText.setText(currentDateString);

    serviceRequestButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));

    editSignageButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
    databaseButton.setOnMouseClicked(event -> Navigation.navigate(Screen.DATABASE_TABLEVIEW));
    pathfindingButton.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));

    menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
    menuBarServices.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));
    menuBarHome.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
    menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate((Screen.DATABASE_TABLEVIEW)));
    menuBarExit.setOnMouseClicked(event -> Platform.exit());

    loggedIn = false;
    logoutButton.setOnMouseClicked(event -> attemptLogin());
    AtomicReference<String> announcementString = new AtomicReference<>("");
    announcementButton.setOnMouseClicked(
        event -> {
          String announcement = announcementTextBox.getText();
          announcementText.setText(announcement);
          announcementString.set(announcement);
        });

    // Initially set the menu bar to invisible
    menuBarVisible(false);
    logoutPopup(false);

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

    userButton.setOnMouseClicked(
        event -> {
          logoutVisible = !logoutVisible;
          logoutPopup(logoutVisible);
        });

    logoutButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
    menuBarServices.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));
    menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
    menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
    menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate(Screen.DATABASE_TABLEVIEW));
    menuBarExit.setOnMouseClicked((event -> Platform.exit()));

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
        menuBarExit,
        "baseline-center",
        exitI,
        "images/sign-out-alt.png",
        "images/sign-out-alt-blue.png");

    // makes the buttons highlight when they are hovered over
    ButtonUtilities.mouseSetup(serviceRequestButton);
    ButtonUtilities.mouseSetup(editSignageButton);
    ButtonUtilities.mouseSetup(pathfindingButton);
    ButtonUtilities.mouseSetup(databaseButton);
    ButtonUtilities.mouseSetup(logoutButton);

    // Page Language Translation Code
    englishButton.setOnMouseClicked(
        event -> {
          translateToEnglish(String.valueOf(announcementString));
        });
    spanishButton.setOnMouseClicked(
        event -> {
          translateToSpanish(String.valueOf(announcementString));
        });
    if (language.equals("english")) {
      translateToEnglish(String.valueOf(announcementString));
    } else if (language.equals("spanish")) {
      translateToSpanish(String.valueOf(announcementString));
    } else // throw error for language not being a valid language
    {
      // throw some sort of error here at some point
    }
  }

  public void attemptLogin() {
    // Get the input login info
    LoginData login = new LoginData(username.getText(), password.getText());

    // If the login was successful
    if (login.attemptLogin()) {
      // Hide text fields and button
      password.setVisible(false);
      username.setVisible(false);
      loginButton.setVisible(false);
      // Set loggedIn as true
      loggedIn = true;

    } else {
      // Clear the fields
      password.clear();
      username.clear();
    }
  }

  public void logoutPopup(boolean bool) {
    logoutBox.setVisible(bool);
  }

  public void menuBarVisible(boolean bool) {
    menuBarHome.setVisible(bool);
    menuBarServices.setVisible(bool);
    menuBarSignage.setVisible(bool);
    menuBarMaps.setVisible(bool);
    menuBarDatabase.setVisible(bool);
    menuBarExit.setVisible(bool);
    menuBarBlank.setVisible(bool);
    menuBar.setVisible(bool);
  }

  public void translateToSpanish(String announcmentString) {
    // Change language variable
    language = "spanish";

    // Menu Bar
    menuBarHome.setText("Principal"); // Home
    menuBarServices.setText("Servicios"); // Services
    menuBarSignage.setText("Se" + nyay + "alizaci" + aO + "n"); // Signage
    menuBarMaps.setText("Navegaci" + aO + "n"); // Pathfinding
    menuBarDatabase.setText("Base de Datos"); // Database
    menuBarExit.setText(("Salida")); // Exit

    // Home Page Buttons
    editSignageButton.setText("Se" + nyay + "alizaci" + aO + "n"); // Signage
    serviceRequestButton.setText("Servicios"); // Services
    pathfindingButton.setText("Navegaci" + aO + "n"); // Pathfinding
    databaseButton.setText("Base de Datos"); // Database

    // Date Bar
    todayIsText.setText("Hoy es..."); // Today is...

    // Announcements Bar
    announcementsText.setText("Anuncios"); // Announcements
    if (announcmentString.equals("")) { // Do this if there are currently no announcements
      announcementText.setText("No hay nuevos anuncios."); // No new announcements.
    }
    announcementTextBox.setPromptText("Texto del Anuncio Aqu" + aI); // Announcement Text Here
    announcementButton.setText("Presentar"); // Submit

    // Logout Button
    logoutButton.setText("Cerrar Sesi" + aO + "n"); // Logout
    Font spanishLogout = new Font("Roboto", 13);
    logoutButton.setFont(spanishLogout);
  }

  public void translateToEnglish(String announcmentString) {
    // Change language variable
    language = "english";

    // Menu Bar
    menuBarHome.setText("Home"); // Keep in English
    menuBarServices.setText("Services"); // Keep in English
    menuBarSignage.setText("Signage"); // Keep in English
    menuBarMaps.setText("Pathfinding"); // Keep in English
    menuBarDatabase.setText("Database"); // Keep in English
    menuBarExit.setText(("Exit")); // Keep in English

    // Home Page Buttons
    editSignageButton.setText("Signage"); // Keep in English
    serviceRequestButton.setText("Services"); // Keep in English
    pathfindingButton.setText("Pathfinding"); // Keep in English
    databaseButton.setText("Database"); // Keep in English

    // Date Bar
    todayIsText.setText("Today is..."); // Keep in English

    // Announcements Bar
    announcementsText.setText("Announcements"); // Keep in English
    if (announcmentString.equals("")) { // Do this if there are currently no announcements
      announcementText.setText("No new announcements."); // Keep in English
    }
    announcementTextBox.setPromptText("Announcement Text Here"); // Keep in English
    announcementButton.setText("Submit"); // Keep in English

    // Logout Button
    logoutButton.setText("Logout"); // Keep in English
    Font englishLogout = new Font("Roboto", 18);
    logoutButton.setFont(englishLogout);
  }
}
