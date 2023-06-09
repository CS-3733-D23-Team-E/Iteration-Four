package edu.wpi.teame.controllers.DatabaseEditor;

import edu.wpi.teame.map.Floor;
import edu.wpi.teame.map.HospitalNode;
import edu.wpi.teame.utilities.ColorPalette;
import edu.wpi.teame.utilities.MapUtilities;
import java.util.LinkedList;
import java.util.List;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import lombok.Setter;
import net.kurobako.gesturefx.GesturePane;

public class MovePreviewController {
  @FXML AnchorPane mapPaneLowerTwo;
  @FXML AnchorPane mapPaneLowerOne;
  @FXML AnchorPane mapPaneOne;
  @FXML AnchorPane mapPaneTwo;
  @FXML AnchorPane mapPaneThree;

  @FXML ImageView mapImageLowerTwo; // Floor L2
  @FXML ImageView mapImageLowerOne; // Floor L1
  @FXML ImageView mapImageOne; // Floor 1
  @FXML ImageView mapImageTwo; // Floor 2
  @FXML ImageView mapImageThree; // Floor 3

  @FXML Tab floorOneTab;
  @FXML Tab floorTwoTab;
  @FXML Tab floorThreeTab;
  @FXML Tab lowerLevelTwoTab;
  @FXML Tab lowerLevelOneTab;

  @FXML TabPane tabPane;

  @FXML Label moveDescription;
  @FXML VBox viewMoveBox;

  Floor currentFloor;

  MapUtilities mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
  MapUtilities mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
  MapUtilities mapUtilityOne = new MapUtilities(mapPaneOne);
  MapUtilities mapUtilityTwo = new MapUtilities(mapPaneTwo);
  MapUtilities mapUtilityThree = new MapUtilities(mapPaneThree);

  boolean widthLoaded = false;
  boolean heightLoaded = false;

  HospitalNode node1;
  HospitalNode node2;
  String name1;
  String name2;
  @Setter boolean bidirectional;

  Circle currentCircle;
  HBox previousLabel;
  List<Node> toNode2Arrow;
  List<Node> toNode1Arrow;

  HospitalNode selectedNode;

  public MovePreviewController(
      HospitalNode node1, HospitalNode node2, String name1, String name2, boolean bidirectional) {
    this.node1 = node1;
    this.node2 = node2;
    this.name1 = name1;
    this.name2 = name2;
    this.bidirectional = bidirectional;
  }

  public MovePreviewController() {
    // This constructor exists so that the move controller can be initialized without setting the
    // nodes and names!
  }

  @FXML
  public void initialize() {
    System.out.println("Initializing move preview!!");
    tabPane
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observable, oldTab, newTab) -> {
              // Set the zoom and position of the new pane to the old one
              AnchorPane oldPane = (AnchorPane) oldTab.getContent();
              GesturePane oldGesture = (GesturePane) oldPane.getChildren().get(0);
              AnchorPane newPane = (AnchorPane) newTab.getContent();
              GesturePane newGesture = (GesturePane) newPane.getChildren().get(0);
              adjustGesture(oldGesture, newGesture);
              // Switch direction of arrow dependent on which node is selected
              if (node2 != null && selectedNode != null && selectedNode.equals(node1)) {
                renderNodeArrow(toNode2Arrow, toNode1Arrow);
              } else if (node1 != null && selectedNode != null && selectedNode.equals(node2)) {
                renderNodeArrow(toNode1Arrow, toNode2Arrow);
              }
            });
    // clearMapUtilities();
    initializeMapUtilities();
    currentFloor = Floor.LOWER_TWO;

    tabPane
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              currentFloor = tabToFloor(newValue);
              refreshMap();
            });

    // TODO do this better

    mapPaneLowerTwo
        .widthProperty()
        .addListener(
            (observable, oldWidth, newWidth) -> {
              if (newWidth.doubleValue() > 0) {
                widthLoaded = true;
              }
              if (widthLoaded && heightLoaded) {
                //                currentFloor = Floor.LOWER_TWO;
                //                runInitFunctions();
                if (node1 != null) forceReload();
              }
            });
    mapPaneLowerTwo
        .heightProperty()
        .addListener(
            (observable, oldHeight, newHeight) -> {
              if (newHeight.doubleValue() > 0) {
                heightLoaded = true;
              }
              if (widthLoaded && heightLoaded) {
                //                currentFloor = Floor.LOWER_TWO;
                //                runInitFunctions();
                if (node1 != null) forceReload();
              }
            });

    // set the move description text

    //    StringBuilder moveDescText = new StringBuilder();
    //    moveDescText.append(name1).append(" to node ").append(node2.getNodeID()).append("\n");
    //    if (bidirectional) {
    //      moveDescText.append(name2).append(" to node ").append(node1.getNodeID()).append("\n");
    //    }
    //    moveDescription.setText(moveDescText.toString());

  }

  private void runInitFunctions() {
    loadNodeOrNodes();
    if (node1 != null) {
      tabPane.getSelectionModel().select(floorToTab(node1.getFloor()));
    } else if (node2 != null) {
      tabPane.getSelectionModel().select(floorToTab(node2.getFloor()));
    }
    List<HospitalNode> nodes = new LinkedList<>();
    updateLabelsNodeDependent();
    //    nodes.add(node1);
    //    nodes.add(node2);
    //    createMoveLabels(viewMoveBox, nodes);
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);
    if (node1 != null) {
      ((GesturePane) currentMapUtility.getPane().getParent())
          .centreOn(
              new Point2D(
                  currentMapUtility.convertX(node1.getXCoord()),
                  currentMapUtility.convertY(node1.getYCoord())));
    } else if (node2 != null) {
      ((GesturePane) currentMapUtility.getPane().getParent())
          .centreOn(
              new Point2D(
                  currentMapUtility.convertX(node2.getXCoord()),
                  currentMapUtility.convertY(node2.getYCoord())));
    }
  }

  public void setNode1(HospitalNode node1, String name1) {
    this.node1 = node1;
    this.name1 = name1;
    runInitFunctions();
    // run code to display a path or node according to how many nodes have been initialized!
  }

  public void unsetNode1() {
    this.node1 = null;
    this.name1 = null;
    runInitFunctions();
    // run code to display a path or node according to how many nodes have been initialized!
  }

  public void setNode2(HospitalNode node2, String name2) {
    this.node2 = node2;
    this.name2 = name2;
    runInitFunctions();
    // run code to display a path or node according to how many nodes have been initialized!
  }

  public void unsetNode2() {
    this.node2 = null;
    this.name2 = null;
    runInitFunctions();
    // run code to display a path or node according to how many nodes have been initialized!
  }

  public Floor tabToFloor(Tab tab) {
    if (tab == lowerLevelTwoTab) {
      return Floor.LOWER_TWO;
    }
    if (tab == lowerLevelOneTab) {
      return Floor.LOWER_ONE;
    }
    if (tab == floorOneTab) {
      return Floor.ONE;
    }
    if (tab == floorTwoTab) {
      return Floor.TWO;
    }
    if (tab == floorThreeTab) {
      return Floor.THREE;
    }
    return Floor.ONE;
  }

  private void initializeMapUtilities() {
    mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
    mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
    mapUtilityOne = new MapUtilities(mapPaneOne);
    mapUtilityTwo = new MapUtilities(mapPaneTwo);
    mapUtilityThree = new MapUtilities(mapPaneThree);

    mapUtilityLowerTwo.setLabelStyle("-fx-font-size: 10pt; -fx-background-color: #F1F1F199;");
    mapUtilityLowerOne.setLabelStyle("-fx-font-size: 10pt; -fx-background-color: #F1F1F199;");
    mapUtilityOne.setLabelStyle("-fx-font-size: 10pt; -fx-background-color: #F1F1F199;");
    mapUtilityTwo.setLabelStyle("-fx-font-size: 10pt; -fx-background-color: #F1F1F199;");
    mapUtilityThree.setLabelStyle("-fx-font-size: 10pt; -fx-background-color: #F1F1F199;");
  }

  private void clearMapUtilities() {
    mapUtilityLowerTwo.removeAll();
    mapUtilityLowerOne.removeAll();
    mapUtilityOne.removeAll();
    mapUtilityTwo.removeAll();
    mapUtilityThree.removeAll();
  }

  public void refreshMap() {
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);
    currentMapUtility.removeAll();
    loadNodeOrNodes();
  }

  public MapUtilities whichMapUtility(Floor currFloor) {
    switch (currFloor) {
      case LOWER_TWO:
        return mapUtilityLowerTwo;
      case LOWER_ONE:
        return mapUtilityLowerOne;
      case ONE:
        return mapUtilityOne;
      case TWO:
        return mapUtilityTwo;
      case THREE:
        return mapUtilityThree;
    }
    return mapUtilityLowerOne;
  }

  public void loadFloorNodes() {
    // create edges
    if (node1.getFloor().equals(currentFloor) || node2.getFloor().equals(currentFloor)) {
      // whichMapUtility(currentFloor).drawMove(node1, node2);
      toNode2Arrow = whichMapUtility(currentFloor).drawMoveArrow(node1, node2);
      if (bidirectional) {
        toNode1Arrow = whichMapUtility(currentFloor).drawMoveArrow(node2, node1);
      } else {
        toNode1Arrow = whichMapUtility(currentFloor).drawMoveArrow(node1, node2);
      }
      renderNodeArrow(toNode2Arrow, toNode1Arrow);
      if (node1.getFloor().equals(currentFloor)) {
        // draw phantom label for node 2
        setupNode(node1, name1);
        if (node2.getFloor().equals(currentFloor)) {
          setupNode(node2, name2);
        } else {
          String nodeLabel = "";
          if (selectedNode.equals(node1) || !bidirectional) {
            nodeLabel = "Moved to floor " + node2.getFloor();
          } else {
            nodeLabel = "Moved from floor " + node2.getFloor();
          }
          whichMapUtility(currentFloor).drawHospitalNodeLabel(node2, nodeLabel);
        }
      } else {
        // draw phantom label for node 1
        setupNode(node2, name2);
        String nodeLabel = "";
        if (selectedNode.equals(node1) || !bidirectional) {
          nodeLabel = "Moved from floor " + node1.getFloor();
        } else {
          nodeLabel = "Moved to floor " + node1.getFloor();
        }
        whichMapUtility(currentFloor).drawHospitalNodeLabel(node1, nodeLabel);
      }
    }
  }

  /** @param nodeNum: either 1 or 2 (representing node1 or node2) */
  public void loadSingleNode(int nodeNum) {
    switch (nodeNum) {
      case 1:
        if (node1.getFloor().equals(currentFloor)) {
          setupNode(node1, name1);
        }
        break;
      case 2:
        if (node2.getFloor().equals(currentFloor)) {
          setupNode(node2, name2);
        }
        break;
    }
  }

  public void loadNodeOrNodes() {
    clearMapUtilities();
    if (node1 != null && node2 != null) {
      loadFloorNodes();
      System.out.println("Loading floor nodes!");
    } else {
      if (node1 != null) {
        selectedNode = node1;
        loadSingleNode(1);
        System.out.println("Loading node 1!");
      } else if (node2 != null) {
        selectedNode = node2;
        loadSingleNode(2);
        System.out.println("Loading node 2!");
      } else {
        // DONT LOAD ANY NODES! CLEAR THE MAP?
        System.out.println("Loading nothing!");
      }
    }
  }

  public void updateLabelsNodeDependent() {
    List<HospitalNode> nodes = new LinkedList<>();
    if (node1 != null) nodes.add(node1);
    if (node2 != null) nodes.add(node2);
    createMoveLabels(viewMoveBox, nodes);
  }

  private void setupNode(HospitalNode node, String name) {

    String nodeID = node.getNodeID();
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);

    currentCircle = currentMapUtility.drawHospitalNode(node);
    Label nodeLabel = currentMapUtility.drawHospitalNodeLabel(node, name);
    nodeLabel.setVisible(true);
  }

  public void createMoveLabels(VBox vbox, List<HospitalNode> path) {
    vbox.getChildren().clear();
    for (int i = 0; i < path.size(); i++) {

      HospitalNode currentNode = path.get(i);
      String destination = "";
      if (node1 != null && node2 != null) {
        destination =
            i == 0
                ? (name1 + " to node " + node2.getNodeID())
                : (bidirectional ? (name2 + " to node " + node1.getNodeID()) : name2);
      } else if (node1 != null) {
        destination = name1;
      } else if (node2 != null) {
        destination = name2;
      }
      System.out.println(destination);

      // Line
      Line line = new Line();
      line.setStartX(0);
      line.setStartY(0);
      line.setEndX(0);
      line.setEndY(50);
      line.setOpacity(0.25);

      // Destination Label
      Label destinationLabel = new Label(destination);
      destinationLabel.setFont(Font.font("Roboto", 16));
      destinationLabel.setTextAlignment(TextAlignment.CENTER);
      destinationLabel.setWrapText(true);

      // Drop Shadow
      DropShadow dropShadow = new DropShadow();
      dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
      dropShadow.setWidth(21);
      dropShadow.setHeight(21);
      dropShadow.setRadius(4);
      dropShadow.setOffsetX(-4);
      dropShadow.setOffsetY(4);
      dropShadow.setSpread(0);
      dropShadow.setColor(new Color(0, 0, 0, 0.25));

      // HBox
      HBox hBox = new HBox();
      hBox.setBackground(
          new Background(
              new BackgroundFill(Color.web("#D9DAD7"), CornerRadii.EMPTY, Insets.EMPTY)));
      hBox.setPrefHeight(65);
      hBox.setEffect(dropShadow);
      hBox.setAlignment(Pos.CENTER_LEFT);
      hBox.setSpacing(10);
      hBox.setPadding(new Insets(0, 10, 0, 10));
      hBox.getChildren().add(destinationLabel);

      // Add the event listener
      hBox.setOnMouseClicked(
          event -> {
            Floor nodeFloor = currentNode.getFloor();

            // reset highlighted node
            currentCircle.setRadius(4);
            currentCircle.setViewOrder(-1);
            System.out.println("oldcircle: " + currentCircle.getId());

            tabPane.getSelectionModel().select(floorToTab(nodeFloor));
            MapUtilities currentMapUtility = whichMapUtility(nodeFloor);
            GesturePane startingPane = ((GesturePane) currentMapUtility.getPane().getParent());

            // Outline the hbox
            hBox.setBorder(
                new Border(
                    new BorderStroke(
                        Color.web(ColorPalette.LIGHT_BLUE.getHexCode()),
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths(2))));

            // Remove the previous outline unless previous is null or the same box is clicked again
            if (previousLabel != null && previousLabel != hBox) {
              previousLabel.setBorder(Border.EMPTY);
            }

            // Zoom in on the starting node
            startingPane.zoomTo(2, startingPane.targetPointAtViewportCentre());

            // Pan so starting node is centered
            startingPane.centreOn(
                new Point2D(
                    currentMapUtility.convertX(currentNode.getXCoord()),
                    currentMapUtility.convertY(currentNode.getYCoord())));

            // get Circle that was selected from label
            List<Node> nodeList =
                currentMapUtility.getCurrentNodes().stream()
                    .filter(
                        node -> {
                          try {
                            return node.getId().equals(currentNode.getNodeID());
                          } catch (NullPointerException n) {
                            return false;
                          }
                        })
                    .toList();
            currentCircle = (Circle) nodeList.get(0);
            System.out.println("Newcircle: " + currentCircle.getId());
            currentCircle.setRadius(9);
            currentCircle.setViewOrder(-5);
            System.out.println("currentCircle: " + currentCircle);
            System.out.println("Node List: " + nodeList);
            if (currentNode.equals(node1)) {
              renderNodeArrow(toNode2Arrow, toNode1Arrow);
            } else {
              renderNodeArrow(toNode1Arrow, toNode2Arrow);
            }
            selectedNode = currentNode;

            // Set the current label as the previous
            previousLabel = hBox;
          });

      // Make the box bigger when hovering
      hBox.setOnMouseEntered(
          event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200));
            scaleTransition.setNode(hBox);
            scaleTransition.setToX(1.02);
            scaleTransition.setToY(1.02);
            scaleTransition.play();
          });
      // Smaller on exit
      hBox.setOnMouseExited(
          event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200));
            scaleTransition.setNode(hBox);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();
          });

      // Add path label to VBox
      vbox.getChildren().add(hBox);
    }
  }

  public Tab floorToTab(Floor floor) {
    if (floor == Floor.LOWER_TWO) {
      return lowerLevelTwoTab;
    }
    if (floor == Floor.LOWER_ONE) {
      return lowerLevelOneTab;
    }
    if (floor == Floor.ONE) {
      return floorOneTab;
    }
    if (floor == Floor.TWO) {
      return floorTwoTab;
    }
    if (floor == Floor.THREE) {
      return floorThreeTab;
    }
    return floorOneTab;
  }

  public void adjustGesture(GesturePane oldGesture, GesturePane newGesture) {
    newGesture.centreOn(oldGesture.targetPointAtViewportCentre());
    newGesture.zoomTo(oldGesture.getCurrentScale(), newGesture.targetPointAtViewportCentre());
  }

  private void renderNodeArrow(List<Node> arrow, List<Node> other) {
    for (Node node : arrow) {
      node.setVisible(true);
    }
    for (Node node : other) {
      node.setVisible(false);
    }
  }

  public void forceReload() {
    tabPane.getSelectionModel().select(floorToTab(Floor.LOWER_ONE));
    tabPane.getSelectionModel().select(floorToTab(Floor.LOWER_TWO));
    tabPane.getSelectionModel().select(floorToTab(Floor.ONE));
    tabPane.getSelectionModel().select(floorToTab(Floor.TWO));
    tabPane.getSelectionModel().select(floorToTab(Floor.THREE));
    tabPane.getSelectionModel().select(floorToTab(node1.getFloor()));
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);
    if (node2 != null && selectedNode != null && selectedNode.equals(node1)) {
      renderNodeArrow(toNode2Arrow, toNode1Arrow);
    } else if (node1 != null && selectedNode != null && selectedNode.equals(node2)) {
      renderNodeArrow(toNode1Arrow, toNode2Arrow);
    }
    ((GesturePane) currentMapUtility.getPane().getParent())
        .centreOn(
            new Point2D(
                currentMapUtility.convertX(node1.getXCoord()),
                currentMapUtility.convertY(node1.getYCoord())));
  }
}
