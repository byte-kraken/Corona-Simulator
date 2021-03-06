package app.classes;

import app.classes.mapEntities.MapNPC;
import app.classes.mapEntities.MapPlayerChar;
import app.classes.mapEntities.MapWall;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;

import static app.util.Constants.STANDARD_MAP_SIZE_X;
import static app.util.Constants.STANDARD_MAP_SIZE_Y;

/**
 * The container object for levels.
 * Represents the playing-field, all entities and level options.
 * Also holds a scaleFactor, indicating how coordinates of corresponding {@link app.classes.mapEntities.MapSprite}
 * need to be scaled to accurately represent their {@link app.classes.gameEntitys}.
 */
public class Map implements Serializable {
    private static final Color VOID_COLOR = Color.WHITE;

    private MapPlayerChar player;
    private final ArrayList<MapNPC> npcs;
    private final ArrayList<MapWall> walls;

    private String mapName;
    private boolean socialDistancing;
    private boolean increasedHygiene;
    private boolean betterMedicine;

    private final int mapSizeX = STANDARD_MAP_SIZE_X;
    private final int mapSizeY = STANDARD_MAP_SIZE_Y;
    private double scaleFactor = 1;

    public Map() {
        this.npcs = new ArrayList<>();
        this.walls = new ArrayList<>();
        addInvisibleMapBorder();
    }

    private void addInvisibleMapBorder() {
        walls.add(new MapWall(-15, -15, mapSizeX + 15, 15));
        walls.add(new MapWall(-15, mapSizeY + 1, mapSizeX + 15, 15));
        walls.add(new MapWall(-15, -15, 15, mapSizeY + 15));
        walls.add(new MapWall(mapSizeX + 1, -15, 15, mapSizeY + 15));
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

    public static Map getStandardTestMap() {
        Map standardMap = new Map();
        standardMap.setMapName("StandardTestMap");
        MapPlayerChar playerChar = new MapPlayerChar(584, 237);
        standardMap.setPlayer(playerChar);

        standardMap.addWall(new MapWall(0, 200, 500, 50));
        standardMap.addWall(new MapWall(500, 500, 200, 200));
        standardMap.addWall(new MapWall(0, 900, 500, 50));


        standardMap.addNPC(new MapNPC(1566, 500, NPCType.Normal));
        standardMap.addNPC(new MapNPC(420, 760, NPCType.Normal));
        standardMap.addNPC(new MapNPC(1000, 100, NPCType.ConspiracyTheorist));

        return standardMap;
    }

    public static Map getStandardTestMap2() {
        Map standardMap2 = new Map();
        int sF = 3; //Scale Factor
        standardMap2.setMapName("StandardTestMap2");
        MapPlayerChar playerChar = new MapPlayerChar(64 * sF, 24 * sF);
        standardMap2.setPlayer(playerChar);

        standardMap2.addWall(new MapWall(0 * sF, 64 * sF, 544 * sF, 8 * sF));
        standardMap2.addWall(new MapWall(120 * sF, 136 * sF, 522 * sF, 8 * sF));
        standardMap2.addWall(new MapWall(304 * sF, 144 * sF, 8 * sF, 72 * sF));
        standardMap2.addWall(new MapWall(304 * sF, 280 * sF, 8 * sF, 88 * sF));
        standardMap2.addWall(new MapWall(384 * sF, 176 * sF, 24 * sF, 40 * sF));
        standardMap2.addWall(new MapWall(384 * sF, 288 * sF, 24 * sF, 40 * sF));
        standardMap2.addWall(new MapWall(552 * sF, 200 * sF, 16 * sF, 72 * sF));

        standardMap2.addNPC(new MapNPC(480 * sF, 170 * sF, NPCType.Normal));
        standardMap2.addNPC(new MapNPC(536 * sF, 320 * sF, NPCType.Normal));
        standardMap2.addNPC(new MapNPC(496 * sF, 200 * sF, NPCType.Normal));
        standardMap2.addNPC(new MapNPC(576 * sF, 176 * sF, NPCType.Normal));

        return standardMap2;
    }


    public static Color getVoidColor() {
        return VOID_COLOR;
    }

    public void addNPC(MapNPC npcToAdd) {
        npcs.add(npcToAdd);
    }

    public void addWall(MapWall wallToAdd) {
        walls.add(wallToAdd);
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public boolean isSocialDistancing() {
        return socialDistancing;
    }

    public void setSocialDistancing(boolean socialDistancing) {
        this.socialDistancing = socialDistancing;
    }

    public boolean isIncreasedHygiene() {
        return increasedHygiene;
    }

    public void setIncreasedHygiene(boolean increasedHygiene) {
        this.increasedHygiene = increasedHygiene;
    }

    public boolean isBetterMedicine() {
        return betterMedicine;
    }

    public void setBetterMedicine(boolean betterMedicine) {
        this.betterMedicine = betterMedicine;
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public static void serialize(Map map, String path) throws IOException {
        FileOutputStream f = new FileOutputStream(new File(path));
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(map);
        o.close();
        f.close();
    }

    public static Map deserialize(String path) throws IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream(new File(path));
        ObjectInputStream oi = new ObjectInputStream(fi);
        Map map = (Map) oi.readObject();
        oi.close();
        fi.close();
        return map;
    }
}
