package DataStore;

/**
 * Eine unveraenderliche Verbindung zu einer Stadt mit Kosten.
 */
public interface Connection {
    City getTo();

    int getCost();
}