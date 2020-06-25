package app.util;

public final class Constants {
    // standard map size
    public static final int STANDARD_MAP_SIZE_X = 1920;
    public static final int STANDARD_MAP_SIZE_Y = 1080;

    // size of one pixel, all coords are sub-sampled accordingly
    public static final double PIXEL_SIZE = 8;

    // location of the world resources
    private static final String fs = System.getProperty("file.separator");
    public static final String LEVELS_FOLDER_PATH = "worlds" + fs + "levels" + fs;
    public static final String OWN_WORLDS_FOLDER_PATH = "worlds" + fs + "ownWorlds" + fs;
    public static final String TMP_WORLD_FOLDER_PATH = "worlds" + fs + "tmp" + fs;
    public static final String TMP_WORLD_PATH = TMP_WORLD_FOLDER_PATH + "tmp";
}
