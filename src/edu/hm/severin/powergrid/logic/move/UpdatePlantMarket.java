package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

/**
 * move Update PlantMarket.
 *
 * @author Severin
 */
public class UpdatePlantMarket extends AbstractProperties implements HotMove {
    /**
     * the game.
     */
    private final OpenGame game;

    /**
     * prototype constructor.
     */
    UpdatePlantMarket() {
        this.game = null;
    }

    /**
     * non-prototype constructor.
     *
     * @param game game of the move
     */
    private UpdatePlantMarket(OpenGame game) {
        this.game = game;
    }

    /**
     * takes upper most plant of the stack and sorts it into PlantMarket.
     *
     * @param real false, um den Zug nur zu testen (keine Aenderung am Datastore) oder
     *             true, um ihn wirklich auszufueheren (aender das Datastore).
     */
    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        final Set<Phase> phases = Set.of(Phase.PlantBuying, Phase.Building, Phase.Bureaucracy);
        //prove if phase is valid for this move

        if (!phases.contains(game.getPhase()))
            return Optional.of(Problem.NotNow);


        // is Actual plants full?
        if (getGame().getPlantMarket().getActual().size() >= game.getEdition().getActualPlants(game.getLevel()))
            return Optional.of(Problem.PlantMarketFine);

        // do future plants and card deck exist?
        final Set<OpenPlant> plantFutureAndHidden = new HashSet<>(game.getPlantMarket().getOpenFuture());
        plantFutureAndHidden.addAll(game.getPlantMarket().getOpenHidden());
        if (plantFutureAndHidden.isEmpty())
            return Optional.of(Problem.NoPlants);
        if (real) {
            //take one plant of deck
            final OpenPlant plant = game.getPlantMarket().getOpenHidden().get(0);
            game.getPlantMarket().getOpenHidden().remove(plant);
            // check in which line of plants to sort
            sortPlantMarket(plant);
        }
        setProperty("type", getType().toString());
        return Optional.empty();
    }

    /**
     * sort the plant in the right market.
     *
     * @param plant plant for sorting
     */
    private void sortPlantMarket(OpenPlant plant) {
        final Set<OpenPlant> futureSet = game.getPlantMarket().getOpenFuture();
        final Set<OpenPlant> actualSet = game.getPlantMarket().getOpenActual();

        final OptionalInt minOfFuture = game.getPlantMarket().getOpenFuture().stream().mapToInt(Plant::getNumber).min();
        if (minOfFuture.isPresent()) {
            if (minOfFuture.getAsInt() < plant.getNumber()) {

                final OpenPlant openPlant = game.getPlantMarket().findPlant(minOfFuture.getAsInt());
                futureSet.remove(openPlant);
                actualSet.add(openPlant);
                futureSet.add(plant);
            } else
                actualSet.add(plant);
        } else
            actualSet.add(plant);

    }


    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame opengame, Optional<OpenPlayer> player) {
        if (this.game != null)
            throw new IllegalStateException("this is not a prototype");
        final HotMove move = new UpdatePlantMarket(opengame);
        final Set<HotMove> result;
        if (move.run(false).isEmpty()) {
            result = Set.of(move);
        } else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.UpdatePlantMarket;
    }
}
