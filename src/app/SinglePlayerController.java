package app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SinglePlayerController {

    private Stage primaryStage;
    private Stage singlePlayerStage;

    @FXML
    private Label nrInfectedLabel;
    @FXML
    private Button toMainMenuButton;
    @FXML
    private Button infectButton;


    public void initialize() {
        infectButton.setOnAction(e -> {
            int infected = Integer.parseInt(nrInfectedLabel.getText());
            nrInfectedLabel.setText(String.valueOf(++infected));
        });
        toMainMenuButton.setOnAction(e -> {
            singlePlayerStage.close();
            primaryStage.show();
        });
    }

    void setSinglePlayerStage(Stage singlePlayerStage) {
        this.singlePlayerStage = singlePlayerStage;
    }

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
