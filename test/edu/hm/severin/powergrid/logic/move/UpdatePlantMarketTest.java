package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
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

    public UpdatePlantMarket getSut() throws ReflectiveOperationException {
        Constructor<UpdatePlantMarket> cTor = UpdatePlantMarket.class
                .getDeclaredConstructor(OpenGame.class);
        cTor.setAccessible(true);
        return cTor.newInstance(game);
    }

    public Constructor<UpdatePlantMarket> getSutNoGame() throws ReflectiveOperationException {
        return UpdatePlantMarket.class
                .getDeclaredConstructor(OpenGame.class);
    }

    @Test
    public void newClass() throws ReflectiveOperationException {
        UpdatePlantMarket sut = getSut();
        assertTrue(sut.hasPriority());
        assertTrue(sut.isAutoFire());
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

    @Test
    public void noActualPlants() throws ReflectiveOperationException {
        Constructor<UpdatePlantMarket> cTor = getSutNoGame();
        cTor.setAccessible(true);
        OpenGame test = factory.newGame(new EditionGermany());
        test.setPhase(Phase.PlantBuying);

        UpdatePlantMarket sut = cTor.newInstance(test);
        Optional<Problem> problem = sut.run(false);
        assertEquals(Problem.NoPlants, problem.get());
        sut = cTor.newInstance(test);
        problem = sut.run(false);
        assertEquals(Problem.NoPlants, problem.get());
    }

    @Test
    public void testCollect1() throws ReflectiveOperationException {
        UpdatePlantMarket sut = getSut();
        Set<HotMove> move = sut.collect(game, Optional.of("invalid"));
        assertTrue(move.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testCollect2() throws ReflectiveOperationException {
        UpdatePlantMarket sut = getSut();
        Set<HotMove> move = sut.collect(game, Optional.empty());
        assertTrue(move.isEmpty());
    }
    @Test public void testCollect3() throws ReflectiveOperationException {
        Constructor<UpdatePlantMarket> cTor = UpdatePlantMarket.class
                .getDeclaredConstructor();
        cTor.setAccessible(true);
        UpdatePlantMarket sut = cTor.newInstance();
        Set<HotMove> move = sut.collect(game, Optional.empty());
        assertTrue(move.isEmpty());
    }

}
