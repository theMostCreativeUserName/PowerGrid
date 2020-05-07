package edu.hm.cs.rs.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Game;
//import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import java.util.Set;

public interface Rules {

 /*   static Rules newRules(OpenGame game) {
        return newRules(System.getProperty("powergrid.rules",
                System.getenv("POWERGRID_RULES")),
                game);
    }

    static Rules newRules(String fqcn, OpenGame game) {
        try {
            return (Rules)Class.forName(fqcn)
                    .getConstructor(OpenGame.class)
                    .newInstance(game);
        }
        catch(ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    Game getGame();

    Set<? extends Move> getMoves(String secret);

    Problem fire(String secret, Move move);*/

}
