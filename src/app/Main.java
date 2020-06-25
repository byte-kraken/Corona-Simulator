package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * The main class of the application.
 */
public class Main extends Application {

    /**
     * Loads constructor of Controller defined in .fxml-file and runs initialize().
     *
     * @param primaryStage: Root stage loaded from {@link Application}. Other stages may be set on top of it.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenuUI.fxml"));
        final Parent root = loader.load();
        MainMenuController mainMenuController = loader.getController();
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root, primaryStage.getWidth(), primaryStage.getHeight()));
        primaryStage.setFullScreen(true);
        primaryStage.show();
        // Controller cannot use parameters because load uses empty constructor. Scenes have to be loaded in afterwards, sadly.
        mainMenuController.armMainMenuButtons(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
