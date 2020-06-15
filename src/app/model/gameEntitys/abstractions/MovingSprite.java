package app.model.gameEntitys.abstractions;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;

public abstract class MovingSprite extends NonMovingSprite implements SpriteInterface
{

    protected double velocityX;
    protected double velocityY;

    public MovingSprite(double positionX, double positionY) {
        super(positionX, positionY);
    }


    @Override
    public void setVelocity(double x, double y)
    {
        velocityX = x;
        velocityY = y;
    }

    @Override
    public void addVelocity(double x, double y)
    {
        velocityX += x;
        velocityY += y;
    }

    @Override
    public void update(double time)
    {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }

    public abstract void render(GraphicsContext gc);

    public abstract Bounds[] getBoundaries();


    public boolean intersects(SpriteInterface s) {
        for (Bounds ownBoundary : this.getBoundaries()) {
            for (Bounds externalBoundary : s.getBoundaries()) {
                if(ownBoundary.intersects(externalBoundary)){
                    return true;
                }
            }
        }
        return false;
    }
    
    public String toString()
    {
        return " Position: [" + positionX + "," + positionY + "]" 
        + " Velocity: [" + velocityX + "," + velocityY + "]";
    }
}