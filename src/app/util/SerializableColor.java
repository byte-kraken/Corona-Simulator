package app.util;

import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * Helper class to make Color serializable.
 * Adapted from {@see https://stackoverflow.com/questions/36748358/saving-color-as-state-in-a-javafx-application}
 */
public class SerializableColor implements Serializable {
    private static final long serialVersionUID = -8099385608150167599L;

    private final double red;
    private final double green;
    private final double blue;
    private final double alpha;

    public SerializableColor(Color color) {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.alpha = color.getOpacity();
    }

    public Color getFXColor() {
        return new Color(red, green, blue, alpha);
    }
}