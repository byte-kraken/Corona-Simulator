package app.classes.mapEntities;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class MapSprite {
    private final double width;
    private final double height;
    private final Color color;
    private double startPositionX;
    private double startPositionY;

    public MapSprite(double x, double y, double width, double height, Color color) {
        startPositionX = x;
        startPositionY = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void setPosition(double x, double y) {
        startPositionX = x;
        startPositionY = y;
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(startPositionX, startPositionY, width, height);
    }

    public boolean intersects(MapSprite s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    public String toString() {
        return " Position: [" + startPositionX + "," + startPositionY + "]";
    }

    public double getStartPositionX() {
        return startPositionX;
    }

    public double getStartPositionY() {
        return startPositionY;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

    public enum SpriteType {
        NPC, WALL, PLAYER_CHAR
    }
}