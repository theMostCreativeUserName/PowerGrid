package edu.hm.severin.powergrid.logic.move;


import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

class TurnOver implements HotMove {

   private final OpenGame game;
   TurnOver() {
       game = null;
   }
   private TurnOver(OpenGame game) {
       this.game = game;
   }

   @Override
   public Optional<Problem> run(boolean real) {
       Objects.requireNonNull(game);
       if (game.getPhase() != Phase.Bureaucracy)
           return Optional.of(Problem.NotNow);

       if (real){
           Bag<Resource> Available= game.getResourceMarket().getOpenAvailable();
           Bag<Resource> Supply = game.getResourceMarket().getOpenSupply();
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
       HotMove move = new TurnOver(game);
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
       return MoveType.TurnOver;
   }
}
