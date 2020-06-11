package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertTrue;

public class EndGameTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test
    private final OpenFactory factory = OpenFactory.newFactory("edu.hm.severin.powergrid.datastore.NeutralFactory");
    private final OpenGame game = factory.newGame(new EditionGermany());

    public EndGame getSut() throws ReflectiveOperationException {
        Constructor<EndGame> cTor =  EndGame.class
                .getDeclaredConstructor(OpenGame.class);
        cTor.setAccessible(true);
        return cTor.newInstance(game);
    }
    public EndGame getSutProto() throws ReflectiveOperationException {
        Constructor<EndGame> cTor = EndGame.class
                .getDeclaredConstructor();
        cTor.setAccessible(true);
        return cTor.newInstance();
    }
    @Test public void getGame() throws ReflectiveOperationException {
        EndGame sut = getSut();
        assertEquals(game, sut.getGame());
        assertEquals(MoveType.EndGame, sut.getType());
    }
    @Test (expected = IllegalStateException.class)
    public void collectNoProto() throws ReflectiveOperationException {
        EndGame sut = getSut();
        sut.collect(game, Optional.empty());
    }
    @Test public void collect2() throws ReflectiveOperationException {
        EndGame sut = getSutProto();
        Set<HotMove> move = sut.collect(game, Optional.empty());
        assertTrue(move.isEmpty());
    }
    @Test public void collect3() throws ReflectiveOperationException {
        EndGame sut = getSutProto();
        OpenPlayer p = factory.newPlayer("mmm", "red");
        OpenPlayer p2 = factory.newPlayer("mmm", "blue");
        game.setPhase(Phase.PlantOperation);
        game.getOpenPlayers().add(p);
        game.getOpenPlayers().add(p2);
        String name = "name";
        //gives one PLayer 21 cities
        for(int i = 0; i<21; i++){
            OpenCity city = factory.newCity(name+i, 1);
            p.getOpenCities().add(city);
        }
        p.setPassed(true);
        p2.setPassed(true);

        Set<HotMove> move = sut.collect(game, Optional.empty());
        assertFalse(move.isEmpty());
        assertEquals(1, move.size());
    }
    @Test public void runWrongPhase() throws ReflectiveOperationException {
        EndGame sut = getSut();
        OpenPlayer p = factory.newPlayer("mmm", "red");
        OpenPlayer p2 = factory.newPlayer("mmm", "blue");
        game.getOpenPlayers().add(p);
        game.getOpenPlayers().add(p2);
        Optional<Problem> problem = sut.run(false);
        assertEquals(Problem.NotNow, problem.get());
    }
    @Test public void runNotPassed1() throws ReflectiveOperationException {
        EndGame sut = getSut();
        OpenPlayer p = factory.newPlayer("mmm", "red");
        OpenPlayer p2 = factory.newPlayer("mmm", "blue");
        p2.setPassed(false);
        game.getOpenPlayers().add(p);
        game.getOpenPlayers().add(p2);
        game.setPhase(Phase.PlantOperation);
        Optional<Problem> problem = sut.run(false);
        assertEquals(Problem.PlayersRemaining, problem.get());
    }
    @Test public void runNotPassed2() throws ReflectiveOperationException {
        EndGame sut = getSut();
        OpenPlayer p = factory.newPlayer("mmm", "red");
        OpenPlayer p2 = factory.newPlayer("mmm", "blue");
        p2.setPassed(false);
        game.getOpenPlayers().add(p);
        game.getOpenPlayers().add(p2);
        p.setPassed(true);
        game.setPhase(Phase.PlantOperation);
        List<OpenPlayer>before = game.getOpenPlayers();
        Optional<Problem> problem = sut.run(false);
        assertEquals(Problem.PlayersRemaining, problem.get());
        assertTrue(game.getPlayers().equals(before));
    }
    @Test public void runNotPassed3() throws ReflectiveOperationException {
        EndGame sut = getSut();
        OpenPlayer p = factory.newPlayer("mmm", "red");
        OpenPlayer p2 = factory.newPlayer("mmm", "blue");
        p2.setPassed(true);
        game.getOpenPlayers().add(p);
        game.getOpenPlayers().add(p2);
        p.setPassed(true);
        game.setPhase(Phase.PlantOperation);
        List<OpenPlayer>before = game.getOpenPlayers();
        Optional<Problem> problem = sut.run(false);
        assertEquals(Problem.GameRunning, problem.get());
    }
    @Test public void runNotPassed4() throws ReflectiveOperationException {
        EndGame sut = getSut();
        OpenPlayer p = factory.newPlayer("mmm", "red");
        OpenPlayer p2 = factory.newPlayer("mmm", "blue");
        p2.setPassed(false);
        game.getOpenPlayers().add(p);
        game.getOpenPlayers().add(p2);
        p.setPassed(true);
        game.setPhase(Phase.PlantOperation);
        String before = game.getOpenPlayers().get(0).getColor();
        String name = "name";
        //gives one PLayer 21 cities
        for(int i = 0; i<21; i++){
            OpenCity city = factory.newCity(name+i, 1);
            p.getOpenCities().add(city);
        }
        p.getOpenPlants().add(factory.newPlant(1, Plant.Type.Coal,2,3));
        p2.getOpenPlants().add(factory.newPlant(40, Plant.Type.Coal,2,3));
        p2.getOpenPlants().add(factory.newPlant(2, Plant.Type.Coal,2,3));
        Optional<Problem> problem = sut.run(true);
        assertEquals(Problem.PlayersRemaining, problem.get());
        assertTrue(before.equals(game.getOpenPlayers().get(0).getColor()));
    }
    @Test public void runNotEndCity() throws ReflectiveOperationException {
        EndGame sut = getSut();
        OpenPlayer p = factory.newPlayer("mmm", "red");
        OpenPlayer p2 = factory.newPlayer("mmm", "blue");
        game.setPhase(Phase.PlantOperation);
        game.getOpenPlayers().add(p);
        game.getOpenPlayers().add(p2);
        p.setPassed(true);
        p2.setPassed(true);

        Optional<Problem> problem = sut.run(true);
        assertEquals(Problem.GameRunning, problem.get());
    }
    @Test public void runWinner() throws ReflectiveOperationException {
        EndGame sut = getSut();
        OpenPlayer p = factory.newPlayer("mmm", "red");
        OpenPlayer p2 = factory.newPlayer("mmm", "blue");
        game.setPhase(Phase.PlantOperation);
        game.getOpenPlayers().add(p);
        game.getOpenPlayers().add(p2);
        String name = "name";
        //gives one PLayer 21 cities
        for(int i = 0; i<21; i++){
            OpenCity city = factory.newCity(name+i, 1);
            p.getOpenCities().add(city);
        }
        p.getOpenPlants().add(factory.newPlant(1, Plant.Type.Coal,2,3));
        p2.getOpenPlants().add(factory.newPlant(40, Plant.Type.Coal,2,3));
        p2.getOpenPlants().add(factory.newPlant(2, Plant.Type.Coal,2,3));
        p.setPassed(true);
        p2.setPassed(true);
        Optional<Problem> problem = sut.run(true);
        assertEquals(Phase.Terminated, game.getPhase());
        assertSame("blue" ,game.getOpenPlayers().get(0).getColor());
    }
    @Test public void runWinner2() throws ReflectiveOperationException {
        EndGame sut = getSut();
        OpenPlayer p = factory.newPlayer("mmm", "red");
        OpenPlayer p2 = factory.newPlayer("mmm", "blue");
        game.setPhase(Phase.PlantOperation);
        game.getOpenPlayers().add(p);
        game.getOpenPlayers().add(p2);
        String name = "name";
        //gives one PLayer 21 cities
        for(int i = 0; i<21; i++){
            OpenCity city = factory.newCity(name+i, 1);
            p.getOpenCities().add(city);
            p2.getOpenCities().add(city);
        }
        p.setPassed(true);
        p2.setPassed(true);
        p.setElectro(20);
        p2.setElectro(300);
        Optional<Problem> problem = sut.run(true);
        assertEquals(Phase.Terminated, game.getPhase());
        assertSame("blue" ,game.getOpenPlayers().get(0).getColor());
    }
}
