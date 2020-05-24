package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import org.junit.Ignore;
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

import static edu.hm.cs.rs.powergrid.logic.MoveType.TurnOver;
import static org.junit.Assert.*;

/**
 * Erste Tests fuer eine Rules-Implementierung.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-30
 */

public class TurnOverTest {
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
    public TurnOverTest() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }


    // Mein Zeug

    @Test
    public void testTurnOver() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Bureaucracy);
        OpenFactory factory = opengame.getFactory();
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.setPassed(false);
        opengame.getOpenPlayers().add(player);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        MoveType have = haveMove.iterator().next().getType();
        // assert
        assertEquals(have, TurnOver);
    }

    @Test
    public void testTurnOverFireNotLevel3() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Bureaucracy);
        OpenFactory factory = opengame.getFactory();
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlayer player2 = factory.newPlayer("NONONONO", "blue");
        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);

        opengame.setRound(20);
        opengame.setLevel(1);

        //Plants
        OpenPlant biggestPlant = factory.newPlant(666, Plant.Type.Eco, 2, 2);
        Set<OpenPlant> plantsActual = opengame.getPlantMarket().getOpenActual();
        List<OpenPlant> plantsHidden = opengame.getPlantMarket().getOpenHidden();
        plantsActual.add(biggestPlant);
        plantsActual.add(factory.newPlant(4, Plant.Type.Coal, 10, 10));
        plantsHidden.add(factory.newPlant(5, Plant.Type.Oil, 5, 2));

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), haveMove.iterator().next());

        // Rohstoffe
        Bag<Resource> available = opengame.getResourceMarket().getOpenAvailable();
        Bag<Resource> supply = opengame.getResourceMarket().getOpenSupply();


        final int availableAmountGarbage = available.count(Resource.Garbage);
        final int availableAmountCoal = available.count(Resource.Coal);
        final int availableAmountUranium = available.count(Resource.Uranium);
        final int availableAmountOil = available.count(Resource.Oil);

        final int supplyAmountGarbage = supply.count(Resource.Garbage);
        final int supplyAmountCoal = supply.count(Resource.Coal);
        final int supplyAmountUranium = supply.count(Resource.Uranium);
        final int supplyAmountOil = supply.count(Resource.Oil);



        //Assert
        assertEquals(availableAmountGarbage, 8);
        assertEquals(availableAmountCoal, 24);
        assertEquals(availableAmountUranium, 3);
        assertEquals(availableAmountOil, 20);

        assertEquals(supplyAmountGarbage, 16);
        assertEquals(supplyAmountCoal, 0);
        assertEquals(supplyAmountUranium, 9);
        assertEquals(supplyAmountOil, 4);

        assertSame(opengame.getRound(), 21);
        assertSame(game.getPhase(), Phase.PlayerOrdering);
        assertTrue(problem.isEmpty());

        assertEquals(opengame.getPlantMarket().getOpenActual().size(), 1);
        assertEquals(opengame.getPlantMarket().getOpenHidden().size(), 44);
        assertTrue(plantsHidden.contains(biggestPlant));
    }

    @Test
    public void testTurnOverFireLevel3() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Bureaucracy);
        OpenFactory factory = opengame.getFactory();
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlayer player2 = factory.newPlayer("NONONONO", "blue");
        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);

        opengame.setRound(20);
        opengame.setLevel(2);

        //Plants
        OpenPlant biggestPlant = factory.newPlant(666, Plant.Type.Eco, 2, 2);
        Set<OpenPlant> plantsActual = opengame.getPlantMarket().getOpenActual();
        List<OpenPlant> plantsHidden = opengame.getPlantMarket().getOpenHidden();
        plantsActual.add(biggestPlant);
        plantsActual.add(factory.newPlant(8000, Plant.Type.Coal, 10, 10));
        plantsHidden.add(factory.newPlant(5, Plant.Type.Oil, 5, 2));

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), haveMove.iterator().next());

        //Assert
        assertSame(opengame.getRound(), 21);
        assertSame(game.getPhase(), Phase.PlayerOrdering);
        assertTrue(problem.isEmpty());

        assertEquals(opengame.getPlantMarket().getOpenActual().size(), 1);
        assertEquals(opengame.getPlantMarket().getOpenHidden().size(), 43);
        assertSame(opengame.getPlantMarket().findPlant(666), null);
    }
}
