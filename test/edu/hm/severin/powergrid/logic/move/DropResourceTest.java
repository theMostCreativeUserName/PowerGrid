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
import org.junit.Ignore;
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

public class DropResourceTest {
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
    public DropResourceTest() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    @Test
    public void testDropResource() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantOperation);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 2, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);

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
        assertTrue(moveTypes.contains(MoveType.DropResource));
    }

    @Test
    public void testDropResource2() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantOperation);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 2, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);

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
        assertFalse(moveTypes.contains(MoveType.DropResource));
    }

    @Test
    public void testDropResource3() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantOperation);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Hybrid, 2, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Oil);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.DropResource).collect(Collectors.toList());
        // assert
        assertSame(moves.size(), 0);
    }

    @Test
    public void testDropResource4() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantOperation);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Hybrid, 2, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Oil);
        player.getOpenResources().add(Resource.Oil);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.DropResource).collect(Collectors.toList());
        // assert
        assertSame(moves.size(), 2);
    }

    @Test
    public void testDropResourceFire() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantOperation);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 2, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.DropResource).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));

        // assert
        assertTrue(problem.isEmpty());
        assertEquals(player.getOpenResources().count(Resource.Coal), 4);
        assertEquals(game.getResourceMarket().getSupply().count(Resource.Coal), 1);

    }

    @Test
    public void testDropResourceFire2() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantOperation);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 2, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.DropResource).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.empty(), moves.get(0));

        // assert
        assertTrue(problem.isEmpty());
        assertEquals(player.getOpenResources().size(), 4);
    }

    @Test
    public void testDropResourceFire3() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantOperation);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Hybrid, 2, 12);
        player.getOpenPlants().add(plant);
        player.setElectro(100);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Oil);
        player.getOpenResources().add(Resource.Oil);
        player.getOpenResources().add(Resource.Oil);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.DropResource).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.empty(), moves.get(0));

        // assert
        assertTrue(problem.isEmpty());
        assertEquals(player.getOpenResources().size(), 5);
    }

    @Test
    public void testDropResourceFire4() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.PlantOperation);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Oil, 2, 12);
        OpenPlant plant3 = factory.newPlant(202, Plant.Type.Coal, 2, 12);
        player.getOpenPlants().add(plant);
        player.getOpenPlants().add(plant3);
        player.setElectro(100);
        player.getOpenResources().add(Resource.Oil);
        player.getOpenResources().add(Resource.Oil);
        player.getOpenResources().add(Resource.Oil);
        player.getOpenResources().add(Resource.Oil);
        player.getOpenResources().add(Resource.Oil);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);
        player.getOpenResources().add(Resource.Coal);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.DropResource).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.empty(), moves.get(0));

        // assert
        assertTrue(problem.isEmpty());
        assertEquals(player.getOpenResources().size(), 8);
        assertSame(player.getOpenResources().count(Resource.Oil), 4);
        assertSame(player.getOpenResources().count(Resource.Coal), 4);
    }


}
