package app.classes.mapEntities;

import app.util.Constants;
import app.util.SerializableColor;
import javafx.scene.paint.Color;

/**
 * The {@link MapSprite} representing a {@link app.classes.gameEntitys.PlayerChar}.
 */
public class MapPlayerChar extends MapSprite {

    public static final SerializableColor COLOR = new SerializableColor(Color.GREEN);

    public MapPlayerChar(double xStartPoint, double yStartPoint) {
        this(xStartPoint, yStartPoint, COLOR);
    }

    public MapPlayerChar(double xStartPoint, double yStartPoint, SerializableColor color) {
        super(xStartPoint, yStartPoint, Constants.PIXEL_SIZE, Constants.PIXEL_SIZE, color);
    }

    public MapPlayerChar(double xStartPoint, double yStartPoint, double width, double height) {
        this(xStartPoint, yStartPoint, width, height, COLOR);
    }

    public MapPlayerChar(double xStartPoint, double yStartPoint, double width, double height, SerializableColor color) {
        super(xStartPoint, yStartPoint, width, height, color);
    }
}
