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

/**
 * Spieler steigt aus Auktion aus
 * @author Auerbach
 */

public class LeaveAuction implements HotMove {

    private final OpenGame game;

    private final Optional<OpenPlayer> player;

    LeaveAuction() {
        game = null;
        player=null;
    }

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
        System.out.println(player.get() + " '''''''' ");
        if (real){
            game.getAuction().getOpenPlayers().remove(player.get());
        }
        return Optional.empty();
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame game, Optional<OpenPlayer> player) {
        if (this.game != null)
            throw new IllegalStateException("Not a prototype");
        final HotMove move = new LeaveAuction(game, player);
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
