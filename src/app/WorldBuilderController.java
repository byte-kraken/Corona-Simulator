package app;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller {@link Controller} for the WorldBuilder.
 * <p>
 * Allows for painting, syntax-checking and exporting of user-generated levels.
 * Painted shapes are stored as serialized {@link Sprite}.
 * <p>
 *
 * @author Felix Ferchhumer
 */
public class WorldBuilderController extends Controller {
    @FXML
    private Label label;
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

    private static final Color VOID_COLOR = Color.WHITE; // default color of playing field
    private static final Color WALL_COLOR = Color.BLACK;
    private static final Color VIRUS_COLOR = Color.GREEN;
    private static final Color NPC_COLOR = Color.RED;
    private static final Color WALL_DRAG_INDICATOR_COLOR = Color.GOLD; // marks starting point of a new wall
    private static final Color WALL_DRAG_UNFINISHED_COLOR = Color.GREY; // color new wall has during dragging
    private static final Color ERRONEOUS_EXPORT = Color.RED;
    private static final Color SUCCESSFUL_EXPORT = Color.DARKGREEN;

    private static final double PIXEL_SIZE = 8; // size of one pixel, all coords are sub-sampled accordingly
    private static final double NPC_SIZE = PIXEL_SIZE; // size of the NPC starting position indicator
    private static final double VIRUS_SIZE = NPC_SIZE; // size of the virus starting position indicator

    private Sprite virusSprite;
    private final List<Sprite> wallSprites = new ArrayList<>();
    private final List<Sprite> npcSprites = new ArrayList<>();

    private Rectangle wall = null;
    private Color color = WALL_COLOR;
    private double wallStartingX, wallStartingY;


    public WorldBuilderController() {
        super();
    }

    public void initialize() {
        toMainMenuButton.setOnAction(e -> returnToPreviousScene());
        wallColorButton.setOnAction(e -> color = WALL_COLOR);
        npcColorButton.setOnAction(e -> color = NPC_COLOR);
        voidColorButton.setOnAction(e -> color = VOID_COLOR);
        virusColorButton.setOnAction(e -> color = VIRUS_COLOR);
        saveButton.setOnAction(e -> {
            try {
                exportSprites();
                label.setTextFill(SUCCESSFUL_EXPORT);
                label.setText("Successfully exported.");
            } catch (IllegalStateException ex) {
                label.setTextFill(ERRONEOUS_EXPORT);
                label.setText(ex.getMessage());
            }
        });
    }

    /**
     * Handles the drawing logic and all mouse events.
     * <p>
     * Makes sure, that no sprites overlap and only one {@link virusSprite} is present at a time.
     */
    @SuppressWarnings("JavadocReference")
    public void armCanvas() {
        paintCanvas.setCursor(Cursor.CROSSHAIR);

        final GraphicsContext graphicsContext = paintCanvas.getGraphicsContext2D();
        //initializes empty map
        graphicsContext.setFill(VOID_COLOR);
        graphicsContext.fillRect(0, 0, paintCanvas.getWidth(), paintCanvas.getHeight());

        paintCanvas.setOnMousePressed(mouseEvent -> {
            int subSampledX = subSample(mouseEvent.getX(), PIXEL_SIZE);
            int subSampledY = subSample(mouseEvent.getY(), PIXEL_SIZE);
            // delete shape
            if (color == VOID_COLOR) {
                if (wallSprites.size() > 0) {
                    wallSprites.stream().filter(sprite -> sprite.getBoundary()
                            .contains(subSampledX, subSampledY)).findAny().ifPresent(wallSprites::remove);
                }
                if (npcSprites.size() > 0) {
                    npcSprites.stream().filter(sprite -> sprite.getBoundary()
                            .contains(subSampledX, subSampledY)).findAny().ifPresent(npcSprites::remove);
                }
                if (virusSprite != null && virusSprite.getBoundary().contains(subSampledX, subSampledY)) {
                    virusSprite = null;
                }
                repaint(graphicsContext);
            }
            // begin drawing wall
            else if (color == WALL_COLOR) {
                // mark starting point of wall
                graphicsContext.setFill(WALL_DRAG_INDICATOR_COLOR);
                graphicsContext.fillRect(subSampledX, subSampledY, NPC_SIZE, NPC_SIZE);

                wallStartingX = subSampledX;
                wallStartingY = subSampledY;
                wall = new Rectangle();
            }
            // if space is not occupied, place NPC
            else if (color == NPC_COLOR) {
                Sprite proposedNPC = new Sprite(subSampledX, subSampledY, NPC_SIZE, NPC_SIZE);
                if ((wallSprites.size() == 0 || wallSprites.stream().noneMatch(sprite -> sprite.intersects(proposedNPC))) &&
                        (virusSprite == null || !virusSprite.intersects(proposedNPC)) &&
                        (npcSprites.size() == 0 || npcSprites.stream().noneMatch(sprite -> sprite.intersects(proposedNPC)))) {
                    npcSprites.add(proposedNPC);
                }
                repaint(graphicsContext);
            }
            // if space is not occupied, place or move virus
            else if (color == VIRUS_COLOR) {
                Sprite proposedVirus = new Sprite(subSampledX, subSampledY, VIRUS_SIZE, VIRUS_SIZE);
                if ((wallSprites.size() == 0 || wallSprites.stream().noneMatch(sprite -> sprite.intersects(proposedVirus))) &&
                        (npcSprites.size() == 0 || npcSprites.stream().noneMatch(sprite -> sprite.intersects(proposedVirus)))) {
                    if (virusSprite != null) {
                        virusSprite.setPosition(subSampledX, subSampledY);
                    } else {
                        virusSprite = proposedVirus;
                    }
                }
                repaint(graphicsContext);
            }
        });

        paintCanvas.setOnMouseDragged(mouseEvent -> {
            int subSampledX = subSample(mouseEvent.getX(), PIXEL_SIZE);
            int subSampledY = subSample(mouseEvent.getY(), PIXEL_SIZE);

            // preview of dragged shape
            if (color == WALL_COLOR) {
                adjustCoords(wall, wallStartingX, wallStartingY, subSampledX, subSampledY);

                repaint(graphicsContext); //deletes previous preview
                graphicsContext.setFill(WALL_DRAG_UNFINISHED_COLOR);
                graphicsContext.fillRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
            }
        });

        paintCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            // final setting of shape
            if (color == WALL_COLOR) {
                wall.setFill(color);
                Sprite newWall = new Sprite(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
                wallSprites.add(newWall);
                npcSprites.removeIf(sprite -> sprite.intersects(newWall));
                if (virusSprite != null && virusSprite.intersects(newWall)) {
                    virusSprite = null;
                }
                repaint(graphicsContext);
            }
        });
    }

    /**
     * Sub-samples all coordinates to fit a pixel grid.
     * <p>
     *
     * @param coordinate: is rounded down to the next pixel
     * @param sampleRate: defines the size of a pixel
     * @return the sub-sampled coordinate
     */
    private int subSample(double coordinate, double sampleRate) {
        return (int) (coordinate - (coordinate % sampleRate));
    }

    /**
     * Clears the canvas and repaints all sprites.
     */
    private void repaint(GraphicsContext graphicsContext) {
        // clears and repaints background
        graphicsContext.setFill(VOID_COLOR);
        graphicsContext.fillRect(0, 0, paintCanvas.getWidth(), paintCanvas.getHeight());
        // paints wall sprites
        graphicsContext.setFill(WALL_COLOR);
        wallSprites.forEach(sprite -> graphicsContext.fillRect(sprite.getBoundary().getMinX(), sprite.getBoundary().getMinY(),
                sprite.getBoundary().getWidth(), sprite.getBoundary().getHeight()));
        // paints NPCs
        graphicsContext.setFill(NPC_COLOR);
        npcSprites.forEach(sprite -> graphicsContext.fillRect(sprite.getBoundary().getMinX(), sprite.getBoundary().getMinY(),
                sprite.getBoundary().getWidth(), sprite.getBoundary().getHeight()));
        // paints virus
        if (virusSprite != null) {
            graphicsContext.setFill(VIRUS_COLOR);
            graphicsContext.fillRect(virusSprite.getBoundary().getMinX(), virusSprite.getBoundary().getMinY(),
                    virusSprite.getBoundary().getWidth(), virusSprite.getBoundary().getHeight());
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
        if (virusSprite == null) {
            throw new IllegalStateException("Starting location for virus must be set.");
        }
        if (npcSprites.size() == 0) {
            throw new IllegalStateException("Starting location for NPCs must be set.");
        }
        if (wallSprites.size() > 0) {
            System.out.println("Walls:");
            wallSprites.forEach(sprite -> System.out.println(sprite.toString())); // TODO: proper exporting
        }
        System.out.println("NPCs:");
        npcSprites.forEach(sprite -> System.out.println(sprite.toString())); // TODO: proper exporting

        System.out.println("Virus:");
        System.out.println(virusSprite.toString()); // TODO: proper exporting

    }

}
