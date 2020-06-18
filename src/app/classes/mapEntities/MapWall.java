package app.classes.mapEntities;

import javafx.scene.paint.Color;

public class MapWall extends MapSprite {

    public static final Color COLOR = Color.BLACK;

    public MapWall(double positionX, double positionY, double width, double height) {
        super(positionX, positionY, width, height, COLOR);
    }
}
