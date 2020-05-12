package edu.hm.severin.powergrid;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.Rules;

import java.util.Optional;
import java.util.Set;

public class WeirdTest {
    public static void main(String[] args) {
        OpenFactory factory = OpenFactory.newFactory("edu.hm.severin.powergrid.datastore.NeutralFactory");
        OpenGame game = factory.newGame(new EditionGermany());
        Rules rules = Rules.newRules("edu.hm.severin.powergrid.logic.StandardRules", game);

        Set<Move> moves = rules.getMoves(Optional.empty());
        rules.fire(Optional.empty(), moves.iterator().next());

    }
}
