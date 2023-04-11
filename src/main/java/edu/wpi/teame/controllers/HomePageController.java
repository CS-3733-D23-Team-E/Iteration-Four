package edu.wpi.teame.controllers;

import edu.wpi.teame.entities.LoginData;
import edu.wpi.teame.navigation.Navigation;
import edu.wpi.teame.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.awt.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

public class HomePageController {
  @FXML MFXButton serviceRequestButton;
  @FXML MFXButton signageButton;
  @FXML MFXButton databaseViewButton;
  @FXML MFXButton mapsButton;
  @FXML MFXButton loginButton;
  @FXML TextField username;
  @FXML TextField password;
  @FXML MFXButton menuButton;
  @FXML MFXButton menuBarHome;
  @FXML MFXButton menuBarServices;
  @FXML MFXButton menuBarMaps;
  @FXML MFXButton menuBarDatabase;
  @FXML MFXButton menuBarExit;

  @FXML VBox menuVBox;

  Boolean loggedIn;

  boolean menuVisibilty = false;

  public void initialize() {

    //        mouseSetup(serviceRequestButton);
    //        mouseSetup(signageButton);
    //        mouseSetup(mapsButton);
    //        mouseSetup(databaseViewButton);

    serviceRequestButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));
    signageButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
    databaseViewButton.setOnMouseClicked(event -> Navigation.navigate(Screen.DATABASE_VIEW));
    mapsButton.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));

    loggedIn = false;
    loginButton.setOnMouseClicked(event -> attemptLogin());


    //Initially set the menuVBox to invisible
    menuVBox.setVisible(false);

    //When the menu button is clicked, invert the value of menuVisibility and set the menuVBox to that value
    //(so each time the menu button is clicked it changes the visibility of menuVBox back and forth)
    menuButton.setOnMouseClicked(event ->
    {
      menuVisibilty = !menuVisibilty;
      menuVBox.setVisible(menuVisibilty);
    });

    //Navigation controls for the button in the menuVBox
    menuBarHome.setOnMouseClicked(event ->
    {
      Navigation.navigate(Screen.HOME);
      menuVisibilty = !menuVisibilty;
    });
    menuBarServices.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));
    menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
    menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate(Screen.DATABASE_VIEW));
    menuBarExit.setOnMouseClicked((event -> Platform.exit()));
  }

  //    private void mouseSetup(MFXButton btn) {
  //      btn.setOnMouseEntered(
  //          event -> {
  //            btn.setStyle(
  //                "-fx-background-color: #ffffff; -fx-alignment: center; -fx-border-color:
  // #192d5a; -fx-border-width: 2;");
  //            btn.setTextFill(Color.web("#192d5aff", 1.0));
  //          });
  //      btn.setOnMouseExited(
  //          event -> {
  //            btn.setStyle("-fx-background-color: #192d5aff; -fx-alignment: center;");
  //            btn.setTextFill(WHITE);
  //          });
  //    }

  public void attemptLogin() {
    // Get the input login info
    LoginData login = new LoginData(username.getText(), password.getText());

    // If the login was successful
    if (login.attemptLogin()) {
      // Hide text fields and button
      password.setVisible(false);
      username.setVisible(false);
      loginButton.setVisible(false);
      // Set loggedIn as true
      loggedIn = true;

    } else {
      // Clear the fields
      password.clear();
      username.clear();
    }
  }
}
