package app;

import app.classes.gameEntitys.NPC;
import app.classes.gameEntitys.Wall;
import app.exceptions.KeyEventhandlerNotSetException;
import app.exceptions.ModelNotSetException;
import app.exceptions.StartScreenNotInitializedException;
import app.model.SinglePlayerModel;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import static app.util.Constants.STANDARD_MAP_SIZE_X;
import static app.util.Constants.STANDARD_MAP_SIZE_Y;

/**
 * Controller {@link Controller} for the SinglePlayerMode.
 * <p>
 * Controls the {@link app.classes.gameEntitys.PlayerChar}, {@link NPC} and the main logic.
 */
public class SinglePlayerMainController extends Controller {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Label nrInfectedLabel;
    @FXML
    private Button toMainMenuButton;
    @FXML
    private Canvas gameCanvas;

    // Input Buffer
    private ArrayList<String> input;

    private SinglePlayerModel siPModel;
    private boolean modelSet = false;

    private boolean keyEventHandlerSet = false;

    private boolean startScreenInitialized = false;

    private boolean gameStarted = false;


    public SinglePlayerMainController() {
        super();
    }

    public void initialize() {
        assert mainAnchorPane != null : "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'singlePlayerMainUI.fxml'.";
        assert nrInfectedLabel != null : "fx:id=\"nrInfectedLabel\" was not injected: check your FXML file 'singlePlayerMainUI.fxml'.";
        assert toMainMenuButton != null : "fx:id=\"toMainMenuButton\" was not injected: check your FXML file 'singlePlayerMainUI.fxml'.";
        assert gameCanvas != null : "fx:id=\"gameCanvas\" was not injected: check your FXML file 'singlePlayerMainUI.fxml'.";

        // Set Size of Canvas
        mainAnchorPane.setMinSize(STANDARD_MAP_SIZE_X, STANDARD_MAP_SIZE_Y);
        mainAnchorPane.setMaxSize(STANDARD_MAP_SIZE_X, STANDARD_MAP_SIZE_Y);

        gameCanvas.setWidth(STANDARD_MAP_SIZE_X);
        gameCanvas.setHeight(STANDARD_MAP_SIZE_Y);

        // Regular fields initialization
        input = new ArrayList<>();

        // Action Handlers
        toMainMenuButton.setOnAction(e -> returnToPreviousScene());
        toMainMenuButton.setFocusTraversable(false); //needs to be set for all focusable elements in the scene, otherwise key detection does not work


        //Action Handlers END
    }

    public void setKeyEventHandler() {
        getPrimaryStage().getScene().setOnKeyPressed(keyEvent -> {
            String code = keyEvent.getCode().toString();
            if (!input.contains(code))
                input.add(code);
        });

        getPrimaryStage().getScene().setOnKeyReleased(keyEvent -> {
            String code = keyEvent.getCode().toString();
            input.remove(code);
        });

        getPrimaryStage().getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.SPACE) {
                if (!gameStarted) {
                    startGame();
                }
            }
        });

        keyEventHandlerSet = true;
    }


    public void setSinglePlayerModel(SinglePlayerModel singlePlayerModel) {
        this.siPModel = singlePlayerModel;
        modelSet = true;
    }

    public void initStartScreen() {
        if (!keyEventHandlerSet) {
            throw new KeyEventhandlerNotSetException();
        }

        if (!modelSet) {
            throw new ModelNotSetException();
        }
        // set Full Screen
        getPrimaryStage().setFullScreen(true);

        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
        gc.setFont(theFont);
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        gc.fillText("Press Space to start the game", (gameCanvas.getWidth() / 2) - 100, (gameCanvas.getHeight() / 2) - 50);

        startScreenInitialized = true;
    }

    /**
     * Main game method. Starts the Game Loop.
     * <p>
     * Can only be started after {@link #initStartScreen()} has been called before}
     */
    public void startGame() {
        if (!startScreenInitialized) {
            throw new StartScreenNotInitializedException();
        }

        gameStarted = true;
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());


        Iterator<NPC> npcSetVeloIter = siPModel.getNPC_Iterator();
        while (npcSetVeloIter.hasNext()) {
            NPC npc = npcSetVeloIter.next();
            npc.setVelocity(ThreadLocalRandom.current().nextDouble(-100, 100)
                    , ThreadLocalRandom.current().nextDouble(-100, 100));
            //npc.setVelocity(0,-10);
        }


        final Long[] lastNanoTime = {System.nanoTime()};

        new AnimationTimer() {

            @Override
            public void handle(long currentNanoTime) {
                double elapsedTime = (currentNanoTime - lastNanoTime[0]) / 1000000000.0;
                lastNanoTime[0] = currentNanoTime;

                Iterator<NPC> npcIterator;
                Iterator<Wall> wallIter;

                //Process Input Start
                //Player Movement Start
                applyPlayerInputs();
                //Player Movement End
                //NPC Movement Start

                npcIterator = siPModel.getNPC_Iterator();
                while (npcIterator.hasNext()) {
                    NPC npc = npcIterator.next();
                    npc.update(elapsedTime);
                }

                //NPC Movement END


                //Process Input End
                //Update Game Start
                siPModel.getPlayer().update(elapsedTime);

                //Collision Detection Start


                //TODO Collision Detection Properly
                wallIter = siPModel.getWall_Iterator();
                while (wallIter.hasNext()) {
                    Wall wall = wallIter.next();
                    if (wall.intersects(siPModel.getPlayer())) {
                        siPModel.getPlayer().wallCollision(elapsedTime);
                    }
                }

                npcIterator = siPModel.getNPC_Iterator();
                while (npcIterator.hasNext()) {
                    NPC npc = npcIterator.next();
                    wallIter = siPModel.getWall_Iterator();
                    while (wallIter.hasNext()) {
                        Wall wall = wallIter.next();
                        if (npc.intersects(wall)) {
                            npc.wallCollision(elapsedTime, wall);
                        }
                    }
                    npc.update(elapsedTime);
                }


                npcIterator = siPModel.getNPC_Iterator();
                while (npcIterator.hasNext()) {
                    NPC npc = npcIterator.next();
                    npc.render(gc);
                }


                //Collision Detection End
                //Update Game End
                //Render Start
                gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

                siPModel.getPlayer().render(gc);


                //drawDebugGrid(gc);
                wallIter = siPModel.getWall_Iterator();
                while (wallIter.hasNext()) {
                    Wall wall = wallIter.next();
                    wall.render(gc);
//                    System.out.println(wall.toString());
                }
                npcIterator = siPModel.getNPC_Iterator();
                while (npcIterator.hasNext()) {
                    NPC npc = npcIterator.next();
                    npc.render(gc);
                }
                //Render End

            }
        }.start();

    }

    /**
     * Checks if player pressed key and adds velocity to Player model accordingly
     */
    private void applyPlayerInputs() {
        siPModel.getPlayer().setVelocity(0, 0);
        int playerSpeed = 200;
        if (input.contains("CONTROL")) {
            playerSpeed = 400;
        }

        if (input.contains("LEFT"))
            siPModel.getPlayer().addVelocity(-playerSpeed, 0);
        if (input.contains("RIGHT"))
            siPModel.getPlayer().addVelocity(playerSpeed, 0);
        if (input.contains("UP"))
            siPModel.getPlayer().addVelocity(0, -playerSpeed);
        if (input.contains("DOWN"))
            siPModel.getPlayer().addVelocity(0, playerSpeed);
    }


    private void drawDebugGrid(GraphicsContext gc) {
        for (int x = 0; x < STANDARD_MAP_SIZE_X; x += 100) {
            gc.fillText(Integer.toString(x), x, 50);
            gc.strokeLine(x, 0, x, STANDARD_MAP_SIZE_Y);
        }

        for (int y = 0; y < STANDARD_MAP_SIZE_Y; y += 100) {
            gc.fillText(Integer.toString(y), 10, y);
            gc.strokeLine(0, y, STANDARD_MAP_SIZE_X, y);
        }
    }
}
