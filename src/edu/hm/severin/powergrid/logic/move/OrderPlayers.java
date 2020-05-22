package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.RandomSource;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.*;
import java.util.stream.Collectors;

/**
 * orders players.
 *
 * @author Severin
 */
public class OrderPlayers implements HotMove {
    /** the game of this session.**/
    private final OpenGame game;

    OrderPlayers() {
        this.game = null;
    }

    private OrderPlayers(OpenGame game) {
        this.game = game;
    }

    /**
     * sorts the list of players.
     *
     * @param real false, um den Zug nur zu testen (keine Aenderung am Datastore) oder
     *             true, um ihn wirklich auszufueheren (aender das Datastore).
     */
    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        if (game.getPhase() != Phase.PlayerOrdering) return Optional.of(Problem.NotNow);
        if (real) {
            // none have plants
            final List<OpenPlayer> players = game.getOpenPlayers();
            final int playersPlantNumber = players.stream().mapToInt(openPlayer -> openPlayer.getOpenPlants().size()).sum();

            if (playersPlantNumber == 0) {
                RandomSource.make().shufflePlayers(players);
                //order players
            } else {
                final List<OpenPlayer> list = players
                        .stream()
                        .sorted(OpenPlayer::compareTo)
                        .collect(Collectors.toList());

                game.getOpenPlayers().clear();
                game.getOpenPlayers().addAll(list);
            }
        }
        return Optional.empty();
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> player) {
        if (player.isPresent()) return Set.of();
        if (this.game != null) throw new IllegalStateException("this is not a prototype!");
        final HotMove move = new OrderPlayers(openGame);
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        Objects.requireNonNull(game);
        return MoveType.OrderPlayers;
    }
}
