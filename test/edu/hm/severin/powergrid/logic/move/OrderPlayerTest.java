package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertSame;

public class OrderPlayerTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test
    private final OpenFactory factory = OpenFactory.newFactory("edu.hm.severin.powergrid.datastore.NeutralFactory");
    private final OpenGame game = factory.newGame(new EditionGermany());
    private final Rules rules = Rules.newRules("edu.hm.severin.powergrid.logic.StandardRules", game);

    public OrderPlayerTest() {
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        System.setProperty("powergrid.randomsource","edu.hm.severin.powergrid.logic.SortingRandom");
    }

    public OrderPlayers getSut() throws ReflectiveOperationException {
        Constructor<OrderPlayers> cTor = OrderPlayers.class
                .getDeclaredConstructor(OpenGame.class);
        cTor.setAccessible(true);
        return cTor.newInstance(game);
    }

    public OrderPlayers getSutProto() throws ReflectiveOperationException {
        Constructor<OrderPlayers> cTor = OrderPlayers.class
                .getDeclaredConstructor();
        cTor.setAccessible(true);
        return cTor.newInstance();
    }

    @Test
    public void getGame() throws ReflectiveOperationException {
        OrderPlayers sut = getSut();
        assertEquals(game, sut.getGame());
    }

    @Test
    public void getType() throws ReflectiveOperationException {
        OrderPlayers sut = getSut();
        assertEquals(MoveType.OrderPlayers, sut.getType());
    }

    @Test(expected = IllegalStateException.class)
    public void testCollect1() throws ReflectiveOperationException {
        OrderPlayers sut = getSut();
        OpenPlayer player = factory.newPlayer("nnnnnn", "Fucking-rainbow");
        assertEquals(Set.of(), sut.collect(null, Optional.of(player)));
        sut.collect(game, Optional.empty());
    }

    @Test
    public void testCollect2() throws ReflectiveOperationException {
        OrderPlayers sut = getSutProto();
        game.setPhase(Phase.PlayerOrdering);
        assertFalse(sut.collect(game, Optional.empty()).isEmpty());
    }

    @Test
    public void testCollect3() throws ReflectiveOperationException {
        OrderPlayers sut = getSutProto();
        game.setPhase(Phase.PlayerOrdering);
        assertFalse(sut.collect(game, Optional.empty()).isEmpty());
    }

    @Test
    public void testRun1() throws ReflectiveOperationException {
        OrderPlayers sut = getSut();
        game.setPhase(Phase.Opening);
        assertEquals(Problem.NotNow, sut.run(false).get());
    }

    @Test
    public void testRun2() throws ReflectiveOperationException {
        // HELP ME FUCKING GOD; HOW DO I TEST THIS
        OrderPlayers sut = getSut();
        game.setPhase(Phase.PlayerOrdering);
        OpenPlayer player1 = factory.newPlayer("abc", "red");
        OpenPlayer player2 = factory.newPlayer("abc", "blue");
        OpenPlayer player3 = factory.newPlayer("abc", "yellow");
        OpenGame open = (OpenGame) game;
        open.getOpenPlayers().add(player1);
        open.getOpenPlayers().add(player2);
        open.getOpenPlayers().add(player3);
        sut.run(true);
        List<String> sorted = new ArrayList<>();

        // so the thing is actually understandable... and can be read by humans
        for (Player player : open.getPlayers()) {
            sorted.add(player.getColor());
        }
        assertEquals("[blue, red, yellow]", sorted.toString());
        assertFalse(player1.hasPassed());
        assertFalse(player2.hasPassed());
        assertFalse(player3.hasPassed());
        assertSame(game.getPhase(), Phase.PlantBuying);

    }
    @Test
    public void testRunDifferentCities() throws ReflectiveOperationException {
        OrderPlayers sut = getSut();
        game.setPhase(Phase.PlayerOrdering);
        OpenPlayer player1 = factory.newPlayer("abc", "red");
        OpenCity city = factory.newCity("n", 2);
        OpenCity city2 = factory.newCity("n2", 2);
        OpenPlant plant = factory.newPlant(1, Plant.Type.Coal,2,3);
        player1.getOpenCities().add(city);
        player1.getOpenCities().add(city2);
        player1.getOpenPlants().add(plant);
        OpenPlayer player2 = factory.newPlayer("abc", "blue");
        player2.getOpenCities().add(city);
        OpenGame open = (OpenGame) game;
        open.getOpenPlayers().add(player1);
        open.getOpenPlayers().add(player2);
        sut.run(true);
        List<String> sorted = new ArrayList<>();

        // so the thing is actually understandable... and can be read by humans
        for (Player player : open.getPlayers()) {
            sorted.add(player.getColor());
        }
        assertEquals("[red, blue]", sorted.toString());
    }
    @Test
    public void testRunSameCities() throws ReflectiveOperationException {
        OrderPlayers sut = getSut();
        game.setPhase(Phase.PlayerOrdering);
        OpenPlayer player1 = factory.newPlayer("abc", "red");
        OpenCity city = factory.newCity("n", 2);
        OpenPlant plant2 = factory.newPlant(5555, Plant.Type.Coal,3,4);
        OpenPlant plant = factory.newPlant(1, Plant.Type.Coal,2,3);
        player1.getOpenCities().add(city);
        player1.getOpenPlants().add(plant);

        OpenPlayer player2 = factory.newPlayer("abc", "blue");
        player2.getOpenCities().add(city);
        player2.getOpenPlants().add(plant2);
        OpenGame open = (OpenGame) game;
        open.getOpenPlayers().add(player1);
        open.getOpenPlayers().add(player2);

        sut.run(true);
        List<String> sorted = new ArrayList<>();

        // so the thing is actually understandable... and can be read by humans
        for (Player player : open.getPlayers()) {
            sorted.add(player.getColor());
        }
        assertEquals("[blue, red]", sorted.toString());
    }
    @Test public void testRun3() throws ReflectiveOperationException {
        OrderPlayers sut = getSutProto();
        OpenGame g = factory.newGame(new EditionGermany());
        g.setPhase(Phase.Opening);
        assertEquals(Set.of(), sut.collect(g,Optional.empty()));
    }
    @Test public void testRun4() throws ReflectiveOperationException {
        OrderPlayers sut = getSut();
        OpenGame g = factory.newGame(new EditionGermany());
        g.setPhase(Phase.PlayerOrdering);
        OpenPlayer player1 = factory.newPlayer("abc", "c");
        OpenPlayer player2 = factory.newPlayer("abc", "b");
        OpenPlayer player3 = factory.newPlayer("abc", "a");
        g.getOpenPlayers().add(player1);
        g.getOpenPlayers().add(player2);
        g.getOpenPlayers().add(player3);
        sut.run(true);
        assertEquals("a", g.getOpenPlayers().get(0).getColor());
        assertEquals("b", g.getOpenPlayers().get(1).getColor());
        assertEquals("c", g.getOpenPlayers().get(2).getColor());
    }
    @Test public void testRun5() throws ReflectiveOperationException {
        OrderPlayers sut = getSut();
        OpenGame g = factory.newGame(new EditionGermany());
        g.setPhase(Phase.PlayerOrdering);
        OpenPlayer player1 = factory.newPlayer("c", "c");
        OpenPlayer player2 = factory.newPlayer("b", "b");
        OpenPlayer player3 = factory.newPlayer("a", "a");
        g.getOpenPlayers().add(player1);
        player2.getOpenPlants().add(factory.newPlant(80, Plant.Type.Coal, 3, 4));
        g.getOpenPlayers().add(player2);
        player1.getOpenPlants().add(factory.newPlant(2, Plant.Type.Coal, 3, 4));
        g.getOpenPlayers().add(player3);
        player3.getOpenPlants().add(factory.newPlant(13, Plant.Type.Coal, 3, 4));

        List<String> before = new ArrayList<>();
        for (Player player : g.getPlayers()) {
            before.add(player.getColor());
        }
        sut.run(true);
        List<String> sorted = new ArrayList<>();

        // so the thing is actually understandable... and can be read by humans
        for (Player player : g.getPlayers()) {
            sorted.add(player.getColor());
        }
        assertEquals("[b, a, c]", sorted.toString());
        assertFalse(before.toString().equals(sorted.toString()));
    }

    //x.20
    @Test
    public void OrderPlayerFire(){
        OpenGame openGame = (OpenGame) game;
        openGame.setPhase(Phase.PlayerOrdering);

        openGame.getPlantMarket().getOpenActual().add(factory.newPlant(333, Plant.Type.Eco, 20, 21));
        openGame.getPlantMarket().getOpenActual().add(factory.newPlant(334, Plant.Type.Eco, 21, 22));

        //Player1
        OpenPlayer player1 = factory.newPlayer("Nein", "rot");
        OpenPlant plant = factory.newPlant(201, Plant.Type.Eco, 10, 10);
        player1.getOpenPlants().add(plant);
        player1.setPassed(true);
        player1.setElectro(2000);

        //Player2
        OpenPlayer player2 = factory.newPlayer("Ja", "blau");
        player2.setPassed(true);

        //game
        openGame.getOpenPlayers().add(player1);
        openGame.getOpenPlayers().add(player2);

        final Set<Move> haveMove = rules.getMoves(Optional.of("Nein"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.OrderPlayers).collect(Collectors.toList());
        Optional<Problem> problem =  rules.fire(Optional.of("Nein"), moves.get(0));

        assertSame(openGame.getPhase(), Phase.PlantBuying);
        assertFalse(player1.hasPassed());
        assertFalse(player2.hasPassed());
        assertTrue(problem.isEmpty());
    }

    @Test
    public void OrderPlayerProperties(){
        OpenGame openGame = (OpenGame) game;
        openGame.setPhase(Phase.PlayerOrdering);

        openGame.getPlantMarket().getOpenActual().add(factory.newPlant(333, Plant.Type.Eco, 20, 21));
        openGame.getPlantMarket().getOpenActual().add(factory.newPlant(334, Plant.Type.Eco, 21, 22));

        //Player1
        OpenPlayer player1 = factory.newPlayer("Nein", "rot");
        OpenPlant plant = factory.newPlant(201, Plant.Type.Eco, 10, 10);
        player1.getOpenPlants().add(plant);
        player1.setPassed(true);
        player1.setElectro(2000);

        //Player2
        OpenPlayer player2 = factory.newPlayer("Ja", "blau");
        player2.setPassed(true);

        //game
        openGame.getOpenPlayers().add(player1);
        openGame.getOpenPlayers().add(player2);

        final Set<Move> haveMove = rules.getMoves(Optional.of("Nein"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.OrderPlayers).collect(Collectors.toList());
        Move move = moves.get(0);
        assertSame(move.getProperties().getProperty("type"), MoveType.OrderPlayers.toString());
    }
}
