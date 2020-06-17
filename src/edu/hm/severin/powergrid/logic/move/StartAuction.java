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
 * Spieler startet Auktion.
 *
 * @author Auerbach
 */
public class StartAuction extends AbstractProperties implements HotMove {

    /**
     * game of move.
     */
    private final OpenGame game;

    /**
     * optional of player for move.
     */
    private final Optional<OpenPlayer> player;

    /**
     * plant for move.
     */
    private final OpenPlant plant;

    /**
     * prototype constructor.
     */
    StartAuction() {
        game = null;
        player = null;
        plant = null;
    }

    /**
     * non-prototype constructor.
     * @param game game of the move
     * @param player optional of player of the move
     * @param plant plant of the move
     */
    private StartAuction(OpenGame game, Optional<OpenPlayer> player, OpenPlant plant) {
        this.game = game;
        this.player = player;
        this.plant = plant;
    }

    /**
     * checks if all Requirements are meet.
     * @return Optional of Problem, empty when no problem
     */
    private Optional<Problem> meetRequirements() {
        if (game.getPhase() != Phase.PlantBuying)
            return Optional.of(Problem.NotNow);
        final List<OpenPlayer> allRemainingPlayer = game.getOpenPlayers().stream().filter(openPlayer -> !openPlayer.hasPassed()).sequential().collect(Collectors.toList());
        if (allRemainingPlayer.isEmpty())
            return Optional.of(Problem.NotYourTurn);
        final OpenPlayer lastPlayerOfList = allRemainingPlayer.get(0);
        if (!lastPlayerOfList.equals(player.get()))
            return Optional.of(Problem.NotYourTurn);
        if (!game.getPlantMarket().getOpenActual().contains(plant))
            return Optional.of(Problem.PlantNotAvailable);
        final int cost = plant.getNumber();
        if (cost > player.get().getElectro())
            return Optional.of(Problem.NoCash);
        return Optional.empty();
    }

    @Override
    public Optional<Problem> run(boolean real) {
        if(meetRequirements().isPresent())
            return meetRequirements();
        if (real) {
            final List<OpenPlayer> players = game.getOpenPlayers();
            final List<OpenPlayer> notPassed = players.stream()
                    .filter(openPlayer -> !openPlayer.hasPassed())
                    .collect(Collectors.toList());
            final OpenAuction auction = game.getFactory().newAuction(plant, notPassed);
            final OpenPlayer currentPlayer = player.get();
            auction.getOpenPlayers().remove(currentPlayer);
            auction.getOpenPlayers().add(currentPlayer);
            game.setPhase(Phase.PlantAuction);
            game.setAuction(auction);
        }
        setProperty("type", getType().toString());
        setProperty("player", player.get().getColor());
        setProperty("plant", String.valueOf(plant.getNumber()));
        return Optional.empty();
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> openPlayer) {
        if (this.game != null)
            throw new IllegalStateException("Not a prototype!");
        return openGame.getPlantMarket().getOpenActual()
                .stream()
                .map(openPlant -> new StartAuction(openGame, openPlayer, openPlant))
                .filter(move -> move.test().isEmpty())
                .collect(Collectors.toSet());
    }

    @Override
    public MoveType getType() {
        return MoveType.StartAuction;
    }
}
