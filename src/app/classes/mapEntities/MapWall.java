package app.classes.mapEntities;

import javafx.scene.paint.Color;

/**
 * The {@link MapSprite} representing a {@link app.classes.gameEntitys.Wall}.
 */
public class MapWall extends MapSprite {

    public static final Color COLOR = Color.BLACK;

    public MapWall(double positionX, double positionY, double width, double height) {
        this(positionX, positionY, width, height, COLOR);
    }

    public MapWall(double positionX, double positionY, double width, double height, Color color) {
        super(positionX, positionY, width, height, color);
    }

    @Override
    public String toString() {
        return "Wall with:" +
                super.toString() +
                "and Width " + getWidth() + "Height: " + getHeight();
    }
}
