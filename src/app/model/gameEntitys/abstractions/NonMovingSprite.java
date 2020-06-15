package app.model.gameEntitys.abstractions;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;

public abstract class NonMovingSprite implements SpriteInterface {
    protected double positionX;
    protected double positionY;

    public NonMovingSprite(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
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
        return " Position: [" + positionX + "," + positionY + "]";
    }
}