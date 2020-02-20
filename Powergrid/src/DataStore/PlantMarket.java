package DataStore;

import java.util.List;
import java.util.Set;

/**
 * Kraftwerksmarkt.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-01-02
 */
public interface PlantMarket {
    /**
     * Entfernt das Kraftwerk mit der gegebenen Nummer.
     * Es spielt kein Rolle, wo das Kraftwerk ist.
     * @param id Nummer eines Kraftwerks.
     * @return Kraftwerk oder null, wenn es nicht existiert.
     */
    Plant removePlant(int id);

    /**
     * Sucht das Kraftwerk mit der gegebenen Nummer.
     * @param id Nummer eines Kraftwerks.
     * @return Kraftwerk oder null, wenn es nicht existiert.
     */
    Plant findPlant(int id);

    /**
     * Sortierte Menge der momentan kaeuflichen Kw.
     * @return Verfuegbare Kw.
     */
    Set<Plant> getActual();

    /**
     * Sortierte Menge der naechstens kaeuflichen Kw.
     * @return Nachrueckende Kw.
     */
    Set<Plant> getFuture();

    /**
     * Liste der Kw, die ins Spiel kommen.
     * @return Liste der Kw.
     */
    List<Plant> getHidden();

    /**
     * Anzahl Kw, bis Stufe 3 startet.
     * @return Anzahl Kw. Nicht negativ.
     */
    int getPlantsToLevel3();
}