package edu.wpi.teame.controllers;

import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

public class RequestSubmittedController {

  @FXML FlowPane goHome;

  public void initialize() {
    goHome.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.HOME);
        });
  }
}
