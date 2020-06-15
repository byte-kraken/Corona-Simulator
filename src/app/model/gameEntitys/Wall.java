package app.model.gameEntitys;

import app.model.gameEntitys.abstractions.NonMovingSprite;
import app.model.gameEntitys.abstractions.SpriteInterface;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall extends NonMovingSprite {

    private final Rectangle wall;

    public Wall(double positionX, double positionY,double width, double height) {
        super(positionX,positionY);
        this.wall = new Rectangle(positionX,positionX,width, height);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(wall.getX(),wall.getY(),wall.getWidth(),wall.getHeight());
    }

    @Override
    public Bounds[] getBoundaries() {
        return new Bounds[]{wall.getBoundsInLocal()};
    }

}
