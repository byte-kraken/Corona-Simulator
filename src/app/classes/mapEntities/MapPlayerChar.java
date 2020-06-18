package app.classes.mapEntities;

import app.constants.Constants;
import javafx.scene.paint.Color;

public class MapPlayerChar extends MapSprite {

    public static final Color COLOR = Color.GREEN;

    public MapPlayerChar(double xStartPoint, double yStartPoint) {
        super(xStartPoint, yStartPoint, Constants.PIXEL_SIZE, Constants.PIXEL_SIZE, COLOR);
    }
}
