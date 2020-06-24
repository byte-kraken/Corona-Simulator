package app.classes;

import app.util.SerializableColor;
import javafx.scene.paint.Color;

/**
 * The different types of {@link app.classes.gameEntitys.NPC} there are.
 * Each has a different name, a color and some special modification.
 */
public enum NPCType {
    Normal("Normal", Color.RED),
    ConspiracyTheorist("Conspiracy Theorist", Color.DARKRED);

    private final String name;
    private final Color color;

    NPCType(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public SerializableColor getColor() {
        return new SerializableColor(color);
    }
}
