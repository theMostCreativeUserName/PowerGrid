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
import java.util.Set;
import java.util.Optional;
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

        if (game.getPhase() != Phase.PlantOperation) return Optional.of(Problem.NotNow);
        final List<OpenPlayer> players = game.getOpenPlayers();
        // list of pass-State of every player, mainly to find out if all players passed already
        final boolean notPassed = players.stream()
                .map(player -> player.hasPassed())
                .collect(Collectors.toList())
                .contains(false);
        if (notPassed) return Optional.of(Problem.PlayersRemaining);

        //get biggest amount of cities of all players, to see if cityLimit is reached
        final int maxCities = players.stream()
                .mapToInt(player -> player.getCities().size())
                .max()
                .getAsInt();
        if (maxCities != game.getEdition().getPlayersEndgameCities()
                .get(players.size()))
            // city limit is not reached
            return Optional.of(Problem.GameRunning);
        if (real) {
            // sort players into the leaderboard and end the game
            final List<OpenPlayer> sorted = players.stream()
                    .sorted(this::leaderBoard)
                    .collect(Collectors.toList());
            players.clear();
            players.addAll(sorted);
            game.setPhase(Phase.Terminated);
        }
        return Optional.empty();
    }

    /**
     * compares the players to create the leaderboard.
     *
     * @param first first player
     * @param second second player
     * @return compare Number
     */
    private int leaderBoard(Player first, Player second) {
        int result = second.getPlants().size() - first.getPlants().size();
        if (result == 0)
            result = second.getElectro() - first.getElectro();
        return result;
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    /**
     * could the move be run.
     *
     * @param openGame   Aktuelles Spiel.
     * @param player der Spieler um den es geht.
     * @return this move, if it could be run
     */
    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> player) {
        if (this.game != null) throw new IllegalStateException("this is not a prototype!");
        final HotMove move = new EndGame(openGame);
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.EndGame;
    }
}
