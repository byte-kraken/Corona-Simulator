package app.classes.gameEntitys;

import app.classes.gameEntitys.abstractions.NonMovingSprite;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall extends NonMovingSprite {

    private final Rectangle wall;

    public Wall(double positionX, double positionY,double width, double height) {
        super(positionX,positionY);
        this.wall = new Rectangle(positionX,positionY,width, height);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(wall.getX(),wall.getY(),wall.getWidth(),wall.getHeight());



        //Debug
//        gc.setFill(Color.RED);
//        gc.fillText(wall.toString(),wall.getX(),wall.getY());
//        gc.setStroke(Color.RED);
//        Bounds b = getBoundaries()[0];
//        gc.strokeRect(b.getMinX(),b.getMinY(),b.getWidth(),b.getHeight());
//        gc.setStroke(Color.BLACK);
    }

    @Override
    public Bounds[] getBoundaries() {
        return new Bounds[]{wall.getBoundsInLocal()};
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Wall with:");
        sb.append(super.toString());
        sb.append("and Width ").append(wall.getWidth()).append("Height: ").append(wall.getHeight());
        return sb.toString();
    }
}
