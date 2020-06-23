package app.classes.mapEntities;

import app.constants.Constants;
import javafx.scene.paint.Color;

/**
 * The {@link MapSprite} representing a {@link app.classes.gameEntitys.PlayerChar}.
 */
public class MapPlayerChar extends MapSprite {

    public static final Color COLOR = Color.GREEN;

    public MapPlayerChar(double xStartPoint, double yStartPoint) {
        this(xStartPoint, yStartPoint, COLOR);
    }

    public MapPlayerChar(double xStartPoint, double yStartPoint, Color color) {
        super(xStartPoint, yStartPoint, Constants.PIXEL_SIZE, Constants.PIXEL_SIZE, color);
    }

    public MapPlayerChar(double xStartPoint, double yStartPoint, double width, double height) {
        this(xStartPoint, yStartPoint, width, height, COLOR);
    }

    public MapPlayerChar(double xStartPoint, double yStartPoint, double width, double height, Color color) {
        super(xStartPoint, yStartPoint, width, height, color);
    }
}
