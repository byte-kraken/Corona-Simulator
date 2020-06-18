package app.classes.mapEntities;

import app.constants.Constants;
import javafx.scene.paint.Color;

import java.util.function.Function;

public abstract class MapNPC extends MapSprite {

    public MapNPC(double xStartPoint, double yStartPoint, Color color) {
        super(xStartPoint, yStartPoint, Constants.PIXEL_SIZE, Constants.PIXEL_SIZE, color);
    }

    public enum npcTypes {
        Normal("Normal", o -> new MapNpcNormal()),
        ConspiracyTheorist("Conspiracy Theorist", o -> new MapNpcConspiracyTheorist());

        private final String name;
        private final Function<Object, MapNPC> type;

        npcTypes(String name, Function<Object, MapNPC> type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Function<Object, MapNPC> getType() {
            return type;
        }
    }
}
