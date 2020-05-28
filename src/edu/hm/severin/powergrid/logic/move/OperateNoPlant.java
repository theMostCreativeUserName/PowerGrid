package edu.hm.severin.powergrid.logic.move;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

class OperateNoPlant implements HotMove {

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
    OperateNoPlant() {
        this.game = null;
        this.player = null;
    }

    /**
     * Non-Prototyp Constructor.
     * @param game this game.
     * @param player the player, who connects a city
     */
    private OperateNoPlant(OpenGame game, Optional<OpenPlayer> player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        if (game.getPhase() != Phase.PlantOperation)
            return Optional.of(Problem.NotNow);
        if (player.get().hasPassed())
            return Optional.of(Problem.AlreadyPassed);

        if (real) {

            player.get().setPassed(true);

        }
        return Optional.empty();
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> openPlayer) {
        if (this.game != null)
            throw new IllegalStateException("this is not a prototype");
        HotMove move = new OperateNoPlant(openGame, openPlayer);
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.OperateNoPlant;
    }


}
