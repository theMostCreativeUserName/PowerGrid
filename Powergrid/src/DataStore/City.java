package DataStore;
import java.util.*;

/**
 * Eine Stadt auf dem Spielplan mit ihren Verbindungen.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-01-02
 */
public interface City {
    /**
     * Name der Stadt.
     * @return Name. Nicht leer und nicht null.
     */
    String getName();

    /**
     * Gebiet, in dem die Stadt liegt.
     * @return Gebiet. Wenigstens 1.
     */
    int getArea();

    /**
     * Verbindungen zu anderen Staedten.
     * Veraenderlich bis zum ersten close-Aufruf, dann unveraenderlich.
     * @return Verbindungen. Nicht null und nicht leer.
     * Jeder Eintrag bildet eine andere Stadt auf die Verbindungskosten dort hin ab.
     */
    Map<City, Integer> getConnections();

    /**
     * Verbindet diese Stadt mit einer anderen.
     * Nur vor dem ersten close-Aufruf erlaubt.
     * @param to   Eine andere Stadt. Nicht null, nicht diese.
     * @param cost Verbindungskosten. Nicht negativ.
     * @throws IllegalStateException wenn diese Stadt geschlossen ist.
     * @throws IllegalArgumentException wenn es schon eine Verbindung zu to gibt.
     */
    void connect(City to, int cost);

    /**
     * Schliesst die Verbindungen dieser Stadt ab.
     * @throws IllegalStateException wenn diese Stadt geschlossen ist.
     */
    void close();
}