package edu.hm.severin.powergrid.logic.move;


import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
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

/**
 * a player connects no city.
 *
 * @author Pietsch
 */
class SupplyElectricity implements HotMove {

    /**
     * Used game.
     */
    private final OpenGame game;


    /**
     * Prototyp Constructor.
     */
    SupplyElectricity() {
        game = null;
    }

    /**
     * Non-Prototyp Constructor.
     *
     * @param game this game.
     */
    private SupplyElectricity(OpenGame game) {
        this.game = game;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        if (game.getPhase() != Phase.PlantOperation)
            return Optional.of(Problem.NotNow);
        final List<OpenPlayer> players = game.getOpenPlayers();
        if (players.stream().filter(Player::hasPassed).count() == 0)
            return Optional.of(Problem.NotNow);

        if (real) {
            game.setPhase(Phase.Bureaucracy);
            for (OpenPlayer player : players)
                player.setPassed(false);

            //plant incoming and resetting
            plantEditing();

        }
        return Optional.empty();
    }

    /**
     * Editing Player and Plant for run true.
     */
    private void plantEditing() {
        final List<OpenPlayer> players = game.getOpenPlayers();
        final List<Integer> moneyGainedPerPlants = game.getEdition().getPoweredCitiesIncome();

        for (OpenPlayer player : players) {
            final long maximalCitiesSupply = player.getOpenPlants().stream().map(Plant::getCities).count();
            final int maximalCitiesPlayer = player.getOpenCities().size();
            final int suppliedCities;
            if (maximalCitiesSupply >= maximalCitiesPlayer)
                suppliedCities = maximalCitiesPlayer;
            else
                suppliedCities = (int) maximalCitiesSupply;

            //Money Editing
            final int currentAmountMoney = player.getElectro();
            final int gainedMoney = moneyGainedPerPlants.get(suppliedCities);
            player.setElectro(currentAmountMoney + gainedMoney);

            //Reset Plants
            for (OpenPlant plant : player.getOpenPlants())
                plant.setOperated(false);
        }

    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> openPlayer) {
        if (this.game != null)
            throw new IllegalStateException("This ist not a protoype");
        final HotMove move = new SupplyElectricity(openGame);
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.SupplyElectricity;
    }
}
