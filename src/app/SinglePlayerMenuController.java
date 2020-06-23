package app;

import app.model.Model;
import app.model.OwnWorldModel;
import app.model.SinglePlayerModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Controls the Main Menu and initializes the model.
 * <p>
 * From here, all other scenes are created with their respective controllers and appearances.
 */
public class SinglePlayerMenuController extends Controller {

    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Button singlePlayerButton;
    @FXML
    private Button multiPlayerButton;
    @FXML
    private Button exitButton;

    /**
     * FXM Fields End
     */
    /**
     * Regular fields
     */

    /**
     * Input Buffer
     */

    private final Model model;


    public SinglePlayerMenuController() {
        super();
        model = new Model();
    }

    public void initialize() {
        singlePlayerButton.setOnAction(event -> {
            final Stage stage = getPrimaryStage();
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("singlePlayerMainUI.fxml"));
            loadScene(loader, stage);
                SinglePlayerMainController controller = loader.getController();
                SinglePlayerModel standardTestModel = new SinglePlayerModel();
                stage.setTitle("Player Menu");
        });
        /*
        multiPlayerButton.setOnAction(event -> {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnWorldUI.fxml"));
            loadScene(loader, primaryStage);
            primaryStage.setTitle("Corona Simulator");
            OwnWorldController controller = loader.getController();
            OwnWorldModel standardTestModel = new OwnWorldModel();
        });
        */

        /**
         * Assertions
         */

        /**
         * Assertions End
         */
        /**
         * Regular fields initialization
         */


        /**
         * Regular fields initialization End
         */

        /**
         * Action Handlers
         */
        exitButton.setOnAction(e -> returnToPreviousScene());
        exitButton.setFocusTraversable(false); //needs to be set for all focusable elements in the scene, otherwise key detection does not work


        /**
         * Action Handlers END
         */
    }
/*
        public void setKeyEventHandler () {
            getPrimaryStage().getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    String code = keyEvent.getCode().toString();
                    if (!input.contains(code))
                        input.add(code);
                }
            });


            getPrimaryStage().getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    String code = keyEvent.getCode().toString();
                    input.remove(code);
                }
            });

            keyEventHandlerSet = true;
        }


        public void setSinglePlayerModel (SinglePlayerMenuModel singlePlayerMenuModel){
            this.siPModel = singlePlayerMenuModel;
            modelSet = true;
        }

        public void initStartScreen () {
            if (!keyEventHandlerSet) {
                throw new KeyEventhandlerNotSetException();
            }

            if (!modelSet) {
                throw new ModelNotSetException();
            }
        }
        */

    private void loadScene(FXMLLoader loader, Stage primaryStage) {
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML-File.", e);
        }
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        Controller controller = loader.getController();
        controller.setGlobalModel(model);
        controller.setPrimaryStage(primaryStage);
        Scene prevScene = primaryStage.getScene();
        controller.setPreviousScene(prevScene);
        primaryStage.setScene(new Scene(root));
    }
}
