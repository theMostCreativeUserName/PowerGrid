package edu.hm.severin.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import edu.hm.severin.powergrid.logic.move.HotMoves;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * calls and orders moves.
 * @author Severin
 * @complexity: xxx
 */
public class StandardRules implements Rules {
    /**
     * the game Object.
     */
    private final OpenGame game;

    public StandardRules(OpenGame game) {
        this.game = game;
    }

    @Override
    public Game getGame() {
        return game;
    }

    /**
     * get all valid moves.
     * @param secret Geheimnis eines Spielers oder leer bei einem neuen Spieler,
     *               der noch kein Geheimnis hat.
     * @return Set of moves.
     */
    @Override
    public Set<Move> getMoves(Optional<String> secret) {
        Set<Move> result = new HashSet<>();
        // gets possible Moves from Companion-Class HotMoves
        for(HotMove prototype : new HotMoves().getPrototypes()) {

            result.addAll(prototype.collect(game, secret));
        }
        return result;
    }

    /**
     * executes Code of the Move.
     * @param secret Geheimnis eines Spielers oder leer bei einem neuen Spieler,
     *               der noch kein Geheimnis hat.
     * @param move   Ein Zug.
     * @return
     */
    @Override
    public Optional<Problem> fire(Optional<String> secret, Move move) {
        // is Move a HotMove?
        Optional<Problem> problem = Optional.empty();
        if (move instanceof HotMove) {
            HotMove hotMove = (HotMove) move;
            //does hotmove reference same game?
            if(!hotMove.getGame().equals(this.getGame())) throw new IllegalStateException("Hackers shall not pass");
            problem = hotMove.fire();
            if (problem.isEmpty()) {
                //getMoves
                Set<Move> nextMoves = getMoves(secret);
                problem = fireNextMoves(nextMoves);
            }
        }else
            throw new IllegalArgumentException("move is invalid");

            return problem;

    }
    private Optional<Problem> fireNextMoves(Set<Move> nextMoves){
        Optional<Problem> problem = Optional.empty();
        for (Move singleMove: nextMoves) {
            HotMove move = (HotMove) singleMove;
            // does move have priority?
            if(singleMove.hasPriority()) {
                problem = move.fire();
                // is move Autofire?
            }else if (singleMove.isAutoFire()){
                problem = move.fire();
            }
        }
        return problem;
    }
}
