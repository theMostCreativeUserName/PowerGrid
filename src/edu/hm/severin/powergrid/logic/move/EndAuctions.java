package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
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
import java.util.stream.Collectors;

/**
 * Auktion beenden.
 *
 * @author Auerbach
 * */

public class EndAuctions extends AbstractProperties implements HotMove {

    /**
     * game of the move.
     */
    private final OpenGame game;

    /**
     * prototyp constructor.
     */
    EndAuctions(){
        this.game=null;
    }

    /**
     * non-prototype constructor.
     * @param game game of the move
     */
    private EndAuctions(OpenGame game){
        this.game=game;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        final Optional<Problem> problem = meetRequirements();
        if(problem.isPresent())
            return problem;

        if(real){
            if (game.getRound() == 1){
                final List<OpenPlayer> sortedPlayerList = game.getOpenPlayers()
                        .stream()
                        .sorted(OpenPlayer::compareTo)
                        .collect(Collectors.toList());
                game.getOpenPlayers().clear();
                game.getOpenPlayers().addAll(sortedPlayerList);

            }
            if (game.getAuction() == null){
                final int numberOfSmallestPlant = game.getPlantMarket()
                        .getOpenActual()
                        .stream()
                        .map(Plant::getNumber)
                        .min(Integer::compareTo)
                        .orElse(-2);
                game.getPlantMarket().removePlant(numberOfSmallestPlant);
            }
            game.getOpenPlayers().forEach(openPlayer -> openPlayer.setPassed(false));
            game.setPhase(Phase.ResourceBuying);
            game.setAuction(null);
        }
        setProperty("type", getType().toString());
        return Optional.empty();
    }

    /**
     * checks if all requirements are okay.
     * @return Optional of Problem, empty when no problem exist
     */
    private Optional<Problem> meetRequirements(){
        if (game.getPhase() != Phase.PlantBuying)
            return Optional.of(Problem.NotNow);

        final List<Boolean> playersNotPassed = game.getOpenPlayers().stream()
                .map(Player::hasPassed)
                .collect(Collectors.toList());
       if (playersNotPassed.contains(false))
            return Optional.of(Problem.PlayersRemaining);

        return Optional.empty();
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame openGame, Optional<OpenPlayer> player) {
        if (this.game != null)
            throw new IllegalStateException("Not a prototype!");
        final HotMove move = new EndAuctions(openGame);
        Set<HotMove> result;
        if (move.run(false).isEmpty())
            result = Set.of(move);
        else
            result = Set.of();
        return result;
    }

    @Override
    public MoveType getType() {
        return MoveType.EndAuctions;
    }
}
