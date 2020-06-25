package app.classes.gameEntitys;

import app.classes.NPCType;
import app.classes.gameEntitys.abstractions.MovingSprite;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Randomly moves around the playing-field, waiting to be infected by {@link PlayerChar}
 * <p>
 * Has an {@link NPCType}, different types have different behaviors.
 */
public class NPC extends MovingSprite {

    private final Circle npcChar;
    private boolean infected;

    private static final Color NORMAL_NPC_COLOR = Color.GOLD;
    private static final Color INFECTED_NPC_COLOR = Color.RED;

    public NPC(double positionX, double positionY) {

        super(positionX, positionY);
        npcChar = new Circle(positionX, positionY, 25);
        infected = false;
    }


    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(infected ? INFECTED_NPC_COLOR : NORMAL_NPC_COLOR);
        gc.fillOval(npcChar.getCenterX() - npcChar.getRadius(),
                npcChar.getCenterY() - npcChar.getRadius(), npcChar.getRadius() * 2,
                npcChar.getRadius() * 2);
    }

    @Override
    public void update(double time) {
        super.update(time);
        npcChar.setCenterX(position.x);
        npcChar.setCenterY(position.y);
    }

    @Override
    public Bounds[] getBoundaries() {
        return new Bounds[]{
                npcChar.getBoundsInLocal()};
    }

    public void wallCollision(double elapsedTime, Wall wall) {

        Bounds wallBounds = wall.getBoundaries()[0];
        if (npcChar.getCenterX() < wallBounds.getMinX() || npcChar.getCenterX() > wallBounds.getMaxX()) {
            directVec.x *= -1;
        }
        if (npcChar.getCenterY() < wallBounds.getMinY() || npcChar.getCenterY() > wallBounds.getMaxY()) {
            directVec.y *= -1;
        }
        update(elapsedTime);
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }
}
