package app.classes.gameEntitys;

import app.classes.gameEntitys.abstractions.MovingSprite;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.concurrent.ThreadLocalRandom;

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
        gc.setFill(infected? INFECTED_NPC_COLOR:NORMAL_NPC_COLOR);
        gc.fillOval(npcChar.getCenterX()-npcChar.getRadius(),
                npcChar.getCenterY()-npcChar.getRadius(), npcChar.getRadius()*2,
                npcChar.getRadius()*2);
    }

    @Override
    public void update(double time) {
        super.update(time);
        npcChar.setCenterX(positionX);
        npcChar.setCenterY(positionY);
    }

    @Override
    public Bounds[] getBoundaries() {
        return new Bounds[]{
                npcChar.getBoundsInLocal()};
    }

    public void wallCollision(double elapsedTime) {
        //TODO Make Vector Reflection instead of random shit
        velocityX = velocityX*-1;
        velocityY = velocityY*-1;
        double angleChange = ThreadLocalRandom.current().nextDouble(-30, 30);
        velocityX += angleChange;
        velocityY -= angleChange;

        if(velocityX > 70){
            velocityX /=2;
        }
        if(velocityY > 70){
            velocityY /=2;
        }

    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }
}
