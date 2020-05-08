package app;

import app.model.Model;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Superclass for the different controllers branching from main controller {@link MainMenuController} such as
 * {@link MultiPlayerController} and {@link SinglePlayerMainController}.
 * <p>
 * FXML elements cannot be generalised, sadly.
 */
class Controller {

    Stage primaryStage;
    Scene previousScene;
    Model model;

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    void setModel(Model model) {
        this.model = model;
    }
}
