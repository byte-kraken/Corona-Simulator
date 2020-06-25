package app;

import app.model.Model;
import app.util.UtilMethods;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Superclass for the different controllers branching from main controller {@link MainMenuController} such as
 * {@link MultiPlayerController} and {@link SinglePlayerMainController}.
 * <p>
 * FXML elements cannot be generalised, sadly.
 */
class Controller {

    private Stage primaryStage;
    private Scene previousScene;
    Model globalModel ;

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    Stage getPrimaryStage() {
        return primaryStage;
    }

    void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    void setGlobalModel(Model globalModel) {
        this.globalModel = globalModel;
    }

    void returnToPreviousScene() {
        primaryStage.setScene(previousScene);
        UtilMethods.adaptStageSizeToScreenResolution(primaryStage);
    }
}
