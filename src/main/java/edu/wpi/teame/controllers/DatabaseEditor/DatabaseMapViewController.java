package edu.wpi.teame.controllers.DatabaseEditor;

import static edu.wpi.teame.map.HospitalNode.allNodes;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.map.*;
import edu.wpi.teame.utilities.MapUtilities;
import edu.wpi.teame.utilities.MoveUtilities;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.time.LocalDate;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.ToggleSwitch;

public class DatabaseMapViewController {

  @FXML DatabaseEditorController databaseEditorController;

  @FXML AnchorPane mapPaneLowerTwo;
  @FXML AnchorPane mapPaneLowerOne;
  @FXML AnchorPane mapPaneOne;
  @FXML AnchorPane mapPaneTwo;
  @FXML AnchorPane mapPaneThree;

  @FXML GesturePane gesturePaneLowerTwo;
  @FXML GesturePane gesturePaneLowerOne;
  @FXML GesturePane gesturePaneOne;
  @FXML GesturePane gesturePaneTwo;
  @FXML GesturePane gesturePaneThree;

  @FXML TabPane tabPane;
  @FXML Tab floorOneTab;
  @FXML Tab floorTwoTab;
  @FXML Tab floorThreeTab;
  @FXML Tab lowerLevelTwoTab;
  @FXML Tab lowerLevelOneTab;

  @FXML ImageView mapImageLowerTwo; // Floor L2
  @FXML ImageView mapImageLowerOne; // Floor L1
  @FXML ImageView mapImageOne; // Floor 1
  @FXML ImageView mapImageTwo; // Floor 2
  @FXML ImageView mapImageThree; // Floor 3
  @FXML ToggleSwitch locationNameToggle;
  @FXML ToggleSwitch movesToggle;

  // -----------------------------------------------------//

  // Entire Sidebar VBox

  @FXML VBox sidebar;

  // Sidebar Title
  @FXML Text sidebarText;

  // Coordinates
  @FXML HBox coordHBox;
  @FXML HBox nodeEdgeHBox;
  @FXML TextField nodeOneTextField;
  @FXML TextField nodeTwoTextField;
  @FXML Label xCoordinateLabel;
  @FXML Label yCoordinateLabel;
  @FXML TextField xField;
  @FXML TextField yField;

  // Location Name
  @FXML VBox locationNameVBox;
  @FXML Label locationNameLabel;
  @FXML SearchableComboBox<String> locationNameCombobox;

  // Location Name Selection Section
  @FXML TextField addLongNameTextField;
  @FXML TextField addShortNameTextField;
  @FXML VBox nodeTypeVBox;
  @FXML SearchableComboBox<LocationName.NodeType> nodeTypeComboBox;

  // Location Name Add/Delete Buttons
  @FXML HBox locationButtonBox;
  @FXML MFXButton addLocationButton;
  @FXML MFXButton removeLocationButton;

  @FXML VBox locationNameAddVBox;

  // Add Building Boxes
  @FXML VBox buildingVBox;
  @FXML SearchableComboBox<String> buildingSelector;

  // Update Buttons
  @FXML HBox updateButtonHBox;
  @FXML MFXButton cancelButton;
  @FXML MFXButton deleteButton;
  @FXML MFXButton confirmButton;

  // Line divider
  @FXML Line divider;

  // -------------------------------------------------------//

  boolean isLocationNamesDisplayed = false;

  Floor currentFloor;
  MapUtilities mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
  MapUtilities mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
  MapUtilities mapUtilityOne = new MapUtilities(mapPaneOne);
  MapUtilities mapUtilityTwo = new MapUtilities(mapPaneTwo);
  MapUtilities mapUtilityThree = new MapUtilities(mapPaneThree);

  private Circle currentCircle;

  HospitalNode currNode;

  boolean widthLoaded = false;
  boolean heightLoaded = false;

  @FXML HBox modeToggleButtonBox;
  @FXML ToggleGroup buttonGroup;
  @FXML ToggleButton dragToggleButton;
  @FXML ToggleButton addNodeToggleButton;
  @FXML ToggleButton panToggleButton;
  @FXML ToggleButton editToggleButton;
  @FXML ToggleButton alignToggleButton;
  @FXML ToggleButton addEdgeToggleButton;
  @FXML Label displayNamesLabel;
  @FXML Label displayMovesLabel;
  List<MoveAttribute> currentMoves = new LinkedList<>();
  List<MoveAttribute> allMoves = new LinkedList<>();
  List<Label> listOfMoveLabel = new LinkedList<>();
  List<Node> allMoveLineNodes = new LinkedList<>();
  MoveUtilities moveUtil = new MoveUtilities();

  enum Mode {
    PAN("PAN"),
    DRAG("DRAG"),
    ADD_NODE("ADD_NODE"),
    ADD_EDGE("ADD_EDGE"),
    EDIT("EDIT"),
    ALIGN("ALIGN");
    private final String buttonName;

    Mode(String buttonName) {
      this.buttonName = buttonName;
    }

    public String getButtonName() {
      return this.buttonName;
    }
  }

  private Mode currentMode = Mode.PAN;

  private void initializeToggleGroup() {
    buttonGroup
        .selectedToggleProperty()
        .addListener(
            (obs, oldToggle, newToggle) -> {
              if (oldToggle == null) {
                return;
              }
              // if the button is pressed again keep it selected
              if (newToggle == null) {
                oldToggle.setSelected(true);
                return;
              }

              // if a new toggle button was pressed
              if (!oldToggle.equals(newToggle)) {

                if ((newToggle).equals(panToggleButton)) {
                  currentMode = Mode.PAN;
                  configurePanView();
                }
                if ((newToggle).equals(addNodeToggleButton)) {
                  currentMode = Mode.ADD_NODE;
                  configureAddNodeView();
                }
                if ((newToggle).equals(editToggleButton)) {
                  currentMode = Mode.EDIT;
                  configureEditNodeView();
                }
                if ((newToggle).equals(dragToggleButton)) {
                  currentMode = Mode.DRAG;
                  configureDragView();
                }
                if ((newToggle).equals(alignToggleButton)) {
                  currentMode = Mode.ALIGN;
                  configureAlignNodeView();
                }
                if ((newToggle).equals(addEdgeToggleButton)) {
                  currentMode = Mode.ADD_EDGE;
                  configureAddEdgeView();
                }
              }
              resetAllFields();
              refreshMap();
            });
  }

  private void configurePanView() {
    // hide sidebar
    sidebar.setVisible(false);

    modeToggleButtonBox.setLayoutX(1252);
    // set toggle button box to end of page
  }

  private void configureAddNodeView() {
    // configure sidebar
    confirmButton.setOnMouseClicked(
        event -> {
          confirmAddNode();
          panToggleButton.setSelected(true);
        });
    cancelButton.setOnMouseClicked(
        event -> {
          refreshMap();
          panToggleButton.setSelected(true);
        });
    sidebarText.setText("Add Node");

    // set toggle button box back
    modeToggleButtonBox.setLayoutX(842);
    // visibility
    setSidebarVisible(true);
    deleteButton.setVisible(false);
  }

  @FXML
  void setOnKeyPressed(KeyEvent event) {
    if (currentMode == Mode.EDIT) {
      if (event.getCode() == KeyCode.DELETE) {
        deleteNode();
        panToggleButton.setSelected(true);
      }
    }
  }

  private void configureDragView() {
    // configure sidebar
    confirmButton.setOnMouseClicked(
        event -> {
          confirmDrag();
          panToggleButton.setSelected(true);
        });
    cancelButton.setOnMouseClicked(
        event -> {
          refreshMap();
          panToggleButton.setSelected(true);
        });
    sidebarText.setText("Drag Mode");

    // set toggle button box back
    modeToggleButtonBox.setLayoutX(842);
    // set coord X and Y visible
    sidebar.setVisible(true);
    coordHBox.setVisible(true);
    // pick the things to set invisible
    locationNameVBox.setVisible(false);
    locationNameAddVBox.setVisible(false);
    locationButtonBox.setVisible(false);
    buildingVBox.setVisible(false);
    deleteButton.setVisible(false);
    divider.setVisible(false);
  }

  private void configureAlignNodeView() {
    // configure sidebar
    confirmButton.setOnMouseClicked(
        event -> {
          confirmAlign();
          panToggleButton.setSelected(true);
        });
    cancelButton.setOnMouseClicked(
        event -> {
          refreshMap();
          panToggleButton.setSelected(true);
        });
    sidebarText.setText("Align Node");

    // set toggle button box back
    modeToggleButtonBox.setLayoutX(842);
    // visibility
    // set coord X and Y visible
    sidebar.setVisible(true);
    // set everything else invisible
    coordHBox.setVisible(false);
    nodeEdgeHBox.setVisible(false);
    locationNameVBox.setVisible(false);
    locationNameAddVBox.setVisible(false);
    locationButtonBox.setVisible(false);
    buildingVBox.setVisible(false);
    deleteButton.setVisible(false);
    divider.setVisible(false);
  }

  private void configureAddEdgeView() {
    // configure sidebar
    confirmButton.setOnMouseClicked(
        event -> {
          confirmAddEdge();
          panToggleButton.setSelected(true);
        });
    cancelButton.setOnMouseClicked(
        event -> {
          refreshMap();
          panToggleButton.setSelected(true);
        });
    sidebarText.setText("Add Edge");

    // set toggle button box back
    modeToggleButtonBox.setLayoutX(842);
    // visibility
    // set coord X and Y visible
    sidebar.setVisible(true);
    nodeEdgeHBox.setVisible(true);

    // set everything else invisible
    locationNameVBox.setVisible(false);
    coordHBox.setVisible(false);
    locationNameAddVBox.setVisible(false);
    locationButtonBox.setVisible(false);
    buildingVBox.setVisible(false);
    deleteButton.setVisible(false);
    divider.setVisible(false);
  }

  private void setSidebarVisible(boolean visibility) {
    sidebar.setVisible(visibility);
    coordHBox.setVisible(visibility);
    nodeEdgeHBox.setVisible(false);
    locationNameVBox.setVisible(visibility);
    locationNameAddVBox.setVisible(visibility);
    locationButtonBox.setVisible(visibility);
    buildingVBox.setVisible(visibility);
    deleteButton.setVisible(visibility);
    divider.setVisible(visibility);
  }

  private void configureEditNodeView() {
    // configure sidebar
    confirmButton.setOnMouseClicked(
        event -> {
          confirmEditNode();
          panToggleButton.setSelected(true);
        });
    cancelButton.setOnMouseClicked(
        event -> {
          refreshMap();
          panToggleButton.setSelected(true);
        });
    deleteButton.setOnMouseClicked(
        event -> {
          deleteNode();
          panToggleButton.setSelected(true);
        });
    sidebarText.setText("Edit Node");

    // set toggle button box back
    modeToggleButtonBox.setLayoutX(842);
    // visibility
    setSidebarVisible(true);
    coordHBox.setVisible(true);
    locationNameVBox.setVisible(true);
    locationNameAddVBox.setVisible(false);
    locationButtonBox.setVisible(false);
    buildingVBox.setVisible(true);
    deleteButton.setVisible(true);
  }

  private void handleAlignNodes(Circle circle) {
    selectedCircles.add(circle);
    highlightCircle(circle, true);
    System.out.println(selectedCircles);
  }

  private void confirmDrag() {
    System.out.println("confirmDrag");
    updateNodes();

    refreshMap();
  }

  private void confirmAddEdge() {
    if (selectedCircles.size() == 2) {
      MapUtilities currentMapUtility = whichMapUtility(currentFloor);
      HospitalNode node1 = circleToHospitalNodeMap.get(selectedCircles.get(0));
      HospitalNode node2 = circleToHospitalNodeMap.get(selectedCircles.get(1));
      SQLRepo.INSTANCE.addEdge(new HospitalEdge(node1.getNodeID(), node2.getNodeID()));
      HospitalNode.addEdge(node1, node2);
      currentMapUtility.drawEdge(node1, node2);
      refreshMap();
    }
  }

  private void confirmAlign() {

    if (selectedCircles.size() < 2) {
      System.out.println(
          "You need at least two circles selected to align!! TODO replace with popup or error message");
      return;
    }

    //    align nodes

    Circle firstCircle = selectedCircles.get(0);
    Circle lastCircle = selectedCircles.get(selectedCircles.size() - 1);

    HospitalNode firstNode = circleToHospitalNodeMap.get(firstCircle);
    HospitalNode lastNode = circleToHospitalNodeMap.get(lastCircle);

    //    math for aligning nodes
    int x1 = firstNode.getXCoord();
    int y1 = firstNode.getYCoord();
    int x2 = lastNode.getXCoord();
    int y2 = lastNode.getYCoord();
    double slope = (double) (y2 - y1) / (x2 - x1);

    MapUtilities currentMapUtility = whichMapUtility(currentFloor);
    for (Circle currCircle : selectedCircles) {
      HospitalNode currNode = circleToHospitalNodeMap.get(currCircle);
      int currX = currNode.getXCoord();
      int currY = currNode.getYCoord();

      // derived equations
      int xPrime =
          (int) ((slope / (slope * slope + 1)) * (slope * x1 + (currX / slope) + currY - y1));
      int yPrime = (int) (slope * (xPrime - x1) + y1);

      currCircle.setCenterX(currentMapUtility.convertX(xPrime));
      currCircle.setCenterY(currentMapUtility.convertY(yPrime));

      updatedCircles.add(currCircle);
    }

    updateNodes();
    refreshMap();
  }

  private void updateNodes() {
    updateNodeDatabase();
    updateAllNodes();
    updatedCircles.clear();
  }

  private void updateAllNodes() {
    for (Circle circle : updatedCircles) {

      MapUtilities currentMapUtility = whichMapUtility(currentFloor);

      HospitalNode node = circleToHospitalNodeMap.get(circle);

      int newX = currentMapUtility.PaneXToImageX(circle.getCenterX());
      int newY = currentMapUtility.PaneYToImageY(circle.getCenterY());

      node.setXCoord(newX);
      node.setYCoord(newY);
    }
  }

  private final HashMap<Line, HospitalEdge> lineToEdgeMap = new HashMap<>();

  private void updateNodeDatabase() {

    // update XYs
    for (Circle circle : updatedCircles) {
      System.out.println("updating database circle");

      MapUtilities currentMapUtility = whichMapUtility(currentFloor);

      HospitalNode node = circleToHospitalNodeMap.get(circle);

      int newX = currentMapUtility.PaneXToImageX(circle.getCenterX());
      int newY = currentMapUtility.PaneYToImageY(circle.getCenterY());

      SQLRepo.INSTANCE.updateNode(node, "xcoord", Integer.toString(newX));
      SQLRepo.INSTANCE.updateNode(node, "ycoord", Integer.toString(newY));
    }
  }

  private void updateOnClick(Circle circle) {
    System.out.println("updateOnClick");
    switch (currentMode) {
      case PAN:
        break;
      case EDIT:
        handleEditNode(circle);
        break;
      case ADD_EDGE:
        handleAddEdge(circle);
        break;
      case ADD_NODE:
        break;
      case DRAG:
        break;
      case ALIGN:
        handleAlignNodes(circle);
        break;
    }
  }

  private void updateOnDrag(Circle circle, MouseEvent mouseEvent) {
    System.out.println("updateOnDrag");
    switch (currentMode) {
      case PAN:
        break;
      case EDIT:
        break;
      case ADD_EDGE:
        break;
      case ADD_NODE:
        break;
      case DRAG:
        handleDragNode(circle, mouseEvent);
        break;
      case ALIGN:
        break;
    }
  }

  private HashMap<Circle, Label> circleToLabelMap = new HashMap<>();

  private void handleDragNode(Circle circle, MouseEvent mouseEvent) {
    whichGesturePane(currentFloor).setGestureEnabled(false);
    unhighlightSelectedCircles();
    selectedCircles.clear();

    highlightCircle(circle, true);

    String nodeID = circle.getId();
    sidebarText.setText("Edit Node: ID = " + nodeID);
    currNode = allNodes.get(nodeID);
    // get x and y from drag and set new x and y for circle and label
    ((Circle) mouseEvent.getSource()).setCenterX(mouseEvent.getX());
    ((Circle) mouseEvent.getSource()).setCenterY(mouseEvent.getY());

    double newX = circle.getCenterX();
    double newY = circle.getCenterY();

    Label currLabel = circleToLabelMap.get(circle);
    currLabel.setLayoutX(newX);
    currLabel.setLayoutY(newY);

    if (!updatedCircles.contains(circle)) {
      updatedCircles.add(circle);
    }

    MapUtilities currentMapUtility = whichMapUtility(currentFloor);
    xField.setText(Integer.toString(currentMapUtility.PaneXToImageX(newX)));
    yField.setText(Integer.toString(currentMapUtility.PaneYToImageY(newY)));

    // update edges based off drag
    List<Node> startEdgesToUpdate =
        currentMapUtility.getCurrentNodes().stream()
            .filter(node -> node.getId().contains("startNode:" + currNode))
            .toList();

    List<Node> endEdgesToUpdate =
        currentMapUtility.getCurrentNodes().stream()
            .filter(node -> node.getId().contains("endNode:" + currNode))
            .toList();

    for (Node node : startEdgesToUpdate) {
      Line line = (Line) node;
      line.setStartX(newX);
      line.setStartY(newY);
    }

    for (Node node : endEdgesToUpdate) {
      Line line = (Line) node;
      line.setEndX(newX);
      line.setEndY(newY);
    }
  }

  private void unhighlightSelectedCircles() {
    for (Circle circle : selectedCircles) {
      highlightCircle(circle, false);
    }
  }

  private void highlightCircle(Circle circle, boolean highlight) {
    if (highlight) {
      circle.setRadius(9);
    } else {
      circle.setRadius(5);
    }
  }

  private void handleAddEdge(Circle circle) {
    System.out.println("handleAddEdge");
    // check if there is a circle already selected, if not add this circle to the list
    if (selectedCircles.isEmpty()) {
      selectedCircles.add(circle);
      highlightCircle(circle, true);
      nodeOneTextField.setText(circleToHospitalNodeMap.get(circle).getNodeID());
      return;
    }

    // if a new second node is selected
    if (!selectedCircles.get(0).equals(circle) && selectedCircles.size() < 2) {
      selectedCircles.add(circle);
      nodeTwoTextField.setText(circleToHospitalNodeMap.get(circle).getNodeID());
      highlightCircle(circle, true);
    }
  }

  private void handleEditNode(Circle circle) {
    unhighlightSelectedCircles();
    selectedCircles.clear();

    String nodeID = circle.getId();
    sidebarText.setText("Edit Node: ID = " + nodeID);

    currNode = circleToHospitalNodeMap.get(circle);

    if (!updatedCircles.contains(circle)) {
      updatedCircles.add(circle);
    }

    String x = Integer.toString(currNode.getXCoord());
    String y = Integer.toString(currNode.getYCoord());
    xField.setText(x);
    yField.setText(y);

    locationNameCombobox.setValue(SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(nodeID)));

    buildingSelector.setValue(currNode.getBuilding());

    deleteButton.setVisible(true);

    selectedCircles.add(circle);
    highlightCircle(circle, true);
  }

  private void confirmEditNode() {
    System.out.println("Edit confirm");
    updateNodes();

    // nodeID is the 16th to the end of the text
    String nodeID = sidebarText.getText().substring(16);
    System.out.println("NodeID: " + nodeID);

    SQLRepo.INSTANCE.updateNode(allNodes.get(nodeID), "xcoord", xField.getText());
    SQLRepo.INSTANCE.updateNode(allNodes.get(nodeID), "ycoord", yField.getText());
    // Update Building
    SQLRepo.INSTANCE.updateNode(allNodes.get(nodeID), "building", buildingSelector.getValue());
    allNodes.get(nodeID).setBuilding(buildingSelector.getValue());

    // refresh and turn off sidebar
    refreshMap();
    System.out.println("update longname and building");
  }

  private void confirmAddNode() {
    int ID = makeNewNodeID();

    HospitalNode node =
        new HospitalNode(
            ID + "",
            Integer.parseInt(xField.getText()),
            Integer.parseInt(xField.getText()),
            currentFloor,
            buildingSelector.getValue());

    SQLRepo.INSTANCE.addNode(node);
    // add respective move
    MoveAttribute move =
        new MoveAttribute(ID, locationNameCombobox.getValue(), LocalDate.now().toString());
    SQLRepo.INSTANCE.addMove(move);
    refreshMap();

    allNodes.put(ID + "", node);
  }

  private final HashMap<Circle, HospitalNode> circleToHospitalNodeMap = new HashMap<>();

  ArrayList<Circle> updatedCircles = new ArrayList<>();

  ArrayList<Circle> selectedCircles = new ArrayList<>();

  @FXML
  public void initialize() {

    databaseEditorController.setOnlySelected(databaseEditorController.mapEditorSwapButton);

    initializeToggleGroup();
    initializeMapUtilities();
    initializeMapGesturePanes();
    initializeButtons();

    currentFloor = Floor.LOWER_TWO;

    updateCombo(); // TODO: Change

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
                currentFloor = Floor.LOWER_TWO;
                loadFloorNodes();
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
                currentFloor = Floor.LOWER_TWO;
                loadFloorNodes();
              }
            });

    configurePanView();
  }

  private void labelsVisibility(boolean visible) {
    for (Label aLabel : circleToLabelMap.values()) {
      aLabel.setVisible(visible);
    }
  }

  private void deleteNode() {

    String nodeID = sidebarText.getText().substring(16);
    HospitalNode currNode = allNodes.get(nodeID);

    SQLRepo.INSTANCE.deletenode(currNode);
    HospitalNode.removeNode(currNode);

    configurePanView();
    refreshMap();
  }

  public void loadFloorNodes() {
    // create nodes
    List<HospitalNode> floorNodes = SQLRepo.INSTANCE.getNodesFromFloor(currentFloor);
    List<HospitalEdge> floorEdges =
        SQLRepo.INSTANCE.getEdgeList().stream()
            .filter(
                edge -> HospitalNode.allNodes.get(edge.getNodeOneID()).getFloor() == currentFloor)
            .toList();

    // create edges
    for (HospitalEdge edge : floorEdges) {

      HospitalNode node1 = HospitalNode.allNodes.get(edge.getNodeOneID());
      HospitalNode node2 = HospitalNode.allNodes.get(edge.getNodeTwoID());

      // only draw edges on the same floor
      if (node1.getFloor() == node2.getFloor()) {
        Line edgeLine = whichMapUtility(currentFloor).drawEdge(node1, node2);
        lineToEdgeMap.put(edgeLine, edge);
      }
    }
    for (HospitalNode node : floorNodes) {
      setupNode(node);
    }
    labelsVisibility(isLocationNamesDisplayed);
    drawMoveArrow();
    renderMoveComponents(allMoveLineNodes, listOfMoveLabel, false);
  }

  private void setupNode(HospitalNode node) {

    String nodeID = node.getNodeID();
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);

    Circle nodeCircle = currentMapUtility.drawHospitalNode(node);
    circleToHospitalNodeMap.put(nodeCircle, allNodes.get(nodeID));

    Label nodeLabel = currentMapUtility.drawHospitalNodeLabel(node);
    nodeLabel.setStyle(
        "-fx-background-color: white; -fx-border-width: .5; -fx-border-color: black");
    nodeLabel.setFont(Font.font("Roboto", 6));
    nodeLabel.setVisible(false);
    circleToLabelMap.put(nodeCircle, nodeLabel);

    nodeCircle.setOnMouseClicked(event -> updateOnClick(nodeCircle));
    nodeCircle.setOnMouseDragged(event -> updateOnDrag(nodeCircle, event));
    if (LocationName.NodeType.HALL
        != LocationName.NodeType.stringToNodeType(
            SQLRepo.INSTANCE.getNodeTypeFromNodeID(Integer.parseInt(node.getNodeID())))) {}
  }

  private void refreshMap() {
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);
    currentMapUtility.removeAll();
    loadFloorNodes();
    renderMoveComponents(allMoveLineNodes, listOfMoveLabel, movesToggle.isSelected());
    unhighlightSelectedCircles();
    selectedCircles.clear();
  }

  private void resetAllFields() {
    xField.setText("");
    yField.setText("");
    locationNameCombobox.setValue("");
    nodeOneTextField.setText("");
    nodeTwoTextField.setText("");
    buildingSelector.setValue("");
    addLongNameTextField.setText("");
    addShortNameTextField.setText("");
    nodeTypeComboBox.setValue(null);
  }

  private void addLocationName() {
    if (addLongNameTextField.getText() != "" && addShortNameTextField.getText() != "") {
      LocationName addedLN =
          new LocationName(
              addLongNameTextField.getText(),
              addShortNameTextField.getText(),
              nodeTypeComboBox.getValue());
      locationNameCombobox.getItems().add(addedLN.getLongName());
      SQLRepo.INSTANCE.addLocation(addedLN);
      addShortNameTextField.setText("");
      addLongNameTextField.setText("");
      nodeTypeComboBox.setValue(null);
    } else {
      // nothing
    }
  }

  private void removeLocation() {
    LocationName toBeDeleted =
        new LocationName(locationNameCombobox.getValue(), "", LocationName.NodeType.INFO);
    SQLRepo.INSTANCE.deleteLocation(toBeDeleted);
    locationNameCombobox.getItems().remove(locationNameCombobox.getValue());
  }

  private void updateCombo() {
    // POPULATE COMBOBOXES

    List<LocationName> locationNames = SQLRepo.INSTANCE.getLocationList();
    List<String> longNames = SQLRepo.INSTANCE.getLongNamesFromLocationName(locationNames);
    locationNameCombobox.setItems(FXCollections.observableList(longNames));

    buildingSelector.setItems(FXCollections.observableArrayList(HospitalNode.allBuildings()));

    nodeTypeComboBox.setItems(
        FXCollections.observableArrayList(LocationName.NodeType.allNodeTypes()));

    // DO EDGE AND ADD STUFF
    locationNameCombobox.setItems(FXCollections.observableList(longNames));

    buildingSelector.setItems(FXCollections.observableArrayList(HospitalNode.allBuildings()));
  }

  public GesturePane whichGesturePane(Floor curFloor) {
    switch (curFloor) {
      case ONE:
        return gesturePaneOne;
      case TWO:
        return gesturePaneTwo;
      case THREE:
        return gesturePaneThree;
      case LOWER_ONE:
        return gesturePaneLowerOne;
      case LOWER_TWO:
        return gesturePaneLowerTwo;
    }
    return null;
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

  private void initializeMapUtilities() {
    mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
    mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
    mapUtilityOne = new MapUtilities(mapPaneOne);
    mapUtilityTwo = new MapUtilities(mapPaneTwo);
    mapUtilityThree = new MapUtilities(mapPaneThree);

    mapUtilityLowerTwo.setLabelStyle("-fx-font-size: 10pt");
    mapUtilityLowerOne.setLabelStyle("-fx-font-size: 10pt");
    mapUtilityOne.setLabelStyle("-fx-font-size: 10pt");
    mapUtilityTwo.setLabelStyle("-fx-font-size: 10pt");
    mapUtilityThree.setLabelStyle("-fx-font-size: 10pt");
  }

  private void initializeMapGesturePanes() {
    mapPaneLowerTwo.setOnMouseClicked(
        event -> {
          gesturePaneLowerTwo.setGestureEnabled(true);
        });
    mapPaneLowerOne.setOnMouseClicked(
        event -> {
          gesturePaneLowerOne.setGestureEnabled(true);
        });
    mapPaneOne.setOnMouseClicked(
        event -> {
          gesturePaneOne.setGestureEnabled(true);
        });
    mapPaneTwo.setOnMouseClicked(
        event -> {
          gesturePaneTwo.setGestureEnabled(true);
        });
    mapPaneThree.setOnMouseClicked(
        event -> {
          gesturePaneThree.setGestureEnabled(true);
        });
  }

  private void initializeButtons() {

    removeLocationButton.setOnAction(
        event -> {
          removeLocation();
        });
    addLocationButton.setOnAction(event -> addLocationName());
    locationNameToggle.setOnMouseClicked(
        event -> {
          isLocationNamesDisplayed = locationNameToggle.isSelected();
          labelsVisibility(isLocationNamesDisplayed);
        });
    movesToggle.setOnMouseClicked(
        event -> {
          renderMoveComponents(allMoveLineNodes, listOfMoveLabel, movesToggle.isSelected());
        });
  }

  private int makeNewNodeID() {
    return (Integer.parseInt(
            allNodes.keySet().stream()
                .sorted(
                    new Comparator<>() {
                      @Override
                      public int compare(String str1, String str2) {
                        return Integer.parseInt((String) str1) - Integer.parseInt((String) str2);
                      }
                    })
                .toList()
                .get(allNodes.size() - 1))
        + 5);
  }

  private void drawMoveArrow() {
    setMoveNodes();
    System.out.println(allMoves);
    HospitalNode curNode;
    MapUtilities curMapUtil;
    HospitalNode futureNode;
    MapUtilities futureMapUtil;
    for (MoveAttribute move : allMoves) {
      futureNode = allNodes.get(move.getNodeID() + "");
      curNode =
          allNodes.get(moveUtil.findMostRecentMoveByDate(move.getLongName()).getNodeID() + "");
      System.out.println(curNode);
      curMapUtil = whichMapUtility(curNode.getFloor());
      futureMapUtil = whichMapUtility(futureNode.getFloor());
      List<Node> listOfNodes =
          curMapUtil.drawArrowLine(
              curNode.getXCoord(),
              curNode.getYCoord(),
              futureNode.getXCoord(),
              futureNode.getYCoord());
      Label fromMoveLabel =
          curMapUtil.createLabel(
              (curNode.getXCoord() + futureNode.getXCoord()) / 2,
              ((curNode.getYCoord() + futureNode.getYCoord()) / 2) - 50,
              move.getLongName()
                  + " at node "
                  + curNode.getNodeID()
                  + " is moving to node "
                  + futureNode.getNodeID()
                  + " on "
                  + move.getDate());
      fromMoveLabel.setStyle(
          "-fx-background-color: white; -fx-border-width: .5; -fx-border-color: black");
      fromMoveLabel.setFont(Font.font("Roboto", 6));
      if (futureNode.getFloor() == curNode.getFloor()) {
        Label toMoveLabel =
            curMapUtil.createLabel(
                futureNode.getXCoord(),
                futureNode.getYCoord() - 50,
                move.getLongName() + " is moving here from " + curNode.getNodeID());
        toMoveLabel.setStyle(
            "-fx-background-color: white; -fx-border-width: .5; -fx-border-color: black");
        toMoveLabel.setFont(Font.font("Roboto", 6));
        listOfMoveLabel.add(toMoveLabel);
      } else {
        Label toMoveLabel =
            curMapUtil.createLabel(
                futureNode.getXCoord(),
                futureNode.getYCoord() - 50,
                move.getLongName()
                    + " is moving here on floor "
                    + futureNode.getFloor().toString()
                    + " from "
                    + curNode.getNodeID());
        toMoveLabel.setStyle(
            "-fx-background-color: white; -fx-border-width: .5; -fx-border-color: black");
        toMoveLabel.setFont(Font.font("Roboto", 6));
        listOfMoveLabel.add(toMoveLabel);
      }
      listOfMoveLabel.add(fromMoveLabel);
      allMoveLineNodes.addAll(listOfNodes);
    }
  }

  private void setMoveNodes() {
    allMoves = moveUtil.getFutureMoves();
    for (MoveAttribute move : allMoves) {
      currentMoves.add(moveUtil.findMostRecentMoveByDate(move.getLongName()));
    }
  }

  private void renderMoveComponents(List<Node> lines, List<Label> labels, boolean visible) {
    for (Node line : lines) {
      line.setVisible(visible);
    }
    for (Label label : labels) {
      label.setVisible(visible);
    }
  }
}
