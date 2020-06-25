package app.model;

import app.classes.Map;
import app.classes.gameEntitys.NPC;
import app.classes.gameEntitys.PlayerChar;
import app.classes.gameEntitys.Wall;
import app.classes.mapEntities.MapNPC;
import app.classes.mapEntities.MapWall;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Model that translates {@link app.classes.mapEntities.MapSprite} to their corresponding {@link app.classes.gameEntitys}.
 */
public class SinglePlayerModel {
    private PlayerChar player;
    private NPC[] npcs;
    private Wall[] walls;
    private SimpleIntegerProperty nrInfected;

    public SinglePlayerModel() {
        nrInfected = new SimpleIntegerProperty(0);
    }

    public SimpleIntegerProperty getNrInfected() {
        return nrInfected;
    }

    public synchronized void addInfected(){
        nrInfected.setValue(nrInfected.getValue()+1);
    }

    public boolean allNPCsAreInfected(){
        return nrInfected.getValue()>=npcs.length;
    }

    public PlayerChar getPlayer() {
        return player;
    }

    public Iterator<NPC> getNPC_Iterator() {
        return Arrays.stream(npcs).iterator();
    }

    public Iterator<Wall> getWall_Iterator() {
        return Arrays.stream(walls).iterator();
    }



    public void loadEntitiesFromMapInModel(Map map) {
        npcs = new NPC[map.getNpcs().size()];
        walls = new Wall[map.getWalls().size()];

        player = new PlayerChar(map.getPlayer().getStartPositionX() * map.getScaleFactor(), map.getPlayer().getStartPositionY() * map.getScaleFactor());
        for (int i = 0; i < map.getNpcs().size(); i++) {
            MapNPC mapNPC = map.getNpcs().get(i);
            npcs[i] = new NPC(mapNPC.getStartPositionX() * map.getScaleFactor(), mapNPC.getStartPositionY() * map.getScaleFactor());
        }
        for (int i = 0; i < map.getWalls().size(); i++) {
            MapWall mapWall = map.getWalls().get(i);
            walls[i] = new Wall(mapWall.getStartPositionX() * map.getScaleFactor(), mapWall.getStartPositionY() * map.getScaleFactor(), mapWall.getWidth() * map.getScaleFactor(), mapWall.getHeight() * map.getScaleFactor());
        }
    }


}
