package app.util;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UtilMethods {
    public static void adaptStageSizeToScreenResolution(Stage primaryStage) {
        boolean found = false;
        for (Screen s : Screen.getScreens()) {
            Rectangle2D bounds = s.getBounds();
            if (bounds.getWidth() == 1920 && bounds.getHeight() == 1080) {
                primaryStage.setX(bounds.getMinX());
                primaryStage.setY(bounds.getMinY());
                primaryStage.setMaxHeight(bounds.getHeight());
                primaryStage.setMaxWidth(bounds.getWidth());
                primaryStage.setFullScreen(true);
                found = true;
                break;
            }
        }
        if (!found) {
            Rectangle2D bounds = Screen.getPrimary().getBounds();
            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setMaxWidth(1920);
            primaryStage.setMaxHeight(1080);
            primaryStage.setResizable(false);
        }
    }
}
