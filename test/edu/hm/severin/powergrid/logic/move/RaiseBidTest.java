package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Auction;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction;
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

import java.util.ArrayList;
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

public class RaiseBidTest {
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
    public RaiseBidTest() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    @Test
    public void testRaiseBid() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();

        opengame.setPhase(Phase.PlantAuction);

        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.setElectro(200);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        player2.setElectro(200);

        OpenPlayer player3 = factory.newPlayer("Rainbow", "pink");
        player3.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        opengame.getOpenPlayers().add(player3);

        //Auction
        List<OpenPlayer> players = List.of(player, player2, player3);
        OpenAuction auction = factory.newAuction(factory.newPlant(30, Plant.Type.Coal, 2, 3), players);
        opengame.setAuction(auction);
        auction.setPlayer(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertTrue(moveTypes.contains(MoveType.RaiseBid));
    }

    @Test
    public void testRaiseBid2() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();

        opengame.setPhase(Phase.PlantAuction);

        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.setElectro(200);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        player2.setElectro(200);

        OpenPlayer player3 = factory.newPlayer("Rainbow", "pink");
        player3.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        opengame.getOpenPlayers().add(player3);

        //Auction
        List<OpenPlayer> players = List.of(player, player2, player3);
        OpenAuction auction = factory.newAuction(factory.newPlant(30, Plant.Type.Coal, 2, 3), players);
        opengame.setAuction(auction);
        auction.setPlayer(player);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertTrue(!moveTypes.contains(MoveType.RaiseBid));
    }

    @Test
    public void testRaiseBid3() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();

        opengame.setPhase(Phase.PlantAuction);

        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.setElectro(200);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        player2.setElectro(200);

        OpenPlayer player3 = factory.newPlayer("Rainbow", "pink");
        player3.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        opengame.getOpenPlayers().add(player3);

        //Auction
        List<OpenPlayer> players = List.of(player, player2, player3);
        OpenAuction auction = factory.newAuction(factory.newPlant(30, Plant.Type.Coal, 2, 3), players);
        opengame.setAuction(auction);
        auction.setPlayer(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertTrue(moveTypes.contains(MoveType.RaiseBid));
    }

    @Test
    public void testRaiseBid4() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();

        opengame.setPhase(Phase.PlantAuction);

        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.setElectro(110);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        player2.setElectro(200);

        OpenPlayer player3 = factory.newPlayer("Rainbow", "pink");
        player3.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        opengame.getOpenPlayers().add(player3);

        //Auction
        List<OpenPlayer> players = List.of(player, player2, player3);
        OpenAuction auction = factory.newAuction(factory.newPlant(30, Plant.Type.Coal, 2, 3), players);
        opengame.setAuction(auction);
        auction.setPlayer(player2);
        auction.setAmount(110);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertTrue(!moveTypes.contains(MoveType.RaiseBid));
    }

    @Test
    public void testRaiseBid5() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();

        opengame.setPhase(Phase.PlantAuction);

        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.setElectro(200);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        player2.setElectro(200);

        OpenPlayer player3 = factory.newPlayer("Rainbow", "pink");
        player3.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        opengame.getOpenPlayers().add(player3);

        //Auction
        List<OpenPlayer> players = List.of(player, player2, player3);
        OpenAuction auction = factory.newAuction(factory.newPlant(30, Plant.Type.Coal, 2, 3), players);
        opengame.setAuction(auction);
        auction.setPlayer(player);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());
        // assert
        assertTrue(!moveTypes.contains(MoveType.RaiseBid));
    }

    @Test
    public void testRaiseBidFire() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();

        opengame.setPhase(Phase.PlantAuction);

        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.setElectro(200);
        player.setPassed(false);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        player2.setElectro(200);
        player.setPassed(false);

        OpenPlayer player3 = factory.newPlayer("Rainbow", "pink");
        player3.setElectro(200);
        player.setPassed(false);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        opengame.getOpenPlayers().add(player3);

        //Auction
        List<OpenPlayer> players = new ArrayList<>();
        players.add(player);
        players.add(player2);
        players.add(player3);
        OpenAuction auction = factory.newAuction(factory.newPlant(30, Plant.Type.Coal, 2, 3), players);
        opengame.setAuction(auction);
        auction.setPlayer(player2);
        auction.setAmount(110);

        List<OpenPlayer> players2 = new ArrayList<>();
        players2.add(player2);
        players2.add(player3);
        players2.add(player);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.RaiseBid).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));
        // assert
        assertTrue(problem.isEmpty());
        assertEquals(auction.getAmount(), 111);
        assertEquals(player, auction.getPlayer());
        assertEquals(players2, auction.getPlayers());
    }

    @Test
    public void testRaiseBidFire2() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();

        opengame.setPhase(Phase.PlantAuction);

        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.setElectro(200);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        player2.setElectro(200);

        OpenPlayer player3 = factory.newPlayer("Rainbow", "pink");
        player3.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        opengame.getOpenPlayers().add(player3);

        //Auction
        List<OpenPlayer> players = List.of(player, player2, player3);
        OpenAuction auction = factory.newAuction(factory.newPlant(30, Plant.Type.Coal, 2, 3), players);
        opengame.setAuction(auction);
        auction.setPlayer(player2);
        auction.setAmount(110);

        List<OpenPlayer> players2 = List.of(player2, player3, player);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.RaiseBid).collect(Collectors.toList());
        opengame.setAuction(null);
        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));
        // assert

        assertEquals(problem.get(), Problem.NotRunning);
    }

    @Test (expected = IllegalStateException.class)
    public void testRaiseBidFireException() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Building);
        OpenFactory factory = opengame.getFactory();

        opengame.setPhase(Phase.PlantAuction);

        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        player.setElectro(200);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        player2.setElectro(200);

        OpenPlayer player3 = factory.newPlayer("Rainbow", "pink");
        player3.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);
        opengame.getOpenPlayers().add(player3);

        //Auction
        List<OpenPlayer> players = List.of(player, player2, player3);
        OpenAuction auction = factory.newAuction(factory.newPlant(30, Plant.Type.Coal, 2, 3), players);
        opengame.setAuction(auction);
        auction.setPlayer(player2);
        auction.setAmount(110);

        List<OpenPlayer> players2 = List.of(player2, player3, player);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.RaiseBid).collect(Collectors.toList());
        HotMove move = (HotMove)moves.get(0);
        move.collect(opengame, Optional.of(factory.newPlayer("Irgendwas", "Mir egal")));
    }


}
