package edu.wpi.teame.controllers;

import static edu.wpi.teame.entities.Settings.Language.ENGLISH;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.map.LocationName;
import edu.wpi.teame.utilities.ButtonUtilities;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.awt.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

  String nyay = "\u00F1"; // ñ
  String aA = "\u0301"; // á
  String aE = "\u00E9"; // é
  String aI = "\u00ED"; // í
  String aO = "\u00F3"; // ó
  String aU = "\u00FA"; // ù
  String aQuestion = "\u00BF"; // Upside down question mark

  boolean menuVisibilty = false;

  public void initialize() {
    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(10);
    dropShadow.setSpread(.71);
    dropShadow.setWidth(21);
    dropShadow.setHeight(50);
    Color paint = new Color(0.0, 0.6175, 0.65, 0.5);
    dropShadow.setColor(paint);

    menuBarVisible(false);
    usernameAccountText.setText(Employee.activeEmployee.getFullName());
    accessLevelAccountText.setText(Employee.activeEmployee.getPermission());

    englishButton.setEffect(dropShadow);

    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  if (Settings.INSTANCE.getLanguage() == ENGLISH) {
                    translateToEnglish();
                  } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
                    translateToSpanish();
                  } else if (Settings.INSTANCE.getLanguage() == Settings.Language.FRENCH) {
                    translateToFrench();
                  } else if (Settings.INSTANCE.getLanguage() == Settings.Language.HAWAIIAN) {
                    //                    translateToHawaiian();
                  }
                }));

    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_EDITOR_PAGE));
    menuBarServices.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));
    menuBarHome.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
    menuBarSettings.setOnMouseClicked(event -> Navigation.navigate(Screen.SETTINGSPAGE));
    menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate((Screen.DATABASE_TABLEVIEW)));
    menuBarExit.setOnMouseClicked(event -> Platform.exit());

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
    ButtonUtilities.mouseSetupMenuBar(
        menuBarExit,
        "baseline-center",
        exitI,
        "images/sign-out-alt.png",
        "images/sign-out-alt-blue.png");

    englishButton.setOnMouseClicked(
        event -> {
          Settings.INSTANCE.setLanguage(Settings.Language.ENGLISH);
          englishButton.setEffect(dropShadow);
          spanishButton.setEffect(null);
          frenchButton.setEffect(null);
          hawaiianButton.setEffect(null);
        });

    spanishButton.setOnMouseClicked(
        event -> {
          Settings.INSTANCE.setLanguage(Settings.Language.SPANISH);
          spanishButton.setEffect(dropShadow);
          englishButton.setEffect(null);
          frenchButton.setEffect(null);
          hawaiianButton.setEffect(null);
        });

    frenchButton.setOnMouseClicked(
        event -> {
          Settings.INSTANCE.setLanguage(Settings.Language.FRENCH);
          frenchButton.setEffect(dropShadow);
          spanishButton.setEffect(null);
          englishButton.setEffect(null);
          hawaiianButton.setEffect(null);
        });

    hawaiianButton.setOnMouseClicked(
        event -> {
          Settings.INSTANCE.setLanguage(Settings.Language.HAWAIIAN);
          hawaiianButton.setEffect(dropShadow);
          spanishButton.setEffect(null);
          frenchButton.setEffect(null);
          englishButton.setEffect(null);
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

  public void menuBarVisible(boolean bool) {
    menuBarHome.setVisible(bool);
    menuBarServices.setVisible(bool);
    menuBarSignage.setVisible(bool);
    menuBarMaps.setVisible(bool);
    menuBarDatabase.setVisible(bool);

    menuBarAbout.setVisible(bool);
    menuBarSettings.setVisible(bool);
    menuBarExit.setVisible(bool);
    menuBarBlank.setVisible(bool);
    menuBar.setVisible(bool);
  }

  public void translateToEnglish() {
    languageLine1.setText("The language you have chosen is: ");
    language.setText("U.S. English");
    languageLine2.setText("To switch languages, press one of the other buttons above.");

    menuBarHome.setText("Home"); // Keep in English
    menuBarServices.setText("Services"); // Keep in English
    menuBarSignage.setText("Signage"); // Keep in English
    menuBarMaps.setText("Pathfinding"); // Keep in English
    menuBarDatabase.setText("Database"); // Keep in English
    menuBarSettings.setText("Settings");
    menuBarAbout.setText("About");
    menuBarHelp.setText("Help");
    menuBarExit.setText(("Exit")); // Keep in English
  }

  public void translateToSpanish() {
    languageLine1.setText("El idioma que ha elegido es: ");
    language.setText("Español");
    languageLine2.setText("Para cambiar de idioma, presione uno de los otros botones de arriba.");

    menuBarHome.setText("Principal"); // Home
    menuBarServices.setText("Servicios"); // Services
    menuBarSignage.setText("Se" + nyay + "alizaci" + aO + "n"); // Signage
    menuBarMaps.setText("Navegaci" + aO + "n"); // Pathfinding
    menuBarDatabase.setText("Base de Datos"); // Database
    menuBarExit.setText(("Salida")); // Exit
    menuBarSettings.setText("Ajustes");
    menuBarAbout.setText("Acerca de");
    menuBarHelp.setText("Ayuda");
  }

  public void translateToFrench() {
    languageLine1.setText("La langue que vous avez choisie est: ");
    language.setText("Français");
    languageLine2.setText("Pour changer de langue, appuyez sur l'un des autres boutons ci-dessus.");

    menuBarHome.setText("Maison"); // Keep in English
    menuBarServices.setText("Service"); // Keep in English
    menuBarSignage.setText("Signalisation"); // Keep in English
    menuBarMaps.setText("Directions"); // Keep in English
    menuBarDatabase.setText("Base de données"); // Keep in English
    menuBarSettings.setText("Paramètres");
    menuBarAbout.setText("À propos");
    menuBarHelp.setText("Aider");
    menuBarExit.setText(("Sortie")); // Keep in English
  }

  //  public void translateToHawaiian() {
  //    languageLine1.setText("ʻO ka ʻōlelo āu i koho ai: ");
  //    language.setText("ʻŌlelo Hawaiʻi");
  //    languageLine2.setText(
  //        "No ka hoʻololi ʻana i nā ʻōlelo, e kaomi i kekahi o nā pihi ʻē aʻe ma luna.");
  //
  //    menuBarHome.setText("Home");
  //    menuBarServices.setText("Lawelawe");
  //    menuBarSignage.setText("Hōʻailona");
  //    menuBarMaps.setText("Kuhikuhi");
  //    menuBarDatabase.setText("Kumu o ka ʻikepili");
  //    menuBarSettings.setText("Hoʻonoho 'ana");
  //    menuBarAbout.setText("Pili ana");
  //    menuBarHelp.setText("Kokua");
  //    menuBarExit.setText(("Puka"));
  //  }
}
