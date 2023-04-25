package edu.wpi.teame.controllers;

import static java.lang.Math.PI;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.map.*;
import edu.wpi.teame.map.pathfinding.AbstractPathfinder;
import edu.wpi.teame.utilities.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.SearchableComboBox;

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
  @FXML MFXButton menuButton;
  @FXML MFXButton menuBarHome;
  @FXML MFXButton menuBarServices;
  @FXML MFXButton menuBarSignage;
  @FXML MFXButton menuBarMaps;
  @FXML MFXButton menuBarDatabase;
  @FXML MFXButton menuBarBlank;
  @FXML MFXButton menuBarExit;
  @FXML VBox menuBar;
  @FXML MFXButton startButton;
  @FXML ImageView mapImageLowerTwo; // Floor L2
  @FXML ImageView mapImageLowerOne; // Floor L1
  @FXML ImageView mapImageOne; // Floor 1
  @FXML ImageView mapImageTwo; // Floor 2
  @FXML ImageView mapImageThree; // Floor 3
  @FXML RadioButton aStarButton;
  @FXML RadioButton dfsButton;
  @FXML RadioButton bfsButton;
  @FXML GesturePane gesturePaneL2;
  @FXML GesturePane gesturePaneL1;
  @FXML GesturePane gesturePane1;
  @FXML GesturePane gesturePane2;
  @FXML GesturePane gesturePane3;
  @FXML ImageView homeI;
  @FXML ImageView servicesI;
  @FXML ImageView signageI;
  @FXML ImageView pathfindingI;
  @FXML ImageView databaseI;
  @FXML ImageView exitI;
  boolean isPathDisplayed = false;
  Floor currentFloor = Floor.LOWER_TWO;
  Circle currentCircle = new Circle();
  static int directionDistance = 0;
  int currentLength = 0;
  HBox previousLabel;
  AbstractPathfinder pf = AbstractPathfinder.getInstance("A*");
  String curLocFromComboBox;
  String destFromComboBox;
  MapUtilities mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
  MapUtilities mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
  MapUtilities mapUtilityOne = new MapUtilities(mapPaneOne);
  MapUtilities mapUtilityTwo = new MapUtilities(mapPaneTwo);
  MapUtilities mapUtilityThree = new MapUtilities(mapPaneThree);

  ObservableList<String> floorLocations =
      FXCollections.observableArrayList(
          SQLRepo.INSTANCE.getLongNamesFromMove(
              SQLRepo.INSTANCE.getMoveAttributeFromFloor(currentFloor)));

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

    // Initially set the menu bar to invisible
    menuBar.setVisible(false);

    // When the menu button is clicked, invert the value of menuVisibility and set the menu bar to
    // that value
    // (so each time the menu button is clicked it changes the visibility of menu bar back and
    // forth)
    menuButton.setOnMouseClicked(
        event -> {
          menuBar.setVisible(!menuBar.isVisible());
        });

    // Navigation controls for the button in the menu bar
    menuBarHome.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    menuBarServices.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.SERVICE_REQUESTS);
          menuBar.setVisible(!menuBar.isVisible());
        });
    menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
    menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
    menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate(Screen.DATABASE_EDITOR));
    menuBarExit.setOnMouseClicked((event -> Platform.exit()));

    // makes the menu bar buttons get highlighted when the mouse hovers over them
    ButtonUtilities.mouseSetupMenuBar(
        menuBarHome,
        "baseline-left",
        homeI,
        "images/house-blank.png",
        "images/house-blank-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarServices,
        "baseline-left",
        servicesI,
        "images/hand-holding-medical.png",
        "images/hand-holding-medical-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarSignage,
        "baseline-left",
        signageI,
        "images/diamond-turn-right.png",
        "images/diamond-turn-right-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarMaps, "baseline-left", pathfindingI, "images/marker.png", "images/marker-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarDatabase,
        "baseline-left",
        databaseI,
        "images/folder-tree.png",
        "images/folder-tree-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarExit,
        "baseline-center",
        exitI,
        "images/sign-out-alt.png",
        "images/sign-out-alt-blue.png");

    // Make sure location list is initialized so that we can filter out the hallways
    SQLRepo.INSTANCE.getLocationList();

    resetComboboxes();
  }

  private void initializeMapUtilities() {
    mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
    mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
    mapUtilityOne = new MapUtilities(mapPaneOne);
    mapUtilityTwo = new MapUtilities(mapPaneTwo);
    mapUtilityThree = new MapUtilities(mapPaneThree);

    mapUtilityLowerTwo.setCircleStyle("-fx-fill: gold; -fx-stroke: black; -fx-stroke-width: 1");
    mapUtilityLowerOne.setCircleStyle("-fx-fill: cyan; -fx-stroke: black; -fx-stroke-width: 1");
    mapUtilityOne.setCircleStyle("-fx-fill: lime; -fx-stroke: black; -fx-stroke-width: 1");
    mapUtilityTwo.setCircleStyle("-fx-fill: hotpink; -fx-stroke: black; -fx-stroke-width: 1");
    mapUtilityThree.setCircleStyle("-fx-fill: orangered; -fx-stroke: black; -fx-stroke-width: 1");

    mapUtilityLowerTwo.setLineStyle("-fx-stroke: gold; -fx-stroke-width: 4");
    mapUtilityLowerOne.setLineStyle("-fx-stroke: cyan; -fx-stroke-width: 4");
    mapUtilityOne.setLineStyle("-fx-stroke: lime; -fx-stroke-width: 4");
    mapUtilityTwo.setLineStyle("-fx-stroke: hotpink; -fx-stroke-width: 4");
    mapUtilityThree.setLineStyle("-fx-stroke: orangered; -fx-stroke-width: 4");
  }

  public void resetComboboxes() {
    floorLocations =
        FXCollections.observableArrayList(
            SQLRepo.INSTANCE.getMoveList().stream()
                .filter(
                    (move) -> // Filter out hallways and long names with no corresponding
                        // LocationName
                        LocationName.allLocations.get(move.getLongName()) == null
                            ? false
                            : LocationName.allLocations.get(move.getLongName()).getNodeType()
                                    != LocationName.NodeType.HALL
                                && LocationName.allLocations.get(move.getLongName()).getNodeType()
                                    != LocationName.NodeType.STAI
                                && LocationName.allLocations.get(move.getLongName()).getNodeType()
                                    != LocationName.NodeType.ELEV
                                && LocationName.allLocations.get(move.getLongName()).getNodeType()
                                    != LocationName.NodeType.REST)
                .map((move) -> move.getLongName())
                .sorted() // Sort alphabetically
                .toList());
    currentLocationList.setItems(floorLocations);
    destinationList.setItems(floorLocations);
    currentLocationList.setValue("");
    destinationList.setValue("");
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

    String toNodeID = SQLRepo.INSTANCE.getNodeIDFromName(to) + "";
    String fromNodeID = SQLRepo.INSTANCE.getNodeIDFromName(from) + "";

    System.out.println("From: " + HospitalNode.allNodes.get(fromNodeID));
    System.out.println("To: " + HospitalNode.allNodes.get(toNodeID));

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
    // Create the labels
    createDirections(pathBox, path);
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

    int startX, startY;
    // create circle to symbolize start
    int x1 = path.get(0).getXCoord();
    int y1 = path.get(0).getYCoord();
    startX = x1;
    startY = y1;
    Circle currentLocationCircle = currentMapUtility.drawStyledCircle(x1, y1, 4);
    currentLocationCircle.setId(path.get(0).getNodeID());
    currentMapUtility.createLabel(x1, y1, 5, 5, "Current Location");

    // draw the lines between each node
    int x2, y2;
    for (int i = 1; i < path.size(); i++) {
      HospitalNode node = path.get(i);
      x2 = node.getXCoord();
      y2 = node.getYCoord();

      Floor newFloor = node.getFloor();
      if (newFloor != currentFloor) {
        Floor oldFloor = currentFloor;
        currentMapUtility.createLabel(x2, y2, "Went to Floor: " + newFloor.toString());
        currentFloor = newFloor;
        currentMapUtility = whichMapUtility(currentFloor);
        currentMapUtility.createLabel(x2, y2, "Came from Floor: " + oldFloor.toString());
      }

      Line pathLine = currentMapUtility.drawStyledLine(x1, y1, x2, y2);
      Circle intermediateCircle = currentMapUtility.drawStyledCircle(x2, y2, 4);
      intermediateCircle.setViewOrder(-1);
      intermediateCircle.setId(node.getNodeID());
      intermediateCircle.setVisible(false);
      x1 = x2;
      y1 = y2;
    }

    // create circle to symbolize end
    Circle endingCircle = currentMapUtility.drawStyledCircle(x1, y1, 4);
    endingCircle.setId(path.get(path.size() - 1).getNodeID());
    endingCircle.toFront();
    currentMapUtility.createLabel(x1, y1, 5, 5, "Destination");

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

    // For each node along the path
    int currentDistance = 0;
    for (int i = 0; i < path.size(); i++) {
      // Get the current node
      HospitalNode currentNode = path.get(i);

      // Get the turn type
      TurnType turnType = getTurn(path, i);
      currentDistance += getDistance(path, i);

      // If the turn type is not straight
      if (turnType != TurnType.STRAIGHT) {
        // Create a direction
        Directions direction = new Directions(path, i, turnType, currentDistance);
        currentDistance = 0;
        // Add the event listener
        direction
            .getHbox()
            .setOnMouseClicked(
                event -> {
                  // reset highlighted node
                  currentCircle.setRadius(4);
                  currentCircle.setViewOrder(-1);
                  currentCircle.setVisible(false);

                  // Set the selected tab to the floor of the node
                  Floor nodeFloor = currentNode.getFloor();
                  tabPane.getSelectionModel().select(floorToTab(nodeFloor));
                  MapUtilities currentMapUtility = whichMapUtility(nodeFloor);
                  GesturePane startingPane =
                      ((GesturePane) currentMapUtility.getPane().getParent());

                  // Outline the hbox
                  direction
                      .getHbox()
                      .setBorder(
                          new Border(
                              new BorderStroke(
                                  Color.web(ColorPalette.LIGHT_BLUE.getHexCode()),
                                  BorderStrokeStyle.SOLID,
                                  CornerRadii.EMPTY,
                                  new BorderWidths(2))));

                  // Remove the previous outline unless previous is null or the same box is clicked
                  // again
                  if (previousLabel != null && previousLabel != direction.getHbox()) {
                    previousLabel.setBorder(Border.EMPTY);
                  }

                  // Zoom in on the starting node
                  startingPane.zoomTo(2, startingPane.targetPointAtViewportCentre());

                  // Pan so starting node is centered
                  startingPane
                      .animate(Duration.millis(200))
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
                  currentCircle.setViewOrder(-5);
                  currentCircle.setVisible(true);

                  // Set the current label as the previous
                  previousLabel = direction.getHbox();
                });
        vbox.getChildren().add(direction.getHbox());
      }
    }
  }

  public TurnType getTurn(List<HospitalNode> path, int index) {
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
    // Elevator
    if ((LocationName.NodeType.stringToNodeType(
                SQLRepo.INSTANCE.getNodeTypeFromNodeID(
                    Integer.parseInt(path.get(index).getNodeID())))
            == LocationName.NodeType.ELEV)
        && (path.get(index).getFloor()) != path.get(index + 1).getFloor()) {
      return TurnType.ELEVATOR;
    }
    // Stairs
    if ((LocationName.NodeType.stringToNodeType(
                SQLRepo.INSTANCE.getNodeTypeFromNodeID(
                    Integer.parseInt(path.get(index).getNodeID())))
            == LocationName.NodeType.STAI)
        && (path.get(index).getFloor()) != path.get(index + 1).getFloor()) {
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
    if (index == 0 || index == path.size() - 1) {
      return 0;
    }
    // Get the points
    double x1 = path.get(index - 1).getXCoord();
    double y1 = path.get(index - 1).getYCoord();
    double x2 = path.get(index).getXCoord();
    double y2 = path.get(index).getYCoord();
    // Calculate length
    int length = (int) Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
    return length;
  }

  private void createLabelsForToggleDisplay(){
    MapUtilities floorOne = new MapUtilities(mapPaneOne);
    MapUtilities floorTwo = new MapUtilities(mapPaneTwo);
    MapUtilities floorThree = new MapUtilities(mapPaneThree);
    MapUtilities lowerOne = new MapUtilities(mapPaneLowerOne);
    MapUtilities lowerTwo = new MapUtilities(mapPaneLowerTwo);
    List<HospitalNode> allNodes = SQLRepo.INSTANCE.getNodeList();
    for (HospitalNode aNode:allNodes){
      if (aNode.getFloor()==Floor.ONE){
        makeLabelForToggle(aNode,floorOne);
      }
      if (aNode.getFloor()==Floor.TWO){
        makeLabelForToggle(aNode,floorTwo);
      }
      if (aNode.getFloor()==Floor.THREE){
        makeLabelForToggle(aNode,floorThree);
      }
      if (aNode.getFloor()==Floor.LOWER_ONE){
        makeLabelForToggle(aNode,lowerOne);
      }
      if (aNode.getFloor()==Floor.LOWER_TWO){
        makeLabelForToggle(aNode,lowerTwo);
      }
    }
  }

  private void makeLabelForToggle(HospitalNode node, MapUtilities mapUtil){
    HBox hBox = new HBox();
    hBox.setBackground(
            new Background(
                    new BackgroundFill(Color.web("#D9DAD7"), CornerRadii.EMPTY, Insets.EMPTY)));
    hBox.setPrefHeight(20);
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.setLayoutX(node.getXCoord());
    hBox.setLayoutY(node.getYCoord());
    Label thisLabel = mapUtil.createLabel(node.getXCoord(),node.getYCoord(),SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(node.getNodeID())));
    thisLabel.setFont(Font.font("Roboto", 8));
    hBox.getChildren().add(thisLabel);

  }
}
