package app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SinglePlayerMainController extends Controller {

    @FXML
    private Label nrInfectedLabel;
    @FXML
    private Button infectButton;
    @FXML
    private Button toMainMenuButton;


    public void initialize() {
        infectButton.setOnAction(e -> {
            int infected = Integer.parseInt(nrInfectedLabel.getText());
            nrInfectedLabel.setText(String.valueOf(++infected));
        });
        toMainMenuButton.setOnAction(e -> primaryStage.setScene(previousScene));
    }
}
