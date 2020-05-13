package app.model;


/**
 * This is the Model of the MVC architecture.
 * <p>
 * Placeholder class, might be used later in the game itself.
 * May also store references to the different levels for level select.
 * <p>
 * Should be combined with JDBC if data needs to be consistent.
 */
public class Model {
    private int nrInfected = 0;

    public int getNrInfected() {
        return nrInfected;
    }

    public void setNrInfected(int nrInfected) {
        this.nrInfected = nrInfected;
    }
}
