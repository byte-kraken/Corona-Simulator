package app.classes.gameEntitys;

import app.classes.gameEntitys.abstractions.MovingSprite;
import app.classes.mapEntities.MapPlayerChar;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * The {@link MovingSprite} controlled by the player.
 * <p>
 * Translates key inputs to move and infect {@link NPC}.
 */
public class PlayerChar extends MovingSprite {

    private final Circle player;
    private Image image;

    public PlayerChar(double positionX, double positionY) {
        super(positionX, positionY);
        player = new Circle(positionX, positionY, 50);
        setImage("imgs\\VIRUS.png");
    }

    public void wallCollision(double time) {
        update(time * -1);
    }

    public void setImage(String filename) {
        try {
            image = new Image(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(double time) {
        super.update(time);
        player.setCenterX(positionX);
        player.setCenterY(positionY);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(MapPlayerChar.COLOR);
//        gc.fillOval(player.getCenterX()-player.getRadius(),
//                player.getCenterY()-player.getRadius(), player.getRadius()*2,
//                player.getRadius()*2);

        if (image != null) {
            gc.drawImage(image, player.getCenterX() - player.getRadius(),
                    player.getCenterY() - player.getRadius(), player.getRadius() * 2,
                    player.getRadius() * 2);
        }


        //Debug
//        gc.fillText(this.toString(),player.getCenterX(),player.getCenterY());
//        gc.setStroke(Color.RED);
//        Bounds b = getBoundaries()[0];
//        gc.strokeRect(b.getMinX(),b.getMinY(),b.getWidth(),b.getHeight());
//        gc.setStroke(Color.BLACK);

    }

    @Override
    public Bounds[] getBoundaries() {
        return new Bounds[]{
                player.getBoundsInLocal()};
    }


}
