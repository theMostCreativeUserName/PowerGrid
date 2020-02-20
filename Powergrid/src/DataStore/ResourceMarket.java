package DataStore;

/**
 * Ein Rohstoffmarkt.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-01-03
 */
public interface ResourceMarket {
    /**
     * Kaeufliche Rohstoffe.
     * @return Momentan im Markt verfuegbare Rohstoffe.
     */
    Bag<Resource> getAvailable();

    /**
     * Rohstoffe im Vorrat liegen. Diese sind nicht im Markt verfuegbar.
     * @return Rohstoffe im Vorrat.
     */
    Bag<Resource> getSupply();

    /**
     * Preis eines Rohstoffes, abhaengig vom aktuellen Angebot.
     * @param resource Rohstoff.
     * @return Preis in Elektro. Nicht negativ.
     */
    int getPrice(Resource resource);

    /**
     * Verschiebt eine Anzahl Rohstoffe vom Vorrat in den Markt.
     * @param resource Rohstoff.
     * @param number Anzahl. Nicht negativ.
     * @throws IllegalArgumentException wenn der Vorrat nicht ausreicht.
     */
    void makeAvailable(Resource resource, int number);
}