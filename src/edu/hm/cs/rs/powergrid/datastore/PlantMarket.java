package edu.hm.cs.rs.powergrid.datastore;

import java.util.List;
import java.util.Set;

/**
 * Ein Kraftwerksmarkt.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-10
 */
public interface PlantMarket {
    /**
     * Die aktuell kaeuflich verfuegbaren Kraftwerke.
     * @return Verfuegbare Kraftwerke, geordnet nach steigenden Nummern.
     * Nicht null, aber eventuell leer.
     */
    Set<Plant> getActual();

    /**
     * Entfernt das Kraftwerk mit der gegebenen Nummer.
     * Es spielt keine Rolle, wo das Kw liegt.
     * @param number Nummer eines Kraftwerks.
     * @return Kraftwerk oder null, wenn es nicht existiert.
     */
    Plant removePlant(int number);

    /**
     * Die demnaechst verfuegbaren Kraftwerke.
     * @return Nachrueckende Kraftwerke, geordnet nach steigenden Nummern.
     * Nicht null, aber eventuell leer.
     */
    Set<Plant> getFuture();

    /**
     * Tiefe des Stapels nicht sichtbarer Kraftwerke.
     * Die Karte "Stufe 3" zaehlt wie ein Kraftwerk mit.
     * @return Anzahl der Kraftwerke im Stapel. Nicht negativ.
     */
    int getNumberHidden();

    /**
     * Sucht das Kraftwerk mit der gegebenen Nummer im aktuellen und im zukuenftigen Markt
     * und im Stapel.
     * Kraftwerke koennen im Lauf des Spieles verschwinden.
     * Dann findet sie diese Methode nicht mehr und liefert null.
     * @param number Nummer eines Kraftwerks.
     * @return Kraftwerk oder null, wenn es keines mit dieser Nummer im Spiel gibt.
     */
    Plant findPlant(int number);

    /** Der Stapel der Kraftwerke, die spaeter verfuegbar kommen.
     * @return Liste von Kraftwerken. Eventuell leer, nicht null.
     */
    List<Plant> getHidden();
}
