package app.classes.gameEntitys.abstractions;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;

import javax.vecmath.Vector2d;

public abstract class MovingSprite extends NonMovingSprite {
//    protected double velocityX;
//    protected double velocityY;

    protected Vector2d directVec;
    protected double speed;

    public MovingSprite(double positionX, double positionY) {
        super(positionX, positionY);
        directVec = new Vector2d(1, 1);
        speed = 0;
    }

    private static void normalizeVectorIfNotZeor(Vector2d velocity) {
        if (velocity.x != 0 || velocity.y != 0) {
            velocity.normalize();
        }
    }


    @Override
    public void setVelocity(double x, double y) {
//        velocityX = x;
//        velocityY = y;
        directVec.set(new double[]{x, y});
        speed = directVec.length();
        normalizeVectorIfNotZeor(directVec);

    }


    @Override
    public void addVelocity(double addInX, double addInY) {
        Vector2d nVeloVector = new Vector2d(addInX, addInY);
        speed += nVeloVector.length();
        normalizeVectorIfNotZeor(nVeloVector);
        directVec.add(nVeloVector);

    }

    @Override
    public void update(double time) {
        position.x += directVec.x * speed * time;
        position.y += directVec.y * speed * time;
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
        return " Position: [" + position.x + "," + position.y + "]"
                + " Velocity: [Direction Vec" + directVec + "; Speed" + speed + "]";
    }
}