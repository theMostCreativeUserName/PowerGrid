package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
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

import java.lang.reflect.Constructor;
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

public class Build1stCityTest {
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
    public Build1stCityTest() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    // Mein Zeug
    @Test
    public void Build1stCityOnePlayer() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.Building);
        opengame.getBoard().getOpenCities().clear();
        opengame.getBoard().getOpenCities().add(factory.newCity("Deathhausen", 666));

        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.setElectro(666);
        player.setPassed(false);
        opengame.getOpenPlayers().add(player);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertTrue(moveTypes.contains(MoveType.Build1stCity));
        assertEquals(moveTypes.size(), 2); //Connect no City und 1 mal Build1stcity
    }

    @Test
    public void Build1stCityTwoPlayer() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.Building);
        opengame.getBoard().getOpenCities().clear();
        opengame.getBoard().getOpenCities().add(factory.newCity("Deathhausen", 666));
        OpenCity city = factory.newCity("Zweithausen", 666);
        opengame.getBoard().getOpenCities().add(city);

        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlayer player2 = factory.newPlayer("Nein No", "gelb");
        player.setElectro(666);
        player.setPassed(false);
        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        player2.getOpenCities().add(city);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertTrue(moveTypes.contains(MoveType.Build1stCity));
        assertEquals(moveTypes.size(), 2); // Kauf von Deathhausen und ConnectNoCity
    }

    @Test
    public void Build1stCityOnePlayer2() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.Building);
        opengame.setLevel(0);
        opengame.getBoard().getOpenCities().clear();
        opengame.getBoard().getOpenCities().add(factory.newCity("Deathhausen", 666));

        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.setElectro(10);
        player.setPassed(false);
        opengame.getOpenPlayers().add(player);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertTrue(moveTypes.contains(MoveType.Build1stCity));
        assertEquals(moveTypes.size(), 2); //Connect no City und 1 mal Build1stcity
    }

    @Test
    public void Build1stCityOnePlayer3() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.Building);
        opengame.setLevel(0);
        opengame.getBoard().getOpenCities().clear();
        opengame.getBoard().getOpenCities().add(factory.newCity("Deathhausen", 666));

        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.setElectro(5);
        player.setPassed(false);
        opengame.getOpenPlayers().add(player);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertFalse(moveTypes.contains(MoveType.Build1stCity));
        assertEquals(moveTypes.size(), 1); //Connect no City
    }

    @Test
    public void Build1stCityFire() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.Building);
        opengame.setLevel(0);
        opengame.getBoard().getOpenCities().clear();
        OpenCity city = factory.newCity("Deathhausen", 666);
        opengame.getBoard().getOpenCities().add(city);
        OpenCity city2 = factory.newCity("Zweithausen", 666);
        opengame.getBoard().getOpenCities().add(city2);


        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlayer player2 = factory.newPlayer("Nein No", "gelb");
        player.setElectro(666);
        player.setPassed(false);
        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        player2.getOpenCities().add(city2);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        Optional<Problem> problem = sut.fire(Optional.of("Hihi"), haveMove.iterator().next());

        // assert
        assertEquals(player.getElectro(), 656);
        assertTrue(player.getCities().contains(city));
        assertEquals(player.getCities().size(), 1);
        assertTrue(problem.isEmpty());
    }

}
