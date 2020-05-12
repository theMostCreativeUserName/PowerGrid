package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class NewPlayerJoins implements HotMove {
    private final OpenGame game;
    private final Optional<String> secret;
    public final boolean priority = false;

    public NewPlayerJoins() {
        game = null;
        secret = null;
    }

    private NewPlayerJoins(OpenGame game, Optional<String> secret) {
        this.game = game;
        this.secret = secret;
    }

    /**
     * runs the Move.
     * @param real false, um den Zug nur zu testen (keine Aenderung am Datastore) oder
     *             true, um ihn wirklich auszufueheren (aender das Datastore).
     */
    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        // test if possible
        // if real, if Phase correct, if mayNumb not reached
        if(game.getPhase() != Phase.Opening) return Optional.of(Problem.NotNow);
        if(game.getPlayers().size() >= game.getEdition().getPlayersMaximum()) return Optional.of(Problem.MaxPlayers);
        if(secret.isPresent()) return Optional.of(Problem.NotNow);
        if(real){
            // code to fire....
            // secret for player
            //String newSecret = Integer.toString(game.getPlayers().size());
            BigInteger number = new BigInteger(80, new SecureRandom());
            String newSecret = number.toString();
            //player color
            String newColor = game.getEdition().getPlayerColors().get(game.getPlayers().size());
            //create Player
            OpenPlayer player = game.getFactory().newPlayer(newSecret, newColor);
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
    public Set<HotMove> collect(OpenGame game, Optional<String> secret) {
        if(this.game != null)
            throw new IllegalStateException("this is not a prototype");
        HotMove move = new NewPlayerJoins(game, secret);
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
        return MoveType.JoinPlayer;
    }

}
