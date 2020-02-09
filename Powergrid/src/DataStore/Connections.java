package DataStore;

/**
 * implements general connection, probably based on city
 */
public class Connections implements Connection {
    private final String to;
    private final int cost;

    Connections(String to, int cost){
        this.to = to;
        this.cost = cost;
    }
    @Override
    public City getTo() {
        return null;
    }

    @Override
    public int getCost() {
        return 0;
    }
}
