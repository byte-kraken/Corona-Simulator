package app.classes;

import app.Sprite;
import app.classes.mapEntities.MapNPC;
import app.classes.mapEntities.MapPlayerChar;
import app.classes.mapEntities.MapWall;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

import static app.constants.Constants.STANDARD_MAP_SIZE_X;
import static app.constants.Constants.STANDARD_MAP_SIZE_Y;

public class Map {
    private MapPlayerChar player;
    private ArrayList<MapNPC> npcs;
    private ArrayList<MapWall> walls;

    private String mapName;

    private int mapSizeX = STANDARD_MAP_SIZE_X;
    private int mapSizeY = STANDARD_MAP_SIZE_Y;

    public Map() {
        this.npcs = new ArrayList<>();
        this.walls = new ArrayList<>();
    }

    public MapPlayerChar getPlayer() {
        return player;
    }

    public ArrayList<MapNPC> getNpcs() {
        return npcs;
    }

    public ArrayList<MapWall> getWalls() {
        return walls;
    }

    public void setPlayer(MapPlayerChar player) {
        this.player = player;
    }

    public void addNPC(MapNPC npcToAdd) {
        npcs.add(npcToAdd);
    }

    public void addWall(MapWall wallToAdd) {
        walls.add(wallToAdd);
    }


    public static Map getStandardTestMap() {
        Map standardMap = new Map();
        MapPlayerChar playerChar = new MapPlayerChar(584, 237);
        standardMap.setPlayer(playerChar);

        standardMap.addWall(new MapWall(0, 346, 600, 50));
        standardMap.addWall(new MapWall(1000, 400, 50, 1344));
        standardMap.addWall(new MapWall(1200, 140, 650, 50));


        standardMap.addNPC(new MapNPC(1566, 500, MapNPC.NPCTYPE.NORMAL));
        standardMap.addNPC(new MapNPC(420, 760, MapNPC.NPCTYPE.NORMAL));
        standardMap.addNPC(new MapNPC(1000, 100, MapNPC.NPCTYPE.NORMAL));

        return standardMap;
    }

    public static void serialize(List<Sprite> NPC, List<Sprite> Wall, Sprite virus, boolean socialDistancing, boolean increasedHygiene, boolean betterMedicine, String filename) throws IOException {
        String fs = System.getProperty("file.separator");
        FileOutputStream f = new FileOutputStream(new File("src"+ fs + "app" + fs + "worlds" + fs + "ownWorlds" + fs + filename + ".xml"));
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(NPC);
        o.writeObject(Wall);
        o.writeObject(virus);
        o.writeBoolean(socialDistancing);
        o.writeBoolean(increasedHygiene);
        o.writeBoolean(betterMedicine);
        o.close();
        f.close();
    }


}
