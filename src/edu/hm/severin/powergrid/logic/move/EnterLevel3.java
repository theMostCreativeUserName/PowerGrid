package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.RandomSource;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * enter level 3.
 *
 * @author Severin
 */
public class EnterLevel3 implements HotMove {
    /**
     * the game.
     */
    private final OpenGame game;

    EnterLevel3() {
        this.game = null;
    }

    private EnterLevel3(OpenGame game) {
        this.game = game;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        final List<Phase> allowedPhases = List.of(Phase.ResourceBuying, Phase.PlantOperation, Phase.PlayerOrdering);
        if (!allowedPhases.contains(game.getPhase()))
            return Optional.of(Problem.NotNow);
        if (game.getLevel() > 1)
            return Optional.of(Problem.WrongLevel);

        List plantLevel3 = game.getPlantMarket().getOpenActual()
                .stream()
                .map(openPlant -> openPlant.getType())
                .collect(Collectors.toList());

        if (!plantLevel3.contains(Plant.Type.Level3))
            return Optional.of(Problem.NoPlants);


            if (real) {
                int level3Plant =getGame().getPlantMarket().getOpenActual()
                        .stream()
                        .filter(plant -> plant.getType() == Plant.Type.Level3)
                        .map(Plant::getNumber)
                        .findFirst()
                        .get();

                // remove level3 plant from market
                getGame().getPlantMarket().removePlant(level3Plant);

                int smallestNumberOfPlant = getGame().getPlantMarket().getOpenActual().stream()
                        .map(Plant::getNumber)
                        .min(Integer::compareTo)
                        .get();
                game.getPlantMarket().removePlant(smallestNumberOfPlant);
                RandomSource.make().shufflePlants(game.getPlantMarket().getOpenHidden());
                game.setLevel(2);

            }
        return Optional.empty();
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    /**
     * could the move be run.
     *
     * @param openGame game.
     * @param player  player, whose turn it is.
     * @return this move, if it could be run
     */
    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> player) {
        if (player.isPresent()) return Set.of();
        if (this.game != null) throw new IllegalStateException("this is not a prototype!");
        final HotMove move = new EnterLevel3(openGame);
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.EnterLevel3;
    }
}
