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
        menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_EDITOR_PAGE));
        menuBarServices.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));
        menuBarHome.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
        menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
        menuBarAbout.setOnMouseClicked(event -> Navigation.navigate((Screen.ABOUT)));
        menuBarSettings.setOnMouseClicked(event -> Navigation.navigate(Screen.SETTINGSPAGE));
        menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate((Screen.DATABASE_TABLEVIEW)));
        menuBarExit.setOnMouseClicked(event -> Platform.exit());


    }
}
