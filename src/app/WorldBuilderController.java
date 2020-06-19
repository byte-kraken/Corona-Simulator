package app;

import app.classes.Map;
import app.classes.mapEntities.MapNPC;
import app.classes.mapEntities.MapPlayerChar;
import app.classes.mapEntities.MapSprite;
import app.classes.mapEntities.MapWall;
import app.constants.Constants;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller {@link Controller} for the WorldBuilder.
 * <p>
 * Allows for painting, syntax-checking and exporting of user-generated levels.
 * Painted shapes are stored as serialized {@link MapSprite}.
 * <p>
 *
 * @author Felix Ferchhumer
 */
public class WorldBuilderController extends Controller {
    @FXML
    private ChoiceBox<String> npcChoiceBox;
    @FXML
    private TextField levelNameTextfield;
    @FXML
    private Label statusLabel;
    @FXML
    private Canvas paintCanvas;
    @FXML
    private CheckBox socialDistancingCheckbox, increasedHygieneCheckbox, betterMedicineCheckbox;
    @FXML
    private Button wallColorButton, npcColorButton, voidColorButton, virusColorButton, saveButton, toMainMenuButton;

    // Sprite Colors
    private static final Color VOID_COLOR = Map.getVoidColor();

    // Temp colors
    private static final Color WALL_DRAG_INDICATOR_COLOR = Color.GOLD; // marks starting point of a new wall
    private static final Color WALL_DRAG_UNFINISHED_COLOR = Color.GREY; // color new wall has during dragging
    private static final Color ERRONEOUS_EXPORT = Color.RED;
    private static final Color SUCCESSFUL_EXPORT = Color.DARKGREEN;

    // Misc
    private final Map map;
    private Rectangle wall = null;
    private double wallStartingX, wallStartingY;
    private MapSprite.SpriteType selectedSprite = MapSprite.SpriteType.WALL;

    public WorldBuilderController() {
        super();
        this.map = new Map();
    }

    public void initialize() {
        toMainMenuButton.setOnAction(e -> returnToPreviousScene());
        wallColorButton.setOnAction(e -> selectedSprite = MapSprite.SpriteType.WALL);
        wallColorButton.setTooltip(new Tooltip("Drag and Drop to paint a wall."));
        npcChoiceBox.setItems(FXCollections.observableList(Arrays.stream(MapNPC.NpcType.values()).map(MapNPC.NpcType::getName).collect(Collectors.toList())));
        npcChoiceBox.setValue(npcChoiceBox.getItems().get(0));
        npcChoiceBox.setOnAction(e -> npcColorButton.fire());
        npcChoiceBox.setTooltip(new Tooltip("Select your preferred NPC type."));
        npcColorButton.setOnAction(e -> selectedSprite = MapSprite.SpriteType.NPC);
        npcColorButton.setTooltip(new Tooltip("Click to set NPC spawn location."));
        voidColorButton.setOnAction(e -> selectedSprite = null);
        voidColorButton.setTooltip(new Tooltip("Click on any shape to delete it."));
        virusColorButton.setOnAction(e -> selectedSprite = MapSprite.SpriteType.PLAYER_CHAR);
        virusColorButton.setTooltip(new Tooltip("Click to set virus spawn location."));
        saveButton.setOnAction(e -> {
            try {
                exportSprites();
                statusLabel.setTextFill(SUCCESSFUL_EXPORT);
                statusLabel.setText("Successfully exported.");
            } catch (IllegalStateException ex) {
                statusLabel.setTextFill(ERRONEOUS_EXPORT);
                statusLabel.setText(ex.getMessage());
            }
        });
        saveButton.setTooltip(new Tooltip("Save and export your map."));
        socialDistancingCheckbox.setTooltip(new Tooltip("NPCs will avoid each other."));
        increasedHygieneCheckbox.setTooltip(new Tooltip("NPCs will have a lower probability to get sick."));
        betterMedicineCheckbox.setTooltip(new Tooltip("NPCs will become healthy quicker."));
    }

    /**
     * Handles the drawing logic and all mouse events.
     * <p>
     * Makes sure that no sprites overlap and only one {@link virusMapSprites} is present at a time.
     */
    @SuppressWarnings("JavadocReference")
    public void armCanvas() {
        paintCanvas.setCursor(Cursor.CROSSHAIR);
        wallColorButton.requestFocus();

        final GraphicsContext graphicsContext = paintCanvas.getGraphicsContext2D();
        //initializes empty map
        graphicsContext.setFill(VOID_COLOR);
        graphicsContext.fillRect(0, 0, paintCanvas.getWidth(), paintCanvas.getHeight());

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
                MapNPC newNPC = new MapNPC(subSampledX, subSampledY, MapNPC.NpcType.values()[npcChoiceBox.getSelectionModel().getSelectedIndex()]);
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
            graphicsContext.setFill(mapSprite.getColor());
            graphicsContext.fillRect(mapSprite.getBoundary().getMinX(), mapSprite.getBoundary().getMinY(),
                    mapSprite.getBoundary().getWidth(), mapSprite.getBoundary().getHeight());
        });

        // paints virus
        if (map.getPlayer() != null) {
            graphicsContext.setFill(map.getPlayer().getColor());
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
    private void exportSprites() throws IllegalStateException {
        if (map.getPlayer() == null) {
            throw new IllegalStateException("Starting location for virus must be set.");
        }
        if (map.getNpcs().size() == 0) {
            throw new IllegalStateException("Starting location for NPCs must be set.");
        }
        if (levelNameTextfield.getCharacters() == null || levelNameTextfield.getCharacters().toString().equals("")) {
            throw new IllegalStateException("Level name must be set.");
        }
        map.setMapName(levelNameTextfield.getCharacters().toString());
        map.setSocialDistancing(socialDistancingCheckbox.isSelected());
        map.setBetterMedicine(betterMedicineCheckbox.isSelected());
        map.setIncreasedHygiene(increasedHygieneCheckbox.isSelected());

        // TODO: proper exporting
        System.out.println("Level Name: " + levelNameTextfield.getCharacters());
        System.out.println("Social Distancing: " + socialDistancingCheckbox.isSelected());
        System.out.println("Increased Hygiene: " + increasedHygieneCheckbox.isSelected());
        System.out.println("Better Medicine: " + betterMedicineCheckbox.isSelected());

        if (map.getWalls().size() > 0) {
            System.out.println("Walls:");
            map.getWalls().forEach(mapSprites -> System.out.println(mapSprites.toString()));
        }
        System.out.println("NPCs:");
        map.getNpcs().forEach(mapSprites -> System.out.println(mapSprites.toString()));

        System.out.println("Virus:");
        System.out.println(map.getPlayer().toString());

    }

}
