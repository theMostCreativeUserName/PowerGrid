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
 * Auktion beenden und Kraftwerk an den Höchstbietenden verkaufen
 *
 * @author Auerbach
 */
public class CloseAuction implements HotMove {

    private final OpenGame game;

    CloseAuction(){
        game=null;
    }

    private CloseAuction(OpenGame game){
        this.game=game;
    }

    @Override
    public Optional<Problem> run(boolean real) {

        if (game.getPhase() != Phase.PlantAuction)
            return Optional.of(Problem.NotNow);
        if(game.getAuction().getOpenPlayers().size()>1)
            return Optional.of(Problem.AuctionRunning);
        if(real){
            game.getPlantMarket().getOpenActual().remove(game.getAuction());
            game.getAuction().getPlayer().getPlants().add(game.getAuction().getPlant());
            game.getAuction().getPlayer().setElectro(game.getAuction().getPlayer().getElectro()-game.getAuction().getAmount());
            game.getAuction().getPlayer().setPassed(true);
            game.setPhase(Phase.PlantBuying);
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
            throw new IllegalStateException("Not a prototype!");
        final HotMove move = new CloseAuction(game);
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.CloseAuction;
    }
}
