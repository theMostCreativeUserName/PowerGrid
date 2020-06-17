package edu.hm.severin.powergrid.logic;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import edu.hm.severin.powergrid.datastore.NeutralFactory;
import edu.hm.severin.powergrid.logic.move.AbstractProperties;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static edu.hm.cs.rs.powergrid.logic.MoveType.JoinPlayer;
import static org.junit.Assert.*;

/**
 * Erste Tests fuer eine Rules-Implementierung.
 *
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @author Severin
 * @version last modified 2020-04-30
 */
public class RulesTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(2); // max seconds per test

    private final OpenGame game;

    private final Rules sut;
    private final NeutralFactory factory;

    /**
     * Fuehrt ein Runnable mehrmals aus.
     */
    private static BiConsumer<Integer, Runnable> times = (n, runnable) -> IntStream.range(0, n).forEach(__ -> runnable.run());

    /**
     * Initialisiert Factory und Spielregeln.
     */
    public RulesTest() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
        factory = new NeutralFactory();
    }

    @Test
    public void getGame() {
        assertSame(game, sut.getGame());
    }

    @Test
    public void invalidSecret() {

        Set<Move> have = sut.getMoves(Optional.of("invalid"));
        assertTrue(have.isEmpty());
    }


    @Test
    public void testJoinPlayer() {
        // arrange
        // act
        Set<Move> have = sut.getMoves(Optional.empty());
        Move move = have.iterator().next();
        // assert
        assertEquals(1, have.size());
        assertSame(JoinPlayer, move.getType());
    }

    @Test
    public void fireJoinPlayer() {
        // arrange
        // act
        Move move = sut.getMoves(Optional.empty()).iterator().next();
        Optional<Problem> problem = sut.fire(Optional.empty(), move);
        // assert
        assertTrue(problem.isEmpty());
        assertEquals(1, game.getPlayers().size());
        assertNotNull(game.getPlayers().get(0).getSecret());
        assertNull(game.getPlayers().get(0).getSecret());
    }


    @Test
    public void joinPlayerMaxPlayers() {

        OpenGame openGame = game;
        int max = openGame.getEdition().getPlayersMaximum();
        for (int i = 0; i < max; i++) {
            OpenPlayer player = factory.newPlayer(i + "secret", i + "color");
            openGame.getOpenPlayers().add(player);
        }
        final Set<Move> moves = sut.getMoves(Optional.empty());
        // assert
        final Set<Move> haveMove = sut.getMoves(Optional.empty());
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());

        assertTrue(moves.isEmpty());
    }

    @Test
    public void getMoves() {
        OpenPlayer p = new NeutralFactory().newPlayer("mm", "red");
        final Set<Move> moves = sut.getMoves(Optional.of("nn"));
        assertTrue(moves.isEmpty());
    }

    @Test
    public void testFire1() {
        game.setPhase(Phase.Opening);
        OpenPlayer p = new NeutralFactory().newPlayer("mm", "red");
        game.getOpenPlayers().add(p);
        Set<Move> move = sut.getMoves(Optional.empty());
        assertFalse(move.isEmpty());
        Object[] m = move.toArray();
        Optional<Problem> problem = sut.fire(Optional.of("mm"), (Move) m[0]);
        assertTrue(problem.isEmpty());
    }

    @Test
    public void testGetNoMovesMoves() {
        game.setPhase(Phase.Bureaucracy);
        OpenPlayer p = new NeutralFactory().newPlayer("mm", "red");

        game.getOpenPlayers().add(p);
        p.setPassed(false);
        Set<Move> move = sut.getMoves(Optional.of("invalid"));
        Set<Move> result = new HashSet<>();
        assertTrue(move.isEmpty());
        assertEquals(result.getClass(), move.getClass());
    }

    @Test
    public void getPrototypes() {
        List<MoveType> types = sut.getPrototypes().stream()
                .map(Move::getType)
                .collect(Collectors.toList());

        assertTrue(types.contains(MoveType.JoinPlayer));
        assertTrue(types.contains(MoveType.CommenceGame));
    }

    @Test
    public void testFireWrong() {
        game.setPhase(Phase.Building);
        OpenPlayer p = new NeutralFactory().newPlayer("mm", "red");
        game.getOpenPlayers().add(p);
        Set<Move> move = sut.getMoves(Optional.empty());
        assertTrue(move.isEmpty());
    }


    @Test
    public void testNextMoves() {
        OpenGame openGame = game;
        openGame.setPhase(Phase.Opening);
        int max = openGame.getEdition().getPlayersMaximum() - 1;
        for (int i = 0; i < max; i++) {
            OpenPlayer player = factory.newPlayer(i + "secret", i + "color");
            openGame.getOpenPlayers().add(player);
        }
        Set<Move> move = sut.getMoves(Optional.empty());
        Optional<Problem> problem = sut.fire(Optional.empty(), move.iterator().next());
        assertEquals(6, openGame.getOpenPlayers().size());
        assertEquals(Phase.PlantBuying, openGame.getPhase());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalMove() {
        class IllegalMove implements Move {
            private final OpenGame game;
            private final OpenPlayer player;

            /**
             * Prototype-Ctor
             */
            IllegalMove() {
                game = null;
                player = null;
            }

            private IllegalMove(OpenGame game, OpenPlayer player) {
                this.game = game;
                this.player = player;
            }

            public Optional<Problem> run(boolean real) {

                return Optional.empty();
            }

            public OpenGame getGame() {
                return game;
            }

            public Set<HotMove> collect(OpenGame game, Optional<OpenPlayer> player) {

                return null;
            }

            public MoveType getType() {
                return null;
            }
        }
        OpenGame openGame = game;
        Move move = new IllegalMove();
        sut.fire(Optional.empty(), move);
    }

    @Test
    public void testFireWithProblem() {
        game.setPhase(Phase.Opening);
        Set<Move> move = sut.getMoves(Optional.empty());
        game.setPhase(Phase.Bureaucracy);
        Optional<Problem> problem = sut.fire(Optional.empty(), move.iterator().next());
        assertTrue(problem.isPresent());
        assertEquals(Problem.NotNow, problem.get());
    }

    // x.18

    @Test
    public void testWithFakeRules(){
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        Rules sut2 = new FakeRulesTestClass(openGame);
        openGame.setPhase(Phase.Opening);

        openGame.getOpenPlayers().add(factory.newPlayer("Nein", "rot"));
        openGame.getOpenPlayers().add(factory.newPlayer("Nein2", "blau"));


        final Set<Move> haveMove = sut2.getMoves(Optional.of("Nein"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.CommenceGame).collect(Collectors.toList());
        Optional<Problem> problem = sut2.fire(Optional.of("Nein"), moves.get(0));

        assertSame(openGame.getPhase(), Phase.Terminated);
    }

    @Test
    public void testWithFakeRules2(){
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        Rules sut2 = new FakeRulesTestClass(openGame);
        openGame.setPhase(Phase.Opening);

        openGame.getOpenPlayers().add(factory.newPlayer("Nein", "rot"));
        openGame.getOpenPlayers().add(factory.newPlayer("Nein2", "blau"));
        openGame.getOpenPlayers().add(factory.newPlayer("Nein3", "gelb"));


        final Set<Move> haveMove = sut2.getMoves(Optional.of("Nein"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.CommenceGame).collect(Collectors.toList());
        Optional<Problem> problem = sut2.fire(Optional.of("Nein"), moves.get(0));

        assertSame(openGame.getPhase(), Phase.PlayerOrdering);
    }

    @Test (expected = IllegalStateException.class)
    public void testWithFakeRules3(){
        final OpenGame openGame2 = OpenFactory.newFactory().newGame(new EditionGermany());
        Rules sut2 = new FakeRulesTestClass(openGame2);
        openGame2.setPhase(Phase.Opening);

        openGame2.getOpenPlayers().add(factory.newPlayer("Nein", "rot"));
        openGame2.getOpenPlayers().add(factory.newPlayer("Nein2", "blau"));
        openGame2.getOpenPlayers().add(factory.newPlayer("Nein3", "gelb"));


        final Set<Move> haveMove = sut2.getMoves(Optional.of("Nein"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.CommenceGame).collect(Collectors.toList());
        Optional<Problem> problem = sut.fire(Optional.of("Nein"), moves.get(0));

    }

}