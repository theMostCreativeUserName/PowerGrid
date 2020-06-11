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

import static org.junit.Assert.*;

/**
 * Erste Tests fuer eine Rules-Implementierung.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-30
 */

public class DropPlantTest {
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
    public DropPlantTest() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    @Test
    public void testDropPlant() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        OpenPlant plant3 = factory.newPlant(202, Plant.Type.Coal, 3, 12);
        OpenPlant plant4 = factory.newPlant(203, Plant.Type.Coal, 3, 12);
        OpenPlant plant5 = factory.newPlant(204, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.getOpenPlants().add(plant3);
        player.getOpenPlants().add(plant4);
        player.getOpenPlants().add(plant5);
        player.setElectro(100);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.DropPlant).collect(Collectors.toList());
        // assert
        assertSame(moves.size(), 4);
    }

    @Test
    public void testDropPlant2() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.Terminated);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        OpenPlant plant3 = factory.newPlant(202, Plant.Type.Coal, 3, 12);
        OpenPlant plant4 = factory.newPlant(203, Plant.Type.Coal, 3, 12);
        OpenPlant plant5 = factory.newPlant(204, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.getOpenPlants().add(plant3);
        player.getOpenPlants().add(plant4);
        player.getOpenPlants().add(plant5);
        player.setElectro(100);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.DropPlant).collect(Collectors.toList());
        // assert
        assertSame(moves.size(), 0 );
    }

    @Test
    public void testDropPlant3() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        OpenPlant plant3 = factory.newPlant(202, Plant.Type.Coal, 3, 12);
        OpenPlant plant4 = factory.newPlant(203, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.getOpenPlants().add(plant3);
        player.getOpenPlants().add(plant4);
        player.setElectro(100);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.DropPlant).collect(Collectors.toList());
        // assert
        assertSame(moves.size(), 0);
    }


    @Test
    public void testDropPlantFire() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        OpenPlant plant3 = factory.newPlant(202, Plant.Type.Coal, 3, 12);
        OpenPlant plant4 = factory.newPlant(203, Plant.Type.Coal, 3, 12);
        OpenPlant plant5 = factory.newPlant(204, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.getOpenPlants().add(plant3);
        player.getOpenPlants().add(plant4);
        player.getOpenPlants().add(plant5);
        player.setElectro(100);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().sequential().filter(Move -> Move.getType() == MoveType.DropPlant).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));

        // assert
        assertTrue(problem.isEmpty());
        assertSame(player.getOpenPlants().size() , 3);
    }

    @Test
    public void testDropPlantFire2() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        OpenPlant plant3 = factory.newPlant(202, Plant.Type.Coal, 3, 12);
        OpenPlant plant4 = factory.newPlant(203, Plant.Type.Coal, 3, 12);
        OpenPlant plant5 = factory.newPlant(204, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.getOpenPlants().add(plant3);
        player.getOpenPlants().add(plant4);
        player.getOpenPlants().add(plant5);
        player.setElectro(100);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().sequential().filter(Move -> Move.getType() == MoveType.DropPlant).collect(Collectors.toList());

        // Neue Kraftwerke für Spieler und entfernen aller Alten, damit size größer als 3 und Kraftwerk nicht Spieler gehört
        player.getOpenPlants().add(factory.newPlant(206, Plant.Type.Eco, 3, 3));
        player.getOpenPlants().add(factory.newPlant(207, Plant.Type.Eco, 3, 3));
        player.getOpenPlants().add(factory.newPlant(208, Plant.Type.Eco, 3, 3));
        player.getOpenPlants().add(factory.newPlant(209, Plant.Type.Eco, 3, 3));

        player.getOpenPlants().remove(plant);
        player.getOpenPlants().remove(plant3);
        player.getOpenPlants().remove(plant4);
        player.getOpenPlants().remove(plant5);

        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));

        // assert
        assertTrue(problem.isPresent());
    }

    @Test
    public void testDropPlantFire3() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        OpenPlant plant3 = factory.newPlant(202, Plant.Type.Coal, 3, 12);
        OpenPlant plant4 = factory.newPlant(203, Plant.Type.Coal, 3, 12);
        OpenPlant plant5 = factory.newPlant(204, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.getOpenPlants().add(plant3);
        player.getOpenPlants().add(plant4);
        player.getOpenPlants().add(plant5);
        player.setElectro(100);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().sequential().filter(Move -> Move.getType() == MoveType.DropPlant).collect(Collectors.toList());

        // Neue Kraftwerke für Spieler und entfernen aller Alten, damit size größer als 3 und Kraftwerk nicht Spieler gehört
        player.getOpenPlants().add(factory.newPlant(206, Plant.Type.Eco, 3, 3));
        player.getOpenPlants().add(factory.newPlant(207, Plant.Type.Eco, 3, 3));
        player.getOpenPlants().add(factory.newPlant(208, Plant.Type.Eco, 3, 3));
        player.getOpenPlants().add(factory.newPlant(209, Plant.Type.Eco, 3, 3));

        player.getOpenPlants().remove(plant);
        player.getOpenPlants().remove(plant3);
        player.getOpenPlants().remove(plant4);
        player.getOpenPlants().remove(plant5);

        Optional<Problem> problem =  sut.fire(Optional.of("Hihi"), moves.get(0));

        // assert
        assertSame(problem.get(), Problem.OtherPlant);
    }

    @Test (expected = IllegalStateException.class)
    public void testDropPlantFireException() {
        // arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        opengame.setPhase(Phase.ResourceBuying);
        OpenFactory factory = opengame.getFactory();


        //Player
        OpenPlayer player = factory.newPlayer("Hihi", "red");
        OpenPlant plant = factory.newPlant(200, Plant.Type.Coal, 3, 12);
        OpenPlant plant3 = factory.newPlant(202, Plant.Type.Coal, 3, 12);
        OpenPlant plant4 = factory.newPlant(203, Plant.Type.Coal, 3, 12);
        OpenPlant plant5 = factory.newPlant(204, Plant.Type.Coal, 3, 12);
        player.getOpenPlants().add(plant);
        player.getOpenPlants().add(plant3);
        player.getOpenPlants().add(plant4);
        player.getOpenPlants().add(plant5);
        player.setElectro(100);

        OpenPlayer player2 = factory.newPlayer("NOOOOO", "blue");
        OpenPlant plant2 = factory.newPlant(201, Plant.Type.Oil, 3, 12);
        player2.getOpenPlants().add(plant2);
        player2.setElectro(200);

        opengame.getOpenPlayers().add(player);
        opengame.getOpenPlayers().add(player2);


        // act
        final Set<Move> haveMove = sut.getMoves(Optional.of("Hihi"));
        List<Move> moves = haveMove.stream().sequential().filter(Move -> Move.getType() == MoveType.DropPlant).collect(Collectors.toList());
        HotMove move = (HotMove) moves.get(0);
        move.collect(opengame, Optional.of(factory.newPlayer("Irgendwas", "Mir egal")));
    }
}
