package edu.wpi.teame.controllers;

import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import java.awt.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class RootController {

  @FXML BorderPane pane;
  PauseTransition pause = new PauseTransition();

  @FXML
  public void initialize() {

    pause.setDuration(Duration.seconds(Settings.INSTANCE.screenSaverTime));
    System.out.println("Time" + Settings.INSTANCE.screenSaverTime);

    Platform.runLater(
        () -> {
          pane.getScene()
              .addEventFilter(
                  MouseEvent.MOUSE_MOVED,
                  new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                      pause.playFromStart();
                    }
                  });
        });

    pause.setOnFinished(event2 -> Navigation.navigate(Screen.SCREEN_SAVER));

    pause.play();
  }
}
