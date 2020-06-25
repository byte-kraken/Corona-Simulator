package app;

import app.model.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controls the Main Menu and initializes the model.
 * <p>
 * From here, all other scenes are created with their respective controllers and appearances.
 */
public class MainMenuController {

    private static final Model model = new Model();

    //private Fields can be read by FXML-File if annotated like this
    @FXML
    private Button singlePlayerButton;
    @FXML
    private Button worldBuilderButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button heading;
    @FXML
    private GridPane gridPane;
    @FXML
    private VBox vBox;
    @FXML
    private HBox hBox;

    public MainMenuController() { //loaded from FXMLLoader
    }

    void armMainMenuButtons(Stage primaryStage) {
        gridPane.setStyle("-fx-background-color: black");
        vBox.setStyle("-fx-background-color: black");
        hBox.setStyle("-fx-background-color: black");
        singlePlayerButton.setStyle("-fx-background-color: transparent");
        worldBuilderButton.setStyle("-fx-background-color: transparent");
        exitButton.setStyle("-fx-background-color: transparent");
        heading.setStyle("-fx-background-color: transparent");
        singlePlayerButton.setTooltip(new Tooltip("Click to Play"));
        worldBuilderButton.setTooltip(new Tooltip("Worldbuilder"));
        exitButton.setTooltip(new Tooltip("Exit"));
        singlePlayerButton.setGraphic(new ImageView(new Image("file:imgs/Play.png", 400, 400, false, false)));
        worldBuilderButton.setGraphic(new ImageView(new Image("file:imgs/Globe.png", 400, 400, false, false)));
        exitButton.setGraphic(new ImageView(new Image("file:imgs/Exit.png", 300, 400, false, false)));
        heading.setGraphic(new ImageView(new Image("file:imgs/Heading.png", 1500, 200, false, false)));
        setSinglePlayerMainScene(primaryStage);
        setWorldBuilderScene(primaryStage);

        exitButton.setOnAction(event -> closeStage(primaryStage));

        primaryStage.getScene().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                closeStage(primaryStage);
            }
        });
    }

    @FXML
    private void initialize() { //loaded from FXMLLoader

    }

    /**
     * Loads the SinglePlayerButton and initializes the singlePlayerScene using {@link SinglePlayerMainController}.
     */
    private void setSinglePlayerMainScene(Stage primaryStage) {
        singlePlayerButton.setOnAction(event -> {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("LevelUI.fxml"));
            loadScene(loader, primaryStage);
            primaryStage.setTitle("Level select ");
            primaryStage.setFullScreen(true);
            LevelController controller = loader.getController();
            controller.armButtons();
        });
    }

    /**
     * Loads the levelEditorButton and initializes the levelEditorScene using {@link WorldBuilderController}.
     */
    private void setWorldBuilderScene(Stage primaryStage) {
        worldBuilderButton.setOnAction(event -> {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("worldBuilder.fxml"));
            loadScene(loader, primaryStage);
            primaryStage.setTitle("World Builder");
            primaryStage.setFullScreen(true);
            WorldBuilderController controller = loader.getController();
            controller.armCanvas(); //canvas has to be loaded after scene was created
        });
    }

    /**
     * Does everything a normal constructor would do.
     * <p>
     * Helper method for creating various scenes and passing required parameters.
     */
    static void loadScene(FXMLLoader loader, Stage primaryStage) {
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML-File.", e);
        }
        Controller controller = loader.getController();
        controller.setGlobalModel(model);
        controller.setPrimaryStage(primaryStage);
        Scene previousScene = primaryStage.getScene();
        controller.setPreviousScene(previousScene);
        primaryStage.setScene(new Scene(root));
    }

    private void closeStage(Stage primaryStage) {
        primaryStage.close();
    }
}
