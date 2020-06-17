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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * a player operate a Plant.
 *
 * @author Pietsch
 */
class OperatePlant extends AbstractProperties implements HotMove {

    /**
     * Used game.
     */
    private final OpenGame game;

    /**
     * Used Player if present.
     */
    private final Optional<OpenPlayer> player;

    /**
     * Plant for Operate.
     */
    private final OpenPlant plant;

    /**
     * Bag of Resources for Operate.
     */
    private final Bag<Resource> resource;

    /**
     * Prototyp Constructor.
     */
    OperatePlant() {
        game = null;
        player = null;
        plant = null;
        resource = null;
    }

    /**
     * Non-Prototyp Constructor.
     *
     * @param game     this game
     * @param player   player who connects the city
     * @param plant    Plant to Check
     * @param resource resource to buy
     */
    private OperatePlant(OpenGame game, Optional<OpenPlayer> player, OpenPlant plant, Bag<Resource> resource) {
        this.game = game;
        this.player = player;
        this.plant = plant;
        this.resource = resource;

    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        if (game.getPhase() != Phase.PlantOperation)
            return Optional.of(Problem.NotNow);
        if (allRequirements().isPresent())
            return allRequirements();
        if (!player.get().getOpenResources().contains(resource))
            return Optional.of(Problem.NoResource);


        if (real) {
            plant.setOperated(true);
            player.get().getOpenResources().remove(resource);
            game.getResourceMarket().getOpenSupply().addAll(resource);
        }
        setProperty("type", getType().toString());
        setProperty("player", player.get().getColor());
        setProperty("plant", String.valueOf(plant.getNumber()));
        setProperty("resources", resource.toString() );
        return Optional.empty();
    }

    /**
     * Check all, excluded phase and storage.
     *
     * @return optional of problem
     */
    private Optional<Problem> allRequirements() {
        final List<OpenPlayer> allRemainingPlayer = game.getOpenPlayers().stream().filter(openPlayer -> !openPlayer.hasPassed()).sequential().collect(Collectors.toList());
        if (allRemainingPlayer.isEmpty())
            return Optional.of(Problem.NotYourTurn);
        final OpenPlayer lastPlayerOfList = allRemainingPlayer.get(0);
        if (!lastPlayerOfList.equals(player.get()))
            return Optional.of(Problem.NotYourTurn);
        if (!player.get().getOpenPlants().contains(plant))
            return Optional.of(Problem.NoPlants);
        if (plant.hasOperated())
            return Optional.of(Problem.PlantHasOperated);

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
        final Set<OperatePlant> moves = new HashSet<>();
        for (OpenPlant openPlant : openPlayer.get().getOpenPlants()) {
            if (openPlant.getResources().size() == 1)
                moves.add(new OperatePlant(openGame, openPlayer, openPlant, openPlant.getResources().iterator().next()));
            else
                moves.addAll(openPlant.getResources()
                        .stream()
                        .map(bagOfResource -> new OperatePlant(openGame, openPlayer, openPlant, bagOfResource))
                        .collect(Collectors.toSet()));
        }
        return moves.stream().filter(move -> move.test().isEmpty())
                .collect(Collectors.toSet());
    }


    @Override
    public MoveType getType() {
        return MoveType.OperatePlant;
    }
}
