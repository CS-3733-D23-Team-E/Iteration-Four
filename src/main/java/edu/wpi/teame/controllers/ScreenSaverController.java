package edu.wpi.teame.controllers;

import edu.wpi.teame.Main;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class ScreenSaverController {
  @FXML MediaView screenSaverMediaView;

  public void initialize() {

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
  }
}
