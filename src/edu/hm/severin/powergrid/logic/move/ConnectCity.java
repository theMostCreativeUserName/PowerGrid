package edu.hm.severin.powergrid.logic.move;


import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
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
 * a player connect a city.
 *
 * @author Pietsch
 */
class ConnectCity extends AbstractProperties implements HotMove {

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
    ConnectCity() {
        game = null;
        player = null;
        city = null;
    }

    /**
     * Non-Prototyp Constructor.
     *
     * @param city   citiy to be build.
     * @param game   this game
     * @param player player who connects the city
     */
    private ConnectCity(OpenGame game, Optional<OpenPlayer> player, OpenCity city) {
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

        final List<Integer> costConnectionList = player.get()
                .getOpenCities()
                .stream()
                .map(openCity -> openCity.getOpenConnections().get(city))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (costConnectionList.isEmpty())
            return Optional.of(Problem.NoCities);
        final int connectionCost = costConnectionList.stream().min(Integer::compareTo).get();
        final int cost = game.getEdition().levelToCityCost().get(game.getLevel()) + connectionCost;
        if (cost > player.get().getElectro())
            return Optional.of(Problem.NoCash);

        if (real) {
            player.get().getOpenCities().add(city);
            final int currentAmount = player.get().getElectro();
            player.get().setElectro(currentAmount - cost);

        }
        setProperty("type", getType().toString());
        setProperty("player", player.get().getColor());
        setProperty("city", city.getName());
        return Optional.empty();
    }

    /**
     * Check all, excluded game and city.
     *
     * @return problem
     */
    private Optional<Problem> allRequirements() {
        if (game.getPhase() != Phase.Building)
            return Optional.of(Problem.NotNow);
        if (testPlayerLastNotPassed().isPresent())
            return testPlayerLastNotPassed();
        if (cityTakenOrConnected().isPresent())
            return cityTakenOrConnected();
        return Optional.empty();
    }

    /**
     * Check all, if PlayerLastNotPassed.
     *
     * @return problem
     */
    private Optional<Problem> testPlayerLastNotPassed() {
        final List<OpenPlayer> allRemainingPlayer = game.getOpenPlayers().stream().filter(openPlayer -> !openPlayer.hasPassed()).sequential().collect(Collectors.toList());
        if (allRemainingPlayer.isEmpty())
            return Optional.of(Problem.NotYourTurn);
        final OpenPlayer lastPlayerOfList = allRemainingPlayer.get(allRemainingPlayer.size() - 1);
        if (!lastPlayerOfList.equals(player.get()))
            return Optional.of(Problem.NotYourTurn);
        return Optional.empty();
    }

    /**
     * Check if a city is already taken or connected.
     *
     * @return problem
     */
    private Optional<Problem> cityTakenOrConnected() {
        if (player.get().getOpenCities().contains(city))
            return Optional.of(Problem.CityAlreadyConnected);
        final boolean takenCity = game.getOpenPlayers().stream().anyMatch(openPlayer -> openPlayer.getOpenCities().stream().anyMatch(openCity -> openCity.equals(city)));
        if (takenCity)
            return Optional.of(Problem.CityTaken);
        return Optional.empty();
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> openPlayer) {
        if (this.game != null)
            throw new IllegalStateException("This ist not a prototype");
        return openGame.getBoard().getOpenCities()
                .stream()
                .map(openCity -> new ConnectCity(openGame, openPlayer, openCity))
                .filter(move -> move.test().isEmpty())
                .collect(Collectors.toSet());
    }

    @Override
    public MoveType getType() {
        return MoveType.ConnectCity;
    }
}
