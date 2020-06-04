package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Erste Tests fuer eine Rules-Implementierung.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-30
 */

public class ScrapPlantTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    /**
     * Spielstand.
     */
    private final Game game;

    /**
     * Spielregeln.
     */
    private final Rules sut;

    /**
     * Fluchtwert fuer kein Geheimnis.
     */
    private final String NO_SECRET = "";

    /**
     * Fuehrt ein Runnable mehrmals aus.
     */
    private static BiConsumer<Integer, Runnable> times = (n, runnable) -> IntStream.range(0, n).forEach(__ -> runnable.run());

    /**
     * Initialisiert Factory und Spielregeln.
     */
    public ScrapPlantTest() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    @Test
    public void testScrapPlant() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertFalse(moveTypes.contains(MoveType.ScrapPlant));
    }

    @Test
    public void testScrapPlant2() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();

        OpenPlant plant3 = factory.newPlant(2, Plant.Type.Coal, 2, 2);
        opengame.getPlantMarket().getOpenActual().add(plant3);
        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenCities().add(factory.newCity("lolhausen", 666));
        player.getOpenCities().add(factory.newCity("lolhausen2", 666));
        player.getOpenCities().add(factory.newCity("lolhausen3", 666));
        player.getOpenCities().add(factory.newCity("lolhausen4", 666));
        player.getOpenCities().add(factory.newCity("lolhausen5", 666));
        player.getOpenCities().add(factory.newCity("lolhausen6", 666));
        player.getOpenCities().add(factory.newCity("lolhausen7", 666));
        player.getOpenPlants().add(plant);
        player.setElectro(100);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertTrue(moveTypes.contains(MoveType.ScrapPlant));
    }

    @Test
    public void testScrapPlant3() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();

        OpenPlant plant3 = factory.newPlant(2, Plant.Type.Coal, 2, 2);
        opengame.getPlantMarket().getOpenActual().add(plant3);
        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenCities().add(factory.newCity("lolhausen", 666));
        player.getOpenCities().add(factory.newCity("lolhausen2", 666));
        player.getOpenPlants().add(plant);
        player.setElectro(100);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertFalse(moveTypes.contains(MoveType.ScrapPlant));
    }

    @Test
    public void testScrapPlant4() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Terminated);
        OpenFactory factory = opengame.getFactory();

        OpenPlant plant3 = factory.newPlant(2, Plant.Type.Coal, 2, 2);
        opengame.getPlantMarket().getOpenActual().add(plant3);
        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenCities().add(factory.newCity("lolhausen", 666));
        player.getOpenCities().add(factory.newCity("lolhausen2", 666));
        player.getOpenCities().add(factory.newCity("lolhausen3", 666));
        player.getOpenPlants().add(plant);
        player.setElectro(100);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertFalse(moveTypes.contains(MoveType.ScrapPlant));
    }


    @Test
    public void testScrapPlantFire() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();

        OpenPlant plant3 = factory.newPlant(2, Plant.Type.Coal, 2, 2);
        opengame.getPlantMarket().getOpenActual().add(plant3);
        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenCities().add(factory.newCity("lolhausen", 666));
        player.getOpenCities().add(factory.newCity("lolhausen2", 666));
        player.getOpenCities().add(factory.newCity("lolhausen3", 666));
        player.getOpenPlants().add(plant);
        player.setElectro(100);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.ScrapPlant).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));

        // assert
        assertTrue(problem.isEmpty());
        assertEquals(opengame.getPlantMarket().findPlant(2), null);
    }

    @Test
    public void testScrapPlantFire2() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();

        OpenPlant plant3 = factory.newPlant(2, Plant.Type.Coal, 2, 2);
        opengame.getPlantMarket().getOpenActual().add(plant3);
        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenCities().add(factory.newCity("lolhausen", 666));
        player.getOpenCities().add(factory.newCity("lolhausen2", 666));
        player.getOpenCities().add(factory.newCity("lolhausen3", 666));
        player.getOpenPlants().add(plant);
        player.setElectro(100);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.ScrapPlant).collect(Collectors.toList());
        opengame.getPlantMarket().removePlant(2);
        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));

        // assert
        assertTrue(problem.isPresent());
    }





}
