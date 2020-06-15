package app;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class LevelEditorController extends Controller {
    private static final Color WALL_COLOR = Color.BLACK;
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
    private static final Color VOID_COLOR = Color.WHITE;
    private static final Color VIRUS_COLOR = Color.GREEN;
    private static final Color NPC_COLOR = Color.RED;
    private static final Color SHAPE_INDICATOR_COLOR = Color.GOLD;
    private static final double NPC_SIZE = 8;
    private final List<Sprite> wallSprites = new ArrayList<>();

    private Color color = WALL_COLOR;
    private final List<Sprite> npcSprites = new ArrayList<>();
    @FXML
    private VBox test;
    private Rectangle shape = null;
    private Sprite virusSprite;

    private double shapeStartingX, shapeStartingY;

    public LevelEditorController() {
        super();
    }

    public void initialize() {
        toMainMenuButton.setOnAction(e -> returnToPreviousScene());
        wallColorButton.setOnAction(e -> color = WALL_COLOR);
        npcColorButton.setOnAction(e -> color = NPC_COLOR);
        voidColorButton.setOnAction(e -> color = VOID_COLOR);
        virusColorButton.setOnAction(e -> color = VIRUS_COLOR);
        saveButton.setOnAction(e -> exportSprites());
    }

    private void exportSprites() {
        if (wallSprites.size() > 0) {
            System.out.println("Walls:");
            wallSprites.forEach(sprite -> System.out.println(sprite.toString()));
        }
        if (npcSprites.size() > 0) {
            System.out.println("NPCs:");
            npcSprites.forEach(sprite -> System.out.println(sprite.toString()));
        }
        if (virusSprite != null) {
            System.out.println("Virus:");
            System.out.println(virusSprite.toString());
        }
    }

    public void armCanvas() {
        final GraphicsContext graphicsContext = paintCanvas.getGraphicsContext2D();

        //initializes empty map
        graphicsContext.setFill(VOID_COLOR);
        graphicsContext.fillRect(0, 0, paintCanvas.getWidth(), paintCanvas.getHeight());

        paintCanvas.setOnMousePressed(mouseEvent -> {
            // delete shape
            if (color == VOID_COLOR) {
                if (wallSprites.size() > 0) {
                    wallSprites.stream().filter(sprite -> sprite.getBoundary()
                            .contains(mouseEvent.getX(), mouseEvent.getY())).findAny().ifPresent(wallSprites::remove);
                }
                if (npcSprites.size() > 0) {
                    npcSprites.stream().filter(sprite -> sprite.getBoundary()
                            .contains(mouseEvent.getX(), mouseEvent.getY())).findAny().ifPresent(npcSprites::remove);
                }
                if (virusSprite != null && virusSprite.getBoundary().contains(mouseEvent.getX(), mouseEvent.getY())) {
                    virusSprite = null;
                }
                repaint(graphicsContext);
            }
            // begin drawing wall
            else if (color == WALL_COLOR) {
                // mark starting point of wall
                graphicsContext.setFill(SHAPE_INDICATOR_COLOR);
                graphicsContext.fillRect(mouseEvent.getX(), mouseEvent.getY(), NPC_SIZE, NPC_SIZE);

                shapeStartingX = mouseEvent.getX();
                shapeStartingY = mouseEvent.getY();
                shape = new Rectangle();
            }
            // if space is not occupied, place NPC
            else if (color == NPC_COLOR) {
                if ((wallSprites.size() == 0 ||
                        wallSprites.stream().noneMatch(sprite -> sprite.getBoundary().contains(mouseEvent.getX(), mouseEvent.getY()))) &&
                        (virusSprite == null ||
                                !virusSprite.getBoundary().contains(mouseEvent.getX(), mouseEvent.getY())) &&
                        (npcSprites.size() == 0 ||
                                npcSprites.stream().noneMatch(sprite -> sprite.getBoundary().contains(mouseEvent.getX(), mouseEvent.getY())))) {
                    npcSprites.add(new Sprite(mouseEvent.getX(), mouseEvent.getY(), NPC_SIZE, NPC_SIZE));
                }
                repaint(graphicsContext);
            }
            // if space is not occupied, place or move virus
            else if (color == VIRUS_COLOR) {
                if (wallSprites.stream().noneMatch(sprite -> sprite.getBoundary().contains(mouseEvent.getX(), mouseEvent.getY())) &&
                        npcSprites.stream().noneMatch(sprite -> sprite.getBoundary().contains(mouseEvent.getX(), mouseEvent.getY()))) {
                    if (virusSprite != null) {
                        virusSprite.setPosition(mouseEvent.getX(), mouseEvent.getY());
                    } else {
                        virusSprite = new Sprite(mouseEvent.getX(), mouseEvent.getY(), NPC_SIZE, NPC_SIZE);
                    }
                }
                repaint(graphicsContext);
            }
        });

        paintCanvas.setOnMouseDragged(mouseEvent -> {
            if (color == WALL_COLOR) {
                double shapeEndingX = mouseEvent.getX();
                double shapeEndingY = mouseEvent.getY();
                adjustCoords(shapeStartingX, shapeStartingY, shapeEndingX, shapeEndingY, shape);

                repaint(graphicsContext);
                graphicsContext.setFill(Color.BLUE);
                graphicsContext.fillRect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
            }
        });

        paintCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            if (color == WALL_COLOR) {
                shape.setFill(color);
                Sprite newWall = new Sprite(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
                wallSprites.add(newWall);
                npcSprites.removeIf(sprite -> sprite.intersects(newWall));
                if (virusSprite != null && virusSprite.intersects(newWall)) {
                    virusSprite = null;
                }
                repaint(graphicsContext);
            }
        });
    }

    private void repaint(GraphicsContext graphicsContext) {
        graphicsContext.setFill(VOID_COLOR);
        graphicsContext.fillRect(0, 0, paintCanvas.getWidth(), paintCanvas.getHeight());
        graphicsContext.setFill(WALL_COLOR);
        wallSprites.forEach(sprite -> graphicsContext.fillRect(sprite.getBoundary().getMinX(), sprite.getBoundary().getMinY(),
                sprite.getBoundary().getWidth(), sprite.getBoundary().getHeight()));
        graphicsContext.setFill(NPC_COLOR);
        npcSprites.forEach(sprite -> graphicsContext.fillRect(sprite.getBoundary().getMinX(), sprite.getBoundary().getMinY(),
                sprite.getBoundary().getWidth(), sprite.getBoundary().getHeight()));
        if (virusSprite != null) {
            graphicsContext.setFill(VIRUS_COLOR);
            graphicsContext.fillRect(virusSprite.getBoundary().getMinX(), virusSprite.getBoundary().getMinY(),
                    virusSprite.getBoundary().getWidth(), virusSprite.getBoundary().getHeight());
        }
    }

    void adjustCoords(double shapeStartingX,
                      double shapeStartingY,
                      double shapeEndingX,
                      double shapeEndingY,
                      Rectangle shape) {
        shape.setX(shapeStartingX);
        shape.setY(shapeStartingY);
        shape.setWidth(shapeEndingX - shapeStartingX);
        shape.setHeight(shapeEndingY - shapeStartingY);

        if (shape.getWidth() < 0) {
            shape.setWidth(-shape.getWidth());
            shape.setX(shape.getX() - shape.getWidth());
        }

        if (shape.getHeight() < 0) {
            shape.setHeight(-shape.getHeight());
            shape.setY(shape.getY() - shape.getHeight());
        }
    }
}
