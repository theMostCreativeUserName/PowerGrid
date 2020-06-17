package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * remove scrap plants.
 *
 */
public class ScrapPlant extends AbstractProperties implements HotMove {
    /**
     * the game.
     */
    private final OpenGame game;

    /**
     * the plant to check.
     */
    private final OpenPlant plant;

    /**
     * Prototyp Constructor.
     */
    ScrapPlant() {
        this.game = null;
        this.plant = null;
    }

    /**
     * Non-Prototyp Constructor.
     * @param game this game
     * @param plant plant to check
     */
    private ScrapPlant(OpenGame game, OpenPlant plant) {
        this.game = game;
        this.plant = plant;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        if (!game.getPlantMarket().getOpenActual().contains(plant))
            return Optional.of(Problem.PlantNotAvailable);
        final Integer highestAmountOfCities = game.getOpenPlayers().stream().map(openPlayer -> openPlayer.getOpenCities().size()).max(Integer::compare).get();
        if (highestAmountOfCities <= plant.getNumber())
            return Optional.of(Problem.PlantSave);

        if (real) {
            game.getPlantMarket().removePlant(plant.getNumber());
        }
        setProperty("type", getType().toString());
        setProperty("plant",  String.valueOf(plant.getNumber()));
        return Optional.empty();
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    /**
     * could the move be run.
     *
     * @param openGame Aktuelles Spiel.
     * @param player   der Spieler um den es geht.
     * @return this move, if it could be run
     */
    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> player) {
        if (this.game != null) throw new IllegalStateException("this is not a prototype!");
        return openGame.getPlantMarket().getOpenActual()
                .stream()
                .map(openPlant -> new ScrapPlant(openGame, openPlant))
                .filter(move -> move.test().isEmpty())
                .collect(Collectors.toSet());
    }

    @Override
    public MoveType getType() {
        return MoveType.ScrapPlant;
    }
}
