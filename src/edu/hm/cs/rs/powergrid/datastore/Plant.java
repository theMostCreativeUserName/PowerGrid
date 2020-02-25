package edu.hm.cs.rs.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import java.util.Set;

/**
 * Ein Kraftwerk.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-02-19
 */
public interface Plant {
    /**
     * Kraftwerks-Typen.
     */
    enum Type {
        Coal(Resource.Coal),
        Oil(Resource.Oil),
        Garbage(Resource.Garbage),
        Uranium(Resource.Uranium),
        /**
         * Kann Kohle und Oel verbrennen.
         * Einzelheiten setzt die Spiellogik um.
         */
        Hybrid,
        /** Braucht keine Rohstoffe. */
        Eco,
        /** Braucht keine Rohstoffe. */
        Fusion,
        /** Pseudo-Kraftwerk: Start von Stufe 3. */
        Level3;

        /**
         * Rohstoff, den dieser Kraftwerkstyp braucht.
         * null, wenn es keinen oder mehrere gibt.
         */
        private final Resource resource;

        Type(Resource resource) {
            this.resource = resource;
        }

        Type() {
            this(null);
        }

        /**
         * Rohstoff, wenn das Kw ausschliesslich einen einzigen braucht.
         * @return Rohstoff, den dieser Kraftwerkstyp braucht.
         * null, wenn das Kw keinen braucht oder mehrere verarbeiten kann.
         */
        public Resource getResource() {
            return resource;
        }

    }

    /**
     * Eindeutige Nummer.
     * @return Nummer. Nicht negativ.
     */
    int getNumber();

    /**
     * Anzahl Staedte, die dieses Kraftwerk versorgen kann.
     * @return Anzahl Staedte. Nicht negativ.
     */
    int getCities();

    /**
     * Anzahl Rohstoffe, die dieses Kraftwerk braucht.
     * @return Anzahl Rohstoffe. Nicht negativ.
     */
    int getNumberOfResources();

    Type getType();

    /**
     * Test, ob dieses Kraftwerk Strom produziert hat.
     * @return true genau dann, wenn dieses Kraftwerk gelaufen ist.
     */
    boolean hasOperated();

    /**
     * Rohstoffsammlungen, die dieses Kraftwerk verbrennen kann.
     * @return Verschiedene Rohstoffsammlungen. Nicht null und nicht leer.
     * Menge und Elemente unveraenderlich.
     */
    Set<Bag<Resource>> getResources();

    /**
     * Legt fest, ob  dieses Kraftwerk Strom produziert hat.
     * @param operated true genau dann, wenn dieses Kw Strom produziert hat.
     */
    void setOperated(boolean operated);
}
