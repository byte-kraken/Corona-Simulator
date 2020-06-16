package app.model;

import app.classes.Map;
import app.classes.gameEntitys.NPC;
import app.classes.gameEntitys.PlayerChar;
import app.classes.gameEntitys.Wall;
import app.classes.mapEntities.MapNPC;
import app.classes.mapEntities.MapWall;

import java.util.Arrays;
import java.util.Iterator;

public class SinglePlayerModel {
    private PlayerChar player;
    private NPC[] npcs;
    private Wall[] walls;

    public SinglePlayerModel() {

    }

    public PlayerChar getPlayer() {
        return player;
    }

    public Iterator<NPC> getNPC_Iterator(){
        return Arrays.stream(npcs).iterator();
    }
    public Iterator<Wall> getWall_Iterator(){
        return Arrays.stream(walls).iterator();
    }

    public void loadEntitiesFromMapInModel(Map map){
        npcs = new NPC[map.getNpcs().size()];
        walls = new Wall[map.getWalls().size()];

        player = new PlayerChar(map.getPlayer().xStartPoint,map.getPlayer().yStartPoint);
        for(int i=0;i<map.getNpcs().size();i++){
            MapNPC mapNPC = map.getNpcs().get(i);
            npcs[i] = new NPC(mapNPC.xStartPoint,mapNPC.yStartPoint);
        }
        for(int i=0;i<map.getWalls().size();i++){
            MapWall mapWall = map.getWalls().get(i);
            walls[i] = new Wall(mapWall.positionX,mapWall.positionY,mapWall.width,mapWall.height);
        }
    }



}
