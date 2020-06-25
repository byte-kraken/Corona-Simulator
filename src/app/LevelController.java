package app;

import app.classes.Map;
import app.model.SinglePlayerModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
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
    private ScrollPane ownWorlds;
    @FXML
    private ScrollPane levels;
    @FXML
    private Button backToMainMenu;

    private final ArrayList<Button> ownWorldButtons = new ArrayList<>();
    private final ArrayList<Button> levelButtons = new ArrayList<>();


    public LevelController() {
        super();
    }

    public void initialize() {
        File folderLevels = new File(LEVELS_FOLDER_PATH);
        createButtons(folderLevels, levelButtons, levels);
        File folderOwnWorlds = new File(OWN_WORLDS_FOLDER_PATH);
        createButtons(folderOwnWorlds, ownWorldButtons, ownWorlds);
        backToMainMenu.setOnAction(e -> returnToPreviousScene());
        backToMainMenu.setFocusTraversable(false);
    }

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

    private void loadStage(String path) {
        final Stage stage = getPrimaryStage();
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("singlePlayerMainUI.fxml"));
        MainMenuController.loadScene(loader, stage);
        stage.setTitle("Corona Simulator");
        SinglePlayerMainController controller = loader.getController();
        SinglePlayerModel standardTestModel = new SinglePlayerModel();
        try {
            standardTestModel.loadEntitiesFromMapInModel(Map.deserialize(path));
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }
        controller.setKeyEventHandler();
        controller.setSinglePlayerModel(standardTestModel);
        controller.initStartScreen();
    }


    private void createButtons(File folder, ArrayList<Button> arrayList, ScrollPane worlds) {
        GridPane buttonGridPane = new GridPane();
        int i = 0;
        Random rand = new Random();
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            String worldName = file.getName();
            Button ownWorldButton = new Button();
            ownWorldButton.setText(worldName);
            String color = "-fx-background-color: rgb(" + rand.nextInt(255) + "," + rand.nextInt(255) + "," + rand.nextInt(255) + ")";
            ownWorldButton.setStyle(color);
            arrayList.add(ownWorldButton);
            buttonGridPane.add(ownWorldButton, i % 2, i / 2);
            i++;
        }
        worlds.contentProperty().set(buttonGridPane);
        worlds.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
}
