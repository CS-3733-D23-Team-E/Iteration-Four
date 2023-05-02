package edu.wpi.teame.entities;

import edu.wpi.teame.Main;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import lombok.Getter;
import org.controlsfx.control.SearchableComboBox;

public class SignageDirectionPicker extends HBox {
  @Getter SearchableComboBox<String> comboBox;
  @Getter SignageComponentData componentData;

  // Images for the icon
  ImageView pickerIcon;
  Image hereImage = new Image(String.valueOf(Main.class.getResource("images/marker.png")));
  Image arrowImage =
      new Image(String.valueOf(Main.class.getResource("images/arrow-alt-right.png")));

  public SignageDirectionPicker(SignageComponentData signageData) {
    this.componentData = signageData;
    // Set up the icon and combobox
    this.pickerIcon = new ImageView(arrowImage);
    pickerIcon.setPreserveRatio(true);
    pickerIcon.setFitWidth(100);

    // Set up the combo box
    this.comboBox = new SearchableComboBox<String>();
    comboBox.setValue(componentData.getLocationNames());
    comboBox.getStyleClass().add("SearchableComboBox");

    // Add both
    this.getChildren().addAll(this.pickerIcon, this.comboBox);
    getStyleClass().add("SignageDirectionPicker");

    // Initialize the behavior for the picker and the arrow
    initInterations();
    updateIcon(componentData.getArrowDirections());
  }

  public void initInterations() {
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
  }

  // Sets the rotation of the icon based on the direction in the SignageComponentData
  public void updateIcon(SignageComponentData.ArrowDirections direction) {
    switch (direction) {
      case UP:
        pickerIcon.setImage(arrowImage);
        pickerIcon.setRotate(-90);
        break;
      case DOWN:
        pickerIcon.setRotate(90);
        break;
      case LEFT:
        pickerIcon.setRotate(180);
        break;
      case RIGHT:
        pickerIcon.setRotate(0);
        break;
      case STOP_HERE:
        pickerIcon.setImage(hereImage);
        pickerIcon.setRotate(0);
    }
  }
}
