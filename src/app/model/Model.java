package app.model;


import java.io.File;

/**
 * This is the Model of the MVC architecture.
 * <p>
 * Data stored here persists across scene changes.
 * <p>
 * Should be combined with GAMESTATE-SAVEFILES if data needs to persist when program is closed.
 */
public class Model {
    private int nrInfected = 0;
    private boolean infectionStarted = false;

    public int getNrInfected() {
        return nrInfected;
    }

    public void setNrInfected(int nrInfected) {
        this.nrInfected = nrInfected;
    }

    public boolean isInfectionStarted() {
        return infectionStarted;
    }

    public void setInfectionStarted(boolean infectionStarted) {
        this.infectionStarted = infectionStarted;
    }

    public File serialize() { //method signature should be changed accordingly; placeholder
        return null; //TODO: Gamestate-Savefiles
    }
}
