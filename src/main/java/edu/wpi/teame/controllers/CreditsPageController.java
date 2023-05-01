package edu.wpi.teame.controllers;

import static edu.wpi.teame.entities.Settings.Language.ENGLISH;

import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CreditsPageController {

  @FXML MFXButton backButton;

  // Elements for screen mode change
  @FXML Text creditsText;
  @FXML Rectangle creditsBox;
  @FXML AnchorPane backgroundPane;

  @FXML Text apiTitle;
  @FXML Text iconsTitle;
  @FXML Text gestureTitle;
  @FXML Text controlsTitle;
  @FXML Text materialTitle;
  @FXML Text iconText;
  @FXML Rectangle backRectangle;
  @FXML Rectangle creditsBoxRectangle;

  @FXML
  public void initialize() {
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ABOUT));

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
                    translateToHawaiian();
                  }
                  if (Settings.INSTANCE.getScreenMode() == Settings.ScreenMode.DARK_MODE) {
                    darkMode();
                  } else if (Settings.INSTANCE.getScreenMode() == Settings.ScreenMode.LIGHT_MODE) {
                    lightMode();
                  }
                }));

    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  public void translateToEnglish() {
    backButton.setText("Back");
    iconText.setText("All icons can be found at flaticon.com ");
    iconsTitle.setText("Icons");
    creditsText.setText("Credits");
  }

  public void translateToSpanish() {
    backButton.setText("Back");
    iconText.setText("All icons can be found at flaticon.com ");
    iconsTitle.setText("Icons");
    creditsText.setText("Credits");
  }

  public void translateToFrench() {
    backButton.setText("Dos");
    iconText.setText("All icons can be found at flaticon.com ");
    iconsTitle.setText("Icons");
    creditsText.setText("Credits");
  }

  public void translateToHawaiian() {
    backButton.setText("Back");
    iconText.setText("All icons can be found at flaticon.com ");
    iconsTitle.setText("Icons");
    creditsText.setText("Credits");
  }

  public void lightMode() {
    backgroundPane.setBackground(Background.fill(Color.web("#e1e1e1")));
    creditsBox.setFill(Color.web("#f1f1f1"));
    creditsBoxRectangle.setFill(Color.web("#f1f1f1"));
    apiTitle.setFill(Color.web("#1f1f1f"));
    iconsTitle.setFill(Color.web("#1f1f1f"));
    gestureTitle.setFill(Color.web("#1f1f1f"));
    controlsTitle.setFill(Color.web("#1f1f1f"));
    materialTitle.setFill(Color.web("#1f1f1f"));
    iconText.setFill(Color.web("#1f1f1f"));
    creditsText.setFill(Color.web("#1f1f1f"));
  }

  public void darkMode() {
    backgroundPane.setBackground(Background.fill(Color.web("#1e1e1e")));
    creditsBox.setFill(Color.web("#292929"));
    creditsBoxRectangle.setFill(Color.web("#292929"));
    apiTitle.setFill(Color.web("#f1f1f1"));
    iconsTitle.setFill(Color.web("#f1f1f1"));
    gestureTitle.setFill(Color.web("#f1f1f1"));
    controlsTitle.setFill(Color.web("#f1f1f1"));
    materialTitle.setFill(Color.web("#f1f1f1"));
    iconText.setFill(Color.web("#f1f1f1"));
    creditsText.setFill(Color.web("#f1f1f1"));
  }
}
