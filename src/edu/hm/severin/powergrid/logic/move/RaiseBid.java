package edu.hm.severin.powergrid.logic.move;


import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;



/**
 * player connects cities and end their turn.
 * @author Pietsch
 */
class RaiseBid extends AbstractProperties implements HotMove {

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
   RaiseBid() {
       game = null;
       player = null;
   }

    /**
     * Non-Prototype Constructor.
     * @param game this game
     * @param player optional of player for game
     */
   private RaiseBid(OpenGame game, Optional<OpenPlayer> player) {

       this.game = game;
       this.player = player;
   }

   @Override
   public Optional<Problem> run(boolean real) {
       Objects.requireNonNull(game);
       if (game.getPhase() != Phase.PlantAuction)
           return Optional.of(Problem.NotNow);
       if (allTestWithAuction().isPresent())
            return allTestWithAuction();
       if (real) {
          editAuction();
       }
       setProperty("type", getType().toString());
       setProperty("player", player.get().getColor());
       return Optional.empty();
   }

    /**
     * Editing the Auction.
     */
    private void editAuction() {
        final OpenAuction auction = game.getAuction();
        auction.setAmount(auction.getAmount() + 1);
        final OpenPlayer currentPlayer = player.get();
        auction.setPlayer(currentPlayer);
        auction.getOpenPlayers().remove(currentPlayer);
        auction.getOpenPlayers().add(currentPlayer);
    }

    /**
     * Testing all with auction.
     * @return Optional of problem if exist else empty
     */
    private Optional<Problem> allTestWithAuction(){
        if (game.getAuction() == null)
            return Optional.of(Problem.NotRunning);
        if (!game.getAuction().getOpenPlayers().get(0).equals(player.get()))
            return Optional.of(Problem.NotYourTurn);
        if (game.getAuction().getPlayer().equals(player.get()))
            return Optional.of(Problem.TopBidder);
        if (player.get().getElectro() <= game.getAuction().getAmount())
            return Optional.of(Problem.NoCash);
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
       final HotMove move = new RaiseBid(openGame, openPlayer);
       Set<HotMove> result;
       if(move.run(false).isEmpty())
           result = Set.of(move);
       else
           result = Set.of();
       return result;
   }

   @Override
   public MoveType getType() {
       return MoveType.RaiseBid;
   }
}
