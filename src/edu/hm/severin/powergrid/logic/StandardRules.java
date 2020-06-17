package edu.hm.severin.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import edu.hm.severin.powergrid.logic.move.HotMoves;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * calls and orders moves.
 *
 * @author Severin
 */
public class StandardRules implements Rules {
    /**
     * the game Object.
     */
    private final OpenGame game;

    /**
     * C-tor.
     *
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
     *
     * @param secret secret of player, empty if player doesn't have one yet
     * @return Set of moves.
     */

    @Override
    public Set<Move> getMoves(Optional<String> secret) {
        //constants
        final Set<HotMove> hotMoves = this.getPrototypes().stream().map(move -> (HotMove) move).collect(Collectors.toSet());
        final Set<Move> result = new HashSet<>();


        //work
        if (secret.isPresent()) {
            final OpenPlayer openplayer = game.findPlayer(secret.get());
            if (openplayer != null) {
                final Optional<OpenPlayer> player = Optional.of(openplayer);
                hotMoves.forEach(prototype -> result.addAll(prototype.collect(game, player)));

            }
        } else {
            final Set<HotMove> joinMove = hotMoves
                    .stream()
                    .filter(move -> move.getType() == MoveType.JoinPlayer)
                    .collect(Collectors.toSet());
            final Set<HotMove> joinMoveInSet = joinMove.iterator().next().collect(game, Optional.empty());

            if (!joinMoveInSet.isEmpty()) {
                result.add(joinMoveInSet.iterator().next());
            }
        }

        return result;
    }

    /**
     * executes Code of the Move.
     *
     * @param secret secret of player, empty if player doesn't have one yet
     * @param move   Ein Zug.
     * @return Optional of Problem, empty when there are no problems
     */
    @Override
    public Optional<Problem> fire(Optional<String> secret, Move move) {
        // is Move a HotMove?
        Optional<Problem> problem = Optional.empty();
        if (move instanceof HotMove) {
            final HotMove hotMove = (HotMove) move;
            //does hotMove reference same game?
            if (!hotMove.getGame().equals(this.getGame())) throw new IllegalStateException("Hackers shall not pass");
            problem = hotMove.fire();
            if (problem.isEmpty()) {
                boolean autoOrPrioMoves = true;
                while (autoOrPrioMoves) {
                    final HotMove nextToFire = fireNextMoves();
                    if (nextToFire == null)
                        autoOrPrioMoves = false;
                    else
                        problem = nextToFire.fire();
                }
            }


        } else
            throw new IllegalArgumentException("move is invalid");

        return problem;

    }

    /**
     * check if Moves after the initial could fire.
     *
     * @return move, if it can fire
     * else null
     */
    private HotMove fireNextMoves() {
        HotMove result = null;
        for (OpenPlayer player : game.getOpenPlayers()) {
            final Set<Move> setOfAllMoves = new HashSet<>();
            final Set<HotMove> hotMoves = this.getPrototypes().stream().map(move -> (HotMove) move).collect(Collectors.toSet());
            hotMoves.forEach(prototype -> setOfAllMoves.addAll(prototype.collect(game, Optional.of(player))));
            if (!setOfAllMoves.isEmpty()) {
                result = checkMovePriorAndAuto(setOfAllMoves);
            }
        }
        return result;
    }


    /**
     * check Moves in a Set for Auto-Fire and Priority.
     *
     * @param setOfAllMoves A set which contains all possible Moves
     * @return move, if it has Auto-Fire or Priority
     * else null
     */
    private HotMove checkMovePriorAndAuto(Set<Move> setOfAllMoves) {
        HotMove resultMove = null;
        for (Move singleMove : setOfAllMoves) {
            // does move have priority?
            if (singleMove.hasPriority()) {
                resultMove = (HotMove) singleMove;
                // is move Auto-fire?
            } else {
                final boolean isAuto = singleMove.isAutoFire();
                if (setOfAllMoves.size() == 1 && isAuto)
                    // this line can not be tested as of yet, because there is no way to artificially create a priority-MoveClass
                    resultMove = (HotMove) singleMove;

            }
        }
        return resultMove;
    }

    @Override
    public Set<Move> getPrototypes() {
        return new HashSet<>(HotMoves.getPrototypes());
    }

}