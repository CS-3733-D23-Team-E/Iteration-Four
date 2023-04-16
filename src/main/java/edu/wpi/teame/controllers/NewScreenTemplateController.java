package edu.wpi.teame.controllers;

import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;

import static javafx.scene.paint.Color.WHITE;

public class NewScreenTemplateController
{
    @FXML MFXButton menuButton;
    @FXML MFXButton menuBarHome;
    @FXML MFXButton menuBarServices;
    @FXML MFXButton menuBarMaps;
    @FXML MFXButton menuBarDatabase;
    @FXML MFXButton menuBarSignage;
    @FXML MFXButton menuBarBlank;
    @FXML MFXButton menuBarExit;
    boolean menuVisibilty = false;


    public void initialize()
    {
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
        menuBarHome.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
        menuBarServices.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));
        menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
        menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
        menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate(Screen.DATABASE_VIEW));
        menuBarExit.setOnMouseClicked((event -> Platform.exit()));
    }

    private void mouseSetup(MFXButton btn) {
        btn.setOnMouseEntered(
                event -> {
                    btn.setStyle(
                            "-fx-background-color: #ffffff; -fx-alignment: center; -fx-border-color: #192d5a; -fx-border-width: 2;");
                    btn.setTextFill(Color.web("#192d5aff", 1.0));
                });
        btn.setOnMouseExited(
                event -> {
                    btn.setStyle("-fx-background-color: #192d5aff; -fx-alignment: center;");
                    btn.setTextFill(WHITE);
                });
    }

    public void menuBarVisible(boolean bool) {
        menuBarHome.setVisible(bool);
        menuBarServices.setVisible(bool);
        menuBarSignage.setVisible(bool);
        menuBarMaps.setVisible(bool);
        menuBarDatabase.setVisible(bool);
        menuBarExit.setVisible(bool);
        menuBarBlank.setVisible(bool);
    }
}
