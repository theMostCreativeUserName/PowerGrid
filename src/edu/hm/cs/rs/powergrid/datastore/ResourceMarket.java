package edu.hm.cs.rs.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;

/**
 * Der Rohstoffmarkt.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-02-22
 */
public interface ResourceMarket {
    /**
     * Kaeufliche Rohstoffe.
     * @return Im Markt verfuegbare Rohstoffmengen.
     */
    Bag<Resource> getAvailable();

    /**
     * Rohstoffe, die momentan im Vorrat liegen und nicht im Markt verfuegbar sind.
     * @return Rohstoffmengen im Vorrat.
     */
    Bag<Resource> getSupply();

    /**
     * Preis eines Rohstoffes, abhaengig vom aktuellen Angebot.
     * @param resource Rohstoff.
     * @return Preis in Elektro. Nicht negativ.
     */
    int getPrice(Resource resource);
}
