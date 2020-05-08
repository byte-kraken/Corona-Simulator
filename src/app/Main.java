package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenuUI.fxml"));
        final Parent root = loader.load();
        MainMenuController mainMenuController = loader.getController();
        // loads Controller constructor defined in .fxml-file and runs initialize()
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        // Controller cannot use parameters because load uses empty constructor. Scenes have to be loaded in afterwards, sadly.
        mainMenuController.loadMainMenuButtons(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
