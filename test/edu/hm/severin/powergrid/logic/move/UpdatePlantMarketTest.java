package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
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
import edu.hm.severin.powergrid.datastore.NeutralPlant;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertSame;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * test for UpdatePlantMarket
 *
 * @author Severin
 */
public class UpdatePlantMarketTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test
    private final OpenFactory factory = OpenFactory.newFactory("edu.hm.severin.powergrid.datastore.NeutralFactory");
    private final OpenGame game = factory.newGame(new EditionGermany());
    private final Rules rules = Rules.newRules("edu.hm.severin.powergrid.logic.StandardRules", game);


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
    public UpdatePlantMarketTest() {
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
    }

    public UpdatePlantMarket getSut() throws ReflectiveOperationException {
        Constructor<UpdatePlantMarket> cTor = UpdatePlantMarket.class
                .getDeclaredConstructor(OpenGame.class);
        cTor.setAccessible(true);
        return cTor.newInstance(game);
    }

    public UpdatePlantMarket getSutProto() throws ReflectiveOperationException {
        Constructor<UpdatePlantMarket> cTor = UpdatePlantMarket.class
                .getDeclaredConstructor();
        cTor.setAccessible(true);
        return cTor.newInstance();
    }


    @Test
    public void getGame() throws ReflectiveOperationException {
        UpdatePlantMarket sut = getSut();
        assertEquals(game, sut.getGame());
    }

    @Test
    public void getType() throws ReflectiveOperationException {
        UpdatePlantMarket sut = getSut();
        assertEquals(MoveType.UpdatePlantMarket, sut.getType());
    }

    @Test
    public void testPhases() throws ReflectiveOperationException {
        UpdatePlantMarket sut = getSut();
        game.getPlantMarket().getOpenHidden().clear();
        game.setPhase(Phase.Opening);
        Optional<Problem> problem = sut.run(false);
        assertEquals(Problem.NotNow, problem.get());
        game.setPhase(Phase.PlantBuying);
        problem = sut.run(false);
        assertEquals(Problem.NoPlants, problem.get());
        game.setPhase(Phase.Building);
        problem = sut.run(false);
        assertEquals(Problem.NoPlants, problem.get());
        game.setPhase(Phase.Bureaucracy);
        problem = sut.run(false);
        assertEquals(Problem.NoPlants, problem.get());
    }


    @Test(expected = IllegalStateException.class)
    public void testCollect1() throws ReflectiveOperationException {
        UpdatePlantMarket sut = getSut();
        Set<HotMove> move = sut.collect(game, Optional.empty());
        assertTrue(move.isEmpty());
    }
    @Test public void testCollect2() throws ReflectiveOperationException {

        UpdatePlantMarket sut = getSutProto();
        Set<HotMove> move = sut.collect(game, Optional.empty());
        assertTrue(move.isEmpty());
    }
    @Test public void testCollect3() throws ReflectiveOperationException {
        UpdatePlantMarket sut = getSutProto();
        game.setPhase(Phase.PlantBuying);
        OpenPlant p = factory.newPlant(1, Plant.Type.Coal, 2,3);
        game.getPlantMarket().getOpenActual().add(p);
        game.getPlantMarket().getOpenFuture().add(p);
        Set<HotMove> move = sut.collect(game, Optional.empty());

        assertFalse(sut.collect(game, Optional.empty()).isEmpty());
        assertEquals(MoveType.UpdatePlantMarket, move.iterator().next().getType());
    }
    @Test public void updatePlantMarketProperties() throws ReflectiveOperationException {
        UpdatePlantMarket sut = getSutProto();
        game.setPhase(Phase.PlantBuying);
        OpenPlant p = factory.newPlant(1, Plant.Type.Coal, 2,3);
        game.getPlantMarket().getOpenActual().add(p);
        game.getPlantMarket().getOpenFuture().add(p);
        Set<HotMove> moves = sut.collect(game, Optional.empty());
        List<Move> moves2 = moves.stream().filter(Move -> Move.getType() == MoveType.UpdatePlantMarket).collect(Collectors.toList());
        Move move = moves2.get(0);
        Assert.assertSame(move.getProperties().getProperty("type"), MoveType.UpdatePlantMarket.toString());

    }
    @Test public void testRunNoPlants() throws ReflectiveOperationException {
        UpdatePlantMarket sut = getSut();
        game.setPhase(Phase.PlantBuying);
        OpenPlant p = factory.newPlant(1, Plant.Type.Coal, 2,3);
        game.getPlantMarket().getOpenActual().add(p);
        game.getPlantMarket().getOpenHidden().clear();
        assertEquals(Problem.NoPlants, sut.run(false).get());
    }
    @Test public void testRunMarketFine() throws ReflectiveOperationException {
        UpdatePlantMarket sut = getSut();
        game.setPhase(Phase.PlantBuying);
        OpenPlant p1 = factory.newPlant(1, Plant.Type.Coal, 2,3);
        OpenPlant p2 = factory.newPlant(2, Plant.Type.Coal, 2,3);
        OpenPlant p3 = factory.newPlant(3, Plant.Type.Coal, 2,3);
        OpenPlant p4 = factory.newPlant(4, Plant.Type.Coal, 2,3);
        game.getPlantMarket().getOpenActual().add(p1);
        game.getPlantMarket().getOpenActual().add(p2);
        game.getPlantMarket().getOpenActual().add(p3);
        game.getPlantMarket().getOpenActual().add(p4);
        assertEquals(Problem.PlantMarketFine, sut.run(false).get());
    }
    @Test public void testRunRealCompleteActualWithFuture(){
        game.setPhase(Phase.PlantBuying);
        game.setLevel(0);
        OpenPlayer pl1 = factory.newPlayer("mmmm", "red");
        OpenPlayer pl2 = factory.newPlayer("nnnn", "blue");
        game.getOpenPlayers().add(pl1);
        game.getOpenPlayers().add(pl2);
        pl1.setPassed(true);
        OpenPlant p1 = factory.newPlant(101, Plant.Type.Coal, 2,3);
        OpenPlant p2 = factory.newPlant(102, Plant.Type.Coal, 2,3);
        OpenPlant p3 = factory.newPlant(103, Plant.Type.Coal, 2,3);
        OpenPlant p4 = factory.newPlant(104, Plant.Type.Coal, 2,3);
        OpenPlant p5 = factory.newPlant(105, Plant.Type.Coal, 2,3);
        OpenPlant p6 = factory.newPlant(106, Plant.Type.Coal, 2,3);
        OpenPlant p7 = factory.newPlant(107, Plant.Type.Coal, 2,3);
        OpenPlant p8 = factory.newPlant(108, Plant.Type.Coal, 2,3);
        //assert plants

        game.getPlantMarket().getOpenActual().add(p1);
        game.getPlantMarket().getOpenActual().add(p2);
        game.getPlantMarket().getOpenActual().add(p3);

        game.getPlantMarket().getOpenFuture().add(p4);
        game.getPlantMarket().getOpenFuture().add(p5);
        game.getPlantMarket().getOpenFuture().add(p6);
        game.getPlantMarket().getOpenFuture().add(p7);

        game.getPlantMarket().getOpenHidden().add(0, p8);
        //act
        Set<Move> moves = rules.getMoves(Optional.of("nnnn"));
        assertSame(1, moves.size());
        rules.fire(Optional.of("nnnn"), moves.iterator().next());
        assertEquals(4, game.getPlantMarket().getOpenActual().size());
        assertEquals(4, game.getPlantMarket().getOpenFuture().size());
        assertFalse(game.getPlantMarket().getOpenHidden().contains(p8));
        assertTrue(game.getPlantMarket().getOpenFuture().contains(p8));
        assertTrue(game.getPlantMarket().getOpenActual().contains(p4));


    }

    @Test public void testRunRealCompleteActualWithHidden(){
        game.setPhase(Phase.PlantBuying);
        game.setLevel(0);
        OpenPlayer pl1 = factory.newPlayer("mmmm", "red");
        OpenPlayer pl2 = factory.newPlayer("nnnn", "blue");
        game.getOpenPlayers().add(pl1);
        game.getOpenPlayers().add(pl2);
        pl1.setPassed(true);
        OpenPlant p1 = factory.newPlant(101, Plant.Type.Coal, 2,3);
        OpenPlant p2 = factory.newPlant(102, Plant.Type.Coal, 2,3);
        OpenPlant p3 = factory.newPlant(103, Plant.Type.Coal, 2,3);
        OpenPlant p4 = factory.newPlant(104, Plant.Type.Coal, 2,3);
        OpenPlant p5 = factory.newPlant(105, Plant.Type.Coal, 2,3);
        OpenPlant p6 = factory.newPlant(106, Plant.Type.Coal, 2,3);
        OpenPlant p7 = factory.newPlant(107, Plant.Type.Coal, 2,3);
        OpenPlant p8 = factory.newPlant(108, Plant.Type.Coal, 2,3);
        //assert plants

        game.getPlantMarket().getOpenActual().add(p1);
        game.getPlantMarket().getOpenActual().add(p2);
        game.getPlantMarket().getOpenActual().add(p3);

        game.getPlantMarket().getOpenHidden().add(0, p4);

        game.getPlantMarket().getOpenFuture().add(p5);
        game.getPlantMarket().getOpenFuture().add(p6);
        game.getPlantMarket().getOpenFuture().add(p7);
        game.getPlantMarket().getOpenFuture().add(p8);
        //act
        Set<Move> moves = rules.getMoves(Optional.of("nnnn"));
        assertSame(1, moves.size());
        rules.fire(Optional.of("nnnn"), moves.iterator().next());
        assertEquals(4, game.getPlantMarket().getOpenActual().size());
        assertEquals(4, game.getPlantMarket().getOpenFuture().size());
        assertFalse(game.getPlantMarket().getOpenHidden().contains(p4));
        assertTrue(game.getPlantMarket().getOpenActual().contains(p4));

    }
    @Test public void testRunRealCompleteActualWithHidden2(){
        game.setPhase(Phase.PlantBuying);
        game.setLevel(0);
        OpenPlayer pl1 = factory.newPlayer("mmmm", "red");
        OpenPlayer pl2 = factory.newPlayer("nnnn", "blue");
        game.getOpenPlayers().add(pl1);
        game.getOpenPlayers().add(pl2);
        pl1.setPassed(true);
        OpenPlant p1 = factory.newPlant(101, Plant.Type.Coal, 2,3);
        OpenPlant p2 = factory.newPlant(102, Plant.Type.Coal, 2,3);
        OpenPlant p3 = factory.newPlant(103, Plant.Type.Coal, 2,3);
        OpenPlant p4 = factory.newPlant(104, Plant.Type.Coal, 2,3);
        OpenPlant p5 = factory.newPlant(104, Plant.Type.Coal, 2,3);
        OpenPlant p6 = factory.newPlant(106, Plant.Type.Coal, 2,3);
        OpenPlant p7 = factory.newPlant(107, Plant.Type.Coal, 2,3);
        OpenPlant p8 = factory.newPlant(108, Plant.Type.Coal, 2,3);
        //assert plants

        game.getPlantMarket().getOpenActual().add(p1);
        game.getPlantMarket().getOpenActual().add(p2);
        game.getPlantMarket().getOpenActual().add(p3);

        game.getPlantMarket().getOpenHidden().add(0, p4);

        game.getPlantMarket().getOpenFuture().add(p5);
        game.getPlantMarket().getOpenFuture().add(p6);
        game.getPlantMarket().getOpenFuture().add(p7);
        game.getPlantMarket().getOpenFuture().add(p8);
        //act
        Set<Move> moves = rules.getMoves(Optional.of("nnnn"));
        assertSame(1, moves.size());
        rules.fire(Optional.of("nnnn"), moves.iterator().next());
        assertEquals(4, game.getPlantMarket().getOpenActual().size());
        assertEquals(4, game.getPlantMarket().getOpenFuture().size());
        assertFalse(game.getPlantMarket().getOpenHidden().contains(p4));
        assertTrue(game.getPlantMarket().getOpenActual().contains(p4));

    }

    @Test public void UpdatePlantMarketFire(){
       //Game
        OpenGame openGame = (OpenGame) game;
        openGame.setPhase(Phase.PlantBuying);
        openGame.setLevel(0);
        game.getPlantMarket().getOpenHidden().clear();

        //Player
        OpenPlayer pl1 = factory.newPlayer("mmmm", "red");
        OpenPlayer pl2 = factory.newPlayer("nnnn", "blue");
        game.getOpenPlayers().add(pl1);
        game.getOpenPlayers().add(pl2);
        pl1.setPassed(true);

        //Plants
        OpenPlant p1 = factory.newPlant(101, Plant.Type.Coal, 2,3);
        OpenPlant p2 = factory.newPlant(102, Plant.Type.Coal, 2,3);
        OpenPlant p3 = factory.newPlant(103, Plant.Type.Coal, 2,3);
        OpenPlant p4 = factory.newPlant(104, Plant.Type.Coal, 2,3);
        OpenPlant p5 = factory.newPlant(105, Plant.Type.Coal, 2,3);
        OpenPlant p6 = factory.newPlant(106, Plant.Type.Coal, 2,3);
        OpenPlant p7 = factory.newPlant(107, Plant.Type.Coal, 2,3);
        OpenPlant p8 = factory.newPlant(108, Plant.Type.Coal, 2,3);

        OpenPlant SameNumber = new NeutralPlant(104, Plant.Type.Coal, 3, 4);

        //Plants in Game
        game.getPlantMarket().getOpenActual().add(p1);
        game.getPlantMarket().getOpenActual().add(p2);
        game.getPlantMarket().getOpenActual().add(p3);


        game.getPlantMarket().getOpenFuture().add(p4);
        game.getPlantMarket().getOpenFuture().add(p6);
        game.getPlantMarket().getOpenFuture().add(p7);
        game.getPlantMarket().getOpenFuture().add(p8);

        game.getPlantMarket().getOpenHidden().add(SameNumber);
        game.getPlantMarket().getOpenHidden().add(p5);

        //act
        final Set<Move> haveMove = rules.getMoves(Optional.of("mmmm"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.UpdatePlantMarket).collect(Collectors.toList());
        Optional<Problem> problem =  rules.fire(Optional.of("mmmm"), moves.get(0));


        //Assert
        assertTrue(problem.isEmpty());
        assertTrue(game.getPlantMarket().getOpenActual().contains(SameNumber));

    }



}
