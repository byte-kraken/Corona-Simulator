package app.classes.mapEntities;

import javafx.scene.paint.Color;

public class MapNpcNormal extends MapNPC {
    public static final Color COLOR = Color.RED;

    public MapNpcNormal(double xStartPoint, double yStartPoint) {
        super(xStartPoint, yStartPoint, COLOR);
    }

    public MapNpcNormal() {
        this(-1, -1);
    }
}
