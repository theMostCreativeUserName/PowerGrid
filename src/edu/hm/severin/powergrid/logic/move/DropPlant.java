package edu.hm.severin.powergrid.logic.move;


import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Drop Plant if player has to much.
 * @author Pietsch
 */
class DropPlant implements HotMove {

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
       if (game.getPhase() == Phase.Opening || game.getPhase() == Phase.Terminated)
           return Optional.of(Problem.NotNow);
       if (player.get().getOpenPlants().size() <= 3)
           return Optional.of(Problem.PlantSave);
       if(!player.get().getOpenPlants().contains(plant))
           return Optional.of(Problem.OtherPlant);

       if (real) {
           player.get().getOpenPlants().remove(plant);
       }
       return Optional.empty();
   }

   @Override
   public OpenGame getGame() {
       return Objects.requireNonNull(game);
   }

   @Override
   public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> player) {
       if (this.game != null)
           throw new IllegalStateException("This ist not a protoype");
       return player.get().getOpenPlants()
               .stream()
               .sequential()
               .map(OpenPlant -> new DropPlant(openGame, player, OpenPlant ))
               .filter(move -> move.test().isEmpty())
               .collect(Collectors.toSet());
   }

   @Override
   public MoveType getType() {
       return MoveType.DropPlant;
   }
}
