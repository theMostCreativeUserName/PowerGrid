package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Set;

/**
 * Companions class to collect all possible moves of the game.
 * @author Severin
 */
public class HotMoves {

    /**
     * collects Prototypes of all possible moves.
     * @return list of possible hotMoves
     */
    public Set<HotMove> getPrototypes() {
        return Set.of(
                new NewPlayerJoins(),
                new ConnectNoCity(),
                new EndBuilding(),
                new TurnOver(),
                new Build1stCity(),
                new UpdatePlantMarket(),
                new OrderPlayers(),
                new EndGame()
        );
    }
}
