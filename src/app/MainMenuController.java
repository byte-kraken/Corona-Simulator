package app;

import app.model.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    private final Model model;

    //private Fields can be read by FXML-File if annotated like this
    @FXML
    private Button singlePlayerButton;
    @FXML
    private Button levelEditorButton;
    @FXML
    private Button multiPlayerButton;
    @FXML
    private Button exitButton;


    public MainMenuController() { //loaded on startup
        model = new Model();
    }


    void loadMainMenuButtons(Stage primaryStage) {
        setSinglePlayerMainScene(primaryStage);
        //setMultiPlayerScene(primaryStage);
        //setLevelEditorScene(primaryStage);

        exitButton.setOnAction(event -> primaryStage.close());
    }

    @FXML //loaded on startup
    private void initialize() {

    }

    private void setSinglePlayerMainScene(Stage primaryStage) {
        singlePlayerButton.setOnAction(event -> {
            //TODO: Level-Select for SinglePlayer using JDBC
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("singlePlayerMainUI.fxml"));
            loadScene(loader, primaryStage);
        });
    }

    private void setMultiPlayerScene(Stage primaryStage) {
        multiPlayerButton.setOnAction(event -> {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("multiPlayerUI.fxml"));
            loadScene(loader, primaryStage);
        });

        //TODO: implement
    }

    private void setLevelEditorScene(Stage primaryStage) {
        //TODO: Resources Folder where own levels can be saved and own ones can be loaded from
    }

    private void loadScene(FXMLLoader loader, Stage primaryStage) {
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML-File.", e);
        }
        Controller controller = loader.getController();
        controller.setModel(model);
        controller.setPrimaryStage(primaryStage);
        Scene previousScene = primaryStage.getScene();
        controller.setPreviousScene(previousScene);
        primaryStage.setScene(new Scene(root));
    }
}
