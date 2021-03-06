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
class EndBuilding extends AbstractProperties implements HotMove {

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
           game.setPhase(Phase.PlantOperation);
           players.forEach(openPlayer -> openPlayer.setPassed(false));
       }
       setProperty("type", getType().toString());
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
