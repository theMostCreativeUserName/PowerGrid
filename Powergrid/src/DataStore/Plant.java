package DataStore;
import java.util.Set;

/**
 * Ein Kraftwerk.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-01-14
 */
public interface Plant {
    /**
     * Eindeutige Nummer des Kraftwerks.
     * Gleichzeitig der minimale Kaufpreis beim Versteigern.
     * @return Nummer. Wenigstens 1.
     */
    int getId();

    /**
     * Anzahl Staedte, die dieses Kraftwerk mit Strom versorgen kann.
     * @return Anzahl Staedt. Wenigstens 1.
     */
    int getCities();

    /**
     * Anzahl Rohstoffe, die dieses Kraftwerk verbraucht.
     * @return Anzahl Rohstoffe. Nicht negativ.
     */
    int getNumberOfResources();

    /**
     * Kraftwerkstyp.
     * @return Typ. Nicht null.
     */
    Type getType();

    /**
     * Test, ob dieses Kraftwerk Strom produziert hat.
     * @return true = Kw hat Strom produziert; false ansonsten.
     */
    boolean hasOperated();

    /**
     * Legt fest, ob das Kraftwerk Strom produziert hat.
     * @param operated true = Kw hat Strom produziert; false ansonsten.
     */
    void setOperated(boolean operated);

    /**
     * Liefert die Anzahl Rogstoffe, die dieses Kw verarbeiten kann.
     * @param resource Rohstoff. Nicht null.
     * @return Anzahl, die dieses Kraftwerk verarbeiten kann.
     */
    int getRequired(Resource resource);

    /**
     * Alle Rohstoffkombinationen, die dieses Kraftwerk verwenden kann.
     * @return Menge von Rohstoffkombinationen. Nicht leer.
     */
    Set<Bag<Resource>> getResources();
}