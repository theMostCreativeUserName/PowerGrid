package edu.hm.severin.powergrid.logic.move;


import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * a player builds their first city.
 *
 * @author Pietsch
 */
class Build1stCity implements HotMove {

    /**
     * Used game.
     */
    private final OpenGame game;

    /**
     * Used Player if present.
     */
    private final Optional<OpenPlayer> player;

    /**
     * Used City.
     */
    private final OpenCity city;

    /**
     * Prototyp Constructor.
     */
    Build1stCity() {
        game = null;
        player = null;
        city = null;
    }

    /**
     * Non-Prototyp Constructor.
     * @param city citiy to be build.
     * @param game this game
     * @param player player who connects the city
     */
    private Build1stCity(OpenGame game, Optional<OpenPlayer> player, OpenCity city) {
        this.game = game;
        this.player = player;
        this.city = city;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        final Optional<Problem> result = allRequirements();

        if (result.isPresent())
            return result;
        if (cityTaken())
            return Optional.of(Problem.CityTaken);
        if (real) {
            final int cost = game.getEdition().levelToCityCost().get(game.getLevel());
            final int currentAmount = player.get().getElectro();
            player.get().setElectro(currentAmount - cost);
            player.get().getOpenCities().add(city);
        }
        return Optional.empty();
    }

    /**
     * Check all, excluded game and city.
     * @return problem
     */
    private Optional<Problem> allRequirements() {
        if (game.getPhase() != Phase.Building)
            return Optional.of(Problem.NotNow);
        if (player.get().hasPassed())
            return Optional.of(Problem.AlreadyPassed);
        if (player.get().getOpenCities().size() != 0)
            return Optional.of(Problem.HasCities);
        final int cost = game.getEdition().levelToCityCost().get(game.getLevel());
        if (cost > player.get().getElectro())
            return Optional.of(Problem.NoCash);
        return Optional.empty();
    }

    /**
     * Check if a city is already taken.
     * @return true, if citiy is already taken
     *          false, if city is not yet taken
     */
    private boolean cityTaken() {
        for (OpenPlayer openPlayer : game.getOpenPlayers())
            for (OpenCity openCity : openPlayer.getOpenCities())
                if (openCity.equals(city))
                    return true;

        return false;
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> openPlayer) {
        if (this.game != null)
            throw new IllegalStateException("This ist not a prototype");
        return openGame.getBoard()
                .getOpenCities()
                .stream()
                .map(openCity -> new Build1stCity(openGame, openPlayer, openCity))
                .filter(move -> move.test().isEmpty())
                .collect(Collectors.toSet());
    }

    @Override
    public MoveType getType() {
        Objects.requireNonNull(game);
        return MoveType.Build1stCity;
    }
}
