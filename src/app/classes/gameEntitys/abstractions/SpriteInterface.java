package app.classes.gameEntitys.abstractions;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;

public interface SpriteInterface {
    void setPosition(double x, double y);

    void setVelocity(double x, double y);

    void addVelocity(double x, double y);

    void update(double time);

    void render(GraphicsContext gc);

    /**
     * @return a Bounds[] with all boundary Objects of a sprite
     */
    Bounds[] getBoundaries();

    /**
     * @param s is an object implementing {@link SpriteInterface}
     * @return true if two such objects touch
     */
    boolean intersects(SpriteInterface s);

}