package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Auktion beenden und Kraftwerk an den Höchstbietenden verkaufen.
 *
 * @author Auerbach
 */
public class CloseAuction extends AbstractProperties implements HotMove {

    /**
     * game of move.
     */
    private final OpenGame game;

    /**
     * constructor of prototype.
     */
    CloseAuction(){
        game=null;
    }

    /**
     * constructor of non-prototype.
     * @param openGame game of the move
     */
    private CloseAuction(OpenGame openGame){
        this.game=openGame;
    }

    @Override
    public Optional<Problem> run(boolean real) {

        if (game.getPhase() != Phase.PlantAuction)
            return Optional.of(Problem.NotNow);
        if(game.getAuction().getOpenPlayers().size()>1)
            return Optional.of(Problem.AuctionRunning);
        if(real){
            final OpenPlant plant = game.getAuction().getPlant();
            game.getPlantMarket().getOpenActual().remove(plant);
            game.getAuction().getPlayer().getOpenPlants().add(plant);
            final int newAmountElectro = game.getAuction().getPlayer().getElectro()-game.getAuction().getAmount();
            game.getAuction().getPlayer().setElectro(newAmountElectro);
            game.getAuction().getPlayer().setPassed(true);
            game.setPhase(Phase.PlantBuying);
        }
        setProperty("type", getType().toString());
        return Optional.empty();
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> player) {
        if (this.game != null)
            throw new IllegalStateException("Not a prototype!");
        final HotMove move = new CloseAuction(openGame);
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
