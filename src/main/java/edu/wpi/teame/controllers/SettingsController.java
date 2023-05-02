package edu.wpi.teame.controllers;

import static edu.wpi.teame.entities.Settings.Language.ENGLISH;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.map.LocationName;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXSlider;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.awt.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javax.swing.*;
import org.controlsfx.control.SearchableComboBox;

public class SettingsController {

  @FXML MFXButton menuButton;
  @FXML MFXButton menuBarHome;
  @FXML MFXButton menuBarServices;
  @FXML MFXButton menuBarMaps;

  @FXML MFXButton menuBarAbout;
  @FXML MFXButton menuBarDatabase;

  @FXML MFXButton menuBarSignage;
  @FXML MFXButton menuBarBlank;
  @FXML MFXButton menuBarHelp;
  @FXML MFXButton menuBarExit;
  @FXML MFXButton logoutButton;
  @FXML MFXButton userButton;
  @FXML ImageView homeI;
  @FXML MFXButton englishButton;
  @FXML MFXButton spanishButton;
  @FXML MFXButton frenchButton;
  @FXML MFXButton hawaiianButton;
  @FXML ImageView aboutI;
  @FXML ImageView helpI;
  @FXML ImageView servicesI;
  @FXML ImageView signageI;
  @FXML ImageView pathfindingI;
  @FXML ImageView databaseI;
  @FXML ImageView settingsI;
  @FXML ImageView exitI;
  @FXML MFXButton menuBarSettings;
  @FXML Text languageLine1;
  @FXML Text language;
  @FXML Text languageLine2;
  @FXML VBox menuBar;

  @FXML MFXTextField currentPass;
  @FXML MFXTextField newPass;
  @FXML MFXTextField confirmPass;

  @FXML MFXButton submitButton;

  @FXML Text usernameAccountText;
  @FXML Text accessLevelAccountText;
  @FXML SearchableComboBox<String> defaultLocationCombo;
  @FXML Button defaultLocationSubmit;
  @FXML Label defaultLocationLabel;

  @FXML MFXRadioButton lightModeButton;
  @FXML MFXRadioButton darkModeButton;

  // elements of page for mode changes
  @FXML StackPane settingsStackPane;

  @FXML AnchorPane accountTabPane;
  @FXML AnchorPane languagesTabPane;
  @FXML Rectangle settingsBackgroundRectangle;
  @FXML Text changePasswordTitle;
  @FXML Text colorSchemeTitle;
  @FXML Text defaultLocationTitle;
  @FXML Text databaseConnectionTitle;

  @FXML Text currentPassText;
  @FXML Text newPassText;
  @FXML Text confirmPassText;

  @FXML Line line1;
  @FXML Line line2;
  @FXML ImageView userImage;
  @FXML TabPane settingsTabs;
  @FXML MFXRadioButton AWSButton;
  @FXML MFXRadioButton WPIButton;

  @FXML
  MFXSlider screenSaverTimeBar;
  @FXML Text screenSaverSelectionTitle;
  @FXML Button screenSaverTimeSubmit;
  @FXML Text screenSaverInstructions;

  String nyay = "\u00F1"; // ñ
  // String aA = "\u0301"; // á
  String aA = "\u00E1";
  String capitalaA = "\u00C1";
  String aE = "\u00E9"; // é
  String aI = "\u00ED"; // í
  String aO = "\u00F3"; // ó
  String aU = "\u00FA"; // ù
  String aQuestion = "\u00BF"; // Upside down question mark

  // Hawaiian Letters

  String oH = "\u014D";
  String okina = "\u02BB"; // Okina ʻ

  String aH = "\u0101";
  String eH = "\u0113";

  // French
  String ceH = "\u00E7";

  // TODO Make a screen saver time adjuster
  public void initialize() {
    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(10);
    dropShadow.setSpread(.71);
    dropShadow.setWidth(21);
    dropShadow.setHeight(50);
    Color paint = new Color(0.0, 0.6175, 0.65, 0.5);
    dropShadow.setColor(paint);

    usernameAccountText.setText(Employee.activeEmployee.getFullName());
    accessLevelAccountText.setText(Employee.activeEmployee.getPermission());

    englishButton.setEffect(dropShadow);
    lightModeButton.setSelected(true);

    if (SQLRepo.INSTANCE.getCurrentdb().equals(SQLRepo.DB.AWS)) {
      AWSButton.setSelected(true);
      WPIButton.setSelected(false);
    } else {
      WPIButton.setSelected(true);
      AWSButton.setSelected(false);
    }

    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  if (Settings.INSTANCE.getLanguage() == ENGLISH) {
                    englishButton.setEffect(dropShadow);
                    spanishButton.setEffect(null);
                    frenchButton.setEffect(null);
                    translateToEnglish();
                  } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
                    spanishButton.setEffect(dropShadow);
                    englishButton.setEffect(null);
                    frenchButton.setEffect(null);
                    translateToSpanish();

                  } else if (Settings.INSTANCE.getLanguage() == Settings.Language.FRENCH) {
                    frenchButton.setEffect(dropShadow);
                    spanishButton.setEffect(null);
                    englishButton.setEffect(null);
                    translateToFrench();
                  }
                  if (Settings.INSTANCE.getScreenMode() == Settings.ScreenMode.DARK_MODE) {
                    darkMode();
                    darkModeButton.setSelected(true);
                    lightModeButton.setSelected(false);
                  } else if (Settings.INSTANCE.getScreenMode() == Settings.ScreenMode.LIGHT_MODE) {
                    lightMode();
                    darkModeButton.setSelected(false);
                    lightModeButton.setSelected(true);
                  }
                }));

    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    englishButton.setOnMouseClicked(
        event -> {
          Settings.INSTANCE.setLanguage(Settings.Language.ENGLISH);
          englishButton.setEffect(dropShadow);
          spanishButton.setEffect(null);
          frenchButton.setEffect(null);
        });

    spanishButton.setOnMouseClicked(
        event -> {
          Settings.INSTANCE.setLanguage(Settings.Language.SPANISH);
          spanishButton.setEffect(dropShadow);
          englishButton.setEffect(null);
          frenchButton.setEffect(null);
        });

    frenchButton.setOnMouseClicked(
        event -> {
          Settings.INSTANCE.setLanguage(Settings.Language.FRENCH);
          frenchButton.setEffect(dropShadow);
          spanishButton.setEffect(null);
          englishButton.setEffect(null);
        });

    lightModeButton.setOnMouseClicked(
        event -> {
          lightModeButton.setSelected(true);
          Settings.INSTANCE.setScreenMode(Settings.ScreenMode.LIGHT_MODE);
          darkModeButton.setSelected(false);
        });

    darkModeButton.setOnMouseClicked(
        event -> {
          darkModeButton.setSelected(true);
          Settings.INSTANCE.setScreenMode(Settings.ScreenMode.DARK_MODE);
          lightModeButton.setSelected(false);
        });

    submitButton.setOnMouseClicked(
        event -> {
          if (currentPass.getText().equals("password")
              && newPass.getText().equals(confirmPass.getText())) {
            SQLRepo.INSTANCE.updateEmployee(
                Employee.activeEmployee, "password", confirmPass.getText());
          }
          currentPass.clear();
          newPass.clear();
          confirmPass.clear();
        });

    defaultLocationSubmit.setOnMouseClicked(
        event -> {
          Settings.INSTANCE.setDefaultLocation(defaultLocationCombo.getValue());
          defaultLocationCombo.setValue(null);
          defaultLocationLabel.setText(
              "Default Location: " + Settings.INSTANCE.getDefaultLocation());
        });

    AWSButton.setOnMouseClicked(
        event -> {
          AWSButton.setSelected(true);
          SQLRepo.INSTANCE.exitDatabaseProgram();
          SQLRepo.INSTANCE.setCurrentdb(SQLRepo.DB.AWS);
          SQLRepo.INSTANCE.switchConnection();
          WPIButton.setSelected(false);
        });
    WPIButton.setOnMouseClicked(
        event -> {
          WPIButton.setSelected(true);
          SQLRepo.INSTANCE.exitDatabaseProgram();
          SQLRepo.INSTANCE.setCurrentdb(SQLRepo.DB.WPI);
          SQLRepo.INSTANCE.switchConnection();
          AWSButton.setSelected(false);
        });

    // Populate the combobox
    ObservableList<String> floorLocations =
        FXCollections.observableArrayList(
            LocationName.allLocations.values().stream()
                .filter(
                    (location) -> // Filter out hallways and long names with no corresponding
                        // LocationName
                        location == null
                            ? false
                            : location.getNodeType() != LocationName.NodeType.HALL
                                && location.getNodeType() != LocationName.NodeType.STAI
                                && location.getNodeType() != LocationName.NodeType.ELEV
                                && location.getNodeType() != LocationName.NodeType.REST)
                .map((location) -> location.getLongName())
                .sorted() // Sort alphabetically
                .toList());

    defaultLocationCombo.setItems(floorLocations);
    if (Settings.INSTANCE.getDefaultLocation() == null) {
      defaultLocationLabel.setText("Default Location: none");
    } else {
      defaultLocationLabel.setText("Default Location: " + Settings.INSTANCE.getDefaultLocation());
    }
  }

  public void translateToEnglish() {
    languageLine1.setText("The language you have chosen is: ");
    language.setText("U.S. English");
    languageLine2.setText("To switch languages, press one of the other buttons above.");
  }

  public void translateToSpanish() {
    languageLine1.setText("El idioma que ha elegido es: ");
    language.setText("Espa" + nyay + "ol");
    languageLine2.setText("Para cambiar de idioma, presione uno de los otros botones de arriba.");
  }

  public void translateToFrench() {
    languageLine1.setText("La langue que vous avez choisie est: ");
    language.setText("Fran" + ceH + "ais");

    languageLine2.setText("Pour changer de langue, appuyez sur l'un des autres boutons ci-dessus.");
  }

  public void lightMode() {
    // settingsStackPane.setStyle("lightmode-background");
    settingsStackPane.setBackground(Background.fill(Color.web("#e1e1e1")));
    accountTabPane.setBackground(Background.fill(Color.web("#e1e1e1")));
    languagesTabPane.setBackground(Background.fill(Color.web("#e1e1e1")));
    language.setFill(Color.web("#1f1f1f"));
    languageLine1.setFill(Color.web("#1f1f1f"));
    languageLine2.setFill(Color.web("#1f1f1f"));
    changePasswordTitle.setFill(Color.web("#1f1f1f"));
    databaseConnectionTitle.setFill(Color.web("#1f1f1f"));
    defaultLocationTitle.setFill(Color.web("#1f1f1f"));
    colorSchemeTitle.setFill(Color.web("#1f1f1f"));
    defaultLocationLabel.setTextFill(Color.web("#1f1f1f"));
    usernameAccountText.setFill(Color.web("#1f1f1f"));
    accessLevelAccountText.setFill(Color.web("#1f1f1f"));
    currentPassText.setFill(Color.web("#1f1f1f"));
    newPassText.setFill(Color.web("#1f1f1f"));
    confirmPassText.setFill(Color.web("#1f1f1f"));
    line1.setFill(Color.web("#012D5A"));
    line2.setFill(Color.web("#012D5A"));
    lightModeButton.setTextFill(Color.web("#1f1f1f"));
    darkModeButton.setTextFill(Color.web("#1f1f1f"));
    settingsTabs
        .lookup(".tab-pane .tab-header-area .tab-header-background")
        .setStyle("-fx-background-color: #f1f1f1");

    screenSaverInstructions.setFill(Color.web("#1f1f1f"));
    screenSaverSelectionTitle.setFill(Color.web("#1f1f1f"));
      AWSButton.setTextFill(Color.web("#1f1f1f"));
      WPIButton.setTextFill(Color.web("#1f1f1f"));
  }

  public void darkMode() {
    // settingsStackPane.setStyle("darkmode-background");
    settingsStackPane.setBackground(Background.fill(Color.web("#1e1e1e")));
    accountTabPane.setBackground(Background.fill(Color.web("#292929")));
    languagesTabPane.setBackground(Background.fill(Color.web("#292929")));
    language.setFill(Color.web("#f1f1f1"));
    languageLine1.setFill(Color.web("#f1f1f1"));
    languageLine2.setFill(Color.web("#f1f1f1"));
    changePasswordTitle.setFill(Color.web("#f1f1f1"));
    colorSchemeTitle.setFill(Color.web("#f1f1f1"));
    databaseConnectionTitle.setFill(Color.web("#f1f1f1"));
    defaultLocationTitle.setFill(Color.web("#f1f1f1"));
    defaultLocationLabel.setTextFill(Color.web("#f1f1f1"));
    usernameAccountText.setFill(Color.web("#f1f1f1"));
    accessLevelAccountText.setFill(Color.web("#f1f1f1"));
    currentPassText.setFill(Color.web("#f1f1f1"));
    newPassText.setFill(Color.web("#f1f1f1"));
    confirmPassText.setFill(Color.web("#f1f1f1"));
    line1.setStroke(Color.web("#5C5C5C"));
    line2.setStroke(Color.web("#5C5C5C"));
    settingsTabs
        .lookup(".tab-pane .tab-header-area .tab-header-background")
        .setStyle("-fx-background-color: #3D3D3D");
    lightModeButton.setTextFill(Color.web("#f1f1f1"));
    darkModeButton.setTextFill(Color.web("#f1f1f1"));

    AWSButton.setTextFill(Color.web("#f1f1f1"));
      WPIButton.setTextFill(Color.web("#f1f1f1"));
      screenSaverInstructions.setFill(Color.web("#f1f1f1"));
      screenSaverSelectionTitle.setFill(Color.web("#f1f1f1"));
  }
}
