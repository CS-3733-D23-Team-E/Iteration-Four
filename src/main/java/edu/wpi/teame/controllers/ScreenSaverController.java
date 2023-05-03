package edu.wpi.teame.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;


public class ScreenSaverController {
  @FXML  WebView webView;

  private Media mediaVideo;

  private MediaPlayer mediaVideoPlayer;

  public void initialize(URL url, ResourceBundle resourceBundle) {

    /*mediaVideo =
        new Media(
            new File("src/main/resources/edu/wpi/teame/images/ScreenSaver.mp4").toURI().toString());
    mediaVideoPlayer = new MediaPlayer(mediaVideo);
    screenSaverMedia.setMediaPlayer(mediaVideoPlayer);
    mediaVideoPlayer.setAutoPlay(true);

    Timeline timeline =
        new Timeline(new KeyFrame(Duration.seconds(1), event -> mediaVideoPlayer.play()));

    timeline.setCycleCount(1);
    timeline.play();*/
  }
}
