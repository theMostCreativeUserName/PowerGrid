package edu.hm.severin.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
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
 */
public class StandardRules implements Rules {
    /**
     * the game Object.
     */
    private final OpenGame game;

    /**
     * C-tor.
     * @param game this game
     */
    public StandardRules(OpenGame game) {
        this.game = game;
    }

    @Override
    public Game getGame() {
        return game;
    }

    /**
     * get all valid moves.
     * @param secret secret of player, empty if player doesn't have one yet
     * @return Set of moves.
     */

    @Override
    public Set<Move> getMoves(Optional<String> secret) {
        /////CHECKSTYLE
        final Set<Move> result = new HashSet<>();
        // to prevent returning from a nested loop returnEarly is introduced, to later return an empty set
        boolean returnEarly = false;
        Optional<OpenPlayer> player = Optional.empty();
        if (secret.isPresent()) {
            final OpenPlayer openplayer = game.findPlayer(secret.get());
            player = Optional.ofNullable(openplayer);
            if (openplayer == null)
                returnEarly = true;

        }
        if(returnEarly)
            return result;
        // gets possible Moves from Companion-Class HotMoves
        for(HotMove prototype : new HotMoves().getPrototypes()) {
            result.addAll(prototype.collect(game, player));
        }
        return result;
    }

    /**
     * executes Code of the Move.
     * @param secret secret of player, empty if player doesn't have one yet
     * @param move   Ein Zug.
     * @return
     */
    @Override
    public Optional<Problem> fire(Optional<String> secret, Move move) {
        // is Move a HotMove?
        Optional<Problem> problem = Optional.empty();
        if (move instanceof HotMove) {
            final HotMove hotMove = (HotMove) move;
            //does hotmove reference same game?
            if(!hotMove.getGame().equals(this.getGame())) throw new IllegalStateException("Hackers shall not pass");
            problem = hotMove.fire();
            if (problem.isEmpty()) {
                //getMoves
                final Set<Move> nextMoves = getMoves(secret);
                problem = fireNextMoves(nextMoves);
            }
        }else
            throw new IllegalArgumentException("move is invalid");

            return problem;

    }
    private Optional<Problem> fireNextMoves(Set<Move> nextMoves){
        Optional<Problem> problem = Optional.empty();
        for (Move singleMove: nextMoves) {
            final HotMove move = (HotMove) singleMove;
            // does move have priority?
            if(singleMove.hasPriority()) {
                problem = move.fire();
                // is move Auto-fire?
            }else if (singleMove.isAutoFire()){
                problem = move.fire();
            }
        }
        return problem;
    }


}
