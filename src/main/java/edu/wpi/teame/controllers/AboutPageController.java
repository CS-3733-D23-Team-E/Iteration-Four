package edu.wpi.teame.controllers;

import static edu.wpi.teame.entities.Settings.Language.ENGLISH;
import static javafx.scene.paint.Color.WHITE;


import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.utilities.ButtonUtilities;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;

import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.awt.*;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.util.Duration;

import javafx.scene.text.Text;


public class AboutPageController {



  @FXML MFXButton creditsButton;

  @FXML VBox logoutBox;
  @FXML MFXButton logoutButton;

  @FXML MFXButton kevinButton;
  @FXML MFXButton jamieButton;
  @FXML MFXButton aarshButton;
  @FXML MFXButton michButton;
  @FXML MFXButton braedenButton;
  @FXML MFXButton meganButton;
  @FXML MFXButton anthonyButton;
  @FXML MFXButton josephButton;
  @FXML MFXButton diyarButton;
  @FXML MFXButton nichButton;
  @FXML MFXButton albertButton;
  @FXML ImageView teamImage;
  @FXML Text infoText;

  @FXML VBox teamVBox;
  @FXML VBox kevinVBox;
  @FXML VBox jamieVBox;
  @FXML VBox aarshVBox;
  @FXML VBox michVBox;
  @FXML VBox braedenVBox;
  @FXML VBox meganVBox;
  @FXML VBox anthonyVBox;
  @FXML VBox josephVBox;
  @FXML VBox diyarVBox;
  @FXML VBox nichVBox;
  @FXML VBox albertVBox;

  boolean menuVisibilty = false;
  boolean logoutVisible = false;

  private MFXButton currentlySelectedButton;

  @FXML
  public void initialize() {

    kevinVBox.setVisible(false);
    jamieVBox.setVisible(false);
    aarshVBox.setVisible(false);
    michVBox.setVisible(false);
    braedenVBox.setVisible(false);
    meganVBox.setVisible(false);
    anthonyVBox.setVisible(false);
    josephVBox.setVisible(false);
    diyarVBox.setVisible(false);
    nichVBox.setVisible(false);
    albertVBox.setVisible(false);

    creditsButton.setOnMouseClicked(event -> Navigation.navigate((Screen.CREDITS)));

    // Initially set the menu bar to invisible

    logoutPopup(false);

    mouseSetup(logoutButton);


    /*Timeline timeline =
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
                                translateToHawaiian();
                              }
                              if (Settings.INSTANCE.getScreenMode() == Settings.ScreenMode.DARK_MODE) {
                                darkMode();
                              } else if (Settings.INSTANCE.getScreenMode() == Settings.ScreenMode.LIGHT_MODE) {
                                lightMode();
                              }
                            }));*/

   /* timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();*/

    kevinButton.setOnMouseClicked(
        event -> {
          showSelectedVBox(kevinVBox);
          setSelectedButton(kevinButton);
        });
    jamieButton.setOnMouseClicked(
        event -> {
          showSelectedVBox(jamieVBox);
          setSelectedButton(jamieButton);
        });
    aarshButton.setOnMouseClicked(
        event -> {
          showSelectedVBox(aarshVBox);
          setSelectedButton(aarshButton);
        });
    michButton.setOnMouseClicked(
        event -> {
          showSelectedVBox(michVBox);
          setSelectedButton(michButton);
        });
    braedenButton.setOnMouseClicked(
        event -> {
          showSelectedVBox(braedenVBox);
          setSelectedButton(braedenButton);
        });
    meganButton.setOnMouseClicked(
        event -> {
          showSelectedVBox(meganVBox);
          setSelectedButton(meganButton);
        });
    anthonyButton.setOnMouseClicked(
        event -> {
          showSelectedVBox(anthonyVBox);
          setSelectedButton(anthonyButton);
        });
    josephButton.setOnMouseClicked(
        event -> {
          showSelectedVBox(josephVBox);
          setSelectedButton(josephButton);
        });
    diyarButton.setOnMouseClicked(
        event -> {
          showSelectedVBox(diyarVBox);
          setSelectedButton(diyarButton);
        });
    nichButton.setOnMouseClicked(
        event -> {
          showSelectedVBox(nichVBox);
          setSelectedButton(nichButton);
        });
    albertButton.setOnMouseClicked(
        event -> {
          showSelectedVBox(albertVBox);
          setSelectedButton(albertButton);
        });

  }

  public void logoutPopup(boolean bool) {
    logoutBox.setVisible(bool);
  }

  private void mouseSetup(MFXButton btn) {
    btn.setOnMouseEntered(
        event -> {
          String style =
              "-fx-background-color: #ffffff; -fx-alignment: center; -fx-border-color: #192d5a; -fx-border-width: 2;";
          btn.setTextFill(Color.web("#192d5aff", 1.0));
        });
    btn.setOnMouseExited(
        event -> {
          if (!btn.getStyle().contains("-fx-font-weight: bold;")) {
            String style = "-fx-background-color: #192d5aff; -fx-alignment: center;";
            btn.setTextFill(WHITE);
          }
        });
  }


  private void showSelectedVBox(VBox selectedVBox) {
    kevinVBox.setVisible(false);
    jamieVBox.setVisible(false);
    aarshVBox.setVisible(false);
    michVBox.setVisible(false);
    braedenVBox.setVisible(false);
    meganVBox.setVisible(false);
    anthonyVBox.setVisible(false);
    josephVBox.setVisible(false);
    diyarVBox.setVisible(false);
    nichVBox.setVisible(false);
    albertVBox.setVisible(false);
    teamVBox.setVisible(false);

    selectedVBox.setVisible(true);
  }

  private void setSelectedButton(MFXButton selectedButton) {
    if (currentlySelectedButton != null) {
      currentlySelectedButton.setStyle("-fx-alignment: center;");
    }
    selectedButton.setStyle(
        "-fx-alignment: center; -fx-border-color: #192d5a; -fx-border-width: 2;");
    currentlySelectedButton = selectedButton;
  }

}
