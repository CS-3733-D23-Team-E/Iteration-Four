package edu.wpi.teame.map;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.Main;
import java.util.List;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import lombok.Getter;

public class Directions extends RadioButton {
  List<HospitalNode> path;
  HBox hbox;
  @Getter TurnType turnType;
  @Getter HospitalNode currentNode;
  @Getter Floor currentFloor;
  int index, distance;

  public Directions(List<HospitalNode> path, int index, TurnType turnType, int distance) {

    // Set values
    this.path = path;
    this.index = index;
    this.hbox = new HBox();
    this.turnType = turnType;
    this.distance = distance;
    this.currentNode = path.get(index);
    this.currentFloor = this.currentNode.getFloor();

    // Configure hbox
    configureHBOX();
    setStyling();
    setInteractions();
    setAttributes();
  }

  public void setStyling() {
    // Drop Shadow
    DropShadow dropShadow = new DropShadow();
    dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
    dropShadow.setWidth(21);
    dropShadow.setHeight(21);
    dropShadow.setRadius(4);
    dropShadow.setOffsetY(3);
    dropShadow.setSpread(0);
    dropShadow.setColor(new Color(0, 0, 0, 0.25));

    // Set the drop shadow
    setEffect(dropShadow);
    getStyleClass().remove("radio-button");
    getStyleClass().add("direction");
  }

  public void setInteractions() {
    // Make the box bigger when hovering
    setOnMouseEntered(
        event -> {
          ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200));
          scaleTransition.setNode(this);
          scaleTransition.setToX(1.02);
          scaleTransition.setToY(1.02);
          scaleTransition.play();
        });
    // Smaller on exit
    setOnMouseExited(
        event -> {
          ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200));
          scaleTransition.setNode(this);
          scaleTransition.setToX(1);
          scaleTransition.setToY(1);
          scaleTransition.play();
        });
  }

  public void configureHBOX() {
    hbox.setAlignment(Pos.CENTER_LEFT);
    hbox.setSpacing(10);
    hbox.setPadding(new Insets(0, 10, 0, 10));
  }

  public void setAttributes() {
    Image icon = null;
    Floor nextFloor;
    String directionsText = "In " + distance + "ft ";
    switch (this.turnType) {
      case START:
        icon = new Image(String.valueOf(Main.class.getResource("images/start.png")));
        directionsText =
            SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(currentNode.getNodeID()));
        break;
      case END:
        icon = new Image(String.valueOf(Main.class.getResource("images/destination.png")));
        directionsText =
            SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(currentNode.getNodeID()))
                + " ("
                + distance
                + "ft)";
        break;
      case RIGHT:
        icon = new Image(String.valueOf(Main.class.getResource("images/right_arrow.png")));
        directionsText += "turn right.";
        break;
      case LEFT:
        icon = new Image(String.valueOf(Main.class.getResource("images/left_arrow.png")));
        directionsText += "turn left.";
        break;
      case STRAIGHT:
        icon = new Image(String.valueOf(Main.class.getResource("images/straight_arrow.png")));
        directionsText += "continue straight.";
        break;
      case ELEVATOR:
        nextFloor = path.get(index + 1).getFloor();
        icon = new Image(String.valueOf(Main.class.getResource("images/elevator.png")));
        if (currentNode.getFloor() != nextFloor) {
          directionsText +=
              "take the elevator to floor "
                  + Floor.floorToString(path.get(index + 1).getFloor())
                  + ".";
        } else {
          directionsText = "Floor " + Floor.floorToString(nextFloor);
        }
        break;
      case STAIRS:
        nextFloor = path.get(index + 1).getFloor();
        icon = new Image(String.valueOf(Main.class.getResource("images/stairs.png")));
        if (currentNode.getFloor() != nextFloor) {
          directionsText +=
              "take the stairs to floor "
                  + Floor.floorToString(path.get(index + 1).getFloor())
                  + ".";
        } else {
          directionsText = "Floor " + Floor.floorToString(nextFloor);
        }
        break;
      case ERROR:
        icon = new Image(String.valueOf(Main.class.getResource("images/interrogation.png")));
        directionsText = "ERROR";
        break;
    }
    // Set the image view icon
    ImageView pathIcon = new ImageView();
    pathIcon.setImage(icon);
    pathIcon.setPreserveRatio(true);
    pathIcon.setFitWidth(30);

    // Draw the dividing line
    Line line = new Line();
    line.setStartX(0);
    line.setStartY(0);
    line.setEndX(0);
    line.setEndY(50);

    // Set the label text
    Label destinationLabel = new Label(directionsText);
    destinationLabel.setFont(Font.font("Roboto", 18));
    destinationLabel.setTextAlignment(TextAlignment.LEFT);
    destinationLabel.setWrapText(true);

    // Add the attributes to the HBox
    hbox.getChildren().addAll(pathIcon, line, destinationLabel);
    this.setGraphic(hbox);
  }
}
