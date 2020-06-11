package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
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
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static edu.hm.cs.rs.powergrid.logic.MoveType.EndBuilding;
import static org.junit.Assert.*;

/**
 * Erste Tests fuer eine Rules-Implementierung.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-30
 */

public class EnterLevel2Test {
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
    public EnterLevel2Test() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    @Test
    public void testEnterLevel2Test() {
        // arrange
        //game
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.Bureaucracy);
        opengame.setLevel(0);


        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.getOpenCities().add(factory.newCity("Testhausen2", 666));
        player.getOpenCities().add(factory.newCity("Testhausen3", 666));
        player.getOpenCities().add(factory.newCity("Testhausen4", 666));
        player.getOpenCities().add(factory.newCity("Testhausen5", 666));
        player.getOpenCities().add(factory.newCity("Testhausen6", 666));
        player.getOpenCities().add(factory.newCity("Testhausen7", 666));
        player.getOpenCities().add(factory.newCity("Testhausen8", 666));
        player.getOpenCities().add(factory.newCity("Testhausen9", 666));
        player.getOpenCities().add(factory.newCity("Testhausen10", 666));
        player.getOpenCities().add(factory.newCity("Testhausen11", 666));
        player.getOpenCities().add(factory.newCity("Testhausen12", 666));
        player.getOpenCities().add(factory.newCity("Testhausen13", 666));


        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");


        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertTrue(moveTypes.contains(MoveType.EnterLevel2));
    }

    @Test
    public void testEnterLevel2Test2() {
        // arrange
        //game
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.Bureaucracy);
        opengame.setLevel(0);


        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.getOpenCities().add(factory.newCity("Testhausen2", 666));
        player.getOpenCities().add(factory.newCity("Testhausen3", 666));
        player.getOpenCities().add(factory.newCity("Testhausen4", 666));
        player.getOpenCities().add(factory.newCity("Testhausen5", 666));
        player.getOpenCities().add(factory.newCity("Testhausen6", 666));
        player.getOpenCities().add(factory.newCity("Testhausen7", 666));


        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");


        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertFalse(moveTypes.contains(MoveType.EnterLevel2));
    }

    @Test
    public void testEnterLevel2Test3() {
        // arrange
        //game
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.Bureaucracy);
        opengame.setLevel(0);


        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.getOpenCities().add(factory.newCity("Testhausen2", 666));
        player.getOpenCities().add(factory.newCity("Testhausen3", 666));
        player.getOpenCities().add(factory.newCity("Testhausen4", 666));
        player.getOpenCities().add(factory.newCity("Testhausen5", 666));
        player.getOpenCities().add(factory.newCity("Testhausen6", 666));
        player.getOpenCities().add(factory.newCity("Testhausen7", 666));
        player.getOpenCities().add(factory.newCity("Testhausen8", 666));
        player.getOpenCities().add(factory.newCity("Testhausen9", 666));
        player.getOpenCities().add(factory.newCity("Testhausen10", 666));


        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");


        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertTrue(moveTypes.contains(MoveType.EnterLevel2));
    }


    @Test
    public void testEnterLevel2Fire() {
        //game
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.Bureaucracy);
        opengame.setLevel(0);


        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.getOpenCities().add(factory.newCity("Testhausen2", 666));
        player.getOpenCities().add(factory.newCity("Testhausen3", 666));
        player.getOpenCities().add(factory.newCity("Testhausen4", 666));
        player.getOpenCities().add(factory.newCity("Testhausen5", 666));
        player.getOpenCities().add(factory.newCity("Testhausen6", 666));
        player.getOpenCities().add(factory.newCity("Testhausen7", 666));
        player.getOpenCities().add(factory.newCity("Testhausen8", 666));
        player.getOpenCities().add(factory.newCity("Testhausen9", 666));
        player.getOpenCities().add(factory.newCity("Testhausen10", 666));


        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);

        //Plantmarket
        opengame.getPlantMarket().getOpenHidden().clear();
        opengame.getPlantMarket().getOpenActual().clear();
        opengame.getPlantMarket().getOpenFuture().clear();

        OpenPlant plant1 = factory.newPlant(3, Plant.Type.Coal, 12 ,12);
        OpenPlant plant2 = factory.newPlant(15, Plant.Type.Eco, 10 , 12);
        opengame.getPlantMarket().getOpenActual().add(plant1);
        opengame.getPlantMarket().getOpenActual().add(plant2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.EnterLevel2).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));

        // assert
        System.out.println(problem);
        assertTrue(problem.isEmpty());
        assertEquals(game.getLevel(), 1);
        assertEquals(game.getPlantMarket().findPlant(3), null);
    }

    @Test
    public void testEnterLevel2Fire2() {
        //game
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.Bureaucracy);
        opengame.setLevel(0);


        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.getOpenCities().add(factory.newCity("Testhausen2", 666));
        player.getOpenCities().add(factory.newCity("Testhausen3", 666));
        player.getOpenCities().add(factory.newCity("Testhausen4", 666));
        player.getOpenCities().add(factory.newCity("Testhausen5", 666));
        player.getOpenCities().add(factory.newCity("Testhausen6", 666));
        player.getOpenCities().add(factory.newCity("Testhausen7", 666));
        player.getOpenCities().add(factory.newCity("Testhausen8", 666));
        player.getOpenCities().add(factory.newCity("Testhausen9", 666));
        player.getOpenCities().add(factory.newCity("Testhausen10", 666));


        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);

        //Plantmarket
        opengame.getPlantMarket().getOpenHidden().clear();
        opengame.getPlantMarket().getOpenActual().clear();
        opengame.getPlantMarket().getOpenFuture().clear();

        OpenPlant plant1 = factory.newPlant(3, Plant.Type.Coal, 12 ,12);
        OpenPlant plant2 = factory.newPlant(15, Plant.Type.Eco, 10 , 12);
        opengame.getPlantMarket().getOpenActual().add(plant1);
        opengame.getPlantMarket().getOpenActual().add(plant2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.EnterLevel2).collect(Collectors.toList());
        opengame.setLevel(2);
        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));

        // assert
        assertSame(problem.get(), Problem.WrongLevel);
    }

    @Test (expected = IllegalStateException.class)
    public void testEnterLevel2FireException() {
        //game
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.Bureaucracy);
        opengame.setLevel(0);


        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.getOpenCities().add(factory.newCity("Testhausen2", 666));
        player.getOpenCities().add(factory.newCity("Testhausen3", 666));
        player.getOpenCities().add(factory.newCity("Testhausen4", 666));
        player.getOpenCities().add(factory.newCity("Testhausen5", 666));
        player.getOpenCities().add(factory.newCity("Testhausen6", 666));
        player.getOpenCities().add(factory.newCity("Testhausen7", 666));
        player.getOpenCities().add(factory.newCity("Testhausen8", 666));
        player.getOpenCities().add(factory.newCity("Testhausen9", 666));
        player.getOpenCities().add(factory.newCity("Testhausen10", 666));


        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);

        //Plantmarket
        opengame.getPlantMarket().getOpenHidden().clear();
        opengame.getPlantMarket().getOpenActual().clear();
        opengame.getPlantMarket().getOpenFuture().clear();

        OpenPlant plant1 = factory.newPlant(3, Plant.Type.Coal, 12 ,12);
        OpenPlant plant2 = factory.newPlant(15, Plant.Type.Eco, 10 , 12);
        opengame.getPlantMarket().getOpenActual().add(plant1);
        opengame.getPlantMarket().getOpenActual().add(plant2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.EnterLevel2).collect(Collectors.toList());
        HotMove move = (HotMove)moves.get(0);
        move.collect(opengame, Optional.of(factory.newPlayer("Irgendwas", "Mir egal")));
    }
}
