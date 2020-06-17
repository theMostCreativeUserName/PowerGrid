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

public class PassAuctionTest {
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
    public PassAuctionTest() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    @Test
    public void testPassAuctionWithOnePlant() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantBuying);
        OpenFactory factory = opengame.getFactory();

        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlayer player1 = factory.newPlayer("Nooo", "bulb");
        player1.setPassed(false);
        player1.setElectro(100);
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.getOpenPlants().add(factory.newPlant(666, Plant.Type.Coal, 3, 3));
        player.setPassed(false);
        player.setElectro(100);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player1);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertTrue(moveTypes.contains(MoveType.PassAuction));
        assertTrue(moveTypes.size() == 2); // PassAuction + UpdatePlantMarket
    }

    @Test
    public void testPassAuctionWithNoPlant() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantBuying);
        OpenFactory factory = opengame.getFactory();

        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlayer player1 = factory.newPlayer("mmm", "bulb");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.setPassed(false);
        player.setElectro(10);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player1);
        player1.setPassed(false);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertTrue(moveTypes.size() == 1); // only UpdatePlantMarket
    }

    @Test
    public void testPassAuctionWithTwoPlayerFirstAndSecondNotPassedCollect() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantBuying);
        OpenFactory factory = opengame.getFactory();

        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.getOpenPlants().add(factory.newPlant(666, Plant.Type.Coal, 3, 3));
        player.setPassed(false);
        player.setElectro(10);

        OpenPlayer player2 = factory.newPlayer("NEEEIIINNN", "pink");
        OpenPlayer player1 = factory.newPlayer("Hihi", "bulb");
        player1.setPassed(false);
        player2.getOpenCities().add(factory.newCity("Berlin", 767));
        player2.getOpenPlants().add(factory.newPlant(767, Plant.Type.Coal, 3, 3));
        player2.setPassed(false);
        player2.setElectro(10);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NEEEIIINNN"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());

        // assert
        assertTrue(moveTypes.contains(MoveType.UpdatePlantMarket));
        assertTrue(moveTypes.size() == 1); // only UpdatePlantMarket
    }

    @Test
    public void testPassAuctionWithTwoPlayerFirstPassedSecondNotFire() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantBuying);
        OpenFactory factory = opengame.getFactory();

        OpenPlant p1 = factory.newPlant(101, Plant.Type.Coal, 2,3);
        OpenPlant p2 = factory.newPlant(102, Plant.Type.Coal, 2,3);
        OpenPlant p3 = factory.newPlant(103, Plant.Type.Coal, 2,3);
        OpenPlant p4 = factory.newPlant(104, Plant.Type.Coal, 2,3);
        opengame.getPlantMarket().getOpenActual().add(p1);
        opengame.getPlantMarket().getOpenActual().add(p2);
        opengame.getPlantMarket().getOpenActual().add(p3);
        opengame.getPlantMarket().getOpenActual().add(p4);

        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlayer player1 = factory.newPlayer("Hihi", "bulb");
        player1.setPassed(false);
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.getOpenPlants().add(factory.newPlant(666, Plant.Type.Coal, 3, 3));
        player.setPassed(true);
        player.setElectro(10);

        OpenPlayer player2 = factory.newPlayer("NEEEIIINNN", "pink");
        player2.getOpenCities().add(factory.newCity("Berlin", 767));
        player2.getOpenPlants().add(factory.newPlant(767, Plant.Type.Coal, 3, 3));
        player2.setPassed(false);
        player2.setElectro(10);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        opengame.getOpenPlayers().add(player1);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NEEEIIINNN"));
        Optional<Problem> problem =  sut.fire(Optional.of("NEEEIIINNN"), haveMove.iterator().next());
        // assert
        assertTrue(player.hasPassed());
        assertTrue(player2.hasPassed());
        assertTrue(problem.isEmpty());
    }

    @Test
    public void testPassAuctionProperties() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantBuying);
        OpenFactory factory = opengame.getFactory();


        //player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.getOpenPlants().add(factory.newPlant(666, Plant.Type.Coal, 3, 3));
        player.setPassed(false);
        player.setElectro(10);

        //player1
        OpenPlayer player1 = factory.newPlayer("NOOO", "bulb");
        player1.setPassed(false);
        player1.setElectro(1000);
        player1.getOpenPlants().add(factory.newPlant(333, Plant.Type.Coal, 10, 10));


        opengame.getPlantMarket().getOpenActual().add(factory.newPlant(456, Plant.Type.Eco, 10, 10));
        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player1);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().sequential().filter(Move -> Move.getType() == MoveType.PassAuction).collect(Collectors.toList());
        Move move = moves.get(0);
        assertSame(move.getProperties().getProperty("type"), MoveType.PassAuction.toString());
        assertSame(move.getProperties().getProperty("player"), player.getColor() );
    }

    @Test
    public void testPassAuctionFire() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantBuying);
        OpenFactory factory = opengame.getFactory();


        //player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.getOpenPlants().add(factory.newPlant(666, Plant.Type.Coal, 3, 3));
        player.setPassed(false);
        player.setElectro(10);

        //player1
        OpenPlayer player1 = factory.newPlayer("NOOO", "bulb");
        player1.setPassed(false);
        player1.setElectro(1000);
        player1.getOpenPlants().add(factory.newPlant(333, Plant.Type.Coal, 10, 10));


        opengame.getPlantMarket().getOpenActual().add(factory.newPlant(456, Plant.Type.Eco, 10, 10));
        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player1);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().sequential().filter(Move -> Move.getType() == MoveType.PassAuction).collect(Collectors.toList());
        assertEquals(moves.size(), 1);

        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));

        assertTrue(problem.isEmpty());
    }

    @Test
    public void testPassAuctionFire2() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantBuying);
        OpenFactory factory = opengame.getFactory();

        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlayer player1 = factory.newPlayer("OLOLOL", "bulb");
        //player.getOpenPlants().add(factory.newPlant(6666, Plant.Type.Coal, 3, 3));
        player1.setPassed(false);
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.getOpenPlants().add(factory.newPlant(666, Plant.Type.Coal, 3, 3));
        player.setPassed(false);
        player.setElectro(10);


        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player1);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.PassAuction).collect(Collectors.toList());
        player.setPassed(true);
        Optional<Problem> problem = sut.fire(Optional.of("Hihi"), moves.get(0));

        assertSame(problem.get(), Problem.NotYourTurn);
    }

    @Test
    public void testPassAuctionFire3() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantBuying);
        OpenFactory factory = opengame.getFactory();

        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlayer player1 = factory.newPlayer("OLOLOL", "bulb");
        //player.getOpenPlants().add(factory.newPlant(6666, Plant.Type.Coal, 3, 3));
        player1.setPassed(false);
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.getOpenPlants().add(factory.newPlant(666, Plant.Type.Coal, 3, 3));
        player.setPassed(false);
        player.setElectro(10);


        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player1);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.PassAuction).collect(Collectors.toList());
        player.setPassed(true);
        player1.setPassed(true);
        Optional<Problem> problem = sut.fire(Optional.of("Hihi"), moves.get(0));

        assertSame(problem.get(), Problem.NotYourTurn);
    }

    @Test (expected = IllegalStateException.class)
    public void testPassAuctionFireException() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantBuying);
        OpenFactory factory = opengame.getFactory();

        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlayer player1 = factory.newPlayer("Hihi", "bulb");
        //player.getOpenPlants().add(factory.newPlant(6666, Plant.Type.Coal, 3, 3));
        player1.setPassed(false);
        player.getOpenCities().add(factory.newCity("Testhausen", 666));
        player.getOpenPlants().add(factory.newPlant(666, Plant.Type.Coal, 3, 3));
        player.setPassed(false);
        player.setElectro(10);


        opengame.getOpenPlayers().add(player);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.PassAuction).collect(Collectors.toList());
        HotMove move = (HotMove)moves.get(0);
        move.collect(opengame, Optional.of(factory.newPlayer("Irgendwas", "Mir egal")));
    }
}
