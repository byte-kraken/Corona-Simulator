package app.classes.mapEntities;

public class MapNPC {
    public double xStartPoint;
    public double yStartPoint;

    private NPCTYPE npctype;

    public MapNPC(double xStartPoint, double yStartPoint, NPCTYPE npctype) {
        this.xStartPoint = xStartPoint;
        this.yStartPoint = yStartPoint;
        this.npctype = npctype;
    }

    public enum NPCTYPE {
        NORMAL
    }
}
