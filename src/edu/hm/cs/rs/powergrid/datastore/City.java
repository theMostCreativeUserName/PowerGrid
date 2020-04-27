package edu.hm.cs.rs.powergrid.datastore;

import java.util.Map;

/**
 * Eine Stadt auf dem Spielplan.
 *
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-02-19
 */
public interface City {
    /**
     * Name der Stadt.
     *
     * @return Name. Nicht leer und nicht null.
     */
    String getName();

    /**
     * Gebiet, in dem die Stadt liegt.
     *
     * @return Gebiet. Wenigstens 1.
     */
    int getRegion();

    /**
     * Verbindet diese Stadt mit einer anderen.
     * Nur vor dem ersten close-Aufruf erlaubt.
     *
     * @param to   Eine andere Stadt. Nicht null, nicht diese.
     * @param cost Verbindungskosten. Nicht negativ.
     * @throws IllegalStateException    wenn diese Stadt geschlossen ist.
     * @throws IllegalArgumentException wenn es schon eine Verbindung zu to gibt.
     */
    void connect(City to, int cost);

    /**
     * Verbindungen zu anderen Staedten.
     * Veraenderlich bis zum ersten close-Aufruf, dann unveraenderlich.
     *
     * @return Verbindungen. Nicht null und nicht leer.
     * Jeder Eintrag bildet eine andere Stadt auf die Verbindungskosten dort hin ab.
     */
    Map<City, Integer> getConnections();

    /**
     * Schliesst die Verbindungen dieser Stadt ab.
     * connect-Aufrufe sind nicht mehr erlaubt, dafuer getConnections.
     *
     * @throws IllegalStateException wenn die Stadt geschlossen ist.
     */
    void close();

    int compareTo(City other);
}
