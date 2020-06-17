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

/**
 * Spieler steigt aus Auktion aus.
 * @author Auerbach
 */

public class LeaveAuction extends AbstractProperties implements HotMove {

    /**
     * game of move.
     */
    private final OpenGame game;

    /**
     * Optional of Player for move.
     */
    private final Optional<OpenPlayer> player;

    /**
     * prototype constructor.
     */
    LeaveAuction() {
        game = null;
        player=null;
    }

    /**
     * non-prototype constructor.
     * @param game game of move
     * @param player optional of player of move
     */
    private LeaveAuction(OpenGame game,Optional<OpenPlayer>player){
        this.game=game;
        this.player=player;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        if (game.getPhase() != Phase.PlantAuction)
            return Optional.of(Problem.NotNow);
        if (!game.getAuction().getOpenPlayers().get(0).equals(player.get()))
            return Optional.of(Problem.NotYourTurn);
        if (game.getAuction().getPlayer().equals(player.get()))
            return Optional.of(Problem.TopBidder);
        if (real){
            game.getAuction().getOpenPlayers().remove(player.get());
        }
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
            throw new IllegalStateException("Not a prototype");
        final HotMove move = new LeaveAuction(openGame, openPlayer);
        Set<HotMove> result;
        if(move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.LeaveAuction;
    }
}
