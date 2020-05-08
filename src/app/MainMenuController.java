package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import app.model.Model;

import java.io.IOException;

public class MainMenuController {

    private Model model;

    //private Fields can be read by FXML-File if annotated like this
    @FXML
    private Button singlePlayerButton;
    @FXML
    private Button worldBuilderButton;
    @FXML
    private Button multiplayerButton;
    @FXML
    private Button exitButton;

    public MainMenuController() { //loaded on startup
        model = new Model();
    }

    @FXML //loaded on startup
    private void initialize() {

    }

    void setSinglePlayerStage(Stage primaryStage) {
        singlePlayerButton.setOnAction(event -> {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("singlePlayer.fxml"));
            final Stage singlePlayerStage = new Stage();
            //singlePlayerStage.initModality(Modality.APPLICATION_MODAL);
            Parent root;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException("We are fucked.", e);
            }
            SinglePlayerController singleplayerController = loader.getController();
            singleplayerController.setPrimaryStage(primaryStage);
            singleplayerController.setSinglePlayerStage(singlePlayerStage);
            singlePlayerStage.setTitle("Single Player");
            singlePlayerStage.setScene(new Scene(root));
            singlePlayerStage.show();
            primaryStage.hide();
            //TODO: Only use one stage and various scenes?
            //TODO: Level-Select for SinglePlayer
            //TODO: Resources Folder where own levels can be saved and ours can be loaded from
        });
        exitButton.setOnAction(event -> primaryStage.close());
    }

    public void setMultiPlayerStage(Stage primaryStage) {
        //TODO
    }

    public void setLevelEditorStage(Stage primaryStage) {
        //TODO
    }
}
