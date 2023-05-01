package edu.wpi.teame.controllers;

import static java.lang.Math.PI;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.map.*;
import edu.wpi.teame.map.Floor;
import edu.wpi.teame.map.HospitalNode;
import edu.wpi.teame.map.LocationName;
import edu.wpi.teame.map.pathfinding.AbstractPathfinder;
import edu.wpi.teame.utilities.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javafx.animation.Interpolator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.ToggleSwitch;

public class MapController {
  @FXML AnchorPane mapPaneOne;
  @FXML AnchorPane mapPaneTwo;
  @FXML AnchorPane mapPaneThree;
  @FXML AnchorPane mapPaneLowerOne;
  @FXML AnchorPane mapPaneLowerTwo;
  @FXML VBox pathBox;
  @FXML TabPane tabPane;
  @FXML Tab floorOneTab;
  @FXML Tab floorTwoTab;
  @FXML Tab floorThreeTab;
  @FXML Tab lowerLevelTwoTab;
  @FXML Tab lowerLevelOneTab;
  @FXML SearchableComboBox<String> currentLocationList;
  @FXML SearchableComboBox<String> destinationList;
  @FXML DatePicker pathfindingDate;
  @FXML MFXButton startButton;
  @FXML RadioButton aStarButton;
  @FXML RadioButton dfsButton;
  @FXML RadioButton bfsButton;
  @FXML Region zoomInRegion;
  @FXML Region zoomOutRegion;
  @FXML RadioButton dijkstraButton;
  @FXML GesturePane gesturePaneL2;
  @FXML GesturePane gesturePaneL1;
  @FXML GesturePane gesturePane1;
  @FXML GesturePane gesturePane2;
  @FXML GesturePane gesturePane3;
  @FXML ToggleSwitch labelSwitch;
  @FXML Button zoomInButton;
  @FXML Button zoomOutButton;
  boolean disableLabel = false;
  boolean isPathDisplayed = false;
  Floor currentFloor = Floor.LOWER_TWO;
  String language;
  Circle currentCircle = new Circle();
  Label currentFrontLabel = null;
  ToggleGroup directionGroup;
  AbstractPathfinder pf = AbstractPathfinder.getInstance("A*");
  String curLocFromComboBox;
  String destFromComboBox;
  MapUtilities mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
  MapUtilities mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
  MapUtilities mapUtilityOne = new MapUtilities(mapPaneOne);
  MapUtilities mapUtilityTwo = new MapUtilities(mapPaneTwo);
  MapUtilities mapUtilityThree = new MapUtilities(mapPaneThree);

  List<Label> allLocationNameLabels = new LinkedList<>();

  MoveUtilities moveUtilities = new MoveUtilities();

  ObservableList<String> floorLocations =
      FXCollections.observableArrayList(
          SQLRepo.INSTANCE.getLongNamesFromMove(
              SQLRepo.INSTANCE.getMoveAttributeFromFloor(currentFloor)));

  HashMap<String, String> nameToNodeID;
  HashMap<String, String> nodeToLongName;

  @FXML
  public void initialize() {
    initializeMapUtilities();
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
            });

    startButton.setOnAction(
        event -> {
          curLocFromComboBox = currentLocationList.getValue();
          destFromComboBox = destinationList.getValue();
          displayPath(curLocFromComboBox, destFromComboBox);
        });

    // Set the svg images for the map buttons
    setSVG();
    // Make sure location list is initialized so that we can filter out the hallways
    LocationName.processLocationList(SQLRepo.INSTANCE.getLocationList());
    resetComboboxes();
    // Set the default date to be the current date
    pathfindingDate.setValue(LocalDate.now());

    // Set the default location
    if (Settings.INSTANCE.getDefaultLocation() != null) {
      currentLocationList.setValue(Settings.INSTANCE.getDefaultLocation());
    }
  }

  private void initializeMapUtilities() {
    mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
    mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
    mapUtilityOne = new MapUtilities(mapPaneOne);
    mapUtilityTwo = new MapUtilities(mapPaneTwo);
    mapUtilityThree = new MapUtilities(mapPaneThree);

    mapUtilityLowerTwo.setCircleStyle("-fx-fill: #009CA6; -fx-stroke: black; -fx-stroke-width: 1");
    mapUtilityLowerOne.setCircleStyle("-fx-fill: #009CA6; -fx-stroke: black; -fx-stroke-width: 1");
    mapUtilityOne.setCircleStyle("-fx-fill: #009CA6; -fx-stroke: black; -fx-stroke-width: 1");
    mapUtilityTwo.setCircleStyle("-fx-fill: #009CA6; -fx-stroke: black; -fx-stroke-width: 1");
    mapUtilityThree.setCircleStyle("-fx-fill: #009CA6; -fx-stroke: black; -fx-stroke-width: 1");

    mapUtilityLowerTwo.setLineStyle("-fx-stroke: #3B63A5; -fx-stroke-width: 4");
    mapUtilityLowerOne.setLineStyle("-fx-stroke: #3B63A5; -fx-stroke-width: 4");
    mapUtilityOne.setLineStyle("-fx-stroke: #3B63A5; -fx-stroke-width: 4");
    mapUtilityTwo.setLineStyle("-fx-stroke: #3B63A5; -fx-stroke-width: 4");
    mapUtilityThree.setLineStyle("-fx-stroke: #3B63A5; -fx-stroke-width: 4");
  }

  public void resetComboboxes() {
    floorLocations =
        FXCollections.observableArrayList(
            LocationName.allLocations.values().stream()
                .filter(
                    (location) -> // Filter out hallways and long names with no corresponding
                        // LocationName
                        location == null
                            ? false
                            : location.getNodeType() != LocationName.NodeType.HALL
                                && location.getNodeType() != LocationName.NodeType.STAI
                                && location.getNodeType() != LocationName.NodeType.ELEV
                                && location.getNodeType() != LocationName.NodeType.REST)
                .map((location) -> location.getLongName())
                .sorted() // Sort alphabetically
                .toList());
    currentLocationList.setItems(floorLocations);
    destinationList.setItems(floorLocations);
    currentLocationList.setValue(null);
    destinationList.setValue(null);
  }

  @FXML
  public void displayPath(String from, String to) {
    if (from == null || from.equals("") || to == null || to.equals("")) {
      return;
    }
    refreshPath();

    // Choose the pathfinding method based on the selected radio button
    if (aStarButton.isSelected()) {
      pf = AbstractPathfinder.getInstance("A*");
    }
    if (dfsButton.isSelected()) {
      pf = AbstractPathfinder.getInstance("DFS");
    }
    if (bfsButton.isSelected()) {
      pf = AbstractPathfinder.getInstance("BFS");
    }
    if (dijkstraButton.isSelected()) {
      pf = AbstractPathfinder.getInstance("Dijkstra");
    }
    nameToNodeID = moveUtilities.getMapForDate(pathfindingDate.getValue());
    nodeToLongName = moveUtilities.invertHashMap(nameToNodeID);
    String toNodeID = nameToNodeID.get(to);
    String fromNodeID = nameToNodeID.get(from);

    List<HospitalNode> path =
        pf.findPath(HospitalNode.allNodes.get(fromNodeID), HospitalNode.allNodes.get(toNodeID));
    if (path == null) {
      System.out.println("Path does not exist");
      return;
    }
    ArrayList<String> pathNames = new ArrayList<>();
    for (HospitalNode node : path) {
      pathNames.add(SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(node.getNodeID())));
    }
    // Create the directions
    createDirections(pathBox, path);
    createLabelsForToggleDisplay(path);
    makeLocationNamesVisible(disableLabel);
    drawPath(path);
    isPathDisplayed = true;
  }

  /**
   * draws the path with lines connecting each node on the map
   *
   * @param path
   */
  private void drawPath(List<HospitalNode> path) {

    // Reset the zoom of gesture panes
    gesturePaneL2.reset();
    gesturePaneL1.reset();
    gesturePane1.reset();
    gesturePane2.reset();
    gesturePane3.reset();

    currentFloor = path.get(0).getFloor();
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);
    Floor startingFloor = currentFloor;
    Floor oldFloor = null;

    int startX, startY;
    // create circle to symbolize start
    int x1 = path.get(0).getXCoord();
    int y1 = path.get(0).getYCoord();
    startX = x1;
    startY = y1;
    Circle currentLocationCircle = currentMapUtility.drawStyledCircle(x1, y1, 4);
    currentLocationCircle.setId(path.get(0).getNodeID());
    currentLocationCircle.setViewOrder(-3);

    Label startLabel = currentMapUtility.createLabel(x1, y1, 5, 5, "Current Location");
    int daysUntilMove =
        moveUtilities.daysCompareMove(
            nodeToLongName.get(path.get(0).getNodeID()), pathfindingDate.getValue());
    startLabel.setTooltip(null);

    if (daysUntilMove > 0 && daysUntilMove <= 7) {

      startLabel.setTooltip(
          new Tooltip("This location will be moved in " + daysUntilMove + " day(s)"));
      startLabel.getTooltip().setFont(new Font("Roboto", 20));
      startLabel.setText(startLabel.getText() + "*");

    } else if (daysUntilMove <= 0 && daysUntilMove >= -7) {

      startLabel.setTooltip(
          new Tooltip("This location recently moved " + -daysUntilMove + " day(s) ago"));
      startLabel.getTooltip().setFont(new Font("Roboto", 20));
      startLabel.setText(startLabel.getText() + "*");
    }

    // draw the lines between each node
    int x2, y2;
    for (int i = 1; i < path.size(); i++) {
      HospitalNode node = path.get(i);
      x2 = node.getXCoord();
      y2 = node.getYCoord();

      oldFloor = currentFloor;
      currentFloor = node.getFloor();

      // If the current floor is different from the previous then change the map utlilty
      if (oldFloor != currentFloor) {
        currentMapUtility = whichMapUtility(currentFloor);
      }

      // Only draw a line between nodes if the current floor is the same as the new floor
      if (oldFloor == currentFloor) {
        currentMapUtility.drawStyledLine(x1, y1, x2, y2);
      }
      Circle intermediateCircle = currentMapUtility.drawStyledCircle(x2, y2, 4);
      intermediateCircle.setViewOrder(-2);
      intermediateCircle.setId(node.getNodeID());
      intermediateCircle.setVisible(false);
      x1 = x2;
      y1 = y2;
    }

    // create circle to symbolize end
    Circle endingCircle = currentMapUtility.drawStyledCircle(x1, y1, 4);
    endingCircle.setId(path.get(path.size() - 1).getNodeID());
    endingCircle.setViewOrder(-5);

    Label endLabel = currentMapUtility.createLabel(x1, y1, 5, 5, "Destination");
    daysUntilMove =
        moveUtilities.daysCompareMove(
            nodeToLongName.get(path.get(path.size() - 1).getNodeID()), pathfindingDate.getValue());
    endLabel.setTooltip(null);
    endLabel.setViewOrder(-8);

    if (daysUntilMove > 0 && daysUntilMove <= 7) {

      endLabel.setTooltip(
          new Tooltip("This location will be moved in " + daysUntilMove + " day(s)"));
      endLabel.getTooltip().setFont(new Font("Roboto", 20));
      endLabel.setText(endLabel.getText() + "*");

    } else if (daysUntilMove < 0 && daysUntilMove >= -7) {

      endLabel.setTooltip(
          new Tooltip("This location recently moved " + -daysUntilMove + " day(s) ago"));
      endLabel.getTooltip().setFont(new Font("Roboto", 20));
      endLabel.setText(endLabel.getText() + "*");
    }

    // Switch the current tab to the same floor as the starting point
    currentFloor = startingFloor;
    tabPane.getSelectionModel().select(floorToTab(startingFloor));
    currentMapUtility = whichMapUtility(currentFloor);
    GesturePane startingPane = ((GesturePane) currentMapUtility.getPane().getParent());

    // Zoom in on the starting node
    startingPane.zoomTo(2, startingPane.targetPointAtViewportCentre());

    // Pan so starting node is centered
    startingPane.centreOn(
        new Point2D(currentMapUtility.convertX(startX), currentMapUtility.convertY(startY)));
  }

  /** removes all the lines in the currentLines list */
  public void refreshPath() {
    currentCircle.setRadius(4);
    mapUtilityLowerTwo.removeAll();
    mapUtilityLowerOne.removeAll();
    mapUtilityOne.removeAll();
    mapUtilityTwo.removeAll();
    mapUtilityThree.removeAll();
    pathBox.getChildren().clear();
    isPathDisplayed = false;
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

  public void createDirections(VBox vbox, List<HospitalNode> path) {

    // Create the toggle group for the buttons
    directionGroup = new ToggleGroup();

    // For each node along the path
    int currentDistance = 0;
    Floor prevFloor = null;
    for (int i = 0; i < path.size(); i++) {

      // Get the turn type
      TurnType turnType = getTurn(path, i);
      // Get the distance
      currentDistance += getDistance(path, i);

      // If the turn type is not straight
      if (turnType != TurnType.STRAIGHT) {
        // Create a direction
        Directions direction = new Directions(path, i, turnType, currentDistance);
        if (i == 0) {
          direction.setSelected(true);
        }
        currentDistance = 0;

        // If the current floor is different from the previous, add a label
        if (direction.getCurrentFloor() != prevFloor) {
          Label newFloorLabel =
              new Label("Floor " + Floor.floorToString(direction.getCurrentFloor()));
          newFloorLabel.getStyleClass().add("direction-label");
          pathBox.getChildren().add(newFloorLabel);
        }
        prevFloor = direction.getCurrentFloor();
        // Add the direction to the vbox and toggle group
        direction.setToggleGroup(directionGroup);
        vbox.getChildren().add(direction);
      }
    }
    // Set the listener for the toggle group
    directionGroup
        .selectedToggleProperty()
        .addListener(
            (observable, oldValue, newValue) -> {

              // Get the node
              HospitalNode currentNode = ((Directions) newValue).getCurrentNode();

              // reset highlighted node
              currentCircle.setRadius(4);
              currentCircle.setViewOrder(-2);
              currentCircle.setVisible(false);

              // Set the selected tab to the floor of the node
              Floor nodeFloor = currentNode.getFloor();
              tabPane.getSelectionModel().select(floorToTab(nodeFloor));
              MapUtilities currentMapUtility = whichMapUtility(nodeFloor);
              GesturePane startingPane = ((GesturePane) currentMapUtility.getPane().getParent());

              // Pan so node is centered
              startingPane
                  .animate(Duration.millis(100))
                  .centreOn(
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
              currentCircle.setRadius(5);
              currentCircle.setViewOrder(-3);
              currentCircle.setVisible(true);
            });
  }

  public TurnType getTurn(List<HospitalNode> path, int index) {
    // Current node
    HospitalNode currentNode = path.get(index);
    // Check if the node is the start or the end
    // Start
    if (index == 0) {
      return TurnType.START;
    }
    // End
    if (index == path.size() - 1) {
      return TurnType.END;
    }
    // Check if the node is an elevator or stairs
    // If the current node is elevator and the next node is on another floor, then set the turn type
    // to elevator
    if ((LocationName.NodeType.stringToNodeType(
                SQLRepo.INSTANCE.getNodeTypeFromNodeID(
                    Integer.parseInt(path.get(index).getNodeID())))
            == LocationName.NodeType.ELEV)
        && ((currentNode.getFloor() != path.get(index + 1).getFloor())
            || (currentNode.getFloor() != path.get(index - 1).getFloor()))) {
      return TurnType.ELEVATOR;
    }
    // If the current node is stairs and the next node is on another floor, then set the turn type
    // to stairs
    if ((LocationName.NodeType.stringToNodeType(
                SQLRepo.INSTANCE.getNodeTypeFromNodeID(
                    Integer.parseInt(path.get(index).getNodeID())))
            == LocationName.NodeType.STAI)
        && ((currentNode.getFloor() != path.get(index + 1).getFloor())
            || (currentNode.getFloor() != path.get(index - 1).getFloor()))) {
      return TurnType.STAIRS;
    }
    // Straight
    double angle = getTurnAngle(path, index);
    if ((angle > 315 || angle < 45) || (angle < 225 && angle > 135)) {
      return TurnType.STRAIGHT;
    }
    // Right
    if (angle >= 45 && angle <= 135) {
      return TurnType.RIGHT;
    }
    // Left
    if (angle >= 225 && angle <= 315) {
      return TurnType.LEFT;
    } else {
      return TurnType.ERROR;
    }
  }

  /**
   * returns the angle between two intersecting lines at a given position along a path
   *
   * @param path
   * @param index
   * @return
   */
  public double getTurnAngle(List<HospitalNode> path, int index) {
    // Get the nodes
    double startX = path.get(index - 1).getXCoord();
    double startY = path.get(index - 1).getYCoord();
    double endX = path.get(index + 1).getXCoord();
    double endY = path.get(index + 1).getYCoord();
    double fixedX = path.get(index).getXCoord();
    double fixedY = path.get(index).getYCoord();

    // Get the angles
    double angle1 = Math.atan2(startY - fixedY, startX - fixedX);
    double angle2 = Math.atan2(endY - fixedY, endX - fixedX);

    double radian = angle1 - angle2;
    double finalAngle = (radian * 180) / PI;
    // Convert negatives to positives: -10 -> 350
    if (finalAngle < 0) {
      finalAngle += 360;
    }
    return finalAngle;
  }

  public int getDistance(List<HospitalNode> path, int index) {
    // If the node is the starting or ending node, return 0
    if (index == 0) {
      return 0;
    }
    // Get the points
    double x1 = path.get(index - 1).getXCoord();
    double y1 = path.get(index - 1).getYCoord();
    double x2 = path.get(index).getXCoord();
    double y2 = path.get(index).getYCoord();
    // Calculate length
    int length = (int) Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
    return length / 3; // insert the actual conversion here if we ever get it
  }

  public void setSVG() {
    // Set the svgs for the zoom in and zoom out buttons
    SVGPath outSVG = new SVGPath();
    outSVG.setContent(
        "M5 10C5 7.23858 7.23858 5 10 5C12.7614 5 15 7.23858 15 10C15 11.381 14.4415 12.6296 13.5355 13.5355C12.6296 14.4415 11.381 15 10 15C7.23858 15 5 12.7614 5 10ZM10 3C6.13401 3 3 6.13401 3 10C3 13.866 6.13401 17 10 17C11.5719 17 13.0239 16.481 14.1921 15.6063L19.2929 20.7071C19.6834 21.0976 20.3166 21.0976 20.7071 20.7071C21.0976 20.3166 21.0976 19.6834 20.7071 19.2929L15.6063 14.1921C16.481 13.0239 17 11.5719 17 10C17 6.13401 13.866 3 10 3ZM8 9C7.44772 9 7 9.44772 7 10C7 10.5523 7.44772 11 8 11H12C12.5523 11 13 10.5523 13 10C13 9.44772 12.5523 9 12 9H8Z");
    zoomOutRegion.setShape(outSVG);
    zoomOutRegion.setStyle("-fx-background-color: 'f1f1f1'");
    SVGPath inSVG = new SVGPath();
    inSVG.setContent(
        "M5 10C5 7.23858 7.23858 5 10 5C12.7614 5 15 7.23858 15 10C15 11.381 14.4415 12.6296 13.5355 13.5355C12.6296 14.4415 11.381 15 10 15C7.23858 15 5 12.7614 5 10ZM10 3C6.13401 3 3 6.13401 3 10C3 13.866 6.13401 17 10 17C11.5719 17 13.0239 16.481 14.1921 15.6063L19.2929 20.7071C19.6834 21.0976 20.3166 21.0976 20.7071 20.7071C21.0976 20.3166 21.0976 19.6834 20.7071 19.2929L15.6063 14.1921C16.481 13.0239 17 11.5719 17 10C17 6.13401 13.866 3 10 3ZM11 8C11 7.44772 10.5523 7 10 7C9.44772 7 9 7.44772 9 8V9H8C7.44772 9 7 9.44772 7 10C7 10.5523 7.44772 11 8 11H9V12C9 12.5523 9.44772 13 10 13C10.5523 13 11 12.5523 11 12V11H12C12.5523 11 13 10.5523 13 10C13 9.44772 12.5523 9 12 9H11V8Z");
    zoomInRegion.setShape(inSVG);
    zoomInRegion.setStyle("-fx-background-color: 'f1f1f1'");

    labelSwitch.setOnMouseClicked(
        event -> {
          disableLabel = labelSwitch.isSelected();
          makeLocationNamesVisible(disableLabel);
        });
    zoomInButton.setOnMouseClicked(
        event -> {
          zoomIn();
        });
    zoomOutButton.setOnMouseClicked(
        event -> {
          zoomOut();
        });
  }

  public void zoomIn() {
    AnchorPane currentPane =
        (AnchorPane) tabPane.getSelectionModel().getSelectedItem().getContent();
    GesturePane currentGesture = (GesturePane) currentPane.getChildren().get(0);
    // Zoom in
    currentGesture
        .animate(Duration.millis(200))
        .interpolateWith(Interpolator.EASE_BOTH)
        .zoomBy(currentGesture.getCurrentScale(), currentGesture.targetPointAtViewportCentre());
  }

  public void zoomOut() {
    AnchorPane currentPane =
        (AnchorPane) tabPane.getSelectionModel().getSelectedItem().getContent();
    GesturePane currentGesture = (GesturePane) currentPane.getChildren().get(0);
    // Zoom in
    currentGesture
        .animate(Duration.millis(200))
        .interpolateWith(Interpolator.EASE_BOTH)
        .zoomBy(
            -(currentGesture.getCurrentScale() / 2), currentGesture.targetPointAtViewportCentre());
  }

  private void makeLocationNamesVisible(boolean isVisible) {
    if (allLocationNameLabels.size() != 0) {
      for (Label aLabel : allLocationNameLabels) {
        aLabel.setVisible(isVisible);
      }
    }
  }

  private void createLabelsForToggleDisplay(List<HospitalNode> nodes) {
    for (HospitalNode aNode : nodes) {
      if (LocationName.NodeType.HALL
          != LocationName.NodeType.stringToNodeType(
              SQLRepo.INSTANCE.getNodeTypeFromNodeID(Integer.parseInt(aNode.getNodeID())))) {
        if (aNode.getFloor() == Floor.ONE) {
          makeLabelForToggle(aNode, mapUtilityOne);
        }
        if (aNode.getFloor() == Floor.TWO) {
          makeLabelForToggle(aNode, mapUtilityTwo);
        }
        if (aNode.getFloor() == Floor.THREE) {
          makeLabelForToggle(aNode, mapUtilityThree);
        }
        if (aNode.getFloor() == Floor.LOWER_ONE) {
          makeLabelForToggle(aNode, mapUtilityLowerOne);
        }
        if (aNode.getFloor() == Floor.LOWER_TWO) {
          makeLabelForToggle(aNode, mapUtilityLowerTwo);
        }
      }
    }
  }

  private void makeLabelForToggle(HospitalNode node, MapUtilities mapUtil) {
    Label thisLabel =
        mapUtil.createLabel(
            node.getXCoord(),
            node.getYCoord(),
            SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(node.getNodeID())));
    thisLabel.setFont(Font.font("Roboto", 8));
    thisLabel.setStyle(
        "-fx-background-color: white; -fx-border-width: .5; -fx-border-color: black");
    thisLabel.setViewOrder(-4);
    thisLabel.setOnMouseClicked(
        event -> {
          if (currentFrontLabel != null) {
            currentFrontLabel.setViewOrder(-4);
          }
          thisLabel.setViewOrder(-5);
          currentFrontLabel = thisLabel;
        });
    allLocationNameLabels.add(thisLabel);
  }

  public void translateToSpanish() {
    // Change language variable
    language = "spanish";
    startButton.setText("Comenzar"); // Start

    // Map Tabs
    lowerLevelTwoTab.setText("Piso Baja 2"); // Lower Level 2
    lowerLevelOneTab.setText("Piso Baja 1"); // Lower Level 1
    floorOneTab.setText("Piso 1");
    floorTwoTab.setText("Piso 2");
    floorThreeTab.setText("Piso 3");

    /* Uncomment when logout button is fixed
    // Logout Button
    logoutButton.setText("Cerrar Sesi" + aO + "n"); // Logout
    Font spanishLogout = new Font("Roboto", 13);
    logoutButton.setFont(spanishLogout);
     */
  }

  public void translateToEnglish() {
    // Change language variable
    language = "english";

    startButton.setText("Start"); // Start

    /* Uncomment when logout button is fixed
    // Logout Button
    logoutButton.setText("Logout"); // Keep in English
    Font englishLogout = new Font("Roboto", 18);
    logoutButton.setFont(englishLogout);
     */
  }
}
