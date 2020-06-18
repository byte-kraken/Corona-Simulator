package app.classes.gameEntitys;

import app.classes.gameEntitys.abstractions.MovingSprite;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class NPC extends MovingSprite {

    private final Circle npcChar;

    public NPC(double positionX, double positionY) {

        super(positionX, positionY);
        npcChar = new Circle(positionX, positionY, 50);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.fillOval(npcChar.getCenterX(), npcChar.getCenterY(), npcChar.getRadius(), npcChar.getRadius());
    }

    @Override
    public Bounds[] getBoundaries() {
        return new Bounds[0];
    }
}
