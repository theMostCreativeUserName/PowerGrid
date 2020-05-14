package edu.hm.cs.rs.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import java.util.Collections;
import java.util.List;

/**
 * Kontrolliert den Einfluss von Zufall im Spiel.
 * Wenn die System-Property powergrid.randomsource einen FQCN definiert,
 * uebernimmt ein Objekt dieses Typs die Zufallsentscheidungen.
 * Ansonsten sind sie nicht vorhersehbar.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-06
 */
public interface RandomSource {
    /**
     * Factory fuer Implementierungen dieses Interface.
     * Objekt des Typs der System-Property powergrid.randomsource oder
     * eines mit unvorhersehbaren Wirkungen.
     */
    static RandomSource make() {
        final String fqcn = System.getProperty("powergrid.randomsource");
        try {
            return fqcn == null?
                    new RandomSource() {}:
                    (RandomSource)Class.forName(fqcn).getConstructor().newInstance();
        } catch(ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mischt die Kraftwerke in einer Liste.
     * @param plants Liste mit Kraftwerken, die diese Methode neu anordnet.
     */
    default void shufflePlants(List<OpenPlant> plants) {
        Collections.shuffle(plants);
    }

    /**
     * Ein Zufallswort zum gegebenen String.
     * @param base Ein String, dem das Zufallswort zugeordnet ist.
     * @return Zufallswort.
     */
    default String babbled(String base) {
        final int wordLength = 4;
        final String xorString = Long.toString(base.hashCode() ^ System.nanoTime());
        return xorString.substring(xorString.length() - wordLength);
    }

    /**
     * Mischt die Spieler in einer Liste.
     * @param players Liste mit Spielern, die diese Methode neu anordnet.
     */
    default void shufflePlayers(List<OpenPlayer> players) {
        Collections.shuffle(players);
    }
}