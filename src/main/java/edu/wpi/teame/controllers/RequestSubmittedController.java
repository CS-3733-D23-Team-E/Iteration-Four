package edu.wpi.teame.controllers;

import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class RequestSubmittedController {

  @FXML FlowPane goHome;

  public void initialize() {
      // Set invisible on init
      goHome.setVisible(false);
  }

  public void popUp() throws InterruptedException {
      goHome.setVisible(true);
      TimeUnit.SECONDS.sleep(3);
      goHome.setVisible(false);
  }
}
