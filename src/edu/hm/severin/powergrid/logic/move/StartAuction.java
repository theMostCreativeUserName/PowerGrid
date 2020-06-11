package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
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
 * Spieler startet Auktion
 *
 * @author Auerbach
 */
public class StartAuction implements HotMove {

    private final OpenGame game;
    private final Optional<OpenPlayer> player;
    private final OpenPlant plant;

    StartAuction() {
        game = null;
        player = null;
        plant = null;
    }

    private StartAuction(OpenGame game, Optional<OpenPlayer> player, OpenPlant plant) {
        this.game = game;
        this.player = player;
        this.plant = plant;
    }

    private Optional<Problem> meetRequirements() {
        if (game.getPhase() != Phase.PlantBuying)
            return Optional.of(Problem.NotNow);
        final List<OpenPlayer> allRemainingPlayer = game.getOpenPlayers().stream().filter(OpenPlayer -> !OpenPlayer.hasPassed()).sequential().collect(Collectors.toList());
        if (allRemainingPlayer.size() == 0)
            return Optional.of(Problem.NotYourTurn);
        final OpenPlayer lastPlayerOfList = allRemainingPlayer.get(0);
        if (!lastPlayerOfList.equals(player.get()))
            return Optional.of(Problem.NotYourTurn);
        if (!game.getPlantMarket().getOpenActual().contains(plant)) {
            return Optional.of(Problem.PlantNotAvailable);
        }
        final int cost = plant.getNumber();
        if (cost > player.get().getElectro())
            return Optional.of(Problem.NoCash);
        return Optional.empty();
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Optional<Problem> problem = meetRequirements();
        if(problem.isPresent())
            return problem;
        if (real) {
            final List<OpenPlayer> players = game.getOpenPlayers();
            final List<OpenPlayer> notPassed = players.stream()
                    .filter(OpenPlayer -> !OpenPlayer.hasPassed())
                    .collect(Collectors.toList());
            final OpenAuction auction = game.getFactory().newAuction(plant, notPassed);
            auction.setAmount(plant.getNumber());
            final OpenPlayer currentPlayer = player.get();
            auction.setPlayer(currentPlayer);
            players.remove(currentPlayer);
            players.add(currentPlayer);
            game.setPhase(Phase.PlantAuction);
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
        final HotMove move = new StartAuction(game, player, plant);
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.StartAuction;
    }
}
