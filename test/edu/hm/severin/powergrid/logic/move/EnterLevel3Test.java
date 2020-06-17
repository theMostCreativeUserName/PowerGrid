package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static junit.framework.TestCase.*;

public class EnterLevel3Test {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test
    private final OpenFactory factory = OpenFactory.newFactory("edu.hm.severin.powergrid.datastore.NeutralFactory");
    private final OpenGame game = factory.newGame(new EditionGermany());

    public EnterLevel3Test() {
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        System.setProperty("powergrid.randomsource","edu.hm.severin.powergrid.logic.SortingRandom");
    }

    public EnterLevel3 getSut() throws ReflectiveOperationException {
        Constructor<EnterLevel3> cTor = EnterLevel3.class
                .getDeclaredConstructor(OpenGame.class);
        cTor.setAccessible(true);
        return cTor.newInstance(game);
    }

    public EnterLevel3 getSutProto() throws ReflectiveOperationException {
        Constructor<EnterLevel3> cTor = EnterLevel3.class
                .getDeclaredConstructor();
        cTor.setAccessible(true);
        return cTor.newInstance();
    }

    @Test
    public void getGame() throws ReflectiveOperationException {
        EnterLevel3 sut = getSut();
        assertEquals(game, sut.getGame());
    }

    @Test
    public void getType() throws ReflectiveOperationException {
        EnterLevel3 sut = getSut();
        assertEquals(MoveType.EnterLevel3, sut.getType());
    }

    @Test(expected = IllegalStateException.class)
    public void testCollect1() throws ReflectiveOperationException {
        EnterLevel3 sut = getSut();
        OpenPlayer player = factory.newPlayer("nnnnnn", "Fucking-rainbow");
        assertEquals(Set.of(), sut.collect(null, Optional.of(player)));
        sut.collect(game, Optional.empty());
    }

    @Test
    public void testCollect2() throws ReflectiveOperationException {
        EnterLevel3 sut = getSutProto();
        game.setPhase(Phase.ResourceBuying);
        OpenPlant p3 = factory.newPlant(2, Plant.Type.Level3,1,3);

        game.getPlantMarket().getOpenActual().add(p3);
        assertFalse(sut.collect(game, Optional.empty()).isEmpty());
    }

    @Test
    public void testCollect3() throws ReflectiveOperationException {
        EnterLevel3 sut = getSutProto();
        game.setPhase(Phase.PlantOperation);
        OpenPlant p3 = factory.newPlant(2, Plant.Type.Level3,1,3);
        game.getPlantMarket().getOpenActual().add(p3);
        assertFalse(sut.collect(game, Optional.empty()).isEmpty());
    }
    @Test
    public void testCollect4() throws ReflectiveOperationException {
        EnterLevel3 sut = getSutProto();
        game.setPhase(Phase.PlayerOrdering);
        OpenPlant p3 = factory.newPlant(2, Plant.Type.Level3,1,3);
        game.getPlantMarket().getOpenActual().add(p3);
        assertFalse(sut.collect(game, Optional.empty()).isEmpty());
    }
    @Test
    public void testCollect5() throws ReflectiveOperationException {
        EnterLevel3 sut = getSutProto();
        game.setPhase(Phase.Bureaucracy);
        game.setLevel(2);

        assertTrue(sut.collect(game, Optional.empty()).isEmpty());
    }
    @Test
    public void testCollect6() throws ReflectiveOperationException {
        EnterLevel3 sut = getSutProto();
        game.setPhase(Phase.ResourceBuying);
        game.setLevel(5);

        assertTrue(sut.collect(game, Optional.empty()).isEmpty());
    }
    @Test
    public void testCollect7() throws ReflectiveOperationException {
        EnterLevel3 sut = getSutProto();
        game.setPhase(Phase.ResourceBuying);
        game.setLevel(1);

        assertTrue(sut.collect(game, Optional.empty()).isEmpty());
    }
    @Test
    public void run() throws ReflectiveOperationException {
        EnterLevel3 sut = getSut();
        game.setPhase(Phase.PlayerOrdering);
        game.setLevel(1);
        OpenPlant p1 = factory.newPlant(101, Plant.Type.Coal,2,3);
        OpenPlant p2 = factory.newPlant(1, Plant.Type.Coal,2,3);
        OpenPlant p4 = factory.newPlant(104, Plant.Type.Coal,2,3);
        OpenPlant p3 = factory.newPlant(200, Plant.Type.Level3,1,3);
        OpenPlant p5 = factory.newPlant(100, Plant.Type.Coal,1,3);

        game.getPlantMarket().getOpenActual().add(p1);
        game.getPlantMarket().getOpenActual().add(p3);
        game.getPlantMarket().getOpenActual().add(p5);
        game.getPlantMarket().getOpenHidden().add(p2);
        game.getPlantMarket().getOpenHidden().add(p4);

        sut.run(true);
        assertEquals(2,game.getLevel());
        assertEquals(1,game.getPlantMarket().getOpenActual().size());
        assertTrue(game.getPlantMarket().getOpenActual().contains(p1));
        assertFalse(game.getPlantMarket().getOpenActual().contains(p5));
        assertFalse(game.getPlantMarket().getOpenActual().contains(p3));
        assertTrue(game.getPlantMarket().getOpenHidden().contains(p2));
        assertTrue(game.getPlantMarket().getOpenHidden().contains(p4));
        assertEquals(p2,game.getPlantMarket().getOpenHidden().get(0));
        assertEquals(null, game.getPlantMarket().findPlant(200));

    }


}
