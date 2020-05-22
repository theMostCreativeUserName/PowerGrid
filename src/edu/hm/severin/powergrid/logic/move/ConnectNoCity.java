package edu.hm.severin.powergrid.logic.move;


import edu.hm.cs.rs.powergrid.datastore.Phase;

import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

 class ConnectNoCity implements edu.hm.cs.rs.powergrid.logic.move.HotMove {

    private final OpenGame game;
    private final Optional<OpenPlayer> player;
    ConnectNoCity() {
        game = null;
        player = null;
    }
    private ConnectNoCity(OpenGame game, Optional<OpenPlayer> player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        if (game.getPhase() != Phase.Building)
            return Optional.of(Problem.NotNow);
        if (player.get().hasPassed())
            return Optional.of(Problem.AlreadyPassed);

        if (real)
            player.get().setPassed(true);
        return Optional.empty();
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame game, Optional<OpenPlayer> player) {
        if (this.game != null)
            throw new IllegalStateException("This ist not a protoype");
        HotMove move = new ConnectNoCity(game, player);
        Set<HotMove> result;
        if(move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        Objects.requireNonNull(game);
        return MoveType.ConnectNoCity;
    }
}
