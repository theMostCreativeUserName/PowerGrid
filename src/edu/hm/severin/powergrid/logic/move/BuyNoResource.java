package edu.hm.severin.powergrid.logic.move;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

class BuyNoResource implements HotMove {

    /**
     * Used game.
     */
    private final OpenGame game;

    /**
     * Used optional of player.
     */
    private final Optional<OpenPlayer> player;

    /**
     * Prototyp Constructor.
     */
    BuyNoResource() {
        this.game = null;
        this.player = null;
    }

    /**
     * Non-Prototyp Constructor.
     *
     * @param game   this game.
     * @param player optional of player.
     */
    private BuyNoResource(OpenGame game, Optional<OpenPlayer> player) {
        this.game = game;
        this.player = player;
    }


    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        if (game.getPhase() != Phase.ResourceBuying)
            return Optional.of(Problem.NotNow);
        final List<OpenPlayer> allRemainingPlayer = game.getOpenPlayers().stream().filter(OpenPlayer -> !OpenPlayer.hasPassed()).sequential().collect(Collectors.toList());
        if (allRemainingPlayer.size() == 0)
            return Optional.of(Problem.NotYourTurn);
        final OpenPlayer lastPlayerOfList = allRemainingPlayer.get(allRemainingPlayer.size() - 1);
        if (!lastPlayerOfList.equals(player.get()))
            return Optional.of(Problem.NotYourTurn);

        if (real) {
            //player passes turn without doing anything
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
        final HotMove move = new BuyNoResource(openGame, openPlayer);
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.BuyNoResource;
    }


}
