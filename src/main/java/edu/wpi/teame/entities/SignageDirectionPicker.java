package edu.wpi.teame.entities;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.Main;
import java.sql.SQLException;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import lombok.Getter;

public class SignageDirectionPicker extends HBox {
  @Getter Label locationLabel;
  @Getter SignageComponentData componentData;
  @Getter ImageView xIcon;
  @Getter boolean isKioskLocationInDB;

  // Images for the icon
  ImageView pickerIcon;
  Image hereImage = new Image(String.valueOf(Main.class.getResource("images/marker.png")));
  Image arrowImage =
      new Image(String.valueOf(Main.class.getResource("images/arrow-alt-right.png")));
  Image x = new Image(String.valueOf(Main.class.getResource("images/x.png")));

  public SignageDirectionPicker(SignageComponentData signageData) {
    this.componentData = signageData;
    // Set up the icon and combobox
    this.pickerIcon = new ImageView(arrowImage);
    pickerIcon.setPreserveRatio(true);
    pickerIcon.setFitWidth(100);
    pickerIcon.setPickOnBounds(true);

    this.xIcon = new ImageView(x);
    xIcon.setPreserveRatio(true);
    xIcon.setFitWidth(30);
    xIcon.setPickOnBounds(true);

    this.isKioskLocationInDB = checkInDB();
    // Set up the combo box
    this.locationLabel = new Label();
    locationLabel.setText(componentData.getLocationNames());

    // Add both
    this.getChildren().addAll(pickerIcon, locationLabel, xIcon);
    getStyleClass().add("SignageDirectionPicker");

    // Initialize the behavior for the picker and the arrow
    initInteractions();
    updateIcon(componentData.getArrowDirections());
  }

  public void initInteractions() {
    // Rotate the image and change the direction
    pickerIcon.setOnMouseClicked(
        event -> {
          // Set the arrow direction in the component data
          componentData.setArrowDirections(
              SignageComponentData.ArrowDirections.values()[
                  (componentData.getArrowDirections().ordinal() + 1) % 5]);
          // Update the icon
          updateIcon(componentData.getArrowDirections());
        });
    xIcon.setOnMouseClicked(
        event -> {
          // Self destruct
          ((FlowPane) this.getParent()).getChildren().remove(this);
        });
  }

  // Checks if the component data is already in the database
  public boolean checkInDB() {
    boolean retBool = true;
    try {
      // Try to get the direction using the component data
      SignageComponentData.ArrowDirections adTemp =
          SQLRepo.INSTANCE.getDirectionFromPKeyL(
              componentData.getDate(),
              componentData.getKiosk_location(),
              componentData.getLocationNames());
      retBool = true;
    } catch (SQLException e) {
      retBool = false;
    }
    return retBool;
  }

  // Sets the rotation of the icon based on the direction in the SignageComponentData
  public void updateIcon(SignageComponentData.ArrowDirections direction) {
    switch (direction) {
      case UP:
        pickerIcon.setRotate(-90);
        break;
      case DOWN:
        pickerIcon.setRotate(90);
        break;
      case LEFT:
        pickerIcon.setRotate(180);
        break;
      case RIGHT:
        pickerIcon.setImage(arrowImage);
        pickerIcon.setRotate(0);
        break;
      case STOP_HERE:
        pickerIcon.setImage(hereImage);
        pickerIcon.setRotate(0);
    }
  }
}
