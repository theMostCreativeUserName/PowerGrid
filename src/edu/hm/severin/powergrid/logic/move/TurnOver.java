package edu.hm.severin.powergrid.logic.move;


import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * refills resourceMarket and starts a new turn.
 *
 * @author Pietsch
 */
class TurnOver implements HotMove {

    /**
     * Used game.
     */
    private final OpenGame game;

    /**
     * Prototyp Constructor.
     */
    TurnOver() {
        game = null;
    }

    /**
     * Non-Prototyp Constructor.
     *
     * @param game this game.
     */
    private TurnOver(OpenGame game) {
        this.game = game;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        if (game.getPhase() != Phase.Bureaucracy)
            return Optional.of(Problem.NotNow);

        if (real) {
            editResourceMarket();
            editPlantMarket();
            final int currentRound = game.getRound();
            game.setRound(currentRound + 1);
            game.setPhase(Phase.PlayerOrdering);
        }

        return Optional.empty();
    }

    /**
     * Doing all editting work for PlantMarket.
     */
    private void editPlantMarket() {
        final List<Integer> allNumbers = game.getPlantMarket()
                .getOpenActual()
                .stream()
                .map(OpenPlant::getNumber)
                .collect(Collectors.toList());
        if (game.getLevel() == 2) {
            final Optional<Integer> smallestNumber = allNumbers.stream().min(Integer::compareTo);
            if (smallestNumber.isPresent())
                game.getPlantMarket().removePlant(smallestNumber.get());

        } else {
            final Optional<Integer> biggestNumber = game.getPlantMarket().getOpenFuture().stream().map(x -> x.getNumber()).max(Integer::compareTo);
            if (biggestNumber.isPresent()) {
                final OpenPlant plant = game.getPlantMarket().findPlant(biggestNumber.get());
                game.getPlantMarket().removePlant(biggestNumber.get());
                game.getPlantMarket().getOpenHidden().add(plant);
            }
        }
    }

    /**
     * Doing all editting work for ResourceMarket.
     */
    private void editResourceMarket() {
        final Bag<Resource> available = game.getResourceMarket().getOpenAvailable();
        final Bag<Resource> supply = game.getResourceMarket().getOpenSupply();
        final Map<Resource, List<List<Integer>>> resourceSupply = game.getEdition().getResourcePlayersToSupply();
        for (Resource resource : resourceSupply.keySet()) {
            final List<List<Integer>> list = resourceSupply.get(resource);
            final int amount = list.get(game.getOpenPlayers().size()).get(game.getLevel());
            for (int editAmount = 0; editAmount < amount; editAmount++) {
                if (supply.count(resource) > 0)
                    available.add(resource);
                supply.remove(resource);
            }
        }
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> player) {
        if (this.game != null)
            throw new IllegalStateException("This ist not a protoype");
        final HotMove move = new TurnOver(openGame);
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.TurnOver;
    }
}
