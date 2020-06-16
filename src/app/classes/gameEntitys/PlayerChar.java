package app.classes.gameEntitys;

import app.classes.gameEntitys.abstractions.MovingSprite;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PlayerChar extends MovingSprite {

    private Circle player;

    public PlayerChar(double positionX, double positionY) {
        super(positionX, positionY);
        player = new Circle(positionX,positionY,50);
    }

    public void wallCollision(double time){
        update(time*-1);
    }

    @Override
    public void update(double time) {
        super.update(time);
        player.setCenterX(positionX);
        player.setCenterY(positionY);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillOval(player.getCenterX(), player.getCenterY(), player.getRadius(), player.getRadius());
    }

    @Override
    public Bounds[] getBoundaries() {
        return new Bounds[]{
                player.getBoundsInLocal()};
    }


}
