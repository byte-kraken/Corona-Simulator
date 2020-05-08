package app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MultiPlayerController extends Controller {

    @FXML
    private Button toMainMenuButton;


    public void initialize() {
        toMainMenuButton.setOnAction(e -> primaryStage.setScene(previousScene));
    }
}
