package edu.hm.severin.powergrid.logic.move;


import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


/**
 * player bought no plant.
 * @author Pietsch
 */
class PassAuction implements HotMove {

    /**
     * Used game.
     */
   private final OpenGame game;

    /**
     * player.
     */
    private final Optional<OpenPlayer> player;

    /**
     * Prototyp Constructor.
     */
   PassAuction() {
       game = null;
       player = null;
   }

    /**
     * Non-Prototype Constructor.
     * @param game this game
     */
   private PassAuction(OpenGame game, Optional<OpenPlayer> player) {

       this.game = game;
       this.player = player;
   }

   @Override
   public Optional<Problem> run(boolean real) {
       Objects.requireNonNull(game);
       if (game.getPhase() != Phase.PlantBuying)
           return Optional.of(Problem.NotNow);
       final List<OpenPlayer> players = game.getOpenPlayers();
       final Optional<OpenPlayer> firstNotPassedPlayer = players.stream()
               .filter(OpenPlayer -> !OpenPlayer.hasPassed())
               .findFirst();
       if (firstNotPassedPlayer.stream().filter( x -> x.equals(player.get())).count() == 0 )
           return Optional.of(Problem.NotYourTurn);

       if (player.get().getOpenPlants().size() == 0)
           return Optional.of(Problem.NoPlants);

       if (real) {
          player.get().setPassed(true);
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
       final HotMove move = new PassAuction(openGame, player);
       Set<HotMove> result;
       if(move.run(false).isEmpty())
           result = Set.of(move);
       else
           result = Set.of();
       return result;
   }

   @Override
   public MoveType getType() {
       return MoveType.PassAuction;
   }
}
