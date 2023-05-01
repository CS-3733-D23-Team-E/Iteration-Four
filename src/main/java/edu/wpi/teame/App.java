package edu.wpi.teame;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  @Setter @Getter private static Stage primaryStage;
  @Setter @Getter private static BorderPane rootPane;

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    /* primaryStage is generally only used if one of your components require the stage to display */
    App.primaryStage = primaryStage;

    final FXMLLoader loader = new FXMLLoader(App.class.getResource("views/Root.fxml"));
    final BorderPane root = loader.load();

    App.rootPane = root;

    final Scene scene = new Scene(root);
    scene.getStylesheets().add(Main.class.getResource("styles/eStyleSheet.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.setMinWidth(1920);
    primaryStage.setMinHeight(1080);
    primaryStage.show();
    Navigation.navigate(Screen.SIGNAGE_TEXT);
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
