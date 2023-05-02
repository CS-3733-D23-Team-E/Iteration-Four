package edu.wpi.teame.controllers.DatabaseEditor;

import static java.lang.Integer.parseInt;

import edu.wpi.teame.App;
import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.map.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import org.controlsfx.control.SearchableComboBox;

public class DatabaseTableViewController {

  @FXML DatabaseEditorController databaseEditorController;

  // common buttons:
  @FXML MFXButton deleteButton;
  @FXML MFXButton addNodeButton;
  @FXML MFXButton addMoveButton;
  @FXML MFXButton addLocationButton;
  @FXML MFXButton addEdgeButton;

  // Tabs
  @FXML TabPane tableTabs;
  @FXML Tab moveTab;
  @FXML Tab edgeTab;
  @FXML Tab nameTab;
  // @FXML Tab requestTab;
  @FXML Tab nodeTab;

  // fields for Moves
  @FXML HBox movesAddZone;
  @FXML MFXTextField IDField;
  @FXML MFXTextField locationField;
  @FXML MFXTextField dateField;

  // table data for Moves
  @FXML TableView<MoveAttribute> moveTable;

  @FXML TableColumn<MoveAttribute, String> nodeIDCol;

  @FXML TableColumn<MoveAttribute, String> nameCol;

  @FXML TableColumn<MoveAttribute, String> dateCol;

  // fields for Nodes
  @FXML HBox nodeAddZone;
  @FXML MFXTextField IDFieldLoc;
  @FXML MFXTextField xField;
  @FXML MFXTextField yField;
  @FXML MFXTextField floorField;
  @FXML MFXTextField buildingField;

  // table data for Nodes
  @FXML TableView<HospitalNode> nodeTable;
  @FXML TableColumn<HospitalNode, String> nodeIDCoordCol;
  @FXML TableColumn<HospitalNode, Integer> nodeXCol;
  @FXML TableColumn<HospitalNode, Integer> nodeYCol;
  @FXML TableColumn<HospitalNode, Floor> floorCol;
  @FXML TableColumn<HospitalNode, String> buildingCol;

  // fields for Location Names
  @FXML HBox locationAddZone;
  @FXML MFXTextField longNameField;
  @FXML MFXTextField shortNameField;
  @FXML MFXTextField locationTypeField;

  // table data for Location Names
  @FXML TableView<LocationName> locationTable;
  @FXML TableColumn<LocationName, String> longNameCol;
  @FXML TableColumn<LocationName, String> shortNameCol;
  @FXML TableColumn<LocationName, String> nodeTypeCol;

  // fields for Edges
  @FXML HBox edgeAddZone;
  @FXML MFXTextField edge1Field;
  @FXML MFXTextField edge2Field;

  // table data for Edges
  @FXML TableView<HospitalEdge> edgeTable;
  @FXML TableColumn<HospitalEdge, String> edge1Col;
  @FXML TableColumn<HospitalEdge, String> edge2Col;

  // table data for service requests
  //  @FXML TableView<ServiceRequestData> requestTable;
  //  @FXML TableColumn<ServiceRequestData, JSONObject> dataCol;
  //  @FXML TableColumn<ServiceRequestData, ServiceRequestData.RequestType> typeCol;
  //  @FXML TableColumn<ServiceRequestData, ServiceRequestData.Status> statusCol;
  //  @FXML TableColumn<ServiceRequestData, String> staffCol;

  //////////////////////////
  @FXML VBox editNodeZone;
  @FXML MFXTextField editNodeIDField;
  @FXML MFXTextField editNodeXField;
  @FXML MFXTextField editNodeYField;
  @FXML SearchableComboBox<Floor> editNodeFloorChoice;
  @FXML SearchableComboBox<String> editNodeBuildingChoice;
  //////////////////////////
  @FXML VBox editMoveZone;
  @FXML MFXTextField editMoveIDField;
  @FXML SearchableComboBox<String> editMoveNameChoice;
  @FXML MFXTextField editMoveDateField; // TODO: MAKE THIS A DATE PICKER

  ///////////////////////////
  @FXML VBox editNameZone;
  @FXML MFXTextField editNameLongField;
  @FXML MFXTextField editNameShortField;
  @FXML SearchableComboBox<LocationName.NodeType> editNameTypeChoice;

  ///////////////////////////
  @FXML VBox editEdgeZone;
  @FXML TextField editEdgeStartField;
  @FXML TextField editEdgeEndField;
  ///////////////////////////
  @FXML MFXButton confirmEditButton;

  FileChooser saveChooser = new FileChooser();
  FileChooser selectChooser = new FileChooser();

  TableView activeTable;
  SQLRepo.Table activeTableEnum;

  String nyay = "\u00F1"; // ñ
  String aA = "\u0301"; // á
  String aE = "\u00E9"; // é
  String aI = "\u00ED"; // í
  String aO = "\u00F3"; // ó
  String aU = "\u00FA"; // ù
  String aQuestion = "\u00BF"; // Upside down question mark

  @FXML
  public void initialize() {

    databaseEditorController.setOnlySelected(databaseEditorController.tableEditorSwapButton);

    activeTable = nodeTable;
    activeTableEnum = SQLRepo.Table.NODE;

    Popup windowPop = new Popup();
    Label popupLabel = new Label("Error: improper formatting");
    popupLabel.setStyle("-fx-background-color: red;");
    windowPop.getContent().add(popupLabel);
    windowPop.setAutoHide(true);

    Popup confirmPop = new Popup();
    Label confirmLabel = new Label("Row added successfully");
    confirmLabel.setStyle("-fx-background-color: green;");
    confirmPop.getContent().add(confirmLabel);
    confirmPop.setAutoHide(true);

    saveChooser.setTitle("Select where to save your file");
    saveChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", ".csv"));
    saveChooser.setInitialFileName("Node_Table");
    selectChooser.setTitle("Select file to import");

    SQLRepo dC = SQLRepo.INSTANCE;
    // dC.connectToDatabase("teame", "teame50");

    // load the database into the table
    nodeIDCol.setCellValueFactory(new PropertyValueFactory<MoveAttribute, String>("nodeID"));
    nameCol.setCellValueFactory(new PropertyValueFactory<MoveAttribute, String>("longName"));
    dateCol.setCellValueFactory(new PropertyValueFactory<MoveAttribute, String>("date"));

    moveTable.setItems(FXCollections.observableArrayList(dC.getMoveList()));
    moveTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldSelection, newSelection) -> {
              if (newSelection != null) {
                displayMoveEdit(newSelection);
              }
            });
    moveTable.setEditable(true);

    longNameCol.setCellValueFactory(new PropertyValueFactory<LocationName, String>("longName"));
    shortNameCol.setCellValueFactory(new PropertyValueFactory<LocationName, String>("shortName"));
    nodeTypeCol.setCellValueFactory(new PropertyValueFactory<LocationName, String>("nodeType"));

    editNameTypeChoice.setItems(
        FXCollections.observableArrayList(LocationName.NodeType.allNodeTypes()));

    List<LocationName> locs = dC.getLocationList();
    ArrayList<String> longNames = new ArrayList<>();
    for (LocationName name : locs) {
      longNames.add(name.getLongName());
    }
    editMoveNameChoice.setItems(FXCollections.observableArrayList(longNames));

    locationTable.setItems(FXCollections.observableArrayList(locs));
    locationTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldSelection, newSelection) -> {
              if (newSelection != null) {
                displayNameEdit(newSelection);
              }
            });
    locationTable.setEditable(true);

    nodeIDCoordCol.setCellValueFactory(new PropertyValueFactory<HospitalNode, String>("nodeID"));
    nodeXCol.setCellValueFactory(new PropertyValueFactory<HospitalNode, Integer>("xCoord"));
    nodeYCol.setCellValueFactory(new PropertyValueFactory<HospitalNode, Integer>("yCoord"));
    floorCol.setCellValueFactory(new PropertyValueFactory<HospitalNode, Floor>("floor"));
    buildingCol.setCellValueFactory(new PropertyValueFactory<HospitalNode, String>("building"));

    editNodeFloorChoice.setItems(FXCollections.observableArrayList(Floor.allFloors()));
    editNodeBuildingChoice.setItems(FXCollections.observableArrayList(HospitalNode.allBuildings()));

    nodeTable.setItems(FXCollections.observableArrayList(dC.getNodeList()));
    nodeTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldSelection, newSelection) -> {
              if (newSelection != null) {
                displayNodeEdit(newSelection);
              }
            });
    nodeTable.setEditable(true);

    edge1Col.setCellValueFactory(new PropertyValueFactory<HospitalEdge, String>("nodeOneID"));
    edge2Col.setCellValueFactory(new PropertyValueFactory<HospitalEdge, String>("nodeTwoID"));

    edgeTable.setItems(FXCollections.observableArrayList(dC.getEdgeList()));
    edgeTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldSelection, newSelection) -> {
              if (newSelection != null) {
                displayEdgeEdit(newSelection);
              }
            });
    edgeTable.setEditable(true);

    //    dataCol.setCellValueFactory(
    //        new PropertyValueFactory<ServiceRequestData, JSONObject>("requestData"));
    //    typeCol.setCellValueFactory(
    //        new PropertyValueFactory<ServiceRequestData, ServiceRequestData.RequestType>(
    //            "requestType"));
    //    statusCol.setCellValueFactory(
    //        new PropertyValueFactory<ServiceRequestData,
    // ServiceRequestData.Status>("requestStatus"));
    //    staffCol.setCellValueFactory(
    //        new PropertyValueFactory<ServiceRequestData, String>("assignedStaff"));
    //
    //    requestTable.setItems(FXCollections.observableArrayList(dC.getServiceRequestList()));
    //    requestTable.setEditable(true);

    moveTable.setPlaceholder(new Label("No rows to display"));

    // backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));

    deleteButton.setOnMouseClicked(
        event -> {
          removeItem();
        });

    App.getPrimaryStage()
        .addEventHandler(
            KeyEvent.KEY_PRESSED,
            new EventHandler<KeyEvent>() {
              @Override
              public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.BACK_SPACE
                    && !activeTableEnum.equals(SQLRepo.Table.SERVICE_REQUESTS)) {
                  removeItem();
                }
              }
            });

    addMoveButton.setOnMouseClicked(
        event -> {
          addMove(windowPop, confirmPop);
        });

    addEdgeButton.setOnMouseClicked(
        event -> {
          addEdge(windowPop, confirmPop);
        });

    addLocationButton.setOnMouseClicked(
        event -> {
          addLocation(windowPop, confirmPop);
        });

    addNodeButton.setOnMouseClicked(
        event -> {
          addNode(windowPop, confirmPop);
        });

    // Event handlers for switching tabs changing the current tab variable
    nameTab.setOnSelectionChanged(
        new EventHandler<Event>() {
          @Override
          public void handle(Event event) {
            changeTab(nameTab, SQLRepo.Table.LOCATION_NAME);
          }
        });

    edgeTab.setOnSelectionChanged(
        new EventHandler<Event>() {
          @Override
          public void handle(Event event) {
            changeTab(edgeTab, SQLRepo.Table.EDGE);
          }
        });

    moveTab.setOnSelectionChanged(
        new EventHandler<Event>() {
          @Override
          public void handle(Event event) {
            changeTab(moveTab, SQLRepo.Table.MOVE);
          }
        });

    nodeTab.setOnSelectionChanged(
        new EventHandler<Event>() {
          @Override
          public void handle(Event event) {
            changeTab(nodeTab, SQLRepo.Table.NODE);
          }
        });

    //    requestTab.setOnSelectionChanged(
    //        new EventHandler<Event>() {
    //          @Override
    //          public void handle(Event event) {
    //            changeTab(requestTab, SQLRepo.Table.SERVICE_REQUESTS);
    //          }
    //        });

    // Page Language Translation Code
    if (Settings.INSTANCE.getLanguage() == Settings.Language.ENGLISH) {
      translateToEnglish();
    } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
      translateToSpanish();
    } else // throw error for language not being a valid language
    {
      // throw some sort of error here at some point
    }
  }

  private void addNode(Popup windowPop, Popup confirmPop) {
    HospitalNode toAdd;
    String nodeI = (IDFieldLoc.getText());
    int nodeX;
    int nodeY;
    String flr = floorField.getText();
    String building = buildingField.getText();
    try {
      nodeX = parseInt(xField.getText());
      nodeY = parseInt(yField.getText());
      toAdd = new HospitalNode(new NodeInitializer(nodeI, nodeX, nodeY, flr, building));
      // DatabaseController.INSTANCE.addToTable(DatabaseController.Table.NODE, toAdd);
      SQLRepo.INSTANCE.addNode(toAdd);
      confirmPop.show(App.getPrimaryStage());
      nodeTable.getItems().clear();
      nodeTable.getItems().addAll(SQLRepo.INSTANCE.getNodeList());
      IDFieldLoc.clear();
      xField.clear();
      yField.clear();
      floorField.clear();
      buildingField.clear();
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
      windowPop.show(App.getPrimaryStage());
    }
  }

  private void addLocation(Popup windowPop, Popup confirmPop) {
    LocationName toAdd;
    String longName = longNameField.getText();
    String shortName = shortNameField.getText();
    LocationName.NodeType type =
        LocationName.NodeType.stringToNodeType(locationTypeField.getText());
    try {
      toAdd = new LocationName(longName, shortName, type);
      // DatabaseController.INSTANCE.addToTable(DatabaseController.Table.LOCATION_NAME, toAdd);
      SQLRepo.INSTANCE.addLocation(toAdd);
      confirmPop.show(App.getPrimaryStage());
      locationTable.getItems().clear();
      locationTable.getItems().addAll(SQLRepo.INSTANCE.getLocationList());
      longNameField.clear();
      shortNameField.clear();
      locationTypeField.clear();
    } catch (RuntimeException e) {
      // have an error pop up
      System.out.println("error caught");
      windowPop.show(App.getPrimaryStage());
      System.out.println(e);
    }
  }

  private void addMove(Popup windowPop, Popup confirmPop) {
    MoveAttribute toAdd;
    String nodeID = IDField.getText();
    String name = locationField.getText();
    String date = dateField.getText();
    // MoveAttribute newMoveAttribute;
    try {
      toAdd = new MoveAttribute(parseInt(nodeID), name, date);
      // DatabaseController.INSTANCE.addToTable(DatabaseController.Table.MOVE, toAdd);
      SQLRepo.INSTANCE.addMove(toAdd);
      confirmPop.show(App.getPrimaryStage());
      moveTable.getItems().clear();
      moveTable.getItems().addAll(SQLRepo.INSTANCE.getMoveList());
      // moveTable.getItems().add(toAdd);
      IDField.clear();
      locationField.clear();
      dateField.clear();
    } catch (RuntimeException e) {
      // have an error pop up
      System.out.println("error caught");
      windowPop.show(App.getPrimaryStage());
    }
  }

  private void addEdge(Popup windowPop, Popup confirmPop) {
    HospitalEdge toAdd;
    String edge1 = edge1Field.getText();
    String edge2 = edge2Field.getText();
    try {
      toAdd = new HospitalEdge(edge1, edge2);
      // DatabaseController.INSTANCE.addToTable(DatabaseController.Table.EDGE, toAdd);
      SQLRepo.INSTANCE.addEdge(toAdd);
      confirmPop.show(App.getPrimaryStage());
      edgeTable.getItems().clear();
      edgeTable.getItems().addAll(SQLRepo.INSTANCE.getEdgeList());
      edge1Field.clear();
      edge2Field.clear();
    } catch (RuntimeException e) {
      // have an error pop up
      System.out.println("error caught");
      windowPop.show(App.getPrimaryStage());
    }
  }

  private void removeItem() {
    Object selectedItem = activeTable.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      activeTable.getItems().remove(selectedItem);
      // DatabaseController.INSTANCE.deleteFromTable(activeTableEnum, selectedItem);
      switch (activeTableEnum) {
        case NODE:
          SQLRepo.INSTANCE.deletenode((HospitalNode) selectedItem);
          nodeTable.getItems().clear();
          nodeTable.getItems().addAll(SQLRepo.INSTANCE.getNodeList());
          break;
        case LOCATION_NAME:
          SQLRepo.INSTANCE.deleteLocation((LocationName) selectedItem);
          locationTable.getItems().clear();
          locationTable.getItems().addAll(SQLRepo.INSTANCE.getLocationList());
          break;
        case MOVE:
          SQLRepo.INSTANCE.deleteMove((MoveAttribute) selectedItem);
          moveTable.getItems().clear();
          moveTable.getItems().addAll(SQLRepo.INSTANCE.getMoveList());
          break;
        case EDGE:
          SQLRepo.INSTANCE.deleteEdge((HospitalEdge) selectedItem);
          edgeTable.getItems().clear();
          edgeTable.getItems().addAll(SQLRepo.INSTANCE.getEdgeList());
          break;
      }
    }
  }

  private void changeTab(Tab tb, SQLRepo.Table table) {
    if (tb.isSelected()) {
      editMoveZone.setVisible(false);
      editNameZone.setVisible(false);
      editEdgeZone.setVisible(false);
      editNodeZone.setVisible(false);
      confirmEditButton.setVisible(false);
      editMoveZone.setManaged(false);
      editNameZone.setManaged(false);
      editEdgeZone.setManaged(false);
      editNodeZone.setManaged(false);
      confirmEditButton.setManaged(false);
      switch (table) {
        case EDGE:
          activeTable = edgeTable;
          activeTableEnum = table;
          deleteButton.setDisable(false);
          saveChooser.setInitialFileName("Edge_Table");
          break;
        case MOVE:
          activeTable = moveTable;
          activeTableEnum = table;
          deleteButton.setDisable(false);
          saveChooser.setInitialFileName("Move_Table");
          break;
        case NODE:
          activeTable = nodeTable;
          activeTableEnum = table;
          deleteButton.setDisable(false);
          saveChooser.setInitialFileName("Node_Table");
          break;
        case LOCATION_NAME:
          activeTable = locationTable;
          activeTableEnum = table;
          deleteButton.setDisable(false);
          saveChooser.setInitialFileName("Name_Table");
          break;
          //        case SERVICE_REQUESTS:
          //          activeTable = requestTable;
          //          activeTableEnum = table;
          //          importButton.setDisable(false);
          //          exportButton.setDisable(false);
          //          deleteButton.setDisable(false);
          //          break;
      }
    }
  }

  private void displayMoveEdit(MoveAttribute move) {
    editMoveZone.setVisible(true);
    editNameZone.setVisible(false);
    editEdgeZone.setVisible(false);
    editNodeZone.setVisible(false);
    editMoveZone.setManaged(true);
    editNameZone.setManaged(false);
    editEdgeZone.setManaged(false);
    editNodeZone.setManaged(false);
    confirmEditButton.setManaged(true);

    String nodeID = Integer.toString(move.getNodeID());

    editMoveIDField.setText(nodeID);
    editMoveNameChoice.setValue(move.getLongName());
    editMoveDateField.setText(move.getDate());

    confirmEditButton.setVisible(true);
    confirmEditButton.setOnAction(
        (event) -> {
          SQLRepo.INSTANCE.updateMove(move, "longName", editMoveNameChoice.getValue());
          SQLRepo.INSTANCE.updateMove(move, "nodeID", editMoveIDField.getText());
          SQLRepo.INSTANCE.updateMove(
              move,
              "date",
              editMoveDateField.getText()); // TODO: Switch to date picker implementation
          moveTable.setItems(FXCollections.observableArrayList(SQLRepo.INSTANCE.getMoveList()));
          moveTable.refresh();
        });
  }

  private void displayNodeEdit(HospitalNode node) {
    editMoveZone.setVisible(false);
    editNameZone.setVisible(false);
    editEdgeZone.setVisible(false);
    editNodeZone.setVisible(true);
    editMoveZone.setManaged(false);
    editNameZone.setManaged(false);
    editEdgeZone.setManaged(false);
    editNodeZone.setManaged(true);
    confirmEditButton.setManaged(true);

    editNodeIDField.setText(node.getNodeID());
    editNodeXField.setText(node.getXCoord() + "");
    editNodeYField.setText(node.getYCoord() + "");
    editNodeFloorChoice.setValue(node.getFloor());
    editNodeBuildingChoice.setValue(node.getBuilding());

    confirmEditButton.setVisible(true);
    confirmEditButton.setOnAction(
        (event) -> {
          SQLRepo.INSTANCE.updateNode(node, "nodeID", editNodeIDField.getText());
          SQLRepo.INSTANCE.updateNode(node, "xcoord", editNodeXField.getText());
          SQLRepo.INSTANCE.updateNode(node, "ycoord", editNodeYField.getText());
          SQLRepo.INSTANCE.updateNode(
              node, "floor", Floor.floorToString(editNodeFloorChoice.getValue()));
          SQLRepo.INSTANCE.updateNode(node, "building", editNodeBuildingChoice.getValue());
          nodeTable.setItems(FXCollections.observableArrayList(SQLRepo.INSTANCE.getNodeList()));
          nodeTable.refresh();
        });
  }

  private void displayEdgeEdit(HospitalEdge edge) {
    editMoveZone.setVisible(false);
    editNameZone.setVisible(false);
    editEdgeZone.setVisible(true);
    editNodeZone.setVisible(false);
    editMoveZone.setManaged(false);
    editNameZone.setManaged(false);
    editEdgeZone.setManaged(true);
    editNodeZone.setManaged(false);
    confirmEditButton.setManaged(true);

    editEdgeStartField.setText(edge.getNodeOneID());
    editEdgeEndField.setText(edge.getNodeTwoID());

    confirmEditButton.setVisible(true);
    confirmEditButton.setOnAction(
        (event) -> {
          SQLRepo.INSTANCE.updateEdge(edge, "startNode", editEdgeStartField.getText());
          SQLRepo.INSTANCE.updateEdge(edge, "startNode", editEdgeEndField.getText());
          edgeTable.setItems(FXCollections.observableArrayList(SQLRepo.INSTANCE.getEdgeList()));
          edgeTable.refresh();
        });
  }

  private void displayNameEdit(LocationName name) {
    editMoveZone.setVisible(false);
    editNameZone.setVisible(true);
    editEdgeZone.setVisible(false);
    editNodeZone.setVisible(false);
    editMoveZone.setManaged(false);
    editNameZone.setManaged(true);
    editEdgeZone.setManaged(false);
    editNodeZone.setManaged(false);
    confirmEditButton.setManaged(true);

    editNameLongField.setText(name.getLongName());
    editNameShortField.setText(name.getShortName());
    editNameTypeChoice.setValue(name.getNodeType());

    confirmEditButton.setVisible(true);
    confirmEditButton.setOnAction(
        (event) -> {
          SQLRepo.INSTANCE.updateLocation(name, "longName", editNameLongField.getText());
          SQLRepo.INSTANCE.updateLocation(name, "shortName", editNameShortField.getText());
          SQLRepo.INSTANCE.updateLocation(
              name, "nodeType", editNameTypeChoice.getValue().toString());
          locationTable.setItems(
              FXCollections.observableArrayList(SQLRepo.INSTANCE.getLocationList()));
          locationTable.refresh();
        });
  }

  public void importTable() {
    File selectedFile = selectChooser.showOpenDialog(App.getPrimaryStage());
    if (selectedFile == null) {
      // cancel
    } else {
      // add the file

      SQLRepo.INSTANCE.importFromCSV(activeTableEnum, selectedFile.getAbsolutePath());
      // refresh
      switch (activeTableEnum) {
        case MOVE:
          activeTable.setItems(FXCollections.observableArrayList(SQLRepo.INSTANCE.getMoveList()));
          break;
        case NODE:
          activeTable.setItems(FXCollections.observableArrayList(SQLRepo.INSTANCE.getNodeList()));
          break;
        case LOCATION_NAME:
          activeTable.setItems(
              FXCollections.observableArrayList(SQLRepo.INSTANCE.getLocationList()));
          break;
        case EDGE:
          activeTable.setItems(FXCollections.observableArrayList(SQLRepo.INSTANCE.getEdgeList()));
          break;
      }
      activeTable.refresh();
    }
  }

  public void exportTable() {
    // File selectedDirectory = directoryChooser.showDialog(App.getPrimaryStage());
    File selectedFile = saveChooser.showSaveDialog(App.getPrimaryStage());
    if (selectedFile == null) {
      // cancel
    } else {
      // export to the given path
      SQLRepo.INSTANCE.exportToCSV(
          activeTableEnum, selectedFile.getParentFile().getAbsolutePath(), selectedFile.getName());
    }
  }

  public void translateToSpanish() {
    // Top Tabs
    nodeTab.setText("Nodo"); // Node
    edgeTab.setText("Bordes"); // Edges
    nameTab.setText("Nombres de Ubicaci" + aO + "n"); // Location Names
    moveTab.setText("Mover"); // Move

    // Right Side Buttons
    Font spanishDelete = new Font("Roboto", 14);
    deleteButton.setFont(spanishDelete);
    deleteButton.setText("Eliminar Fila Seleccionada"); // Delete Selected Row

    // Bottom Row Text Fields
    IDFieldLoc.setPromptText("ID de Nodo"); // Node ID
    xField.setPromptText("Coordenada X"); // X Coord
    yField.setPromptText("Coordenada Y"); // Y Coord
    floorField.setPromptText("Piso"); // Floor
    buildingField.setPromptText("Edificio"); // Building
    addNodeButton.setText("A" + nyay + "adir Nodo"); // Add Node

    // Column Text Fields
    floorCol.setText("Piso"); // Floor
    buildingCol.setText("Edificio"); // Building
  }

  public void translateToEnglish() {
    // Top Tabs
    nodeTab.setText("Node"); // Keep in English
    edgeTab.setText("Edges"); // Keep in English
    nameTab.setText("Location Names"); // Keep in English
    moveTab.setText("Mover"); // Keep in English

    // Right Side Buttons
    Font englishDelete = new Font("Roboto", 18);
    deleteButton.setFont(englishDelete);
    deleteButton.setText("Delete Selected Row"); // Keep in English

    // Bottom Row Text Fields
    IDFieldLoc.setPromptText("Node ID"); // Keep in English
    xField.setPromptText("X Coord"); // Keep in English
    yField.setPromptText("Y Coord"); // Keep in English
    floorField.setPromptText("Floor"); // Keep in English
    buildingField.setPromptText("Building"); // Keep in English
    addNodeButton.setText("Add Node"); // Keep in English

    // Column Text Fields
    floorCol.setText("Floor"); // Keep in English
    buildingCol.setText("Building"); // Keep in English
  }
}
