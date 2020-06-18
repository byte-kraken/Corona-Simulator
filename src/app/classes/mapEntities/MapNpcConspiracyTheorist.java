package app.classes.mapEntities;

import javafx.scene.paint.Color;

public class MapNpcConspiracyTheorist extends MapNPC {
    public static final Color COLOR = Color.DARKRED;

    public MapNpcConspiracyTheorist(double xStartPoint, double yStartPoint) {
        super(xStartPoint, yStartPoint, COLOR);
    }
}
