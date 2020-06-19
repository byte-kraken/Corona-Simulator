package app.classes.mapEntities;

import app.constants.Constants;
import javafx.scene.paint.Color;

public class MapNPC extends MapSprite {

    public MapNPC(double xStartPoint, double yStartPoint, NpcType type) {
        super(xStartPoint, yStartPoint, Constants.PIXEL_SIZE, Constants.PIXEL_SIZE, type.color);
    }

    public enum NpcType {
        Normal("Normal", Color.RED),
        ConspiracyTheorist("Conspiracy Theorist", Color.DARKRED);

        private final String name;
        private final Color color;

        NpcType(String name, Color color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public Color getColor() {
            return color;
        }
    }
}
