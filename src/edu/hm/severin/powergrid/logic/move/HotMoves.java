package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Set;

public class HotMoves {

    public Set<HotMove> getPrototypes(){
        return Set.of(new NewPlayerJoins());
    }
}
