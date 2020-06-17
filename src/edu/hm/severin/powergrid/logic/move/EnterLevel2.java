package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * enter level 2.
 */
public class EnterLevel2 extends AbstractProperties implements HotMove {
    /**
     * the game.
     */
    private final OpenGame game;

    /**
     * Prototyp Constructor.
     */
    EnterLevel2() {
        this.game = null;
    }

    /**
     * Non-Prototype Constructor.
     * @param game this game
     */
    private EnterLevel2(OpenGame game) {
        this.game = game;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        if (game.getPhase() != Phase.Bureaucracy)
            return Optional.of(Problem.NotNow);
        if (game.getLevel() != 0)
            return Optional.of(Problem.WrongLevel);

        final Integer highestAmountOfCities = game.getOpenPlayers().stream().map(openPlayer -> openPlayer.getOpenCities().size()).max(Integer::compare).get();
        final int neededCities = game.getEdition().getPlayersLevel2Cities().get(game.getOpenPlayers().size());
        if (highestAmountOfCities < neededCities)
            return Optional.of(Problem.NoCities);


        if (real) {
            game.setLevel(1);
            final int numberOfSmallestPlant = game.getPlantMarket().getOpenActual().stream().map(Plant::getNumber).min(Integer::compareTo).get();
            game.getPlantMarket().removePlant(numberOfSmallestPlant);
        }
        setProperty("type", getType().toString());
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
        if (this.game != null)
            throw new IllegalStateException("This ist not a protoype");
        final HotMove move = new EnterLevel2(openGame);
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.EnterLevel2;
    }
}
