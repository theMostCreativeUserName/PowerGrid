package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.Problem;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.lang.reflect.Constructor;
import java.util.Optional;

import static junit.framework.TestCase.*;

public class EndResourceBuyingTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test
    private final OpenFactory factory = OpenFactory.newFactory("edu.hm.severin.powergrid.datastore.NeutralFactory");
    private final OpenGame game = factory.newGame(new EditionGermany());

    public EndResourceBuyingTest() {
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        System.setProperty("powergrid.randomsource","edu.hm.severin.powergrid.logic.SortingRandom");
    }

    public EndResourceBuying getSut() throws ReflectiveOperationException {
        Constructor<EndResourceBuying> cTor = EndResourceBuying.class
                .getDeclaredConstructor(OpenGame.class);
        cTor.setAccessible(true);
        return cTor.newInstance(game);
    }

    public EndResourceBuying getSutProto() throws ReflectiveOperationException {
        Constructor<EndResourceBuying> cTor = EndResourceBuying.class
                .getDeclaredConstructor();
        cTor.setAccessible(true);
        return cTor.newInstance();
    }

    @Test public void getGame() throws ReflectiveOperationException {
        EndResourceBuying sut = getSut();
        assertSame(game,sut.getGame());
    }
    @Test public void run() throws ReflectiveOperationException {
        EndResourceBuying sut = getSut();
        game.setPhase(Phase.ResourceBuying);
        OpenPlayer p = factory.newPlayer("very very secret", "mysterious Colour");
        game.getOpenPlayers().add(p);
        p.setPassed(true);
        sut.run(true);

        assertEquals(Phase.Building, game.getPhase());
        assertFalse(p.hasPassed());
    }
    @Test public void run2() throws ReflectiveOperationException {
        EndResourceBuying sut = getSut();
        game.setPhase(Phase.ResourceBuying);
        OpenPlayer p = factory.newPlayer("very very secret", "mysterious Colour");
        game.getOpenPlayers().add(p);
        p.setPassed(false);
        Optional<Problem> problem = sut.run(true);

        assertEquals(Phase.ResourceBuying, game.getPhase());
        assertTrue(problem.isPresent());
    }
    @Test public void run3() throws ReflectiveOperationException {
        EndResourceBuying sut = getSut();
        game.setPhase(Phase.Bureaucracy);
        OpenPlayer p = factory.newPlayer("very very secret", "mysterious Colour");
        Optional<Problem> problem = sut.run(true);

        assertEquals(Phase.Bureaucracy, game.getPhase());
        assertTrue(problem.isPresent());
    }
    @Test (expected = IllegalStateException.class)
    public void collect() throws ReflectiveOperationException {
        EndResourceBuying sut = getSut();
        sut.collect(game, Optional.empty());
    }
}
