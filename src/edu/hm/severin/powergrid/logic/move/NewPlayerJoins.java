package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.RandomSource;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * creates a new player for the Game-Session.
 * @author Severin
 */
class NewPlayerJoins implements HotMove {
    /**
     * the game.
     */
    private final OpenGame game;

    /**
     * Prototype - CTor.
     */
    public NewPlayerJoins() {
        game = null;
    }

    private NewPlayerJoins(OpenGame game) {
        this.game = game;
    }

    /**
     * creates a new player.
     *
     * @param real false, um den Zug nur zu testen (keine Aenderung am Datastore) oder
     *             true, um ihn wirklich auszufueheren (aender das Datastore).
     */
    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        // test if possible
        // if real, if Phase correct, if mayNumb not reached
        if (game.getPhase() != Phase.Opening) return Optional.of(Problem.NotNow);
        if (game.getPlayers().size() >= game.getEdition().getPlayersMaximum()) return Optional.of(Problem.MaxPlayers);
        if (real) {
            //player color
            final String newColor = game.getEdition().getPlayerColors().get(game.getPlayers().size());
            // secret for player
            final String newSecret = RandomSource.make().babbled(newColor);
            //create Player
            final OpenPlayer player = game.getFactory().newPlayer(newSecret, newColor);
            // insert in Game
            game.getOpenPlayers().add(player);
            //game.getPlayers().add(player);
        }
        return Optional.empty();
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame game, Optional<OpenPlayer> player) {
        if (player.isPresent()) return Set.of();
        if (this.game != null)
            throw new IllegalStateException("this is not a prototype");
        final HotMove move = new NewPlayerJoins(game);
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
        return MoveType.JoinPlayer;
    }

}
