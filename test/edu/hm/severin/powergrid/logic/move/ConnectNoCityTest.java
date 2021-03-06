package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
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
public class ConnectNoCityTest {
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
    public ConnectNoCityTest() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }


    // Mein Zeug
    @Test
    public void testConnectNoCityNotPassed() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.setPassed(false);
        opengame.getOpenPlayers().add(player);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == ConnectNoCity).collect(Collectors.toList());
        // assert
        assertEquals(moves.size(), 1);
    }

    @Test
    public void testConnectNoCityProperties() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.setPassed(false);
        opengame.getOpenPlayers().add(player);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == ConnectNoCity).collect(Collectors.toList());
        Move move = moves.get(0);
        // assert
        assertSame(move.getProperties().getProperty("type"), MoveType.ConnectNoCity.toString());
        assertSame(move.getProperties().getProperty("player"), player.getColor() );
    }

    @Test
    public void testConnectNoCityPassedAfterGetMoves() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.setPassed(false);
        opengame.getOpenPlayers().add(player);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == ConnectNoCity).collect(Collectors.toList());
        player.setPassed(true);
        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));
        // assert

        assertEquals(problem.get(), Problem.NotYourTurn);
    }

    @Test
    public void testConnectNoCityFire() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        opengame.getBoard().getOpenCities().add(factory.newCity("Ja", 123));
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.setPassed(false);
        OpenPlayer player2 = factory.newPlayer("I Dont Care", "Literally");
        player2.setPassed(false);
        player2.setElectro(666);
        OpenPlayer player3 = factory.newPlayer("There are many Questions", "Do Ghosts exist?");
        player3.setPassed(false);


        opengame.getOpenPlayers().add(player3);
        opengame.getOpenPlayers().add(player2);
        opengame.getOpenPlayers().add(player);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == ConnectNoCity).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));
        // assert
        assertTrue(problem.isEmpty());
        assertTrue(player.hasPassed());
    }
    @Test
    public void testConnectNoCityFire2() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.setPassed(false);
        OpenPlayer player2 = factory.newPlayer("I Dont Care", "Literally");
        player2.setPassed(false);
        OpenPlayer player3 = factory.newPlayer("There are many Questions", "Do Ghosts exist?");
        player3.setPassed(false);
        opengame.getOpenPlayers().add(player3);
        opengame.getOpenPlayers().add(player2);
        opengame.getOpenPlayers().add(player);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == ConnectNoCity).collect(Collectors.toList());
        player.setPassed(true);
        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));
        // assert
        assertSame(problem.get(), Problem.NotYourTurn);
    }

    @Test (expected = IllegalStateException.class)
    public void testConnectNoCityException() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.setPassed(false);
        OpenPlayer player2 = factory.newPlayer("I Dont Care", "Literally");
        player2.setPassed(false);
        OpenPlayer player3 = factory.newPlayer("There are many Questions", "Do Ghosts exist?");
        player3.setPassed(false);
        opengame.getOpenPlayers().add(player3);
        opengame.getOpenPlayers().add(player2);
        opengame.getOpenPlayers().add(player);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == ConnectNoCity).collect(Collectors.toList());
        HotMove move = (HotMove)moves.get(0);
        move.collect(opengame, Optional.of(factory.newPlayer("Irgendwas", "Mir egal")));
    }

}