package edu.hm.severin.powergrid.logic.move;


import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


class EndBuilding implements HotMove {

   private final OpenGame game;
   EndBuilding() {
       game = null;
   }
   private EndBuilding(OpenGame game) {
       this.game = game;
   }

   @Override
   public Optional<Problem> run(boolean real) {
       Objects.requireNonNull(game);
       if (game.getPhase() != Phase.Building)
           return Optional.of(Problem.NotNow);
       final List<OpenPlayer> players = game.getOpenPlayers();
       boolean allPassed = true;
       for (OpenPlayer player : players)
           if (!player.hasPassed())
               allPassed = false;
       if (!allPassed)
           return Optional.of(Problem.NotNow);

       if (real) {
           game.setPhase(Phase.PlantOperation);
           for (OpenPlayer player : players )
               player.setPassed(false);
       }
       return Optional.empty();
   }

   @Override
   public OpenGame getGame() {
       return Objects.requireNonNull(game);
   }

   @Override
   public Set<HotMove> collect(OpenGame game, Optional<OpenPlayer> player) {
       if (this.game != null)
           throw new IllegalStateException("This ist not a protoype");
       HotMove move = new EndBuilding(game);
       Set<HotMove> result;
       if(move.run(false).isEmpty())
           result = Set.of(move);
       else
           result = Set.of();
       return result;
   }

   @Override
   public MoveType getType() {
       Objects.requireNonNull(game);
       return MoveType.EndBuilding;
   }
}
