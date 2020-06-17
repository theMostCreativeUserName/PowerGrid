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
 * Drop Plant if player has to much.
 * @author Pietsch
 */
class DropPlant extends AbstractProperties implements HotMove {

    /**
     * Used game.
     */
   private final OpenGame game;

    /**
     * Used game.
     */
    private final Optional<OpenPlayer> player;

    /**
     * Used game.
     */
    private final OpenPlant plant;

    /**
     * Prototyp Constructor.
     */
   DropPlant() {
       game = null;
       player = null;
       plant = null;
   }

    /**
     * Non-Prototype Constructor.
     * @param game this game
     * @param player the player
     * @param plant the plant
     */
   private DropPlant(OpenGame game, Optional<OpenPlayer> player, OpenPlant plant) {
       this.game = game;
       this.player = player;
       this.plant = plant;
   }

   @Override
   public Optional<Problem> run(boolean real) {
       Objects.requireNonNull(game);
       final int maximalAmountOfPlants = game.getEdition().getPlayersPlantsLimit().get(game.getOpenPlayers().size());
       if (player.get().getOpenPlants().size() <= maximalAmountOfPlants)
           return Optional.of(Problem.PlantSave);
       if(!player.get().getOpenPlants().contains(plant))
           return Optional.of(Problem.OtherPlant);

       if (real) {
           player.get().getOpenPlants().remove(plant);
       }
       setProperty("type", getType().toString());
       setProperty("player", player.get().getColor());
       setProperty("plant", String.valueOf(plant.getNumber()));
       return Optional.empty();
   }

   @Override
   public OpenGame getGame() {
       return Objects.requireNonNull(game);
   }

   @Override
   public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> openPlayer) {
       if (this.game != null)
           throw new IllegalStateException("This ist not a protoype");
       return openPlayer.get().getOpenPlants()
               .stream()
               .sequential()
               .map(openPlant -> new DropPlant(openGame, openPlayer, openPlant ))
               .filter(move -> move.test().isEmpty())
               .collect(Collectors.toSet());
   }

   @Override
   public MoveType getType() {
       return MoveType.DropPlant;
   }
}
