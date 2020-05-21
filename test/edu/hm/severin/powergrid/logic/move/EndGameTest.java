package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
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
import static org.junit.Assert.assertTrue;

public class EndGameTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test
    private final OpenFactory factory = OpenFactory.newFactory("edu.hm.severin.powergrid.datastore.NeutralFactory");
    private final OpenGame game = factory.newGame(new EditionGermany());

    public EndGame getSut() throws ReflectiveOperationException {
        Constructor<EndGame> cTor =  EndGame.class
                .getDeclaredConstructor(OpenGame.class);
        cTor.setAccessible(true);
        return cTor.newInstance(game);
    }
    public EndGame getSutProto() throws ReflectiveOperationException {
        Constructor<EndGame> cTor = EndGame.class
                .getDeclaredConstructor();
        cTor.setAccessible(true);
        return cTor.newInstance();
    }
    @Test public void getGame() throws ReflectiveOperationException {
        EndGame sut = getSut();
        assertEquals(game, sut.getGame());
        assertEquals(MoveType.EndGame, sut.getType());
    }
    @Test (expected = IllegalStateException.class)
    public void collect1() throws ReflectiveOperationException {
        EndGame sut = getSut();
        sut.collect(game, Optional.empty());
    }
    @Test public void collect2() throws ReflectiveOperationException {
        EndGame sut = getSutProto();
        Set<HotMove> move = sut.collect(game, Optional.empty());
        assertTrue(move.isEmpty());
    }
    @Test public void collect3() throws ReflectiveOperationException {
        EndGame sut = getSutProto();
        game.setPhase(Phase.PlantOperation);
        Set<HotMove> move = sut.collect(game, Optional.empty());
        assertTrue(move.isEmpty());
    }
    @Test public void run1() throws ReflectiveOperationException {
        EndGame sut = getSut();
        OpenPlayer p = factory.newPlayer("mmm", "red");
        OpenPlayer p2 = factory.newPlayer("mmm", "blue");
        game.getOpenPlayers().add(p);
        game.getOpenPlayers().add(p2);
        Optional<Problem> problem = sut.run(false);
        assertEquals(Problem.NotNow, problem.get());
        problem = sut.run(false); 
    }
}
