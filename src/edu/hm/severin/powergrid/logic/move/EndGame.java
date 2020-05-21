package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ends the game.
 *
 * @author Severin
 * @complexity:
 */
public class EndGame implements HotMove {
    /**
     * the game.
     */
    private final OpenGame game;

    EndGame() {
        this.game = null;
    }

    private EndGame(OpenGame game) {
        this.game = game;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        List<OpenPlayer> players = game.getOpenPlayers();
        if (game.getPhase() != Phase.PlantOperation) return Optional.of(Problem.NotNow);
        // list of pass-State of every player
        boolean allPassed = players.stream()
                .map(player -> player.hasPassed())
                .collect(Collectors.toList())
                .contains(false);
        if (!allPassed) return Optional.of(Problem.PlayersRemaining);

        //get biggest amount of cities of all players, to see if cityLimit is reached
        int maxCities = players.stream()
                .mapToInt(player -> player.getCities().size())
                .max().getAsInt();
        if (maxCities == game.getEdition().getPlayersEndgameCities()
                .get(players.size())) return Optional.of(Problem.PlayersRemaining);

        if (real) {
            List<OpenPlayer> sorted = players.stream()
                    .sorted((p1, p2)->leaderBoard(p1, p2))
                    .collect(Collectors.toList());
            players.clear();
            players.addAll(sorted);
            game.setPhase(Phase.Terminated);
        }
        return Optional.empty();
    }

    private int leaderBoard(Player p1, Player p2){
        int result = p2.getPlants().size()- p1.getPlants().size();
        if(result == 0)
            result = p2.getElectro()-p2.getElectro();
        return result;
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame game, Optional<OpenPlayer> player) {
        if (player.isPresent()) return Set.of();
        if (this.game != null) throw new IllegalStateException("this is not a prototype!");
        HotMove move = new EndGame(game);
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        Objects.requireNonNull(game);
        return MoveType.EndGame;
    }
}
