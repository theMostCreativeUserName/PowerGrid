package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
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

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class OrderPlayerTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test
    private final OpenFactory factory = OpenFactory.newFactory("edu.hm.severin.powergrid.datastore.NeutralFactory");
    private final OpenGame game = factory.newGame(new EditionGermany());

    public OrderPlayerTest() {
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        System.setProperty("powergrid.random", "edu.hm.cs.rs.powergrid.logic.SortingRandomSource");
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
        assertEquals(Set.of(), sut.collect(null, Optional.of("invalid")));
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
        for (Player player : open.getPlayers()) {
            sorted.add(player.getColor());
        }
        assertFalse(true);
        System.out.println(sorted);
    }
    @Test
    public void testRun3() throws ReflectiveOperationException {
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
        for (Player player : open.getPlayers()) {
            sorted.add(player.getColor());
        }
        assertEquals("[red, blue]", sorted.toString());
    }
    @Test
    public void testRun4() throws ReflectiveOperationException {
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
        System.out.println(game.getOpenPlayers().get(0).getColor());
        List<String> sorted = new ArrayList<>();
        for (Player player : open.getPlayers()) {
            sorted.add(player.getColor());
        }
        System.out.println(sorted);
        assertEquals("[blue, red]", sorted.toString());
    }
}
