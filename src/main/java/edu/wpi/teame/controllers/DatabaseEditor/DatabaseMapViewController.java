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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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
  @FXML Label xCoordinateLabel;
  @FXML Label yCoordinateLabel;
  @FXML TextField xField;
  @FXML TextField yField;

  // Location Name
  @FXML VBox locationNameVBox;
  @FXML Label locationNameLabel;
  @FXML SearchableComboBox<String> locationNameCombobox;

  // Location Name Selection Section
  @FXML HBox locationNameAddHBox;
  @FXML TextField addLongNameTextField;
  @FXML TextField addShortNameTextField;
  @FXML VBox nodeTypeVBox;
  @FXML SearchableComboBox<LocationName.NodeType> nodeTypeComboBox;

  // Location Name Add/Delete Buttons
  @FXML HBox locationButtonBox;
  @FXML MFXButton addLocationButton;
  @FXML MFXButton removeLocationButton;

  // Edge Table
  @FXML TableView<HospitalNode> edgeView;
  @FXML TableColumn edgeColumn;

  // Add Edge Boxes
  @FXML VBox addEdgeVBox;
  @FXML SearchableComboBox<String> addEdgeField;
  @FXML MFXButton addEdgeButton;
  @FXML MFXButton removeEdgeButton;

  // Add Building Boxes
  @FXML HBox buildingHBox;
  @FXML SearchableComboBox<String> buildingSelector;

  // Update Buttons
  @FXML HBox updateButtonHBox;
  @FXML MFXButton cancelButton;
  @FXML MFXButton deleteButton;
  @FXML MFXButton confirmButton;

  // -------------------------------------------------------//

  boolean isLocationNamesDisplayed = false;

  Floor currentFloor;
  MapUtilities mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
  MapUtilities mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
  MapUtilities mapUtilityOne = new MapUtilities(mapPaneOne);
  MapUtilities mapUtilityTwo = new MapUtilities(mapPaneTwo);
  MapUtilities mapUtilityThree = new MapUtilities(mapPaneThree);

  private Circle currentCircle;
  private Label currentLabel;

  List<HospitalNode> edges = new LinkedList<>();
  List<HospitalNode> addList = new LinkedList<>();
  List<HospitalNode> deleteList = new LinkedList<>();

  List<HospitalNode> workingList = new LinkedList<>();
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
    confirmButton.setOnMouseClicked(event -> confirmAddNode());
    cancelButton.setOnMouseClicked(event -> refreshMap());
    sidebarText.setText("Add Node");

    // set toggle button box back
    modeToggleButtonBox.setLayoutX(842);
    // visibility
    setSidebarVisible(true);
    deleteButton.setVisible(false);
  }

  private void configureEditNodeView() {
    // configure sidebar
    confirmButton.setOnMouseClicked(event -> confirmEditNode());
    cancelButton.setOnMouseClicked(event -> refreshMap());
    deleteButton.setOnMouseClicked(event -> deleteNode());
    sidebarText.setText("Edit Node");

    // set toggle button box back
    modeToggleButtonBox.setLayoutX(842);
    // visibility
    setSidebarVisible(true);
  }

  private void configureDragView() {
    // configure sidebar
    confirmButton.setOnMouseClicked(event -> confirmDrag());
    cancelButton.setOnMouseClicked(event -> refreshMap());
    sidebarText.setText("Drag Mode");

    // set toggle button box back
    modeToggleButtonBox.setLayoutX(842);
    // set coord X and Y visible
    sidebar.setVisible(true);
    coordHBox.setVisible(true);
    // pick the things to set invisible
    locationNameVBox.setVisible(false);
    locationNameAddHBox.setVisible(false);
    locationButtonBox.setVisible(false);
    edgeView.setVisible(false);
    addEdgeVBox.setVisible(false);
    buildingHBox.setVisible(false);
    deleteButton.setVisible(false);
  }

  private void configureAlignNodeView() {
    // configure sidebar
    confirmButton.setOnMouseClicked(event -> confirmAlign());
    cancelButton.setOnMouseClicked(event -> refreshMap());
    sidebarText.setText("Align Node");

    // set toggle button box back
    modeToggleButtonBox.setLayoutX(842);
    // visibility
    // set coord X and Y visible
    sidebar.setVisible(true);
    coordHBox.setVisible(true);
    // set everything else invisible
    locationNameVBox.setVisible(false);
    locationNameAddHBox.setVisible(false);
    locationButtonBox.setVisible(false);
    edgeView.setVisible(false);
    addEdgeVBox.setVisible(false);
    buildingHBox.setVisible(false);
    deleteButton.setVisible(false);
  }

  private void configureAddEdgeView() {
    // configure sidebar
    confirmButton.setOnMouseClicked(event -> confirmAddEdge());
    cancelButton.setOnMouseClicked(event -> refreshMap());
    sidebarText.setText("Add Edge");

    // set toggle button box back
    modeToggleButtonBox.setLayoutX(842);
    // visibility
    // set coord X and Y visible
    sidebar.setVisible(true);
    coordHBox.setVisible(true);
    // set everything else invisible
    locationNameVBox.setVisible(false);
    locationNameAddHBox.setVisible(false);
    locationButtonBox.setVisible(false);
    edgeView.setVisible(false);
    addEdgeVBox.setVisible(false);
    buildingHBox.setVisible(false);
    deleteButton.setVisible(false);
  }

  private void setSidebarVisible(boolean visibility) {
    sidebar.setVisible(visibility);
    coordHBox.setVisible(visibility);
    locationNameVBox.setVisible(visibility);
    locationNameAddHBox.setVisible(visibility);
    locationButtonBox.setVisible(visibility);
    edgeView.setVisible(visibility);
    addEdgeVBox.setVisible(visibility);
    buildingHBox.setVisible(visibility);
    deleteButton.setVisible(visibility);
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

  private void confirmAddEdge() {}

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

    //    //    // update allnodes
    //    //    // update database
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

  private void handleAddNode() {
    //    setViewOnlyVisible(addNodeView);
  }

  private void handleAddEdge(Circle circle) {
    System.out.println("handleAddEdge");
    // check if there is a circle already selected, if not add this circle to the list
    if (selectedCircles.isEmpty()) {
      selectedCircles.add(circle);
      highlightCircle(circle, true);
      System.out.println("is empty and added");
      return;
    }

    // if a new second node is selected
    if (!selectedCircles.get(0).equals(circle)) {
      selectedCircles.add(circle);
      highlightCircle(circle, true);
      MapUtilities currentMapUtility = whichMapUtility(currentFloor);
      HospitalNode node1 = circleToHospitalNodeMap.get(selectedCircles.get(0));
      HospitalNode node2 = circleToHospitalNodeMap.get(circle);
      SQLRepo.INSTANCE.addEdge(new HospitalEdge(node1.getNodeID(), node2.getNodeID()));
      HospitalNode.addEdge(node1, node2);
      currentMapUtility.drawEdge(node1, node2);
      refreshMap();
    }
  }
  //    // APPEARS WHEN YOU CLICK ON A NODE
  //    private void updateEditMenu() {
  //      String nodeID = circle.getId();
  //      editPageText.setText("Edit Node: ID = " + nodeID);
  //
  //      currNode = allNodes.get(nodeID);
  //
  //      String x = Integer.toString(currNode.getXCoord());
  //      String y = Integer.toString(currNode.getYCoord());
  //      xField.setText(x);
  //      yField.setText(y);
  //
  //      updateNodeEditMenuFields(nodeID);
  //    }
  //
  //    private void updateNodeEditMenuFields(String nodeID) {
  //      edges =
  //          SQLRepo.INSTANCE.getEdgeList().stream()
  //              .filter((edge) -> (edge.getNodeOneID().equals(nodeID)))
  //              .toList();
  //
  //      workingList = new LinkedList<>();
  //
  //      workingList.addAll(edges);
  //
  //      addList = new LinkedList<>();
  //      deleteList = new LinkedList<>();
  //
  //      longNameSelector.setValue(SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(nodeID)));
  //
  //        buildingSelector.setValue(currNode.getBuilding());
  //      confirmButton.setOnAction(
  //          (event) -> {
  //            uploadChangesToDatabase();
  //          });
  //
  //      edgeView.setItems(FXCollections.observableList(workingList));
  //
  //      deleteNodeButton.setVisible(true);

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
    // add respective edges TODO
    //    edgeUpdateDatabase();
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

    // Sidebar functions
    cancelButton.setOnAction(event -> cancel());
    //    confirmButton.setOnAction(event -> uploadChangesToDatabase());
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

    edgeColumn.setCellValueFactory(new PropertyValueFactory<HospitalNode, String>("nodeID"));
    configurePanView();
  }

  private void cancel() {
    if (currentCircle != null) {
      currentCircle.setRadius(5);
      currentLabel.setVisible(false);
    }
    refreshMap();
    currentCircle = null;
    currentLabel = null;
    displayAddMenu();
  }

  private void labelsVisibility(boolean visible) {
    for (Label aLabel : circleToLabelMap.values()) {
      aLabel.setVisible(visible);
    }
    if (currentLabel != null) {
      currentLabel.setVisible(true);
    }
  }

  private void deleteNode() {

    String nodeID = sidebarText.getText().substring(16);
    HospitalNode currNode = allNodes.get(nodeID);

    SQLRepo.INSTANCE.deletenode(currNode);
    HospitalNode.removeNode(currNode);
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
    //    allNodeLabels.clear();
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
            SQLRepo.INSTANCE.getNodeTypeFromNodeID(Integer.parseInt(node.getNodeID())))) {
      //      allNodeLabels.add(nodeLabel);
    }
  }

  public void refreshMap() {
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);
    currentMapUtility.removeAll();
    loadFloorNodes();
    renderMoveComponents(allMoveLineNodes, listOfMoveLabel, movesToggle.isSelected());
    unhighlightSelectedCircles();
    selectedCircles.clear();
  }

  private void setEditMenuVisible(boolean isVisible) {
    if (isVisible) {
      sidebarText.setText("Edit Node");
    } else {
      sidebarText.setText("Add Node");
    }
  }

  //  // APPEARS WHEN YOU CLICK ON A NODE
  //  private void updateEditMenu() {
  //    String nodeID = currentCircle.getId();
  //    editPageText.setText("Edit Node: ID = " + nodeID);
  //
  //    currNode = allNodes.get(nodeID);
  //
  //    String x = Integer.toString(currNode.getXCoord());
  //    String y = Integer.toString(currNode.getYCoord());
  //    xField.setText(x);
  //    yField.setText(y);
  //
  //    updateNodeEditMenuFields(nodeID);
  //  }

  //  private void updateNodeEditMenuFields(String nodeID) {
  //    edges =
  //        SQLRepo.INSTANCE.getEdgeList().stream()
  //            .filter((edge) -> (edge.getNodeOneID().equals(nodeID)))
  //            .toList();
  //
  //    workingList = new LinkedList<>();
  //
  //    workingList.addAll(edges);
  //
  //    addList = new LinkedList<>();
  //    deleteList = new LinkedList<>();
  //
  //    longNameSelector.setValue(SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(nodeID)));
  //
  //    buildingSelector.setValue(currNode.getBuilding());
  //    confirmButton.setOnAction(
  //        (event) -> {
  //          uploadChangesToDatabase();
  //        });
  //
  //    edgeView.setItems(FXCollections.observableList(workingList));
  //
  //    deleteNodeButton.setVisible(true);
  //  }

  //  // called when node is dragged
  //  private void dragUpdate(MouseEvent mouseEvent) {
  //    String nodeID = currentCircle.getId();
  //    editPageText.setText("Edit Node: ID = " + nodeID);
  //    currNode = allNodes.get(nodeID);
  //
  //    updateNodeEditMenuFields(nodeID);
  //
  //    // get x and y from drag and set new x and y for circle and label
  //    ((Circle) mouseEvent.getSource()).setCenterX(mouseEvent.getX());
  //    ((Circle) mouseEvent.getSource()).setCenterY(mouseEvent.getY());
  //
  //    double newX = currentCircle.getCenterX();
  //    double newY = currentCircle.getCenterY();
  //    currentLabel.setLayoutX(newX);
  //    currentLabel.setLayoutY(newY);
  //
  //    // get image coordinates and update on edit menu
  //    MapUtilities currentMapUtility = whichMapUtility(currentFloor);
  //    int imageX = currentMapUtility.PaneXToImageX(currentCircle.getCenterX());
  //    int imageY = currentMapUtility.PaneYToImageY(currentCircle.getCenterY());
  //    xField.setText(Integer.toString(imageX));
  //    yField.setText(Integer.toString(imageY));
  //
  //    // update edges based off drag
  //    List<Node> startEdgesToUpdate =
  //        currentMapUtility.getCurrentNodes().stream()
  //            .filter(node -> node.getId().contains("startNode:" + currNode))
  //            .toList();
  //
  //    List<Node> endEdgesToUpdate =
  //        currentMapUtility.getCurrentNodes().stream()
  //            .filter(node -> node.getId().contains("endNode:" + currNode))
  //            .toList();
  //
  //    for (Node node : startEdgesToUpdate) {
  //      Line line = (Line) node;
  //      line.setStartX(newX);
  //      line.setStartY(newY);
  //    }
  //
  //    for (Node node : endEdgesToUpdate) {
  //      Line line = (Line) node;
  //      line.setEndX(newX);
  //      line.setEndY(newY);
  //    }
  //  }

  // APPEARS WHEN YOU CLICK OFF A NODE/CANCEL (DEFAULT)
  private void displayAddMenu() {
    setEditMenuVisible(false);

    int nodeID = makeNewNodeID();
    currentCircle = new Circle();
    currentCircle.setId(nodeID + "");
    // System.out.println(nodeID);
    sidebarText.setText("Add Node: ID = " + nodeID);

    // System.out.println("making sure we are here");

    // clear all items
    xField.setText("");
    yField.setText("");
    edges = new LinkedList<>();
    addList = new LinkedList<>();
    workingList = new LinkedList<>();
    deleteList = new LinkedList<>();
    locationNameCombobox.setValue(null);
    buildingSelector.setValue(null);
    // edgeView.getItems().clear();
    deleteButton.setVisible(false);

    edgeView.setItems(FXCollections.observableList(workingList));

    //    confirmButton.setOnAction(
    //        (event -> {
    //          addNodeToDatabase();
    //        }));
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

  //    private void uploadChangesToDatabase() {
  //      String nodeID = currentCircle.getId();
  //      HospitalNode hospitalNode = allNodes.get(nodeID);
  //      LocationName.NodeType nodeType =
  //          LocationName.NodeType.stringToNodeType(
  //              SQLRepo.INSTANCE.getNodeTypeFromNodeID(Integer.parseInt(nodeID)));
  //
  //      List<HospitalNode> nodesToBeUpdated = new ArrayList<>();
  //      nodesToBeUpdated.add(hospitalNode);
  //
  //      if (nodeType == LocationName.NodeType.ELEV) {
  //        // Getting Elevator (elevator letter) which is at the 10th index TODO parse/link
  // elevator
  //        // better
  //        String elevatorName =
  //            SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(nodeID)).substring(0, 10);
  //
  //        List<LocationName> locationNames = SQLRepo.INSTANCE.getLocationList();
  //        locationNames =
  //            locationNames.stream()
  //                .filter(locationName -> locationName.getLongName().contains(elevatorName))
  //                .toList();
  //        nodesToBeUpdated =
  //            locationNames.stream()
  //                .map(
  //                    locationName ->
  //                        HospitalNode.allNodes.get(
  //                            Integer.toString(
  //
  // SQLRepo.INSTANCE.getNodeIDFromName(locationName.getLongName()))))
  //                .toList();
  //
  //        for (HospitalNode node : nodesToBeUpdated) {
  //
  //          String currentNodeID = node.getNodeID();
  //
  //          String newX = xField.getText();
  //          String newY = yField.getText();
  //
  //          // update the database
  //          SQLRepo.INSTANCE.updateNode(node, "xcoord", newX);
  //          SQLRepo.INSTANCE.updateNode(node, "ycoord", newY);
  //          edgeUpdateDatabase();
  //
  //          if (currentNodeID.equals(nodeID)) {
  //            SQLRepo.INSTANCE.updateUsingNodeID(
  //                nodeID,
  //                SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(nodeID)),
  //                "longName",
  //                longNameSelector.getValue());
  //            continue;
  //          }
  //        }
  //
  //      } else {
  //        String newX = xField.getText();
  //        String newY = yField.getText();
  //        nodeID = hospitalNode.getNodeID();
  //
  //        // update the database
  //        SQLRepo.INSTANCE.updateNode(hospitalNode, "xcoord", newX);
  //        SQLRepo.INSTANCE.updateNode(hospitalNode, "ycoord", newY);
  //        edgeUpdateDatabase();
  //
  //        // EDIT THE MOVE
  //        SQLRepo.INSTANCE.updateUsingNodeID(
  //            nodeID,
  //            SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(nodeID)),
  //            "longName",
  //            longNameSelector.getValue());
  //      }
  //
  //      // RELOAD THE DATABASE
  //      refreshMap();
  //
  //      // CLOSE THE MENU
  //      displayAddMenu();
  //    }

  //  private void addNodeToDatabase() {
  //    int id = makeNewNodeID();
  //    // add respective node
  //    HospitalNode node =
  //        new HospitalNode(
  //            id + "",
  //            Integer.parseInt(xField.getText()),
  //            Integer.parseInt(yField.getText()),
  //            currentFloor,
  //            buildingSelector.getValue());
  //    SQLRepo.INSTANCE.addNode(node);
  //    // add respective move
  //    MoveAttribute move =
  //        new MoveAttribute(id, longNameSelector.getValue(), LocalDate.now().toString());
  //    SQLRepo.INSTANCE.addMove(move);
  //    // add respective edges
  //    edgeUpdateDatabase();
  //    refreshMap();
  //  }

  private void updateCombo() {
    // POPULATE COMBOBOXES

    List<LocationName> locationNames = SQLRepo.INSTANCE.getLocationList();
    List<String> longNames = SQLRepo.INSTANCE.getLongNamesFromLocationName(locationNames);
    locationNameCombobox.setItems(FXCollections.observableList(longNames));

    buildingSelector.setItems(FXCollections.observableArrayList(HospitalNode.allBuildings()));

    nodeTypeComboBox.setItems(
        FXCollections.observableArrayList(LocationName.NodeType.allNodeTypes()));

    addEdgeField.setItems(FXCollections.observableList(allNodes.keySet().stream().toList()));

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
          //          gesturePaneLowerTwo.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
        });
    mapPaneLowerOne.setOnMouseClicked(
        event -> {
          gesturePaneLowerOne.setGestureEnabled(true);
          //          gesturePaneLowerOne.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
        });
    mapPaneOne.setOnMouseClicked(
        event -> {
          gesturePaneOne.setGestureEnabled(true);
          //          gesturePaneOne.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
        });
    mapPaneTwo.setOnMouseClicked(
        event -> {
          gesturePaneTwo.setGestureEnabled(true);
          //          gesturePaneTwo.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
        });
    mapPaneThree.setOnMouseClicked(
        event -> {
          gesturePaneThree.setGestureEnabled(true);
          //          gesturePaneThree.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
        });
  }

  private void initializeButtons() {
    addEdgeButton.setOnAction(
        (event -> {
          // if item is in edge list, remove from delete list
          if (edges.contains(addEdgeField.getValue())) {
            deleteList.remove(addEdgeField.getValue());
          } else { // if item is not in edge list, add to add list
            addList.add(allNodes.get(addEdgeField.getValue()));
          }
          workingList.add(allNodes.get(addEdgeField.getValue()));
          // System.out.println("item added to working list!");
          // refresh the table
          refreshEdgeTable();
        }));

    removeEdgeButton.setOnAction(
        (event -> {
          // if item is in edge list, add to delete list
          if (edges.contains(edgeView.getSelectionModel().getSelectedItem())) {
            deleteList.add(edgeView.getSelectionModel().getSelectedItem());
            // System.out.println("added to delete list!");
          } else { // if item is not in the edge list, remove from add list
            addList.remove(edgeView.getSelectionModel().getSelectedItem());
          }
          workingList.remove(edgeView.getSelectionModel().getSelectedItem());
          refreshEdgeTable();
        }));
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

  private void refreshEdgeTable() {
    edgeView.setItems(FXCollections.observableList(workingList));
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

  //  public void translateToSpanish() {
  //    // Map Tabs
  //    lowerLevelTwoTab.setText("Piso Baja 2"); // Lower Level 2
  //    lowerLevelOneTab.setText("Piso Baja 1"); // Lower Level 1
  //    floorOneTab.setText("Piso 1"); // Floor 1
  //    floorTwoTab.setText("Piso 2"); // Floor 2
  //    floorThreeTab.setText("Piso 3"); // Floor 3
  //
  //    // Bottom Buttons
  //    Font spanishDisplay = new Font("Roboto", 11);
  //
  //    Font spanishButtons = new Font("Roboto", 8);
  //    panToggleButton.setFont(spanishButtons);
  //    addNodeToggleButton.setFont(spanishButtons);
  //    editToggleButton.setFont(spanishButtons);
  //    dragToggleButton.setFont(spanishButtons);
  //    alignToggleButton.setFont(spanishButtons);
  //    addEdgeToggleButton.setFont(spanishButtons);
  //    panToggleButton.setText("Panear"); // Pan
  //    addNodeToggleButton.setText("A" + nyay + "adir"); // Add
  //    editToggleButton.setText("Editar"); // Edit
  //    dragToggleButton.setText("Arrastrar"); // Drag
  //    alignToggleButton.setText("Alinear"); // Align
  //    addEdgeToggleButton.setText("Borde"); // Edge
  //
  //    Font displayNames = new Font("Roboto", 10);
  //    displayNamesLabel.setFont(displayNames);
  //    displayMovesLabel.setFont(displayNames);
  //    displayNamesLabel.setText("Mostrar Nombres"); // Show Names
  //    displayMovesLabel.setText("Mostrar Movimientos"); // Show Moves
  //
  //    // Align Node Buttons
  //    alignNodeText.setText("Alinear Nodo"); // Align Node
  //    cancelButton4.setText("Cancelar"); // Cancel
  //    deleteNodeButton4.setText("Eliminar"); // Delete
  //    alignConfirmButton.setText("Confirmar"); // Confirm
  //
  //    // Add Node Buttons
  //    addNodeText.setText("A" + nyay + "adir Nodo"); // Add Node
  //    xCoordinateLabel.setText("Coordenada X"); // X Coordinate
  //    addNodeXField.setPromptText("Coordenada X"); // X Coordinate
  //    yCoordinateLabel.setText("Coordenada Y"); // Y Coordinate
  //    addNodeYField.setPromptText("Coordenada Y"); // Y Coordinate
  //    locationNameLabel.setText("Nombre de Ubicaci" + Settings.INSTANCE.aO + "n"); // Locaton Name
  //    addNodeLongNameSelector.setPromptText(
  //        "Nombre de Ubicaci" + Settings.INSTANCE.aO + "n"); // Location Name
  //    longNameText.setText("Nombre Largo"); // Long Name
  //    newLongNameField13.setPromptText("Nombre Largo"); // Long Name
  //    longNameText2.setText("Nombre Largo"); // Long Name
  //    newLongNameField11.setPromptText("Nombre Largo"); // Long Name
  //    nodeTypeText.setText("Tipo de Nodo"); // Node Type
  //    nodeTypeChoice1.setPromptText("Tipo de Nodo"); // Node Type
  //    addLocationButton1.setText(
  //        "A" + nyay + "adir Ubicaci" + Settings.INSTANCE.aO + "n"); // Add location
  //    removeLocationButton1.setText(
  //        "Llevarse Ubicaci" + Settings.INSTANCE.aO + "n"); // remove location
  //    edgeColumn1.setText("ID de Nodo"); // Edge
  //    newEdgeText.setText("Borde Nuevo"); // New Edge
  //    addEdgeButton1.setText("A" + nyay + "adir Borde");
  //    removeEdgeButton1.setText("Llevarse Borde");
  //    buildingText.setText("Edificio");
  //    cancelButton1.setText("Cancelar");
  //    deleteNodeButton1.setText("Eliminar");
  //    addConfirmButton.setText("Confirmar");
  //
  //    // Drag Buttons
  //    dragNodeText.setText("Arrastrar Nodo"); // Drag Node
  //    xCoordinateLabel2.setText("Coordenada X");
  //    dragNodeXField.setText("Coordenada X");
  //    yCoordinateLabel2.setText("Coordenada Y");
  //    dragNodeYField.setText("Coordenada Y");
  //    cancelButton3.setText("Cancelar");
  //    deleteNodeButton3.setText("Eliminar");
  //    dragConfirmButton.setText("Confirmar");
  //
  //    // Edit Buttons
  //    editPageText.setText("Editar Nodo");
  //    xCoordinateLabel9.setText("Coordenada X");
  //    xField.setText("Coordenada X");
  //    yCoordinateLabel9.setText("Coordenada Y");
  //    yField.setText("Coordenada Y");
  //    locationNameLabel9.setText("Nombre de Ubicaci" + Settings.INSTANCE.aO + "n");
  //    longNameSelector.setPromptText("Nombre de Ubicaci" + Settings.INSTANCE.aO + "n");
  //    longNameText9.setText("Nombre Largo");
  //    newLongNameField.setText("Nombre Largo");
  //    longNameText99.setText("Nombre Largo");
  //    newLongNameField9.setText("Nombre Largo");
  //    nodeTypeText9.setText("Tipo de Nodo");
  //    addLocationButton.setText("A" + nyay + "adir Ubicaci" + Settings.INSTANCE.aO + "n");
  //    removeLocationButton.setText("Llevarse Ubicaci" + Settings.INSTANCE.aO + "n");
  //    edgeColumn.setText("Borde");
  //    newEdgeText2.setText("Borde Nuevo");
  //    addEdgeButton.setText("A" + nyay + "adir Borde");
  //    removeEdgeButton.setText("Llevarse Borde");
  //    buildingText9.setText("Edificio");
  //    cancelButton.setText("Cancelar");
  //    deleteNodeButton.setText("Eliminar");
  //    editConfirmButton.setText("Confirmar");
  //  }
  //
  //  public void translateToEnglish() {
  //    // Map Tabs
  //    lowerLevelTwoTab.setText("Lower Level 2"); // Keep in English
  //    lowerLevelOneTab.setText("Lower Level 1"); // Keep in English
  //    floorOneTab.setText("Floor 1"); // Keep in English
  //    floorTwoTab.setText("Floor 2"); // Keep in English
  //    floorThreeTab.setText("Floor 3"); // Keep in ENglish
  //
  //    // Bottom Buttons
  //    Font englishDisplay = new Font("Roboto", 12);
  //
  //    Font englishButtons = new Font("Roboto", 12);
  //    panToggleButton.setFont(englishButtons); // Keep in English
  //    addNodeToggleButton.setFont(englishButtons); // Keep in English
  //    editToggleButton.setFont(englishButtons); // Keep in English
  //    dragToggleButton.setFont(englishButtons); // Keep in English
  //    alignToggleButton.setFont(englishButtons); // Keep in English
  //    addEdgeToggleButton.setFont(englishButtons); // Keep in English
  //    panToggleButton.setText("Pan"); // Keep in English
  //    addNodeToggleButton.setText("Add"); // Keep in English
  //    editToggleButton.setText("Edit"); // Keep in English
  //    dragToggleButton.setText("Drag"); // Keep in English
  //    alignToggleButton.setText("Align"); // Keep in English
  //    addEdgeToggleButton.setText("Edge"); // Keep in English
  //
  //    Font displayNames = new Font("Roboto", 12);
  //    displayNamesLabel.setFont(displayNames);
  //    displayMovesLabel.setFont(displayNames);
  //    displayNamesLabel.setText("Show Names"); // Show Names
  //    displayMovesLabel.setText("Show Moves"); // Show Moves
  //
  //    // Align Node Buttons
  //    alignNodeText.setText("Align Node"); // Align Node
  //    cancelButton4.setText("Cancel"); // Cancel
  //    deleteNodeButton4.setText("Delete"); // Delete
  //    alignConfirmButton.setText("Confirm"); // Confrm
  //
  //    // Add Node Buttons
  //    addNodeText.setText("Add Node"); // Add Node
  //    xCoordinateLabel.setText("X Coordinate"); // X Coordinate
  //    addNodeXField.setPromptText("X Coordinate"); // X Coordinate
  //    yCoordinateLabel.setText("Y Coordinate"); // Y Coordinate
  //    addNodeYField.setPromptText("Y Coordinate"); // Y Coordinate
  //    locationNameLabel.setText("Location Name"); // Locaton Name
  //    addNodeLongNameSelector.setPromptText("Location Name"); // Location Name
  //    longNameText.setText("Long Name"); // Long Name
  //    newLongNameField13.setPromptText("Long Name"); // Long Name
  //    longNameText2.setText("Short Name"); // Long Name
  //    newLongNameField11.setPromptText("Short Name"); // Long Name
  //    nodeTypeText.setText("Node Type"); // Node Type
  //    nodeTypeChoice1.setPromptText("Node Type"); // Node Type
  //    addLocationButton1.setText("Add Location"); // Add location
  //    removeLocationButton1.setText("Remove Location"); // remove location
  //    edgeColumn1.setText("Node ID"); // Edge
  //    newEdgeText2.setText("New Edge"); // New Edge
  //    addEdgeButton1.setText("Add Edge");
  //    removeEdgeButton1.setText("Remove Edge");
  //    buildingText.setText("Building");
  //    cancelButton1.setText("Cancel");
  //    deleteNodeButton1.setText("Delete");
  //    addConfirmButton.setText("Confirm");
  //
  //    // Drag Buttons
  //    dragNodeText.setText("Drag Node"); // Drag Node
  //    xCoordinateLabel2.setText("X Coordinate");
  //    dragNodeXField.setText("X Coordinate");
  //    yCoordinateLabel2.setText("Y Coordinate");
  //    dragNodeYField.setText("Y Coordinate");
  //    cancelButton3.setText("Cancel");
  //    deleteNodeButton3.setText("Delete");
  //    dragConfirmButton.setText("Confirm");
  //
  //    // Edit Buttons
  //    editPageText.setText("Edit Node");
  //    xCoordinateLabel9.setText("X Coordinate");
  //    xField.setText("X Coordinate");
  //    yCoordinateLabel9.setText("Y Coordinate");
  //    yField.setText("Y Coordinate");
  //    locationNameLabel9.setText("Location Name");
  //    longNameSelector.setPromptText("Location Name");
  //    longNameText9.setText("Long Name");
  //    newLongNameField.setText("Long Name");
  //    longNameText99.setText("Short Name");
  //    newLongNameField9.setText("Short Name");
  //    nodeTypeText9.setText("Node Type");
  //    addLocationButton.setText("Add Location");
  //    removeLocationButton.setText("Remove Location");
  //    edgeColumn.setText("Edge");
  //    newEdgeText2.setText("New Edge");
  //    addEdgeButton.setText("Add Edge");
  //    removeEdgeButton.setText("Remove Edge");
  //    buildingText9.setText("Building");
  //    cancelButton.setText("Cancel");
  //    deleteNodeButton.setText("Delete");
  //    editConfirmButton.setText("Confirm");
  //  }
}
