package app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller {@link Controller} for the MultiPlayerMode.
 * <p>
 * Replica of {@link SinglePlayerMainController} with additional features?
 */

public class MultiPlayerController extends Controller {

    public MultiPlayerController() {
        super();
    }

    @FXML
    private Button toMainMenuButton;


    public void initialize() {
        toMainMenuButton.setOnAction(e -> returnToPreviousScene());
    }
}
