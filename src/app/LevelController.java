package app;

import app.classes.Map;
import app.model.SinglePlayerModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static app.util.Constants.LEVELS_FOLDER_PATH;
import static app.util.Constants.OWN_WORLDS_FOLDER_PATH;

public class LevelController extends Controller {
    @FXML
    private ScrollPane ownWorlds, levels;
    @FXML
    private Button backToMainMenu, levelButton, ownWorldButton;
    @FXML
    private ImageView careerView, sandBoxView;
    @FXML
    private TitledPane level, ownWorld;

    private final ArrayList<Button> ownWorldButtons = new ArrayList<>();
    private final ArrayList<Button> levelButtons = new ArrayList<>();


    public LevelController() {
        super();
    }

    /**
     * Loads and arms all FXML elements, where possible.
     */
    public void initialize() {
        levelButton.setStyle("-fx-background-color: transparent");
        ownWorldButton.setStyle("-fx-background-color: transparent");
        levelButton.setTooltip(new Tooltip("Click to play career mode"));
        ownWorldButton.setTooltip(new Tooltip("Click to play your own Worlds"));
        levelButton.setGraphic(new ImageView(new Image("file:imgs/Career.png", 300, 300, false, false)));
        ownWorldButton.setGraphic(new ImageView(new Image("file:imgs/SandBox.png", 300, 300, false, false)));
        AnchorPane.setLeftAnchor(levelButton, 0.0);
        AnchorPane.setRightAnchor(levelButton, 0.0);
        AnchorPane.setTopAnchor(levelButton, 0.0);
        AnchorPane.setBottomAnchor(levelButton, 0.0);
        AnchorPane.setLeftAnchor(ownWorldButton, 0.0);
        AnchorPane.setRightAnchor(ownWorldButton, 0.0);
        AnchorPane.setTopAnchor(ownWorldButton, 0.0);
        AnchorPane.setBottomAnchor(ownWorldButton, 0.0);
        levelButton.setAlignment(Pos.CENTER);
        ownWorldButton.setAlignment(Pos.CENTER);
        level.setExpanded(false);
        ownWorld.setExpanded(false);
        levelButton.setOnAction(e -> {
            level.setExpanded(true);
            careerView.setImage(new Image("file:imgs/Career.png"));
            levelButton.setVisible(false);
        });
        ownWorldButton.setOnAction(e -> {
            ownWorld.setExpanded(true);
            sandBoxView.setImage(new Image("file:imgs/SandBox.png"));
            ownWorldButton.setVisible(false);
        });

        File folderLevels = new File(LEVELS_FOLDER_PATH);
        createButtons(folderLevels, levelButtons, levels);
        File folderOwnWorlds = new File(OWN_WORLDS_FOLDER_PATH);
        createButtons(folderOwnWorlds, ownWorldButtons, ownWorlds);
        backToMainMenu.setOnAction(e -> returnToPreviousScene());
        backToMainMenu.setFocusTraversable(false);
    }

    /**
     * Is to be called immediately after the constructor.
     * Initializes any options not customizable before {@link #initialize}.
     */
    public void armButtons() {
        levelButtons.forEach(b -> {
            b.setMinSize(850, 230);
            b.setOnAction(e -> loadStage(LEVELS_FOLDER_PATH + b.getText()));
        });
        ownWorldButtons.forEach(b -> {
            b.setMinSize(850, 230);
            b.setOnAction(e -> loadStage(OWN_WORLDS_FOLDER_PATH + b.getText()));
        });
    }

    /**
     * Helper method for creating various scenes and passing required parameters.
     */
    private void loadStage(String path) {
        final Stage stage = getPrimaryStage();
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("singlePlayerMainUI.fxml"));
        MainMenuController.loadScene(loader, stage);
        stage.setTitle("Corona Simulator");
        SinglePlayerMainController controller = loader.getController();
        SinglePlayerModel loadedModel = new SinglePlayerModel();
        try {
            loadedModel.loadEntitiesFromMapInModel(Map.deserialize(path));
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }
        controller.setKeyEventHandler();
        controller.setSinglePlayerModel(loadedModel);
        controller.initStartScreen();
    }

    /**
     * Is to be called immediately after {@link #initialize}.
     * Load buttons dynamically in the scrollPane.
     */
    private void createButtons(File folder, ArrayList<Button> arrayList, ScrollPane worlds) {
        GridPane buttonGridPane = new GridPane();
        Font font = new Font("System", 30);
        int i = 0;
        Random rand = new Random();
        if (folder.listFiles() != null) {
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                String worldName = file.getName();
                Button ownWorldButton = new Button();
                ownWorldButton.setText(worldName);
                ownWorldButton.setFont(font);
                String color = "-fx-background-color: rgb(" + rand.nextInt(255) + "," + rand.nextInt(255) + "," + rand.nextInt(255) + ")";
                ownWorldButton.setStyle(color);
                arrayList.add(ownWorldButton);
                buttonGridPane.add(ownWorldButton, i % 2, i / 2);
                i++;
            }
        }
        worlds.contentProperty().set(buttonGridPane);
        worlds.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
}
