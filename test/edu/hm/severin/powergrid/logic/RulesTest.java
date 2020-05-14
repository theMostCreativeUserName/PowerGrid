package edu.hm.severin.powergrid.logic;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import static edu.hm.cs.rs.powergrid.logic.MoveType.JoinPlayer;
import static org.junit.Assert.*;

import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Erste Tests fuer eine Rules-Implementierung.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-30
 */
public class RulesTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    /** Spielstand. */
    private final Game game;

    /** Spielregeln. */
    private final Rules sut;

    /** Fluchtwert fuer kein Geheimnis. */
    private final String NO_SECRET = "";

    /** Fuehrt ein Runnable mehrmals aus. */
    private static BiConsumer<Integer, Runnable> times = (n, runnable) -> IntStream.range(0, n).forEach(__ -> runnable.run());

    /** Initialisiert Factory und Spielregeln. */
    public RulesTest() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }
    @Test
    public void testGetGame() {
        assertSame(game, sut.getGame());
    }

    @Test public void testGetMovesInvalidSecret() {
        // arrange
        // act
        final Set<Move> have = sut.getMoves(Optional.of("invalid"));
        // assert
        assertTrue("falsches Geheimnis, keine Zuege", have.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class) public void testFireFakeMove() {
        // arrange
        // act
        sut.fire(Optional.of("invalid"), new Move() {
            @Override public MoveType getType() {
                return JoinPlayer;
            }

            @Override public Properties getProperties() {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Test public void testJoinPlayer() {
        // arrange
        // act
        final Set<Move> have = sut.getMoves(Optional.empty());
        final Move move = have.iterator().next();
        // assert
        assertEquals("Ein moeglicher Zug", 1, have.size());
        assertSame("Zug ist Mitspielen", JoinPlayer, move.getType());
    }

    @Test public void testFireJoinPlayer() {
        // arrange
        // act
        final Move move = sut.getMoves(Optional.empty()).iterator().next();
        final Optional<Problem> problem = sut.fire(Optional.empty(), move);
        // assert
        assertTrue("Neuer Spieler akzeptiert", problem.isEmpty());
        assertEquals("Spieler ist als erster und bisher einziger dabei", 1, game.getPlayers().size());
        assertNotNull("Geheimnis des Spielers verfuegbar", game.getPlayers().get(0).getSecret());
        assertNull("Zweiter Abruf verweigert", game.getPlayers().get(0).getSecret());
    }

    @Test public void testCommenceGameTooFewPlayers() {
        // arrange
        times.accept(game.getEdition().getPlayersMinimum() - 1,
                () -> fireMove(JoinPlayer, NO_SECRET));
        // act
        final String secret1 = reapSecrets().get(0);
        Set<Move> moves = sut.getMoves(Optional.of(secret1));
        // assert
        assertTrue(moves.isEmpty());
    }


    @Test public void testJoinPlayerMaxPlayers() {
        // arrange
        times.accept(game.getEdition().getPlayersMaximum(),
                () -> fireMove(JoinPlayer, NO_SECRET));
        // act
        final Set<Move> moves = sut.getMoves(Optional.empty());
        // assert
        assertTrue("keine weiteren Spieler", moves.isEmpty());
    }




    /**
     * Fuehrt einen Zug aus, der gelingen muss.
     * @param type        Typ des Zuges.
     * @param secretOrNot Geheimnis oder Leerstring, wenn keines noetig ist.
     */
    private void fireMove(MoveType type, String secretOrNot) {
        final Optional<String> secret = secretOrNot == NO_SECRET? Optional.empty(): Optional.of(secretOrNot);
        final Optional<Move> move = sut.getMoves(secret)
                .stream()
                .filter(amove -> amove.getType() == type)
                .findAny();
        assertTrue(move.isPresent());
        assertTrue(sut.fire(secret, move.get()).isEmpty());
    }

    /**
     * Sammelt die Geheimnisse aller Spieler ein.
     * @return Liste mit den Geheimnissen der Spieler.
     */
    private List<String> reapSecrets() {
        return game.getPlayers()
                .stream()
                .map(Player::getSecret)
                .collect(Collectors.toList());
    }


}