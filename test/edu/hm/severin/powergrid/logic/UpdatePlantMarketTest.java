package edu.hm.severin.powergrid.logic;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class UpdatePlantMarketTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    /** Spielstand. */
    private final Game game;

    /** Spielregeln. */
    private Rules sut;

    /** Fluchtwert fuer kein Geheimnis. */
    private final String NO_SECRET = "";

    /** Fuehrt ein Runnable mehrmals aus. */
    private static BiConsumer<Integer, Runnable> times = (n, runnable) -> IntStream.range(0, n).forEach(__ -> runnable.run());

    /** Initialisiert Factory und Spielregeln. */
    public UpdatePlantMarketTest() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        System.setProperty("powergrid.randomsource", "edu.hm.cs.rs.powergrid.logic.RandomSource");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        openGame.setPhase(Phase.PlantBuying);
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    @Test public void testUpdatePlantMarket() {

        final Move move = sut.getMoves(Optional.empty()).iterator().next();
        Optional<Problem> problem = sut.fire(Optional.empty(), move);
        sut.fire(Optional.empty(), move);
        sut.fire(Optional.empty(), move);
        sut.fire(Optional.empty(), move);
        sut.fire(Optional.empty(), move);
        sut.fire(Optional.empty(), move);
        problem = sut.fire(Optional.empty(), move);

        assertSame(game.getPlayers().size(), 6);
        assertEquals(Problem.MaxPlayers, problem.get());

    }
}
