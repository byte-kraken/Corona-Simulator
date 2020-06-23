package app.classes;

import app.classes.mapEntities.MapNPC;
import app.classes.mapEntities.MapPlayerChar;
import app.classes.mapEntities.MapWall;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;

import static app.constants.Constants.STANDARD_MAP_SIZE_X;
import static app.constants.Constants.STANDARD_MAP_SIZE_Y;

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
        MapPlayerChar playerChar = new MapPlayerChar(584, 237);
        standardMap.setPlayer(playerChar);

        standardMap.addWall(new MapWall(0, 200, 500, 50));
        standardMap.addWall(new MapWall(500, 500, 200, 200));
        standardMap.addWall(new MapWall(0, 900, 500, 50));


        standardMap.addNPC(new MapNPC(1566, 500, MapNPC.NpcType.Normal));
        standardMap.addNPC(new MapNPC(420, 760, MapNPC.NpcType.Normal));
        standardMap.addNPC(new MapNPC(1000, 100, MapNPC.NpcType.ConspiracyTheorist));

        return standardMap;
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
}
