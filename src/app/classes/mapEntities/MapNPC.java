package app.classes.mapEntities;

import app.classes.NPCType;
import app.constants.Constants;

/**
 * The {@link MapSprite} representing an {@link app.classes.gameEntitys.NPC}.
 */
public class MapNPC extends MapSprite {

    public MapNPC(double xStartPoint, double yStartPoint) {
        this(xStartPoint, yStartPoint, NPCType.Normal);
    }

    public MapNPC(double xStartPoint, double yStartPoint, double width, double height) {
        this(xStartPoint, yStartPoint, width, height, NPCType.Normal);
    }

    public MapNPC(double xStartPoint, double yStartPoint, NPCType type) {
        this(xStartPoint, yStartPoint, Constants.PIXEL_SIZE, Constants.PIXEL_SIZE, type);
    }

    public MapNPC(double xStartPoint, double yStartPoint, double width, double height, NPCType type) {
        super(xStartPoint, yStartPoint, width, height, type.getColor());
    }

}
