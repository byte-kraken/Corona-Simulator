package app;

import app.classes.Map;
import app.model.Model;
import app.model.SinglePlayerModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controls the Main Menu and initializes the model.
 * <p>
 * From here, all other scenes are created with their respective controllers and appearances.
 */
public class MainMenuController {

    private final Model model;

    //private Fields can be read by FXML-File if annotated like this
    @FXML
    private Button singlePlayerButton;
    @FXML
    private Button worldBuilderButton;
    @FXML
    private Button multiPlayerButton;
    @FXML
    private Button exitButton;


    public MainMenuController() { //loaded from FXMLLoader
        model = new Model();
    }

    void armMainMenuButtons(Stage primaryStage) {
        setSinglePlayerMainScene(primaryStage);
        //setMultiPlayerScene(primaryStage);
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
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("singlePlayerMainUI.fxml"));
            loadScene(loader, primaryStage);
            primaryStage.setTitle("Corona Simulator");
            SinglePlayerMainController controller = loader.getController();
            SinglePlayerModel standardTestModel = new SinglePlayerModel();
            standardTestModel.loadEntitiesFromMapInModel(Map.getStandardTestMap2());
            controller.setKeyEventHandler();
            controller.setSinglePlayerModel(standardTestModel);
            controller.initStartScreen();


        });
    }

    /**
     * Loads the multiPlayerButton and initializes the multiPlayerScene using {@link MultiPlayerController}.
     */
    private void setMultiPlayerScene(Stage primaryStage) {
        multiPlayerButton.setOnAction(event -> {
            //TODO: Implement multiPlayerUI.fxml
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("multiPlayerUI.fxml"));
            loadScene(loader, primaryStage);
        });

        //TODO: implement
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
        //TODO: Resources Folder where own levels can be saved and own ones can be loaded from
    }

    /**
     * Does everything a normal constructor would do.
     * <p>
     * Helper method for creating various scenes and passing required parameters.
     */
    private void loadScene(FXMLLoader loader, Stage primaryStage) {
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
