package edu.wpi.teame.controllers;

import edu.wpi.teame.App;
import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.Main;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import java.awt.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class ScreenSaverController {

  @FXML MediaView screenSaverMediaView;

  Point mouseLocation;
  int notMovedTime = 0;

  public void initialize() {

    mouseLocation = new Point();

    Media media = new Media(Main.class.getResource("videos/ScreenSaver.mp4").toString());

    MediaPlayer mediaVideoPlayer = new MediaPlayer(media);

    screenSaverMediaView.setMediaPlayer(mediaVideoPlayer);

    mediaVideoPlayer
        .bufferProgressTimeProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              if (newValue != Duration.UNKNOWN) {
                mediaVideoPlayer.play();
              }
            });

    mediaVideoPlayer.setOnEndOfMedia(() -> logout());
    // screenSaverMediaView.setOnMouseMoved(event -> Navigation.navigate(Screen.HOME));
    Platform.runLater(
        () -> {
          PauseTransition p = new PauseTransition();
          p.setDuration(Duration.seconds(2));
          p.setOnFinished(
              event -> {
                screenSaverMediaView
                    .getScene()
                    .addEventFilter(
                        MouseEvent.MOUSE_MOVED,
                        new EventHandler<MouseEvent>() {
                          @Override
                          public void handle(MouseEvent event) {
                            Navigation.navigate(Screen.HOME);
                            mediaVideoPlayer.stop();
                            mediaVideoPlayer.setStartTime(Duration.seconds(0));
                            System.out.println("Sanity Check");
                            App.getRootPane()
                                .getScene()
                                .removeEventFilter(MouseEvent.MOUSE_MOVED, this);
                          }
                        });
              });

          p.play();
        });
  }

  public void logout() {
    SQLRepo.INSTANCE.exitDatabaseProgram();
    Navigation.navigate(Screen.SIGNAGE_TEXT);
  }
}
