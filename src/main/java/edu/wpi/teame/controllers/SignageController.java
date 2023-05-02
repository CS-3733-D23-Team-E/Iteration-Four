package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.entities.SignageComponentData;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

public class SignageController {
  @FXML MFXButton userButton;
  @FXML MFXButton loginButton;
  @FXML MFXTextField usernameField;
  @FXML MFXTextField passwordField;
  @FXML StackPane loginStack;
  @FXML VBox loginFailBox;

  @FXML MFXButton closeButton;

  @FXML Text timeText;
  @FXML Text dateText;

  @FXML ImageView arrowOneRight;
  @FXML ImageView arrowTwoRight;
  @FXML ImageView arrowThreeRight;
  @FXML ImageView arrowFourRight;
  @FXML ImageView arrowOneLeft;
  @FXML ImageView arrowTwoLeft;
  @FXML ImageView arrowThreeLeft;
  @FXML ImageView arrowFourLeft;

  @FXML Label labelOneRight;
  @FXML Label labelTwoRight;
  @FXML Label labelThreeRight;
  @FXML Label labelFourRight;
  @FXML Label labelOneLeft;
  @FXML Label labelTwoLeft;
  @FXML Label labelThreeLeft;
  @FXML Label labelFourLeft;

  @FXML Label stopHereLabel;

  static class ArrowAndLabel {
    @Setter @Getter Label label;
    @Setter @Getter ImageView arrow;

    public ArrowAndLabel(Label label, ImageView arrow) {
      this.label = label;
      this.arrow = arrow;
    }
  }

  private final LinkedList<ArrowAndLabel> arrowsAndLabels = new LinkedList<>();

  boolean loginVisible = false;

  public void initialize() {
    loginPopout(false);
    loginFailBox.setVisible(false);

    userButton.setOnMouseClicked(
        event -> {
          System.out.println("hello");
          loginVisible = !loginVisible;
          loginPopout(loginVisible);
          usernameField.requestFocus();
          usernameField.positionCaret(0);
        });
    usernameField.setOnKeyPressed(
        event -> {
          if (event.getCode() == (KeyCode.ENTER)) attemptLogin();
        });
    passwordField.setOnKeyPressed(
        event -> {
          if (event.getCode() == (KeyCode.ENTER)) attemptLogin();
        });
    loginButton.setOnMouseClicked(
        event -> {
          attemptLogin();
        });

    LocalTime currentTime = LocalTime.now();
    LocalDate currentDate = LocalDate.now();

    // Format current date as a string
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    String currentDateString = currentDate.format(dateFormat);
    // Format the current time as a string
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
    String currentTimeString = currentTime.format(timeFormat);

    timeText.setText(currentTimeString);
    dateText.setText(currentDateString);

    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  LocalTime now = LocalTime.now();
                  String formattedTime = now.format(timeFormat);
                  timeText.setText(formattedTime);
                }));

    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    initializeArrowsAndLabels();
    populateSignage();
  }

  private void initializeArrowsAndLabels() {
    this.arrowsAndLabels.add(new ArrowAndLabel(labelOneLeft, arrowOneLeft));
    this.arrowsAndLabels.add(new ArrowAndLabel(labelTwoLeft, arrowTwoLeft));
    this.arrowsAndLabels.add(new ArrowAndLabel(labelThreeLeft, arrowThreeLeft));
    this.arrowsAndLabels.add(new ArrowAndLabel(labelFourLeft, arrowFourLeft));
    this.arrowsAndLabels.add(new ArrowAndLabel(labelOneRight, arrowOneRight));
    this.arrowsAndLabels.add(new ArrowAndLabel(labelTwoRight, arrowTwoLeft));
    this.arrowsAndLabels.add(new ArrowAndLabel(labelThreeRight, arrowThreeRight));
    this.arrowsAndLabels.add(new ArrowAndLabel(labelFourRight, arrowFourRight));
  }

  private void populateSignage() {
    List<SignageComponentData> listOfSignage = SQLRepo.INSTANCE.getSignageList();
    //    listOfSignage =
    //        listOfSignage.stream()
    //            .filter(
    //                (signageComponentData) ->
    //
    // signageComponentData.getKiosk_location().equals(Settings.INSTANCE.currentKiosk))
    //            .toList();
    //
    //    int currentBox = 0;
    //    for (SignageComponentData signageComponentData : listOfSignage) {
    //      String locationName = signageComponentData.getLocationNames();
    //      SignageComponentData.ArrowDirections direction =
    // signageComponentData.getArrowDirections();
    //
    //      if (currentBox > 8) {
    //        break;
    //      }
    //
    //      if (direction == SignageComponentData.ArrowDirections.STOP_HERE) {
    //        updateStopHere(signageComponentData);
    //      }
    //
    //      ImageView arrow = arrowsAndLabels.get(currentBox).arrow;
    //      rotateArrow(arrow, direction);
    //      arrow.setRotate(arrow.getRotate() + 90);
    //
    //      Label label = arrowsAndLabels.get(currentBox).label;
    //      label.setText(locationName);
    //
    //      currentBox++;
    //    }
    //
    //    System.out.println(listOfSignage);
  }

  public void updateStopHere(SignageComponentData signageComponentData) {
    String newText = signageComponentData.getLocationNames();
    String currentText = stopHereLabel.getText();
    stopHereLabel.setText(currentText + "\n" + newText);
  }

  private void rotateArrow(ImageView arrow, SignageComponentData.ArrowDirections arrowDirection) {
    switch (arrowDirection) {
      case UP:
        arrow.setRotate(-90);
        break;
      case DOWN:
        arrow.setRotate(90);
        break;
      case LEFT:
        arrow.setRotate(180);
        break;
      case RIGHT:
        arrow.setRotate(0);
        break;
      case STOP_HERE:
        break;
    }
  }

  private void attemptLogin() {
    Employee staffMember =
        SQLRepo.INSTANCE.connectToDatabase(
            usernameField.getText(), passwordField.getText(), SQLRepo.DB.WPI);
    if (staffMember == null) {
      loginFailBox.setVisible(true);
      closeButton.setOnMouseClicked(
          event -> {
            loginFailBox.setVisible(false);
          }); // popup to
      // display incorrect login message
      return;
    }
    //     Employee.currentEmployee = staffMember;

    // Successful login
    Navigation.navigate(Screen.HOME);
  }

  public void loginPopout(boolean bool) {
    loginStack.setVisible(bool);
  }
}
