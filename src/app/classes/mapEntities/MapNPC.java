package app.classes.mapEntities;

import app.constants.Constants;
import javafx.scene.paint.Color;

public abstract class MapNPC extends MapSprite {

    public MapNPC(double xStartPoint, double yStartPoint, Color color) {
        super(xStartPoint, yStartPoint, Constants.PIXEL_SIZE, Constants.PIXEL_SIZE, color);
    }
}
