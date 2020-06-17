package edu.hm.severin.powergrid.logic.move;


import edu.hm.cs.rs.powergrid.datastore.Phase;

import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * a player connects no city.
 *
 * @author Pietsch
 */
class ConnectNoCity extends AbstractProperties implements HotMove {

    /**
     * Used game.
     */
    private final OpenGame game;

    /**
     * User Player.
     */
    private final Optional<OpenPlayer> player;

    /**
     * Prototyp Constructor.
     */
    ConnectNoCity() {
        game = null;
        player = null;
    }

    /**
     * Non-Prototyp Constructor.
     * @param game this game.
     * @param player the player, who connects a city
     */
    private ConnectNoCity(OpenGame game, Optional<OpenPlayer> player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        if (game.getPhase() != Phase.Building)
            return Optional.of(Problem.NotNow);
        final List<OpenPlayer> allRemainingPlayer = game.getOpenPlayers().stream().filter(openPlayer -> !openPlayer.hasPassed()).sequential().collect(Collectors.toList());
        if (allRemainingPlayer.isEmpty())
            return Optional.of(Problem.NotYourTurn);
        final OpenPlayer lastPlayerOfList = allRemainingPlayer.get(allRemainingPlayer.size() - 1);
        if (!lastPlayerOfList.equals(player.get()))
            return Optional.of(Problem.NotYourTurn);

        if (real)
            player.get().setPassed(true);
        setProperty("type", getType().toString());
        setProperty("player", player.get().getColor());
        return Optional.empty();
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> openPlayer) {
        if (this.game != null)
            throw new IllegalStateException("This ist not a protoype");
        final HotMove move = new ConnectNoCity(openGame, openPlayer);
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.ConnectNoCity;
    }
}
