package app.model.gameEntitys;

import app.model.gameEntitys.abstractions.MovingSprite;
import app.model.gameEntitys.abstractions.SpriteInterface;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PlayerChar extends MovingSprite {

    private Circle player;

    public PlayerChar(double positionX, double positionY) {
        super(positionX, positionY);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillOval(player.getCenterX(), player.getCenterY(), player.getRadius(), player.getRadius());
    }

    @Override
    public Bounds[] getBoundaries() {
        return new Bounds[]{player.getBoundsInLocal()};
    }


}
