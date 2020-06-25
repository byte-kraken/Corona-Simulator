package app.classes.gameEntitys.abstractions;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;

import javax.vecmath.Vector2d;

public abstract class NonMovingSprite implements SpriteInterface {
    //protected double positionX;
    //protected double positionY;

    protected Vector2d position;

    public NonMovingSprite(double positionX, double positionY) {
//        this.positionX = positionX;
//        this.positionY = positionY;

        position = new Vector2d(positionX, positionY);
    }

    public void setPosition(double x, double y) {
        position.x = x;
        position.y = y;
    }

    public void setVelocity(double x, double y) {
        System.out.println("Non-Moving Sprite has no Velocity");
    }

    public void addVelocity(double x, double y) {
        System.out.println("Non-Moving Sprite has no Velocity");
    }

    public void update(double time) {
        System.out.println("Non-Moving Sprite does no move");
    }

    public abstract void render(GraphicsContext gc);

    public abstract Bounds[] getBoundaries();

    public boolean intersects(SpriteInterface s) {
        for (Bounds ownBoundary : this.getBoundaries()) {
            for (Bounds externalBoundary : s.getBoundaries()) {
                if (ownBoundary.intersects(externalBoundary)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String toString() {
        return " Position: [" + position.x + "," + position.y + "]";
    }
}