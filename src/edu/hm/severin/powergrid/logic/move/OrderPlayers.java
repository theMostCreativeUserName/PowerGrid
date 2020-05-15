package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.RandomSource;
import edu.hm.cs.rs.powergrid.logic.SortingRandomSource;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.*;
import java.util.stream.Collectors;

/**
 * orders players.
 *
 * @author Severin
 */
public class OrderPlayers implements HotMove {
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
            List<OpenPlayer> players = game.getOpenPlayers();
            long playersNoPlant = players.stream().filter(player -> player.getPlants().isEmpty()).count();
            if (playersNoPlant == players.size()) {
                RandomSource.make().shufflePlayers(players);
            } else {
                //ToDo: finisch this shit
                //ignore this this is not finished....
                //why?????
               players.stream().sorted(this::comparePlayers);
            }
        }
        return Optional.empty();
    }

    private int comparePlayers(Player first, Player second) {
        int result;
        result = second.getCities().size() - first.getCities().size();
        if (result == 0) {
            OptionalInt firstMax = first.getPlants().stream().mapToInt(plant -> plant.getNumber()).max();
            OptionalInt secondMax = second.getPlants().stream().mapToInt(plant -> plant.getNumber()).max();
            result = secondMax.getAsInt() - firstMax.getAsInt();
        }
        return result;
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame game, Optional<String> secret) {
        if (secret.isPresent()) return Set.of();
        if (this.game != null) throw new IllegalStateException("this is not a prototype!");
        HotMove move = new OrderPlayers(game);
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
