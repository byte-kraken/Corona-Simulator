package app;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 * Controller {@link Controller} for the SinglePlayerMode.
 * <p>
 * Controls the virus, NPCs and the main logic.
 */
public class SinglePlayerMainController extends Controller {

    private static final double VIRUS_MOVEMENT_ANIMATION_DURATION = 0.25; //determines smoothness of animation when moving
    @FXML
    private Label nrInfectedLabel;
    @FXML
    private Button toMainMenuButton;
    private static final int VIRUS_MOVEMENT_PER_TRIGGER = 5; //determines how many points in x/y coords the virus should move when triggered
    private static final double VIRUS_RADIUS = 50; //determines virus radius
    @FXML
    private Circle virusCircle;

    public SinglePlayerMainController() {
        super();
    }

    public void initialize() {
        toMainMenuButton.setOnAction(e -> returnToPreviousScene());
        toMainMenuButton.setFocusTraversable(false); //needs to be set for all focusable elements in the scene, otherwise key detection does not work
    }

    public void armVirus() { //Virus should maybe be an own class.
        //TODO: initialize proper design in FXML-File
        //TODO: initialize proper starting location based on map
        virusCircle.setCenterX(200); //placeholder
        virusCircle.setCenterY(150); //placeholder
        virusCircle.setRadius(VIRUS_RADIUS); //placeholder
        virusCircle.setFill(Color.GREEN); //placeholder
        virusCircle.setStroke(Color.GREEN); //placeholder

        final TranslateTransition transition = new TranslateTransition(Duration.seconds(VIRUS_MOVEMENT_ANIMATION_DURATION), virusCircle); //responsible for smooth animation
        transition.setOnFinished(actionEvent -> {
            virusCircle.setCenterX(virusCircle.getTranslateX() + virusCircle.getCenterX());
            virusCircle.setCenterY(virusCircle.getTranslateY() + virusCircle.getCenterY());
            virusCircle.setTranslateX(0);
            virusCircle.setTranslateY(0);
        });

        virusCircle.requestFocus(); //required to enable keyPressDetection
        handleVirusKeyPresses(getPrimaryStage().getScene(), virusCircle);

        nrInfectedLabel.setText(String.valueOf(model.getNrInfected())); //placeholder
    }

    private void triggerInfect(Scene currentScene, Circle virus) {
        //TODO: Infection logic
        //placeholder
        if (!model.isInfectionStarted()) {
            System.out.println("Starting infection.");
            model.setInfectionStarted(true);
        }
        model.setNrInfected(model.getNrInfected() + 1);
        nrInfectedLabel.setText(String.valueOf(model.getNrInfected()));
    }

    private void handleVirusKeyPresses(Scene currentScene, Circle virus) {
        currentScene.setOnKeyPressed(actionEvent -> {
            double proposedX, proposedY;
            switch (actionEvent.getCode()) {
                case W:
                case UP:
                    proposedX = virus.getCenterX();
                    proposedY = virus.getCenterY() - VIRUS_MOVEMENT_PER_TRIGGER;
                    if (checkPositionValid(proposedX, proposedY, VIRUS_RADIUS, VIRUS_RADIUS)) {
                        virus.setCenterX(proposedX);
                        virus.setCenterY(proposedY);
                    }
                    break;
                case S:
                case DOWN:
                    proposedX = virus.getCenterX();
                    proposedY = virus.getCenterY() + VIRUS_MOVEMENT_PER_TRIGGER;
                    if (checkPositionValid(proposedX, proposedY, VIRUS_RADIUS, VIRUS_RADIUS)) {
                        virus.setCenterX(proposedX);
                        virus.setCenterY(proposedY);
                    }
                    break;
                case A:
                case LEFT:
                    proposedX = virus.getCenterX() - VIRUS_MOVEMENT_PER_TRIGGER;
                    proposedY = virus.getCenterY();
                    if (checkPositionValid(proposedX, proposedY, VIRUS_RADIUS, VIRUS_RADIUS)) {
                        virus.setCenterX(proposedX);
                        virus.setCenterY(proposedY);
                    }
                    break;
                case D:
                case RIGHT:
                    proposedX = virus.getCenterX() + VIRUS_MOVEMENT_PER_TRIGGER;
                    proposedY = virus.getCenterY();
                    if (checkPositionValid(proposedX, proposedY, VIRUS_RADIUS, VIRUS_RADIUS)) {
                        virus.setCenterX(proposedX);
                        virus.setCenterY(proposedY);
                    }
                    break;
                case SPACE:
                    triggerInfect(currentScene, virus);
                    break;
                case ESCAPE:
                    returnToPreviousScene();
                    break;
            }
        });
    }

    private boolean checkPositionValid(double x, double y, double width, double height) {
        //System.out.format("Current x: %s, y: %s (Radius: %s); Borders width: %s, height: %s\n", x, y, width, primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        return checkPositionInBound(x, y, width, height) && checkPositionNoWall(x, y, width, height);
    }

    private boolean checkPositionInBound(double x, double y, double width, double height) {
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth(); //TODO: Instead of using the screen's dimensions, the playingField's should be used
        double screenHeight = Screen.getPrimary().getVisualBounds().getWidth();
        return (x - width >= 0 && x + width <= screenWidth && y - height >= 0 && y + height <= screenHeight);
    }

    private boolean checkPositionNoWall(double x, double y, double width, double height) {
        return true; //TODO: Read map and create method to check for wall at coords.
    }
}
