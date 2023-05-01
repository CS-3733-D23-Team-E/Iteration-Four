package edu.wpi.teame.controllers;

import static edu.wpi.teame.entities.Settings.Language.ENGLISH;

import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.awt.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class AboutPageController {

  @FXML MFXButton creditsButton;

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

  @FXML Text csText;
  @FXML Text softEngText;
  @FXML Text wongText;
  @FXML Text teamNameText;
  @FXML Text copyrightText;

  // elements for dark mode & languages

  @FXML Line line2;
  @FXML Text kevinRole;
  @FXML Text kevinMajor;
  @FXML Text kevinFood;

  @FXML Text meganRole;
  @FXML Text meganMajor;
  @FXML Text meganFood;

  @FXML Text anthonyRole;
  @FXML Text anthonyMajor;
  @FXML Text anthonyFood;

  @FXML Text nickRole;
  @FXML Text nickMajor;
  @FXML Text nickFood;

  @FXML Text michRole;
  @FXML Text michMajor;
  @FXML Text michFood;

  @FXML Text jamieRole;
  @FXML Text jamieMajor;
  @FXML Text jamieFood;

  @FXML Text diyarRole;
  @FXML Text diyarMajor;
  @FXML Text diyarFood;

  @FXML Text albertRole;
  @FXML Text albertMajor;
  @FXML Text albertFood;

  @FXML Text braedenRole;
  @FXML Text braedenMajor;
  @FXML Text braedenFood;

  @FXML Text josephRole;
  @FXML Text josephMajor;
  @FXML Text josephFood;

  @FXML Text aarshRole;
  @FXML Text aarshMajor;
  @FXML Text aarshFood;

  private MFXButton currentlySelectedButton;

  // Elements for Screen Mode
  @FXML Rectangle copyrightBox;
  @FXML AnchorPane backgroundPane;

  @FXML Rectangle titleBox;
  @FXML Rectangle photoBox;
  @FXML Rectangle teamBox;

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

  /* private void mouseSetup(MFXButton btn) {
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
  }*/

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

  public void translateToEnglish() {
    csText.setText("WPI Computer Science Department");
    softEngText.setText("CS3733-D23 Software Engineering");
    wongText.setText("Prof. Wilson Wong");
    teamNameText.setText("Team Ethical Easter Bunnies");
    infoText.setText(
        "Special Thanks to Brigham And Women's Hospital & their Representative Andrew Shinn for their Time & Input");
  }

  public void translateToSpanish() {
    csText.setText("WPI Departamento de Ciencias de la Computaci" + Settings.INSTANCE.aO + "n");
    softEngText.setText("CS3733-D23 Engineering Software");
    wongText.setText("Prof. Wilson Wong");
    teamNameText.setText("Conejitos de pascua " + Settings.INSTANCE.aE + "ticos del equipo");
    infoText.setText(
        "Agradecimientos especiales a Brigham And Women's Hospital & su representante Andrew Shinn por su tiempo y participaci"
            + Settings.INSTANCE.aO
            + "n");
  }

  public void translateToFrench() {
    csText.setText("WPI D" + Settings.INSTANCE.aE + "partement d'informatique");
    softEngText.setText("CS3733-D23 g" + Settings.INSTANCE.aE + "nie logiciel");
    wongText.setText("Prof. Wilson Wong");
    teamNameText.setText(
        "Lapins de p"
            + Settings.INSTANCE.aEH
            + "ques "
            + Settings.INSTANCE.aE
            + "thiques d'"
            + Settings.INSTANCE.aE
            + "quipe");
    infoText.setText(
        "Remerciement sp"
            + Settings.INSTANCE.aE
            + "cial "
            + Settings.INSTANCE.aA
            + " Brigham And Women's Hospital & leur repr"
            + Settings.INSTANCE.aE
            + "sentant Andrew Shinn por leur temps et leur contribution");
  }

  public void translateToHawaiian() {
    csText.setText(
        "WPI"
            + Settings.INSTANCE.okina
            + "Ohiana "
            + Settings.INSTANCE.okina
            + "epekema kamepiula");
    softEngText.setText("CS3733-D23 " + Settings.INSTANCE.okina + "enehana lako polokalamu");
    wongText.setText("Prof. Wilson Wong");
    teamNameText.setText(
        "Hui "
            + Settings.INSTANCE.okina
            + "Etika Easter"
            + Settings.INSTANCE.okina
            + "lole l"
            + Settings.INSTANCE.aH
            + "paki");
    infoText.setText(
        "Mahalo nui i"
            + Settings.INSTANCE.aH
            + " Brigham And Women's Hospital & ko lakou lunamakaainana Andrew Shinn no ko l"
            + Settings.INSTANCE.aH
            + "kou manawa a me ke komo "
            + Settings.INSTANCE.okina
            + "ana");
  }

  public void darkMode() {
    backgroundPane.setBackground(Background.fill(Color.web("#1e1e1e")));
    copyrightBox.setFill(Color.web("#292929"));
    teamBox.setFill(Color.web("#292929"));
    photoBox.setFill(Color.web("#292929"));
    titleBox.setFill(Color.web("#292929"));

    csText.setFill(Color.web("f1f1f1"));
    softEngText.setFill(Color.web("f1f1f1"));
    wongText.setFill(Color.web("f1f1f1"));
    teamNameText.setFill(Color.web("f1f1f1"));
    copyrightText.setFill(Color.web("f1f1f1"));
    infoText.setFill(Color.web("f1f1f1"));

    meganMajor.setFill(Color.web("#f1f1f1"));
    meganRole.setFill(Color.web("#f1f1f1"));
    meganFood.setFill(Color.web("#f1f1f1"));

    anthonyMajor.setFill(Color.web("#f1f1f1"));
    anthonyRole.setFill(Color.web("#f1f1f1"));
    anthonyFood.setFill(Color.web("#f1f1f1"));

    braedenFood.setFill(Color.web("#f1f1f1"));
    braedenMajor.setFill(Color.web("#f1f1f1"));
    braedenRole.setFill(Color.web("#f1f1f1"));

    jamieFood.setFill(Color.web("#f1f1f1"));
    jamieMajor.setFill(Color.web("#f1f1f1"));
    jamieRole.setFill(Color.web("#f1f1f1"));

    josephFood.setFill(Color.web("#f1f1f1"));
    josephMajor.setFill(Color.web("#f1f1f1"));
    josephRole.setFill(Color.web("#f1f1f1"));

    diyarFood.setFill(Color.web("#f1f1f1"));
    diyarMajor.setFill(Color.web("#f1f1f1"));
    diyarRole.setFill(Color.web("#f1f1f1"));

    kevinFood.setFill(Color.web("#f1f1f1"));
    kevinMajor.setFill(Color.web("#f1f1f1"));
    kevinRole.setFill(Color.web("#f1f1f1"));

    michFood.setFill(Color.web("#f1f1f1"));
    michMajor.setFill(Color.web("#f1f1f1"));
    michRole.setFill(Color.web("#f1f1f1"));

    nickFood.setFill(Color.web("#f1f1f1"));
    nickRole.setFill(Color.web("#f1f1f1"));
    nickMajor.setFill(Color.web("#f1f1f1"));

    albertFood.setFill(Color.web("#f1f1f1"));
    albertMajor.setFill(Color.web("#f1f1f1"));
    albertRole.setFill(Color.web("#f1f1f1"));

    aarshFood.setFill(Color.web("#f1f1f1"));
    aarshMajor.setFill(Color.web("#f1f1f1"));
    aarshRole.setFill(Color.web("#f1f1f1"));

    meganButton.setBackground(Background.fill(Color.web("#292929")));
    meganButton.setTextFill(Color.web("f1f1f1"));
    meganButton.setBorder(Border.stroke(Color.web("#3D3D3D")));

    kevinButton.setBackground(Background.fill(Color.web("#292929")));
    kevinButton.setBorder(Border.stroke(Color.web("#3D3D3D")));
    kevinButton.setTextFill(Color.web("f1f1f1"));

    anthonyButton.setBackground(Background.fill(Color.web("#292929")));
    anthonyButton.setBorder(Border.stroke(Color.web("#3D3D3D")));
    anthonyButton.setTextFill(Color.web("f1f1f1"));

    albertButton.setBackground(Background.fill(Color.web("#292929")));
    albertButton.setBorder(Border.stroke(Color.web("#3D3D3D")));
    albertButton.setTextFill(Color.web("f1f1f1"));

    jamieButton.setBackground(Background.fill(Color.web("#292929")));
    jamieButton.setBorder(Border.stroke(Color.web("#3D3D3D")));
    jamieButton.setTextFill(Color.web("f1f1f1"));

    josephButton.setBackground(Background.fill(Color.web("#292929")));
    josephButton.setBorder(Border.stroke(Color.web("#3D3D3D")));
    josephButton.setTextFill(Color.web("f1f1f1"));

    nichButton.setBackground(Background.fill(Color.web("#292929")));
    nichButton.setBorder(Border.stroke(Color.web("#3D3D3D")));
    nichButton.setTextFill(Color.web("f1f1f1"));

    michButton.setBackground(Background.fill(Color.web("#292929")));
    michButton.setBorder(Border.stroke(Color.web("#3D3D3D")));
    michButton.setTextFill(Color.web("f1f1f1"));

    diyarButton.setBackground(Background.fill(Color.web("#292929")));
    diyarButton.setBorder(Border.stroke(Color.web("#3D3D3D")));
    diyarButton.setTextFill(Color.web("f1f1f1"));

    braedenButton.setBackground(Background.fill(Color.web("#292929")));
    braedenButton.setBorder(Border.stroke(Color.web("#3D3D3D")));
    braedenButton.setTextFill(Color.web("f1f1f1"));

    aarshButton.setBackground(Background.fill(Color.web("#292929")));
    aarshButton.setBorder(Border.stroke(Color.web("#3D3D3D")));
    aarshButton.setTextFill(Color.web("f1f1f1"));

    line2.setStroke(Color.web("#5C5C5C"));
  }

  public void lightMode() {

    backgroundPane.setBackground(Background.fill(Color.web("#1e1e1e")));
    copyrightBox.setFill(Color.web("#f1f1f1"));
    teamBox.setFill(Color.web("#f1f1f1"));
    photoBox.setFill(Color.web("#f1f1f1"));
    titleBox.setFill(Color.web("#f1f1f1"));

    csText.setFill(Color.web("1f1f1f"));
    softEngText.setFill(Color.web("1f1f1f"));
    wongText.setFill(Color.web("1f1f1f"));
    copyrightText.setFill(Color.web("1f1f1f"));
    teamNameText.setFill(Color.web("1f1f1f"));
    infoText.setFill(Color.web("1f1f1f"));

    meganMajor.setFill(Color.web("#1f1f1f"));
    meganRole.setFill(Color.web("#1f1f1f"));
    meganFood.setFill(Color.web("#1f1f1f"));

    anthonyMajor.setFill(Color.web("#1f1f1f"));
    anthonyRole.setFill(Color.web("#1f1f1f"));
    anthonyFood.setFill(Color.web("#1f1f1f"));

    braedenFood.setFill(Color.web("#1f1f1f"));
    braedenMajor.setFill(Color.web("#1f1f1f"));
    braedenRole.setFill(Color.web("#1f1f1f"));

    jamieFood.setFill(Color.web("#1f1f1f"));
    jamieMajor.setFill(Color.web("#1f1f1f"));
    jamieRole.setFill(Color.web("#1f1f1f"));

    josephFood.setFill(Color.web("#1f1f1f"));
    josephMajor.setFill(Color.web("#1f1f1f"));
    josephRole.setFill(Color.web("#1f1f1f"));

    diyarFood.setFill(Color.web("#1f1f1f"));
    diyarMajor.setFill(Color.web("#1f1f1f"));
    diyarRole.setFill(Color.web("#1f1f1f"));

    kevinFood.setFill(Color.web("#1f1f1f"));
    kevinMajor.setFill(Color.web("#1f1f1f"));
    kevinRole.setFill(Color.web("#1f1f1f"));

    michFood.setFill(Color.web("#1f1f1f"));
    michMajor.setFill(Color.web("#1f1f1f"));
    michRole.setFill(Color.web("#1f1f1f"));

    nickFood.setFill(Color.web("#1f1f1f"));
    nickRole.setFill(Color.web("#1f1f1f"));
    nickMajor.setFill(Color.web("#1f1f1f"));

    albertFood.setFill(Color.web("#1f1f1f"));
    albertMajor.setFill(Color.web("#1f1f1f"));
    albertRole.setFill(Color.web("#1f1f1f"));

    aarshFood.setFill(Color.web("#1f1f1f"));
    aarshMajor.setFill(Color.web("#1f1f1f"));
    aarshRole.setFill(Color.web("#1f1f1f"));

    meganButton.setBackground(Background.fill(Color.web("#f1f1f1")));
    meganButton.setTextFill(Color.web("1f1f1f"));
    meganButton.setBorder(Border.stroke(Color.web("#e1e1e1")));

    kevinButton.setBackground(Background.fill(Color.web("#f1f1f1")));
    kevinButton.setBorder(Border.stroke(Color.web("#e1e1e1")));
    kevinButton.setTextFill(Color.web("1f1f1f"));

    anthonyButton.setBackground(Background.fill(Color.web("#f1f1f1")));
    anthonyButton.setBorder(Border.stroke(Color.web("#e1e1e1")));
    anthonyButton.setTextFill(Color.web("1f1f1f"));

    albertButton.setBackground(Background.fill(Color.web("#f1f1f1")));
    albertButton.setBorder(Border.stroke(Color.web("#e1e1e1")));
    albertButton.setTextFill(Color.web("1f1f1f"));

    jamieButton.setBackground(Background.fill(Color.web("#f1f1f1")));
    jamieButton.setBorder(Border.stroke(Color.web("#e1e1e1")));
    jamieButton.setTextFill(Color.web("1f1f1f"));

    josephButton.setBackground(Background.fill(Color.web("#f1f1f1")));
    josephButton.setBorder(Border.stroke(Color.web("#e1e1e1")));
    josephButton.setTextFill(Color.web("1f1f1f"));

    nichButton.setBackground(Background.fill(Color.web("#f1f1f1")));
    nichButton.setBorder(Border.stroke(Color.web("#e1e1e1")));
    nichButton.setTextFill(Color.web("1f1f1f"));

    michButton.setBackground(Background.fill(Color.web("#f1f1f1")));
    michButton.setBorder(Border.stroke(Color.web("#e1e1e1")));
    michButton.setTextFill(Color.web("1f1f1f"));

    diyarButton.setBackground(Background.fill(Color.web("#f1f1f1")));
    diyarButton.setBorder(Border.stroke(Color.web("#e1e1e1")));
    diyarButton.setTextFill(Color.web("1f1f1f"));

    braedenButton.setBackground(Background.fill(Color.web("#f1f1f1")));
    braedenButton.setBorder(Border.stroke(Color.web("#e1e1e1")));
    braedenButton.setTextFill(Color.web("1f1f1f"));

    aarshButton.setBackground(Background.fill(Color.web("#f1f1f1")));
    aarshButton.setBorder(Border.stroke(Color.web("#e1e1e1")));
    aarshButton.setTextFill(Color.web("1f1f1f"));

    line2.setStroke(Color.web("#1f1f1f"));
  }
}
