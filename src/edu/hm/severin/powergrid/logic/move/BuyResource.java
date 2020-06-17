package edu.hm.severin.powergrid.logic.move;


import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import edu.hm.severin.powergrid.ListBag;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * a player bought a resource.
 *
 * @author Pietsch
 */
class BuyResource extends AbstractProperties implements HotMove {

    /**
     * Used game.
     */
    private final OpenGame game;

    /**
     * Used Player if present.
     */
    private final Optional<OpenPlayer> player;

    /**
     * Resource of Move.
     */
    private final Resource resourceOfMove;

    /**
     * Prototyp Constructor.
     */
    BuyResource() {
        game = null;
        player = null;
        resourceOfMove = null;
    }

    /**
     * Non-Prototyp Constructor.
     *
     * @param game           this game
     * @param player         player who connects the city
     * @param resourceOfMove resource to buy
     */
    private BuyResource(OpenGame game, Optional<OpenPlayer> player, Resource resourceOfMove) {
        this.game = game;
        this.player = player;
        this.resourceOfMove = resourceOfMove;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        if (game.getPhase() != Phase.ResourceBuying)
            return Optional.of(Problem.NotNow);

        final Optional<Problem> result = allRequirements();
        if (result.isPresent())
            return result;

        final Optional<Problem> result2 = storageOfPlayer();
        if (result2.isPresent())
            return result2;

        if (real) {
            player.get().getOpenResources().add(resourceOfMove);
            game.getResourceMarket().getOpenAvailable().remove(resourceOfMove);
            final int cost = game.getResourceMarket().getPrice(resourceOfMove);
            final int electroBeforeBuy = player.get().getElectro();
            player.get().setElectro(electroBeforeBuy - cost);
        }
        setProperty("type", getType().toString());
        setProperty("player", player.get().getColor());
        setProperty("resource", resourceOfMove.toString());
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
        final OpenPlayer lastPlayerOfList = allRemainingPlayer.get(allRemainingPlayer.size() - 1);
        if (!lastPlayerOfList.equals(player.get()))
            return Optional.of(Problem.NotYourTurn);
        if (game.getResourceMarket().getOpenAvailable().count(resourceOfMove) == 0)
            return Optional.of(Problem.NoResource);
        final int cost = game.getResourceMarket().getPrice(resourceOfMove);
        if (cost > player.get().getElectro())
            return Optional.of(Problem.NoCash);

        return Optional.empty();
    }

    /**
     * Check if a player have enough Space.
     *
     * @return optional of problem
     */
    private Optional<Problem> storageOfPlayer() {
        int amountOfStorage = 0;
        if (this.resourceOfMove == Resource.Coal || this.resourceOfMove == Resource.Oil) {
            final Set<OpenPlant> allHybridPlants = player.get().getOpenPlants()
                    .stream()
                    .filter(plant -> plant.getType() == Plant.Type.Hybrid)
                    .collect(Collectors.toSet());
            if (allHybridPlants.isEmpty()) {
                amountOfStorage = storageOfNonHybridPlants(this.resourceOfMove);
            } else {
                // Find Other Resource
                final Resource otherResource = allHybridPlants.stream()
                        .map(Plant::getResources)
                        .map(setOfResourceBags -> setOfResourceBags.stream()
                                .filter(bagOfResource -> !bagOfResource.contains(this.resourceOfMove))
                                .map(bagOfResource -> bagOfResource.iterator().next())
                                .findFirst())
                        .findFirst().get().get();


                //Safe Storage of Non-Hybrid of Resource
                amountOfStorage = storageOfNonHybridPlants(this.resourceOfMove);
                //Amount of other stored Resource and stored resources of player
                final int saveStorageOfOther = storageOfNonHybridPlants(otherResource);

                final int amountOfOtherResource = player.get().getResources().count(otherResource);

                //Amount of mixed storage
                int storageOfHybrids = allHybridPlants.stream()
                        .mapToInt(Plant::getNumberOfResources)
                        .sum();
                storageOfHybrids = storageOfHybrids * 2; //Number*2 = Storage Space


                //Part of Calculations
                amountOfStorage += saveStorageOfOther + storageOfHybrids - amountOfOtherResource;

            }
        } else
            amountOfStorage = storageOfNonHybridPlants(this.resourceOfMove);

        if (player.get().getOpenResources().count(resourceOfMove) >= amountOfStorage)
            return Optional.of(Problem.NoCapacity);
        return Optional.empty();
    }

    /**
     * Calculate the storage of the save space of a resource.
     *
     * @param resource for non Hybrid Plants
     * @return int amount of save storage
     */
    private int storageOfNonHybridPlants(Resource resource) {
        int amountOfStorage = 0;

        final int spaceFromPlant = player.get().getOpenPlants()
                .stream()
                .filter(plant -> plant.getResources().size() == 1)
                .filter(plant -> plant.getResources().contains(new ListBag<>().add(resource, plant.getNumberOfResources())))
                .mapToInt(Plant::getNumberOfResources)
                .sum();

        amountOfStorage = amountOfStorage + spaceFromPlant * 2;

        return amountOfStorage;
    }


    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> openPlayer) {
        if (this.game != null)
            throw new IllegalStateException("This ist not a prototype");
        final List<Resource> allResources = List.of(Resource.Coal, Resource.Oil, Resource.Uranium, Resource.Garbage);
        return allResources
                .stream()
                .map(resource -> new BuyResource(openGame, openPlayer, resource))
                .filter(move -> move.test().isEmpty())
                .collect(Collectors.toSet());
    }

    @Override
    public MoveType getType() {
        return MoveType.BuyResource;
    }
}
