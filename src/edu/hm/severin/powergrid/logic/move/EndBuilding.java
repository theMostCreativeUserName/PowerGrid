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
 * player connects cities and end their turn.
 * @author Pietsch
 */
class EndBuilding implements HotMove {

    /**
     * Used game.
     */
   private final OpenGame game;

    /**
     * Prototyp Constructor.
     */
   EndBuilding() {
       game = null;
   }

    /**
     * Non-Prototype Constructor.
     * @param game this game
     */
   private EndBuilding(OpenGame game) {
       this.game = game;
   }

   @Override
   public Optional<Problem> run(boolean real) {
       Objects.requireNonNull(game);
       if (game.getPhase() != Phase.Building)
           return Optional.of(Problem.NotNow);
       final List<OpenPlayer> players = game.getOpenPlayers();
       if (players.stream().filter(Player::hasPassed).count() != players.size())
           return Optional.of(Problem.NotNow);

       if (real) {
           System.out.println("Ja dieses Scheiß Ding führt diesen Kack aus");
           game.setPhase(Phase.PlantOperation);
           for (OpenPlayer player : players ) {
               player.setPassed(false);
               System.out.println(player.hasPassed());
           }
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
       final HotMove move = new EndBuilding(openGame);
       Set<HotMove> result;
       if(move.run(false).isEmpty())
           result = Set.of(move);
       else
           result = Set.of();
       return result;
   }

   @Override
   public MoveType getType() {
       return MoveType.EndBuilding;
   }
}
