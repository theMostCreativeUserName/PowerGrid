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

/**
 * start the game.
 *
 * @author Auerbach
 */
class GameBegins extends AbstractProperties implements HotMove {

    /**
     * the game of the move.
     **/
    private final OpenGame game;
    /**
     * the player of this move.
     **/
    private final OpenPlayer player;

    /**
     * Prototype-Ctor.
     */
    GameBegins() {
        game = null;
        player = null;
    }

    /**
     * Non-Prototype-Ctor.
     *
     * @param game   game for the move
     * @param player player for the move
     */
    GameBegins(OpenGame game, OpenPlayer player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        if (game.getPhase() != Phase.Opening) return Optional.of(Problem.NotNow);

        if (!game.getOpenPlayers().contains(player)) return Optional.of(Problem.NotYourTurn);
        if (game.getPlayers().size() < game.getEdition().getPlayersMinimum()) return Optional.of(Problem.TooFewPlayers);

        if (real) {
            game.setLevel(0);
            game.setRound(1);
            game.setPhase(Phase.PlayerOrdering);
            //Kraftwerksmarkt initialisieren
            initPlantMarket();
            game.getBoard().closeRegions(game.getEdition().getRegionsUsed().get(game.getOpenPlayers().size()));
            game.getOpenPlayers()
                    .forEach(openPlayer -> openPlayer.setElectro(game.getEdition().getInitialElectro()));
            game.getBoard().close();
        }
        setProperty("type", getType().toString());
        setProperty("player", player.getColor());
        return Optional.empty();
    }

    /**
     * edit Plant Market.
     */
    public void initPlantMarket() {
        final int minNumberPlant = game.getPlantMarket().getOpenHidden().get(0).getNumber();
        int currentPlantNumber = minNumberPlant;
        final int actualSize = game.getEdition().getActualPlants(game.getLevel());

        while (currentPlantNumber < minNumberPlant + actualSize){
            final OpenPlant plant = game.getPlantMarket().findPlant(currentPlantNumber);
            game.getPlantMarket().removePlant(currentPlantNumber);
            game.getPlantMarket().getOpenActual().add(plant);
            currentPlantNumber++;
        }
        final int totalSize = game.getEdition().getFuturePlants(game.getLevel()) + currentPlantNumber;

        while (currentPlantNumber < totalSize) {
            final OpenPlant plant = game.getPlantMarket().findPlant(currentPlantNumber);
            game.getPlantMarket().removePlant(currentPlantNumber);
            game.getPlantMarket().getOpenFuture().add(plant);
            currentPlantNumber++;
        }
        final OpenPlant plant13 = game.getPlantMarket().findPlant(13);
        game.getPlantMarket().getOpenHidden().remove(plant13);

        // remove plants from hidden, predefined by rules, and mix
        RandomSource.make().shufflePlants(game.getPlantMarket().getOpenHidden());
        final List<OpenPlant> plantsToRemove = game.getPlantMarket().getOpenHidden()
                .subList(0, game.getEdition().getPlayersPlantsInitiallyRemoved().get(game.getOpenPlayers().size()));
        game.getPlantMarket().getOpenHidden().removeAll(plantsToRemove);

        //add plant 13 on top of hidden
        game.getPlantMarket().getOpenHidden().add(0, plant13);

        // Level 3 under hidden
        final OpenPlant level3 = game.getFactory().newPlant(0, Plant.Type.Level3, 1, 1);
        game.getPlantMarket().getOpenHidden().add(level3);
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> openPlayer) {
        if (this.game != null)
            throw new IllegalStateException("This ist not a prototype");
        final HotMove move = new GameBegins(openGame, openPlayer.get());
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.CommenceGame;
    }

    @Override
    public boolean hasPriority() {
        boolean priority = false;
        if (game != null && game.getEdition().getPlayersMaximum() == game.getOpenPlayers().size()){
            priority = true;}

        return priority;
    }
}
