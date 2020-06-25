package app;

import app.classes.Map;
import app.classes.NPCType;
import app.classes.mapEntities.MapNPC;
import app.classes.mapEntities.MapPlayerChar;
import app.classes.mapEntities.MapSprite;
import app.classes.mapEntities.MapWall;
import app.util.Constants;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static app.util.Constants.*;

/**
 * Controller {@link Controller} for the WorldBuilder.
 * <p>
 * Allows for painting, syntax-checking and exporting of user-generated {@link Map}.
 * Painted shapes are stored as serialized {@link MapSprite}.
 */
public class WorldBuilderController extends Controller {
    @FXML
    private ChoiceBox<String> npcChoiceBox, loadWorldChoiceBox;
    @FXML
    private TextField levelNameTextfield;
    @FXML
    private Label statusLabel;
    @FXML
    private Canvas paintCanvas;
    @FXML
    private CheckBox socialDistancingCheckbox, increasedHygieneCheckbox, betterMedicineCheckbox;
    @FXML
    private Button wallCreateButton, npcCreateButton, deleteEntityButton, deleteAllButton, virusColorButton, saveButton, toMainMenuButton, loadWorldButton;

    // Sprite Colors
    private static final Color VOID_COLOR = Map.getVoidColor();

    // Temp colors
    private static final Color WALL_DRAG_INDICATOR_COLOR = Color.GOLD; // marks starting point of a new wall
    private static final Color WALL_DRAG_UNFINISHED_COLOR = Color.GREY; // color new wall has during dragging
    private static final Color ERRONEOUS_EXPORT_COLOR = Color.RED;
    private static final Color SUCCESSFUL_EXPORT_COLOR = Color.DARKGREEN;
    private static final Color NEUTRAL_MESSAGE_COLOR = Color.DARKBLUE;

    // Misc
    private Map map;
    private boolean tmpFileLoaded = false;
    private boolean mapExported = false;
    private final String workingDirectory = OWN_WORLDS_FOLDER_PATH; // change to LEVELS_FOLDER_PATH for level creation
    private Rectangle wall = null;
    private double wallStartingX, wallStartingY;
    private MapSprite.SpriteType selectedSprite = MapSprite.SpriteType.WALL;

    public WorldBuilderController() {
        super();
        Map loadedTmpMap;
        String path = TMP_WORLD_PATH;
        File tmpFile = new File(path);
        if (tmpFile.exists()) {
            try {
                loadedTmpMap = Map.deserialize(path);
                tmpFileLoaded = true;
            } catch (IOException | ClassNotFoundException e) {
                loadedTmpMap = new Map();
            }
        } else {
            loadedTmpMap = new Map();
        }
        this.map = loadedTmpMap;
    }

    /**
     * Loads and arms all FXML elements, where possible.
     */
    public void initialize() {
        toMainMenuButton.setOnAction(e -> returnToPreviousScene());

        statusLabel.setTextFill(NEUTRAL_MESSAGE_COLOR);
        statusLabel.setText("Use the controls at the top to draw and name your level. Press " + saveButton.getText() + " when you are done!");

        wallCreateButton.setOnAction(e -> selectedSprite = MapSprite.SpriteType.WALL);
        wallCreateButton.setTooltip(new Tooltip("Drag and Drop to paint a wall."));

        npcChoiceBox.setItems(FXCollections.observableList(Arrays.stream(NPCType.values()).map(NPCType::getName).collect(Collectors.toList())));
        npcChoiceBox.setValue(npcChoiceBox.getItems().get(0));
        npcChoiceBox.setOnAction(e -> npcCreateButton.fire());
        npcChoiceBox.setTooltip(new Tooltip("Select your preferred NPC type."));
        npcCreateButton.setOnAction(e -> selectedSprite = MapSprite.SpriteType.NPC);
        npcCreateButton.setTooltip(new Tooltip("Click to set NPC spawn location."));

        deleteEntityButton.setOnAction(e -> selectedSprite = null);
        deleteEntityButton.setTooltip(new Tooltip("Click on any shape to delete it."));
        deleteAllButton.setOnAction(e -> {
            map = new Map();
            repaint(paintCanvas.getGraphicsContext2D());
        });
        deleteAllButton.setTooltip(new Tooltip("Click to reset map."));
        virusColorButton.setOnAction(e -> selectedSprite = MapSprite.SpriteType.PLAYER_CHAR);
        virusColorButton.setTooltip(new Tooltip("Click to set virus spawn location."));

        File root = new File(workingDirectory);
        if (root.exists() && root.listFiles() != null) {
            loadWorldChoiceBox.setItems(FXCollections.observableList(Arrays.stream(Objects.requireNonNull(root.listFiles())).map(File::getName).collect(Collectors.toList())));
        } else {
            loadWorldChoiceBox.setVisible(false);
            loadWorldButton.setVisible(false);
            loadWorldButton.setTooltip(new Tooltip("Click to load one of your worlds."));
            loadWorldChoiceBox.setTooltip(new Tooltip("Select a world to load!"));
        }

        saveButton.setOnAction(e -> {
            try {
                File wd = new File(workingDirectory);
                if (!wd.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    wd.mkdir();
                }
                File file = new File(workingDirectory + levelNameTextfield.getText());
                if (levelNameTextfield.getText() != null && !levelNameTextfield.getText().isEmpty()) {
                    if (!file.exists()) {
                        exportSprites();
                        statusLabel.setTextFill(SUCCESSFUL_EXPORT_COLOR);
                        statusLabel.setText("Successfully exported.");
                    } else {
                        statusLabel.setTextFill(ERRONEOUS_EXPORT_COLOR);
                        statusLabel.setText("This name is already taken. Please choose a valid name!");
                    }
                } else {
                    statusLabel.setTextFill(ERRONEOUS_EXPORT_COLOR);
                    statusLabel.setText("Please choose a valid name!");
                }
            } catch (IllegalStateException ex) {
                statusLabel.setTextFill(ERRONEOUS_EXPORT_COLOR);
                statusLabel.setText(ex.getMessage());
            } catch (FileNotFoundException ex) {
                statusLabel.setTextFill(ERRONEOUS_EXPORT_COLOR);
                statusLabel.setText("File not found");
            } catch (IOException ex) {
                statusLabel.setTextFill(ERRONEOUS_EXPORT_COLOR);
                statusLabel.setText("Internal Error: Please check your world name");
            }
        });
        saveButton.setTooltip(new Tooltip("Save and export your map."));

        socialDistancingCheckbox.setTooltip(new Tooltip("NPCs will avoid each other."));
        increasedHygieneCheckbox.setTooltip(new Tooltip("NPCs will have a lower probability to get sick."));
        betterMedicineCheckbox.setTooltip(new Tooltip("NPCs will become healthy quicker."));
    }

    /**
     * Is to be called immediately after the constructor.
     * Initializes any options not customizable before {@link #initialize}.
     */
    public void armCanvas() {
        paintCanvas.setCursor(Cursor.CROSSHAIR);
        wallCreateButton.requestFocus();

        // makes sure that canvas is scaled correctly and follows agreed upon format
        assert paintCanvas.getWidth() / paintCanvas.getHeight() == 16.0 / 9.0 : "Canvas scaling does not fit format of 16/9";
        map.setScaleFactor((double) Constants.STANDARD_MAP_SIZE_X / paintCanvas.getWidth());

        final GraphicsContext graphicsContext = paintCanvas.getGraphicsContext2D();

        if (tmpFileLoaded) {
            loadWorldParameters();
        }

        loadWorldButton.setOnAction(e -> {
            if (!loadWorldChoiceBox.getSelectionModel().isEmpty()) {
                String worldName = loadWorldChoiceBox.getSelectionModel().selectedItemProperty().getValue();
                try {
                    map = Map.deserialize(workingDirectory + worldName);
                    loadWorldParameters();
                } catch (IOException | ClassNotFoundException ioException) {
                    ioException.printStackTrace();
                }
                repaint(graphicsContext);
            }
        });

        handleMouseEvents(graphicsContext);
        repaint(graphicsContext);
    }

    /**
     * Handles the drawing logic and all mouse events.
     * Makes sure that no {@link MapSprite} overlap and only one {@link MapPlayerChar} is present at a time.
     *
     * @param graphicsContext: Where stuff is drawn.
     */
    private void handleMouseEvents(GraphicsContext graphicsContext) {
        paintCanvas.setOnMousePressed(mouseEvent -> {
            statusLabel.setText("");
            int subSampledX = subSample(mouseEvent.getX());
            int subSampledY = subSample(mouseEvent.getY());
            // delete shape
            if (selectedSprite == null) {
                if (map.getWalls().size() > 0) {
                    map.getWalls().stream().filter(mapSprites -> mapSprites.getBoundary()
                            .contains(subSampledX, subSampledY)).findAny().ifPresent(map.getWalls()::remove);
                }
                if (map.getNpcs().size() > 0) {
                    map.getNpcs().stream().filter(mapSprites -> mapSprites.getBoundary()
                            .contains(subSampledX, subSampledY)).findAny().ifPresent(map.getNpcs()::remove);
                }
                if (map.getPlayer() != null && map.getPlayer().getBoundary().contains(subSampledX, subSampledY)) {
                    map.setPlayer(null);
                }
                repaint(graphicsContext);
            }
            // begin drawing wall
            else if (selectedSprite == MapSprite.SpriteType.WALL) {
                // mark starting point of wall
                graphicsContext.setFill(WALL_DRAG_INDICATOR_COLOR);
                graphicsContext.fillRect(subSampledX, subSampledY, Constants.PIXEL_SIZE, Constants.PIXEL_SIZE);

                wallStartingX = subSampledX;
                wallStartingY = subSampledY;
                wall = new Rectangle();
            }
            // if space is not occupied, place NPC
            else if (selectedSprite == MapSprite.SpriteType.NPC) {
                MapNPC newNPC = new MapNPC(subSampledX, subSampledY, NPCType.values()[npcChoiceBox.getSelectionModel().getSelectedIndex()]);
                if ((map.getWalls().size() == 0 || map.getWalls().stream().noneMatch(mapSprites -> mapSprites.intersects(newNPC))) &&
                        (map.getPlayer() == null || !map.getPlayer().intersects(newNPC)) &&
                        (map.getNpcs().size() == 0 || map.getNpcs().stream().noneMatch(mapSprites -> mapSprites.intersects(newNPC)))) {
                    map.addNPC(newNPC);
                }
                repaint(graphicsContext);
            }
            // if space is not occupied, place or move virus
            else if (selectedSprite == MapSprite.SpriteType.PLAYER_CHAR) {
                MapPlayerChar newVirus = new MapPlayerChar(subSampledX, subSampledY);
                if ((map.getWalls().size() == 0 || map.getWalls().stream().noneMatch(mapSprites -> mapSprites.intersects(newVirus))) &&
                        (map.getNpcs().size() == 0 || map.getNpcs().stream().noneMatch(mapSprites -> mapSprites.intersects(newVirus)))) {
                    if (map.getPlayer() != null) {
                        map.getPlayer().setPosition(subSampledX, subSampledY);
                    } else {
                        map.setPlayer(newVirus);
                    }
                }
                repaint(graphicsContext);
            }
        });

        paintCanvas.setOnMouseDragged(mouseEvent -> {
            int subSampledX = subSample(mouseEvent.getX());
            int subSampledY = subSample(mouseEvent.getY());

            // preview of dragged shape
            if (selectedSprite == MapSprite.SpriteType.WALL) {
                adjustCoords(wall, wallStartingX, wallStartingY, subSampledX, subSampledY);

                repaint(graphicsContext); //deletes previous preview
                graphicsContext.setFill(WALL_DRAG_UNFINISHED_COLOR);
                graphicsContext.fillRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
            }
        });

        paintCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            // final setting of shape
            if (selectedSprite == MapSprite.SpriteType.WALL) {
                MapWall newWall = new MapWall(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
                if (newWall.getBoundary().getWidth() > 0 && newWall.getBoundary().getHeight() > 0) {
                    map.addWall(newWall);
                    map.getNpcs().removeIf(mapSprites -> mapSprites.intersects(newWall));
                    if (map.getPlayer() != null && map.getPlayer().intersects(newWall)) {
                        map.setPlayer(null);
                    }
                }
                repaint(graphicsContext);
            }
        });
    }

    /**
     * Sub-samples all coordinates to fit a pixel grid.
     * <p>
     *
     * @param coordinate : is rounded down to the next pixel
     * @return the sub-sampled coordinate
     */
    private int subSample(double coordinate) {
        return (int) (coordinate - (coordinate % Constants.PIXEL_SIZE));
    }

    /**
     * Clears the canvas and repaints all sprites.
     */
    private void repaint(GraphicsContext graphicsContext) {
        // clears and repaints background
        graphicsContext.setFill(VOID_COLOR);
        graphicsContext.fillRect(0, 0, paintCanvas.getWidth(), paintCanvas.getHeight());

        // paints wall and NPC sprites
        Stream.concat(map.getWalls().stream(), map.getNpcs().stream()).forEach(mapSprite -> {
            graphicsContext.setFill(mapSprite.getColor().getFXColor());
            graphicsContext.fillRect(mapSprite.getBoundary().getMinX(), mapSprite.getBoundary().getMinY(),
                    mapSprite.getBoundary().getWidth(), mapSprite.getBoundary().getHeight());
        });

        // paints virus
        if (map.getPlayer() != null) {
            graphicsContext.setFill(map.getPlayer().getColor().getFXColor());
            graphicsContext.fillRect(map.getPlayer().getBoundary().getMinX(), map.getPlayer().getBoundary().getMinY(),
                    map.getPlayer().getBoundary().getWidth(), map.getPlayer().getBoundary().getHeight());
        }
    }

    /**
     * Makes sure that a dragged wall is displayed correctly when endpoint is higher / farther to left than initial x and y
     *
     * @param wall           : the shape which coordinates are being adjusted
     * @param shapeStartingX : X coordinate of first mouse-press
     * @param shapeStartingY : Y coordinate of first mouse-press
     * @param shapeEndingX   : current X coordinate on method call
     * @param shapeEndingY   : current Y coordinate on method call
     */
    private void adjustCoords(Rectangle wall, double shapeStartingX, double shapeStartingY, double shapeEndingX, double shapeEndingY) {
        wall.setX(shapeStartingX);
        wall.setY(shapeStartingY);
        wall.setWidth(shapeEndingX - shapeStartingX);
        wall.setHeight(shapeEndingY - shapeStartingY);

        if (wall.getWidth() < 0) {
            wall.setWidth(-wall.getWidth());
            wall.setX(wall.getX() - wall.getWidth());
        }

        if (wall.getHeight() < 0) {
            wall.setHeight(-wall.getHeight());
            wall.setY(wall.getY() - wall.getHeight());
        }
    }

    /**
     * Exports all sprites and their position to a file.
     * Checks if all required map elements are present, otherwise {@throws IllegalStateException}
     */
    private void exportSprites() throws IllegalStateException, IOException {
        if (map.getPlayer() == null) {
            throw new IllegalStateException("Starting location for virus must be set.");
        }
        if (map.getNpcs().size() == 0) {
            throw new IllegalStateException("Starting location for NPCs must be set.");
        }
        if (levelNameTextfield.getText() == null || levelNameTextfield.getText().equals("")) {
            throw new IllegalStateException("Level name must be set.");
        }
        setWorldParameters();
        Map.serialize(map, workingDirectory + map.getMapName());
        mapExported = true;


        File tmpFile = new File(TMP_WORLD_PATH);
        if (tmpFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            tmpFile.delete();
        }
    }

    /**
     * Meant to be called before saving a map.
     * Saves checkboxes and name to map.
     */
    private void setWorldParameters() {
        map.setMapName(levelNameTextfield.getText());
        map.setSocialDistancing(socialDistancingCheckbox.isSelected());
        map.setBetterMedicine(betterMedicineCheckbox.isSelected());
        map.setIncreasedHygiene(increasedHygieneCheckbox.isSelected());
    }

    /**
     * Meant to be called after loading a map.
     * Restores checkboxes and name from map.
     */
    private void loadWorldParameters() {
        levelNameTextfield.setText(map.getMapName());
        socialDistancingCheckbox.setSelected(map.isSocialDistancing());
        betterMedicineCheckbox.setSelected(map.isBetterMedicine());
        increasedHygieneCheckbox.setSelected(map.isIncreasedHygiene());
    }

    /**
     * Saves any unsaved changes in a tmp-File before returning to the previous scene.
     * This file is reloaded upon opening the WorldBuilder.
     */
    void returnToPreviousScene() {
        if (!mapExported) {
            setWorldParameters();
            File td = new File(TMP_WORLD_FOLDER_PATH);
            if (!td.exists()) {
                //noinspection ResultOfMethodCallIgnored
                td.mkdir();
            }
            try {
                Map.serialize(map, TMP_WORLD_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.returnToPreviousScene();
    }

}
