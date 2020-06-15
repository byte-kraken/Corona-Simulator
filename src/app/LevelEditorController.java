package app;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class LevelEditorController extends Controller {
    private static final Color WALL_COLOR = Color.BLACK;
    private static final Color VOID_COLOR = Color.WHITE;
    private static final Color VIRUS_COLOR = Color.GREEN;
    private static final Color NPC_COLOR = Color.RED;
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
    private Color color = WALL_COLOR;

    public LevelEditorController() {
        super();
    }

    public void initialize() {
        toMainMenuButton.setOnAction(e -> returnToPreviousScene());
        wallColorButton.setOnAction(e -> color = WALL_COLOR);
        npcColorButton.setOnAction(e -> color = NPC_COLOR);
        voidColorButton.setOnAction(e -> color = VOID_COLOR);
        virusColorButton.setOnAction(e -> color = VIRUS_COLOR);
        saveButton.setOnAction(e -> exportPicture());
    }

    public void armCanvas() {
        final GraphicsContext graphicsContext = paintCanvas.getGraphicsContext2D();
        //initializes empty map
        graphicsContext.setFill(VOID_COLOR);
        graphicsContext.fillRect(0, 0, paintCanvas.getWidth(), paintCanvas.getHeight());
        //initializes stroke
        graphicsContext.setFill(color);
        graphicsContext.setStroke(color);
        graphicsContext.setLineWidth(1);

        paintCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            graphicsContext.setFill(color);
            graphicsContext.setStroke(color);
            graphicsContext.beginPath();
            graphicsContext.moveTo(mouseEvent.getX(), mouseEvent.getY());
            graphicsContext.stroke();
        });
        paintCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            graphicsContext.lineTo(mouseEvent.getX(), mouseEvent.getY());
            graphicsContext.stroke();
        });
    }


    private void exportPicture() {

    }
}
