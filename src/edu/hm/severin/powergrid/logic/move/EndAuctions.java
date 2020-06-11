package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import org.w3c.dom.ls.LSOutput;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Auktion beenden
 *
 * @author Auerbach
 * */

public class EndAuctions implements HotMove {

    private final OpenGame game;

    EndAuctions(){
        this.game=null;
    }

    private EndAuctions(OpenGame game){
        this.game=game;
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Optional<Problem> problem = meetRequirements();
        if(problem.isPresent())
            return problem;

        if(real){
            if (game.getRound() == 1){
                game.getOpenPlayers()
                        .stream()
                        .map(openPlayer -> openPlayer.getOpenPlants()
                                .stream()
                                .mapToInt(plant -> plant.getNumber()).max().getAsInt())
                        .sorted(Integer::compareTo);

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
            game.getOpenPlayers().stream().forEach(openPlayer -> openPlayer.setPassed(false));
            game.setPhase(Phase.ResourceBuying);
            game.setAuction(null);
        }
        return Optional.empty();
    }

    private Optional<Problem> meetRequirements(){
        if (game.getPhase() != Phase.PlantBuying) return Optional.of(Problem.NotNow);

        final List playersNotPassed = game.getOpenPlayers().stream()
                .map(player -> player.hasPassed())
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
    public Set<HotMove> collect(OpenGame game, Optional<OpenPlayer> player) {
        if (this.game != null)
            throw new IllegalStateException("Not a prototype!");
        final HotMove move = new EndAuctions(game);
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
