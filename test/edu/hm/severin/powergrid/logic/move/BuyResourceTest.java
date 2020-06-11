package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import edu.hm.severin.powergrid.ListBag;
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

public class BuyResourceTest {
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
    public BuyResourceTest() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    @Test
    public void testBuyResource() {
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
        assertFalse(moveTypes.contains(MoveType.BuyResource));
    }

    @Test
    public void testBuyResource2() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.setPassed(false);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);
        player2.setPassed(false);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).filter(MoveType -> MoveType == edu.hm.cs.rs.powergrid.logic.MoveType.BuyResource).collect(Collectors.toList());

        // assert
        assertTrue(moveTypes.contains(MoveType.BuyResource));
        assertEquals(moveTypes.size(), 1);
    }

    @Test
    public void testBuyResource3() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.setPassed(true);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);
        player2.setPassed(true);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).filter(MoveType -> MoveType == edu.hm.cs.rs.powergrid.logic.MoveType.BuyResource).collect(Collectors.toList());

        // assert
        assertFalse(moveTypes.contains(MoveType.BuyResource));
        assertEquals(moveTypes.size(), 0);
    }

    @Test
    public void testBuyResource4() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.setPassed(false);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);
        player2.setPassed(false);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);

        opengame.getResourceMarket().getOpenAvailable().remove(Resource.Oil, 19);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).filter(MoveType -> MoveType == edu.hm.cs.rs.powergrid.logic.MoveType.BuyResource).collect(Collectors.toList());

        // assert
        assertFalse(moveTypes.contains(MoveType.BuyResource));
        assertEquals(moveTypes.size(), 0);
    }

    @Test
    public void testBuyResource5() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.setPassed(false);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(1);
        player2.setPassed(false);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);

        opengame.getResourceMarket().getOpenAvailable().remove(Resource.Oil, 19);
        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).filter(MoveType -> MoveType == edu.hm.cs.rs.powergrid.logic.MoveType.BuyResource).collect(Collectors.toList());

        // assert
        assertFalse(moveTypes.contains(MoveType.BuyResource));
        assertEquals(moveTypes.size(), 0);
    }

    @Test
    public void testBuyResource6() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.setPassed(false);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(3);
        player2.setPassed(false);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).filter(MoveType -> MoveType == edu.hm.cs.rs.powergrid.logic.MoveType.BuyResource).collect(Collectors.toList());

        // assert
        assertTrue(moveTypes.contains(MoveType.BuyResource));
        assertEquals(moveTypes.size(), 1);
    }

    @Test
    public void testBuyResource7() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.setPassed(false);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 1, 12);
        player2.getOpenPlants().add(plant2);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.setElectro(3);
        player2.setPassed(false);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);

        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).filter(MoveType -> MoveType == edu.hm.cs.rs.powergrid.logic.MoveType.BuyResource).collect(Collectors.toList());

        // assert
        assertFalse(moveTypes.contains(MoveType.BuyResource));
        assertEquals(moveTypes.size(), 0);
    }


    @Test
    public void testResourceFire() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.setPassed(false);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);
        player2.setPassed(false);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.BuyResource).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.of("NOOOOO"), moves.get(0));

        // assert
        assertTrue(problem.isEmpty());
        assertEquals(player2.getElectro(), 197);
        assertEquals(player2.getOpenResources().size(), 1);
        assertEquals(game.getResourceMarket().getAvailable().count(Resource.Oil), 17);

    }

    @Test
    public void testResourceFire2() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.setPassed(false);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 2, 12);
        player2.getOpenPlants().add(plant2);
        OpenPlant plant3 = factory.newPlant(210, Plant.Type.Oil, 2, 12);
        player2.getOpenPlants().add(plant3);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.setElectro(200);
        player2.setPassed(false);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.BuyResource).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.of("NOOOOO"), moves.get(0));

        // assert
        assertTrue(problem.isEmpty());
        assertEquals(player2.getOpenResources().size(), 4);
        assertEquals(game.getResourceMarket().getAvailable().count(Resource.Oil), 17);

    }

    @Test
    public void testResourceFire3() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.setPassed(false);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 2, 12);
        player2.getOpenPlants().add(plant2);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.setElectro(5);
        player2.setPassed(false);

        System.out.println(player);
        System.out.println(player2);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.BuyResource).collect(Collectors.toList());
        player2.setElectro(1);
        Optional<Problem> problem =  sut.fire(Optional.of("NOOOOO"), moves.get(0));

        // assert
        assertTrue(problem.isPresent());
        assertEquals(player2.getElectro(), 1);
        assertEquals(player2.getOpenResources().size(), 3);
        assertEquals(game.getResourceMarket().getAvailable().count(Resource.Oil), 18);

    }

    //Since here with Hybrid Plants
    @Test
    public void testBuyResource8() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.setPassed(false);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 2, 12);
        OpenPlant plant3 = factory.newPlant(202, Plant.Type.Hybrid, 2, 12);
        player2.getOpenPlants().add(plant2);
        player2.getOpenPlants().add(plant3);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);

        player2.setElectro(5);
        player2.setPassed(false);

        System.out.println(player);
        System.out.println(player2);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.BuyResource).collect(Collectors.toList());


        // assert
        assertSame( moves.size(), 2); // Kauf von Kohle und Öl
    }

    @Test
    public void testBuyResource9() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.setPassed(false);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 2, 12);
        OpenPlant plant3 = factory.newPlant(202, Plant.Type.Hybrid, 2, 12);
        OpenPlant plant4 = factory.newPlant(203, Plant.Type.Coal, 2, 12);
        player2.getOpenPlants().add(plant2);
        player2.getOpenPlants().add(plant3);
        player2.getOpenPlants().add(plant4);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Coal);
        player2.getOpenResources().add(Resource.Coal);
        player2.getOpenResources().add(Resource.Coal);
        player2.getOpenResources().add(Resource.Coal);
        player2.getOpenResources().add(Resource.Coal);
        player2.getOpenResources().add(Resource.Coal);

        player2.setElectro(5);
        player2.setPassed(false);

        System.out.println(player);
        System.out.println(player2);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.BuyResource).collect(Collectors.toList());


        // assert
        assertSame( moves.size(), 0); // Kauf von Kohle und Öl
    }

    @Test
    public void testBuyResource10() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.setPassed(false);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Coal, 2, 12);
        OpenPlant plant3 = factory.newPlant(202, Plant.Type.Hybrid, 2, 12);
        plant3.getResources().clear();
        plant3.getResources().add(new ListBag<>(Resource.Oil, Resource.Oil));
        plant3.getResources().add(new ListBag<>(Resource.Oil, Resource.Coal));
        plant3.getResources().add(new ListBag<>(Resource.Coal, Resource.Coal));
        OpenPlant plant4 = factory.newPlant(203, Plant.Type.Oil, 2, 12);
        player2.getOpenPlants().add(plant2);
        player2.getOpenPlants().add(plant3);
        player2.getOpenPlants().add(plant4);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Coal);
        player2.getOpenResources().add(Resource.Coal);
        player2.getOpenResources().add(Resource.Coal);
        player2.getOpenResources().add(Resource.Coal);
        player2.getOpenResources().add(Resource.Coal);
        player2.getOpenResources().add(Resource.Coal);

        player2.setElectro(5);
        player2.setPassed(false);

        System.out.println(player);
        System.out.println(player2);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.BuyResource).collect(Collectors.toList());


        // assert
        assertSame( moves.size(), 0); // Kauf von Kohle und Öl
    }

    @Test
    public void testBuyResource11() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.setPassed(false);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 2, 12);
        OpenPlant plant3 = factory.newPlant(202, Plant.Type.Hybrid, 2, 12);
        player2.getOpenPlants().add(plant2);
        player2.getOpenPlants().add(plant3);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Coal);
        player2.getOpenResources().add(Resource.Coal);
        player2.getOpenResources().add(Resource.Coal);

        player2.setElectro(5);
        player2.setPassed(false);

        System.out.println(player);
        System.out.println(player2);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.BuyResource).collect(Collectors.toList());


        // assert
        assertSame( moves.size(), 2); // Kauf von Kohle und Öl
    }

    @Test (expected = IllegalStateException.class)
    public void testResourceFireException() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.setPassed(false);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 2, 12);
        player2.getOpenPlants().add(plant2);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.getOpenResources().add(Resource.Oil);
        player2.setElectro(5);
        player2.setPassed(false);

        System.out.println(player);
        System.out.println(player2);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("NOOOOO"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.BuyResource).collect(Collectors.toList());
        HotMove move = (HotMove)moves.get(0);
        move.collect(opengame, Optional.of(factory.newPlayer("Irgendwas", "Mir egal")));

    }




}
