package app.model.gameEntitys;

import app.model.gameEntitys.abstractions.MovingSprite;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;

public class NPC extends MovingSprite {

    private Circle npcChar;

    public NPC(double positionX, double positionY) {
        super(positionX, positionY);
    }

    @Override
    public void render(GraphicsContext gc) {

    }

    @Override
    public Bounds[] getBoundaries() {
        return new Bounds[0];
    }
}
