package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * move Update PlantMarket
 * @author Severin
 */
public class UpdatePlantMarket implements HotMove {
    /**
     * the game.
     */
    private final OpenGame game;

    UpdatePlantMarket() {
        this.game = null;
    }

    private UpdatePlantMarket(OpenGame game) {
        this.game = game;
    }

    /**
     * takes upper most plant of the stack and sorts it into PlantMarket.
     * @param real false, um den Zug nur zu testen (keine Aenderung am Datastore) oder
     *             true, um ihn wirklich auszufueheren (aender das Datastore).
     */
    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        Set<Phase> phases = Set.of(Phase.PlantBuying, Phase.Building, Phase.Bureaucracy);
        //prove if phase is valid for this move
        if (!phases.contains(game.getPhase()))
            return Optional.of(Problem.NotNow);
        Set<Plant> actualPlants = game.getPlantMarket().getActual();
        // do plants actually exist?
        if(getGame().getPlantMarket().getActual().isEmpty())
            return Optional.of(Problem.NoPlants);
        if(getGame().getPlantMarket().getFuture().isEmpty())
            return Optional.of(Problem.NoPlants);
        if(real) {
            Optional<Plant> plant = game.getPlantMarket().getFuture().stream().findFirst();
            game.getPlantMarket().getFuture().remove(plant.get());
            game.getPlantMarket().getActual().add(plant.get());
        }
    return Optional.empty();
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame game, Optional<String> secret) {
        if(secret.isPresent()) return Set.of();
        if (this.game != null)
            throw new IllegalStateException("this is not a prototype");
        HotMove move = new UpdatePlantMarket(game);
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        Objects.requireNonNull(game);
        return MoveType.UpdatePlantMarket;
    }
}
