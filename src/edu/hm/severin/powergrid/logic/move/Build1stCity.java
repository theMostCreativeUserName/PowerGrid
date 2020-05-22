package edu.hm.severin.powergrid.logic.move;


import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;


import java.util.Objects;
import java.util.Optional;
import java.util.Set;

class Build1stCity implements HotMove {

   private final OpenGame game;
   private final Optional<OpenPlayer> player;
   private final OpenCity city;
   Build1stCity() {
       game = null;
       player = null;
       city = null;
   }
   private Build1stCity(OpenGame game, Optional<OpenPlayer> player, OpenCity city) {
       this.game = game;
       this.player = player;
       this.city = city;
   }

   @Override
   public Optional<Problem> run(boolean real) {
       Objects.requireNonNull(game);
       if (game.getPhase() != Phase.Building)
           return Optional.of(Problem.NotNow);
       if (player.get().hasPassed())
           return Optional.of(Problem.AlreadyPassed);
       if (player.get().getOpenCities().size() != 0)
           return Optional.of(Problem.HasCities);
       if (real)
           player.get().setPassed(true);
       return Optional.empty();
   }

   @Override
   public OpenGame getGame() {
       return Objects.requireNonNull(game);
   }

   @Override
   public Set<HotMove> collect(OpenGame game, Optional<OpenPlayer> player) {
       if(player.isPresent()) return Set.of();
       if (this.game != null)
           throw new IllegalStateException("This ist not a prototype");
       HotMove move = new Build1stCity(game, player, city);
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
       return MoveType.Build1stCity;
   }
}
