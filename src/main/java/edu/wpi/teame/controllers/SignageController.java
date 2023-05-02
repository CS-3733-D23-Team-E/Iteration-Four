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
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.swing.text.html.ImageView;

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

  @FXML
    ImageView arrowOneRight;
    @FXML
    ImageView arrowTwoRight;
    @FXML
    ImageView arrowThreeRight;
    @FXML
    ImageView arrowFourRight;
    @FXML
    ImageView arrowOneLeft;
    @FXML
    ImageView arrowTwoLeft;
    @FXML
    ImageView arrowThreeLeft;
    @FXML
    ImageView arrowFourLeft;

    @FXML Label labelOneRight;
    @FXML Label labelTwoRight;
    @FXML Label labelThreeRight;
    @FXML Label labelFourRight;
    @FXML Label labelOneLeft;
    @FXML Label labelTwoLeft;
    @FXML Label labelThreeLeft;
    @FXML Label labelFourLeft;


    // TODO: Make login work

  boolean loginVisible = false;

  public void initialize() {

    List<SignageComponentData> listOfSignage = SQLRepo.INSTANCE.getSignageList();

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

    populateSignage();
  }

  private void populateSignage() {

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
