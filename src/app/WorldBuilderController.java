package app;

import app.classes.Map;
import app.classes.mapEntities.*;
import app.constants.Constants;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
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
    private TextField levelNameTextfield;
    @FXML
    private CheckBox socialDistancingCheckbox;
    @FXML
    private CheckBox increasedHygieneCheckbox;
    @FXML
    private CheckBox betterMedicineCheckbox;
    @FXML
    private Label statusLabel;
    @FXML
    private Button toMainMenuButton;
    @FXML
    private Canvas paintCanvas;
    @FXML
    private Button wallColorButton;
    @FXML
    private Button npcColorButton;
    @FXML
    private Button voidColorButton;
    @FXML
    private Button virusColorButton;
    @FXML
    private Button saveButton;

    // Sprite Colors
    private static final Color VOID_COLOR = Map.getVoidColor();

    // Temp colors
    private static final Color WALL_DRAG_INDICATOR_COLOR = Color.GOLD; // marks starting point of a new wall
    private static final Color WALL_DRAG_UNFINISHED_COLOR = Color.GREY; // color new wall has during dragging
    private static final Color ERRONEOUS_EXPORT = Color.RED;
    private static final Color SUCCESSFUL_EXPORT = Color.DARKGREEN;
    private final List<MapWall> wallMapSprites = new ArrayList<>();
    private final List<MapNPC> npcMapSprites = new ArrayList<>();

    // Sprites
    private MapPlayerChar virusMapSprites;

    // Misc
    private Rectangle wall = null;
    private double wallStartingX, wallStartingY;
    private MapSprite selectedSprite = new MapWall(-1, -1, 0, 0);

    public WorldBuilderController() {
        super();
    }

    public void initialize() {
        toMainMenuButton.setOnAction(e -> returnToPreviousScene());
        wallColorButton.setOnAction(e -> selectedSprite = new MapWall(-1, -1, -1, -1));
        voidColorButton.setFocusTraversable(false);
        wallColorButton.setTooltip(new Tooltip("Drag and Drop to paint a wall."));
        npcColorButton.setOnAction(e -> { // TODO: Implement GUI Selector
            if (Math.random() < 0.5) {
                selectedSprite = new MapNpcNormal(-1, -1);
            } else {
                selectedSprite = new MapNpcConspiracyTheorist(-1, -1);
            }
        });
        npcColorButton.setTooltip(new Tooltip("Click to set NPC spawn location."));
        voidColorButton.setOnAction(e -> selectedSprite = null);
        voidColorButton.setTooltip(new Tooltip("Click on any shape to delete it."));
        virusColorButton.setOnAction(e -> selectedSprite = new MapPlayerChar(-1, -1));
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
                if (wallMapSprites.size() > 0) {
                    wallMapSprites.stream().filter(mapSprites -> mapSprites.getBoundary()
                            .contains(subSampledX, subSampledY)).findAny().ifPresent(wallMapSprites::remove);
                }
                if (npcMapSprites.size() > 0) {
                    npcMapSprites.stream().filter(mapSprites -> mapSprites.getBoundary()
                            .contains(subSampledX, subSampledY)).findAny().ifPresent(npcMapSprites::remove);
                }
                if (virusMapSprites != null && virusMapSprites.getBoundary().contains(subSampledX, subSampledY)) {
                    virusMapSprites = null;
                }
                repaint(graphicsContext);
            }
            // begin drawing wall
            else if (selectedSprite instanceof MapWall) {
                // mark starting point of wall
                graphicsContext.setFill(WALL_DRAG_INDICATOR_COLOR);
                graphicsContext.fillRect(subSampledX, subSampledY, Constants.PIXEL_SIZE, Constants.PIXEL_SIZE);

                wallStartingX = subSampledX;
                wallStartingY = subSampledY;
                wall = new Rectangle();
            }
            // if space is not occupied, place NPC
            else if (selectedSprite instanceof MapNPC) {
                selectedSprite.setPosition(subSampledX, subSampledY);
                if ((wallMapSprites.size() == 0 || wallMapSprites.stream().noneMatch(mapSprites -> mapSprites.intersects(selectedSprite))) &&
                        (virusMapSprites == null || !virusMapSprites.intersects(selectedSprite)) &&
                        (npcMapSprites.size() == 0 || npcMapSprites.stream().noneMatch(mapSprites -> mapSprites.intersects(selectedSprite)))) {
                    npcMapSprites.add((MapNPC) selectedSprite);
                }
                repaint(graphicsContext);
                npcColorButton.fire();
            }
            // if space is not occupied, place or move virus
            else if (selectedSprite instanceof MapPlayerChar) {
                selectedSprite.setPosition(subSampledX, subSampledY);
                if ((wallMapSprites.size() == 0 || wallMapSprites.stream().noneMatch(mapSprites -> mapSprites.intersects(selectedSprite))) &&
                        (npcMapSprites.size() == 0 || npcMapSprites.stream().noneMatch(mapSprites -> mapSprites.intersects(selectedSprite)))) {
                    if (virusMapSprites != null) {
                        virusMapSprites.setPosition(subSampledX, subSampledY);
                    } else {
                        virusMapSprites = (MapPlayerChar) selectedSprite;
                    }
                }
                repaint(graphicsContext);
            }
        });

        paintCanvas.setOnMouseDragged(mouseEvent -> {
            int subSampledX = subSample(mouseEvent.getX());
            int subSampledY = subSample(mouseEvent.getY());

            // preview of dragged shape
            if (selectedSprite instanceof MapWall) {
                adjustCoords(wall, wallStartingX, wallStartingY, subSampledX, subSampledY);

                repaint(graphicsContext); //deletes previous preview
                graphicsContext.setFill(WALL_DRAG_UNFINISHED_COLOR);
                graphicsContext.fillRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
            }
        });

        paintCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            // final setting of shape
            if (selectedSprite instanceof MapWall) {
                wall.setFill(selectedSprite.getColor());
                MapWall newWall = new MapWall(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
                if (newWall.getBoundary().getWidth() > 0 && newWall.getBoundary().getHeight() > 0) {
                    wallMapSprites.add(newWall);
                    npcMapSprites.removeIf(mapSprites -> mapSprites.intersects(newWall));
                    if (virusMapSprites != null && virusMapSprites.intersects(newWall)) {
                        virusMapSprites = null;
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
        Stream.concat(wallMapSprites.stream(), npcMapSprites.stream()).forEach(mapSprite -> {
            graphicsContext.setFill(mapSprite.getColor());
            graphicsContext.fillRect(mapSprite.getBoundary().getMinX(), mapSprite.getBoundary().getMinY(),
                    mapSprite.getBoundary().getWidth(), mapSprite.getBoundary().getHeight());
        });

        // paints virus
        if (virusMapSprites != null) {
            graphicsContext.setFill(virusMapSprites.getColor());
            graphicsContext.fillRect(virusMapSprites.getBoundary().getMinX(), virusMapSprites.getBoundary().getMinY(),
                    virusMapSprites.getBoundary().getWidth(), virusMapSprites.getBoundary().getHeight());
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
        if (virusMapSprites == null) {
            throw new IllegalStateException("Starting location for virus must be set.");
        }
        if (npcMapSprites.size() == 0) {
            throw new IllegalStateException("Starting location for NPCs must be set.");
        }
        if (levelNameTextfield.getCharacters() == null || levelNameTextfield.getCharacters().equals("")) {
            throw new IllegalStateException("Level name must be set.");
        }

        // TODO: proper exporting
        System.out.println("Level Name: " + levelNameTextfield.getCharacters());
        System.out.println("Social Distancing: " + socialDistancingCheckbox.isSelected());
        System.out.println("Increased Hygiene: " + increasedHygieneCheckbox.isSelected());
        System.out.println("Better Medicine: " + betterMedicineCheckbox.isSelected());

        if (wallMapSprites.size() > 0) {
            System.out.println("Walls:");
            wallMapSprites.forEach(mapSprites -> System.out.println(mapSprites.toString()));
        }
        System.out.println("NPCs:");
        npcMapSprites.forEach(mapSprites -> System.out.println(mapSprites.toString()));

        System.out.println("Virus:");
        System.out.println(virusMapSprites.toString());

    }

}
