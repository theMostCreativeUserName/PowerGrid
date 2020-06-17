package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
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

    @Test public void runWinnerMorePlants() throws ReflectiveOperationException {
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
        OpenPlant plant1 = factory.newPlant(1, Plant.Type.Coal,2,3);
        OpenPlant plant2 = factory.newPlant(40, Plant.Type.Coal,2,3);
        OpenPlant plant3 = factory.newPlant(2, Plant.Type.Coal,2,3);
        plant1.setOperated(true);
        plant2.setOperated(true);
        plant3.setOperated(true);
        p.getOpenPlants().add(plant1);
        p2.getOpenPlants().add(plant2);
        p2.getOpenPlants().add(plant3);
        p.setPassed(true);
        p2.setPassed(true);
        Optional<Problem> problem = sut.run(true);
        assertEquals(Phase.Terminated, game.getPhase());
        assertSame("blue" ,game.getOpenPlayers().get(0).getColor());
    }
    @Test public void runWinnerByElectro() throws ReflectiveOperationException {
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
    @Test public void runWinnerByPlants2() throws ReflectiveOperationException {
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
        OpenPlant plant1 = factory.newPlant(101, Plant.Type.Coal,2,3);
        OpenPlant plant2 = factory.newPlant(140, Plant.Type.Coal,2,3);
        OpenPlant plant3 = factory.newPlant(102, Plant.Type.Coal,2,3);
        OpenPlant plant4 = factory.newPlant(103, Plant.Type.Coal,2,3);
        OpenPlant plant5 = factory.newPlant(104, Plant.Type.Coal,2,3);
        OpenPlant plant6 = factory.newPlant(105, Plant.Type.Coal,2,3);
        plant1.setOperated(true);
        plant2.setOperated(true);
        plant3.setOperated(true);
        plant4.setOperated(false);
        plant5.setOperated(false);
        plant6.setOperated(false);
        p.getOpenPlants().add(plant1);
        p.getOpenPlants().add(plant4);
        p.getOpenPlants().add(plant5);
        p2.getOpenPlants().add(plant6);
        p2.getOpenPlants().add(plant2);
        p2.getOpenPlants().add(plant3);

        p.setPassed(true);
        p2.setPassed(true);
        p.setElectro(0);
        p2.setElectro(0);
        Optional<Problem> problem = sut.run(true);
        assertEquals(Phase.Terminated, game.getPhase());
        assertSame("blue" ,game.getOpenPlayers().get(0).getColor());
    }

    @Test public void runWinnerFireByPhillip() {
       //Rules
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        Rules sut = Rules.newRules(game);
        //Player
        OpenPlayer p = factory.newPlayer("mmm", "red");
        OpenPlayer p2 = factory.newPlayer("nnn", "blue");

        p.setPassed(true);
        p2.setPassed(true);
        p.setElectro(200);
        p2.setElectro(0);

        game.getOpenPlayers().add(p2);
        game.getOpenPlayers().add(p);

        //Game
        game.setPhase(Phase.PlantOperation);

        //Cities
        String name = "name";
        //gives one PLayer 21 cities
        for(int i = 0; i<21; i++){
            OpenCity city = factory.newCity(name+i, 1);
            p.getOpenCities().add(city);
            p2.getOpenCities().add(city);
        }

        //Plant
        OpenPlant plant1 = factory.newPlant(101, Plant.Type.Coal,2,3);
        plant1.setOperated(false);
        OpenPlant plant2 = factory.newPlant(102, Plant.Type.Coal,2,3);
        plant2.setOperated(false);
        OpenPlant plant3 = factory.newPlant(103, Plant.Type.Coal,2,3);
        plant3.setOperated(true);

        //Plant Player2
        OpenPlant plant4 = factory.newPlant(104, Plant.Type.Coal,2,3);
        plant4.setOperated(false);
        OpenPlant plant5 = factory.newPlant(105, Plant.Type.Coal,2,3);
        plant5.setOperated(true);
        OpenPlant plant6 = factory.newPlant(106, Plant.Type.Coal,2,3);
        plant6.setOperated(true);


        p.getOpenPlants().add(plant1);
        p.getOpenPlants().add(plant2);
        p.getOpenPlants().add(plant3);

        p2.getOpenPlants().add(plant4);
        p2.getOpenPlants().add(plant5);
        p2.getOpenPlants().add(plant6);

        List<OpenPlayer> EndingList = List.of(p2, p);

        final Set<Move> haveMove = sut.getMoves(Optional.of("mmm"));
        List<Move> moves = haveMove.stream().sequential().filter(Move -> Move.getType() == MoveType.EndGame).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.of("mmm"), moves.get(0));

        assertEquals(Phase.Terminated, game.getPhase());
        assertEquals(EndingList, game.getOpenPlayers());
    }

    @Test public void runWinnerFireByPhillipProperties() {
        //Rules
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        Rules sut = Rules.newRules(game);
        //Player
        OpenPlayer p = factory.newPlayer("mmm", "red");
        OpenPlayer p2 = factory.newPlayer("nnn", "blue");

        p.setPassed(true);
        p2.setPassed(true);
        p.setElectro(200);
        p2.setElectro(0);

        game.getOpenPlayers().add(p2);
        game.getOpenPlayers().add(p);

        //Game
        game.setPhase(Phase.PlantOperation);

        //Cities
        String name = "name";
        //gives one PLayer 21 cities
        for(int i = 0; i<21; i++){
            OpenCity city = factory.newCity(name+i, 1);
            p.getOpenCities().add(city);
            p2.getOpenCities().add(city);
        }

        //Plant
        OpenPlant plant1 = factory.newPlant(101, Plant.Type.Coal,2,3);
        plant1.setOperated(false);
        OpenPlant plant2 = factory.newPlant(102, Plant.Type.Coal,2,3);
        plant2.setOperated(false);
        OpenPlant plant3 = factory.newPlant(103, Plant.Type.Coal,2,3);
        plant3.setOperated(true);

        //Plant Player2
        OpenPlant plant4 = factory.newPlant(104, Plant.Type.Coal,2,3);
        plant4.setOperated(false);
        OpenPlant plant5 = factory.newPlant(105, Plant.Type.Coal,2,3);
        plant5.setOperated(true);
        OpenPlant plant6 = factory.newPlant(106, Plant.Type.Coal,2,3);
        plant6.setOperated(true);


        p.getOpenPlants().add(plant1);
        p.getOpenPlants().add(plant2);
        p.getOpenPlants().add(plant3);

        p2.getOpenPlants().add(plant4);
        p2.getOpenPlants().add(plant5);
        p2.getOpenPlants().add(plant6);

        List<OpenPlayer> EndingList = List.of(p2, p);

        final Set<Move> haveMove = sut.getMoves(Optional.of("mmm"));
        List<Move> moves = haveMove.stream().sequential().filter(Move -> Move.getType() == MoveType.EndGame).collect(Collectors.toList());
        Move move = moves.get(0);
        assertSame(move.getProperties().getProperty("type"), MoveType.EndGame.toString());
    }
}
