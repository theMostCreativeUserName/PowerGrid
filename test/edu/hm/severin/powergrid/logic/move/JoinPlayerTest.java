package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class JoinPlayerTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test
    private final OpenFactory factory = OpenFactory.newFactory("edu.hm.severin.powergrid.datastore.NeutralFactory");
    private final OpenGame game = factory.newGame(new EditionGermany());

    public JoinPlayerTest() {
        System.setProperty("powergrid.randomsource","edu.hm.severin.powergrid.logic.SortingRandom");
    }

    public NewPlayerJoins getSut() throws ReflectiveOperationException {
       Constructor<NewPlayerJoins> cTor =  NewPlayerJoins.class
                .getDeclaredConstructor(OpenGame.class);
       cTor.setAccessible(true);
       return cTor.newInstance(game);
    }
    public NewPlayerJoins getSutProto() throws ReflectiveOperationException {
        Constructor<NewPlayerJoins> cTor = NewPlayerJoins.class
                .getDeclaredConstructor();
        cTor.setAccessible(true);
        return cTor.newInstance();
    }

    @Test public void newClass() throws ReflectiveOperationException {
        NewPlayerJoins sut = getSut();
        assertFalse(sut.hasPriority());
        assertFalse(sut.isAutoFire());

    }
    @Test public void illegalPhase() throws ReflectiveOperationException {
        NewPlayerJoins sut = getSut();
        game.setPhase(Phase.Building);
        Optional<Problem> problem = sut.run(true);
        assertSame(Problem.NotNow, problem.get());
    }
    @Test public void maxPlayersAlready() throws ReflectiveOperationException {
        NewPlayerJoins sut = getSut();
        sut.run(true);
        sut.run(true);
        sut.run(true);
        sut.run(true);
        sut.run(true);
        sut.run(true);
        Optional<Problem>problem = sut.run(true);
        // tries to add 7 players
        assertSame(Problem.MaxPlayers, problem.get());
    }
    @Test public void addPlayers() throws ReflectiveOperationException {
        NewPlayerJoins sut = getSut();
        sut.run(true);
        assertSame(game.getOpenPlayers().size(), 1);
        assertEquals("FF0001X", game.getOpenPlayers().get(0).getSecret());
        sut.run(true);
        sut.run(true);
        sut.run(true);
        assertEquals("0000F2X", game.getOpenPlayers().get(1).getSecret());
        assertEquals("00FF03X", game.getOpenPlayers().get(2).getSecret());
        assertEquals("FFFF04X", game.getOpenPlayers().get(3).getSecret());
        assertSame(game.getOpenPlayers().size(), 4);
        sut.run(true);
        sut.run(true);
        assertEquals("000005X", game.getOpenPlayers().get(4).getSecret());
        assertEquals("8000F6X", game.getOpenPlayers().get(5).getSecret());
        assertSame(game.getOpenPlayers().size(), 6);
    }
    @Test public void getGame() throws ReflectiveOperationException {
        NewPlayerJoins sut = getSut();
        assertEquals(game, sut.getGame());
    }
    @Test public void getType() throws ReflectiveOperationException {
        NewPlayerJoins sut = getSut();
        assertEquals(MoveType.JoinPlayer, sut.getType());
    }
    @Test (expected = IllegalStateException.class)
    public void illegalCollect() throws ReflectiveOperationException {
        NewPlayerJoins sut = getSut();
        sut.collect(game, Optional.empty());
    }
    @Test public void collect() throws ReflectiveOperationException {
        NewPlayerJoins sut = getSutProto();
        Set<HotMove> move = sut.collect(game,Optional.empty());
        assertFalse(move.isEmpty());
      }
    @Test public void collectProperties() throws ReflectiveOperationException {
        NewPlayerJoins sut = getSutProto();
        Set<HotMove> moves= sut.collect(game,Optional.empty());
        List<Move> moves2 = moves.stream().filter(Move -> Move.getType() == MoveType.JoinPlayer).collect(Collectors.toList());
        Move move = moves2.get(0);
        assertSame(move.getProperties().getProperty("type"), MoveType.JoinPlayer.toString());
    }
    @Test public void testRun() throws ReflectiveOperationException {
        NewPlayerJoins sut = getSutProto();
        OpenGame g = factory.newGame(new EditionGermany());
        g.setPhase(Phase.Bureaucracy);
        assertEquals(Set.of(), sut.collect(g,Optional.empty()));
    }

}
