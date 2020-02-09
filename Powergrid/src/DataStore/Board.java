package DataStore;

import java.util.*;

/**
 * Der Spielplan mit den Staedten.
         * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-01-03
        */
public interface Board {
    /**
     * Sucht eine Stadt.
     * @param name Name.
     * @return Stadt mit dem Namen oder null, wenn es keine gibt.
     */
    City findCity(String name);

    /**
     * Entfernt alle Staedte mit einer Region ueber der Grenze.
     * Loescht auch alle Verbindungen von und zu entfernten Staedten.
     * @param limit Staedte in Regionen bis zu dieser Nummer bleiben bestehen.
     * Staedte darueber verschwinden.
     * @throws IllegalStateException wenn der Spielplan geschlossen ist.
     */
    void closeRegions(int limit);

    /**
     * Menge aller Staedte.
     * @return Staedte. Nicht null.
     * Veraenderlich bis zum ersten close-Aufruf. Dann unveraenderlich.
     */
    Set<City> getCities();

    /**
     * Schliesst diesen Spielplan.
     * Schliesst auch alle Staedte.
     * @throws IllegalStateException wenn der Spielplan geschlossen ist.
     */
    void close();
}