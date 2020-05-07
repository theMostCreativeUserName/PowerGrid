package edu.hm.cs.rs.powergrid.datastore;

import java.util.Map;

/**
 * Eine Stadt auf dem Spielplan.
 * Alphabetisch nach Namen geordnet.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-02-29
 */
public interface City extends Comparable<City> {
    /**
     * Name der Stadt.
     * @return Name. Nicht leer und nicht null.
     */
    String getName();

    /**
     * Gebiet, in dem die Stadt liegt.
     * @return Gebiet. Wenigstens 1.
     */
    int getRegion();

    /**
     * Verbindungen zu anderen Staedten.
     * Veraenderlich bis zum ersten close-Aufruf, dann unveraenderlich.
     * @return Verbindungen. Nicht null und nicht leer.
     * Jeder Eintrag bildet eine andere Stadt auf die Verbindungskosten dort hin ab.
     */
    Map<City, Integer> getConnections();

    /**
     * Schliesst die Verbindungen dieser Stadt ab.
     * connect-Aufrufe sind nicht mehr erlaubt, dafuer getConnections.
     * @throws IllegalStateException wenn die Stadt geschlossen ist.
     */
    void close();
}
