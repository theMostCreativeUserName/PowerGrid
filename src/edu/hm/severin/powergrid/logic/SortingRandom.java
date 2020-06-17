package edu.hm.severin.powergrid.logic;


import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.RandomSource;

import java.util.Comparator;
import java.util.List;

/** Deterministische Zufallsentscheidungen fuer Tests.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-06
 */
public class SortingRandom implements RandomSource {

    /**
     * sorts plants by plantNumber.
     * @param plants Liste mit Kraftwerken, die diese Methode neu anordnet.
     */
    @Override public void shufflePlants(List<OpenPlant> plants) {
        plants.sort(Comparator.comparingInt(Plant::getNumber));
    }

    /**
     * sorts players alphabetically.
     * @param players Liste mit Spielern, die diese Methode neu anordnet.
     */
    @Override public void shufflePlayers(List<OpenPlayer> players) {
        players.sort(Comparator.comparing(Player::getColor));
    }

    @Override public String babbled(String base) {
        return base + 'X';
    }
}