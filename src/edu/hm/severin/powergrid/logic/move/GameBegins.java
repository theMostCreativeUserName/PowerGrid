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

/**
 * start the game
 * @author Auerbach
 */
public class GameBegins implements HotMove {

    private final OpenGame game;
    private final OpenPlayer player;

    /**
     * Prototype-Ctor
     */
    GameBegins(){
        game=null;
        player=null;
    }

    private GameBegins(OpenGame game,OpenPlayer player){
        this.game=game;
        this.player=player;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        if(game.getPhase()!= Phase.Opening) return Optional.of(Problem.NotNow);
        List<OpenPlayer> players=game.getOpenPlayers();
        if(!players.contains(player)) return Optional.of(Problem.NotNow);
        if(game.getPlayers().size()<game.getEdition().getPlayersMinimum()) return Optional.of(Problem.TooFewPlayers);
        if(real){
            game.setLevel(0);
            game.setRound(1);
            game.setPhase(Phase.PlayerOrdering);
            //Kraftwerksmarkt initialisieren
            game.getBoard().closeRegions(players.size());
            players.stream().forEach(OpenPlayer -> player.setElectro(50));
            game.getBoard().close();
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
            throw new IllegalStateException("This ist not a prototype");
        final HotMove move = new GameBegins(openGame, player.get());
        Set<HotMove> result;
        if(move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.CommenceGame;
    }
}
