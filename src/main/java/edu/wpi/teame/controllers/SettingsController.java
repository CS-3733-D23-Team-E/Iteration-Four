package edu.wpi.teame.controllers;

import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class SettingsController {

    @FXML
    MFXButton logoutButton;
    @FXML MFXButton userButton;
    @FXML
    ImageView homeI;
    @FXML MFXButton englishButton;
    @FXML MFXButton spanishButton;
    @FXML ImageView aboutI;
    @FXML ImageView servicesI;
    @FXML ImageView signageI;
    @FXML ImageView pathfindingI;
    @FXML ImageView databaseI;
    @FXML ImageView settingsI;
    @FXML ImageView exitI;
    @FXML MFXButton menuBarSettings;


    public void initialize()
    {
        menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_EDITOR_PAGE));
        menuBarServices.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));
        menuBarHome.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
        menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
        menuBarSettings.setOnMouseClicked(event -> Navigation.navigate(Screen.SETTINGSPAGE));
        menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate((Screen.DATABASE_TABLEVIEW)));
        menuBarExit.setOnMouseClicked(event -> Platform.exit());
    }
}
