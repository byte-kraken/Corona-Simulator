package app;

import app.classes.gameEntitys.NPC;
import app.classes.gameEntitys.Wall;
import app.exceptions.KeyEventhandlerNotSetException;
import app.exceptions.ModelNotSetException;
import app.model.SinglePlayerModel;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
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

/**
 * Controller {@link Controller} for the SinglePlayerMode.
 * <p>
 * Controls the virus, NPCs and the main logic.
 */
public class SinglePlayerMainController extends Controller {
    /**
     * FXMl Fields
     */

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

    /**
     * FXM Fields End
     */
    /**
     * Regular fields
     */

    /**
     * Input Buffer
     */
    private ArrayList<String> input;

    private SinglePlayerModel siPModel;
    private boolean modelSet = false;

    private boolean keyEventHandlerSet = false;

    private boolean gameStarted = false;


    public SinglePlayerMainController() {
        super();
    }

    public void initialize() {

        /**
         * Assertions
         */

        assert mainAnchorPane != null : "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'singlePlayerMainUI.fxml'.";
        assert nrInfectedLabel != null : "fx:id=\"nrInfectedLabel\" was not injected: check your FXML file 'singlePlayerMainUI.fxml'.";
        assert toMainMenuButton != null : "fx:id=\"toMainMenuButton\" was not injected: check your FXML file 'singlePlayerMainUI.fxml'.";
        assert gameCanvas != null : "fx:id=\"gameCanvas\" was not injected: check your FXML file 'singlePlayerMainUI.fxml'.";

        /**
         * Assertions End
         */
        /**
         * Regular fields initialization
         */

        input = new ArrayList<String>();

        /**
         * Regular fields initialization End
         */

        /**
         * Action Handlers
         */
        toMainMenuButton.setOnAction(e -> returnToPreviousScene());
        toMainMenuButton.setFocusTraversable(false); //needs to be set for all focusable elements in the scene, otherwise key detection does not work


        /**
         * Action Handlers END
         */
    }

    public void setKeyEventHandler() {
        getPrimaryStage().getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String code = keyEvent.getCode().toString();
                if (!input.contains(code))
                    input.add(code);
            }
        });


        getPrimaryStage().getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String code = keyEvent.getCode().toString();
                input.remove(code);
            }
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
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
        gc.setFont(theFont);
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        gc.fillText("Press Space to start the game", (gameCanvas.getWidth() / 2) - 100, (gameCanvas.getHeight() / 2) - 50);


    }


    public void startGame() throws ModelNotSetException, KeyEventhandlerNotSetException {

        gameStarted = true;
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        final Long[] lastNanoTime = {System.nanoTime()};

        new AnimationTimer() {

            @Override
            public void handle(long currentNanoTime) {
                double elapsedTime = (currentNanoTime - lastNanoTime[0]) / 1000000000.0;
                lastNanoTime[0] = currentNanoTime;

                siPModel.getPlayer().setVelocity(0, 0);
                if (input.contains("LEFT"))
                    siPModel.getPlayer().addVelocity(-100, 0);
                if (input.contains("RIGHT"))
                    siPModel.getPlayer().addVelocity(100, 0);
                if (input.contains("UP"))
                    siPModel.getPlayer().addVelocity(0, -100);
                if (input.contains("DOWN"))
                    siPModel.getPlayer().addVelocity(0, 100);

                siPModel.getPlayer().update(elapsedTime);
                //TODO Collision Detection Properly
                Iterator<Wall> wallIter = siPModel.getWall_Iterator();
                while (wallIter.hasNext()) {
                    Wall wall = wallIter.next();
                    if (wall.intersects(siPModel.getPlayer())) {
                        siPModel.getPlayer().wallCollision(elapsedTime);
                    }
//                    System.out.println(wall.toString());
                }


                //TODO END
                gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

                siPModel.getPlayer().render(gc);

                gc.fillText("100", 100, 100);
                gc.fillText("200", 200, 200);
                gc.fillText("500", 500, 500);
                gc.fillText("500x", 500, 100);
                gc.fillText("500y", 100, 500);
                gc.fillText("1000", 1000, 1000);
                gc.fillText("1000x", 1000, 100);
                gc.fillText("1000x", 100, 1000);


                wallIter = siPModel.getWall_Iterator();
                while (wallIter.hasNext()) {
                    Wall wall = wallIter.next();
                    wall.render(gc);
//                    System.out.println(wall.toString());
                }

                Iterator<NPC> npcIterator = siPModel.getNPC_Iterator();
                while (npcIterator.hasNext()) {
                    NPC npc = npcIterator.next();
                    npc.render(gc);
//
                }


            }
        }.start();

    }
}
