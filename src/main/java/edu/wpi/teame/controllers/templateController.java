package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.AlertData;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.entities.LoginData;
import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.utilities.ButtonUtilities;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class templateController
{
    @FXML MFXButton menuBarHome;
    @FXML MFXButton menuBarServices;
    @FXML MFXButton menuBarMaps;
    @FXML MFXButton menuBarAbout;
    @FXML MFXButton menuBarDatabase;
    @FXML MFXButton menuBarSignage;
    @FXML MFXButton menuBarBlank;
    @FXML MFXButton menuBarExit;
    @FXML VBox menuBar;
    @FXML VBox logoutBox;
    @FXML MFXButton logoutButton;
    @FXML MFXButton userButton;
    @FXML ImageView homeI;
    @FXML ImageView aboutI;
    @FXML ImageView servicesI;
    @FXML ImageView signageI;
    @FXML ImageView pathfindingI;
    @FXML ImageView databaseI;
    @FXML ImageView settingsI;
    @FXML ImageView exitI;
    @FXML MFXButton menuBarSettings;
    Boolean loggedIn;
    String language = "english";
    boolean menuVisibilty = false;
    boolean logoutVisible = false;

    String nyay = "\u00F1"; // ñ
    String aA = "\u0301"; // á
    String aE = "\u00E9"; // é
    String aI = "\u00ED"; // í
    String aO = "\u00F3"; // ó
    String aU = "\u00FA"; // ù
    String aQuestion = "\u00BF"; // Upside down question mark

    public void initialize()
    {
        //Menu Bar Navigation code
        menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_EDITOR_PAGE));
        menuBarServices.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));
        menuBarHome.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
        menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
        menuBarAbout.setOnMouseClicked(event -> Navigation.navigate((Screen.ABOUT)));
        menuBarSettings.setOnMouseClicked(event -> Navigation.navigate(Screen.SETTINGSPAGE));
        menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate((Screen.DATABASE_TABLEVIEW)));
        menuBarExit.setOnMouseClicked(event -> Platform.exit());

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
                menuBarSettings,
                "baseline-left",
                settingsI,
                "images/settingsicon.png",
                "images/settingsicon-blue.png");
        ButtonUtilities.mouseSetupMenuBar(
                menuBarExit,
                "baseline-center",
                exitI,
                "images/sign-out-alt.png",
                "images/sign-out-alt-blue.png");
    }

    public void translateToSpanish()
    {
        //Add code once page is set up
    }

    public void translateToEnglish()
    {
        //Add code once page is set up
    }
}
