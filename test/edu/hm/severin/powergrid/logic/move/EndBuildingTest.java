package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static edu.hm.cs.rs.powergrid.logic.MoveType.ConnectNoCity;
import static edu.hm.cs.rs.powergrid.logic.MoveType.EndBuilding;
import static edu.hm.cs.rs.powergrid.logic.MoveType.JoinPlayer;
import static org.junit.Assert.*;

/**
 * Erste Tests fuer eine Rules-Implementierung.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-30
 */

public class EndBuildingTest {
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
    public EndBuildingTest() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    @Test
    public void testEndBuilding() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player2.getOpenCities().add(factory.newCity("LOL Dat is ne Stadt", 999));
        player.setPassed(true);
        player2.setPassed(true);
        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == EndBuilding).collect(Collectors.toList());
        // assert
        assertEquals(moves.size(), 1);
    }

    @Test
    public void testEndBuildingProperties() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player2.getOpenCities().add(factory.newCity("LOL Dat is ne Stadt", 999));
        player.setPassed(true);
        player2.setPassed(true);
        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == EndBuilding).collect(Collectors.toList());
        Move move = moves.get(0);
        assertSame(move.getProperties().getProperty("type"), MoveType.EndBuilding.toString());
    }

    @Test
    public void testEndBuildingFire() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(300, Plant.Type.Uranium, 2, 1);
        player.getOpenPlants().add(plant);
        plant.setOperated(false);
        player.getOpenResources().add(Resource.Uranium);
        player.getOpenResources().add(Resource.Uranium);
        player.getOpenResources().add(Resource.Uranium);
        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.setPassed(true);
        player2.setPassed(true);
        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == EndBuilding).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));
        // assert
        assertFalse(player.hasPassed());
        assertFalse(player2.hasPassed());
        assertTrue(problem.isEmpty());
        assertSame(opengame.getPhase(), Phase.PlantOperation);
    }

    @Test (expected = IllegalStateException.class)
    public void testEndBuildingException() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(300, Plant.Type.Uranium, 2, 1);
        player.getOpenPlants().add(plant);
        plant.setOperated(false);
        player.getOpenResources().add(Resource.Uranium);
        player.getOpenResources().add(Resource.Uranium);
        player.getOpenResources().add(Resource.Uranium);
        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.setPassed(true);
        player2.setPassed(true);
        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == EndBuilding).collect(Collectors.toList());
        HotMove move = (HotMove)moves.get(0);
        move.collect(opengame, Optional.of(factory.newPlayer("Irgendwas", "Mir egal")));
    }
}
