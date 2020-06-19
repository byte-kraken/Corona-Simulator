package app.classes.gameEntitys.abstractions;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;

public interface SpriteInterface
{
    public void setPosition(double x, double y);

    public void setVelocity(double x, double y);

    public void addVelocity(double x, double y);

    public void update(double time);

    public  void render(GraphicsContext gc);


    /**
     * Returns an Bounds[] with all boundary Objects of a sprite
     * @return
     */
    public Bounds[] getBoundaries();


    /**
     * Returns true if two SpriteIterface objects touch each other
     * @param s is a Sprite Object
     * @return
     */
    public abstract boolean  intersects(SpriteInterface s);

}