package edu.hm.cs.sol.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenBoard;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlantMarket;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

/**
 * Erste Tests fuer Datastore, getrennt in zwei Sichten readonly und readwrite.
 *
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-30
 */
public class Smoke6Test {
    @Rule
    public final Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    /**
     * Factory.
     */
    private final OpenFactory factory;

    /**
     * Initialisiert die Factory.
     */
    public Smoke6Test() throws IOException {
        // TODO: Ersetzen Sie den Wert durch den FQCN Ihrer Factory-Implementierung
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        factory = OpenFactory.newFactory();
    }

    /**
     * Eine Ausgabe.
     */
    private Edition edition = new EditionGermany();

    @Test
    public void testFactoryInitialized() {
        assertNotNull("factory exists", factory);
    }

    @Test
    public void testOpenCityIsModifiable() {
        OpenCity sut = factory.newCity("Duckburg", 1);
        OpenCity other = factory.newCity("Mouseton", 1);
        sut.getOpenConnections().put(other, 1);
        assertEquals("factory creates mutable cities", 1, (long) sut.getConnections().get(other));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCityIsReadonly() {
        City sut = factory.newCity("Duckburg", 1);
        City other = factory.newCity("Mouseton", 1);
        sut.getConnections().put(other, 1);
    }

    @Test
    public void testOpenBoardIsModifiable() {
        OpenBoard board = factory.newBoard(edition);
        assertEquals("board holds specified cities",
                edition.getCitySpecifications().size(),
                board.getCities().size());
        board.getOpenCities().add(factory.newCity("Duckburg", 1));
        assertTrue("factory creates mutable board", board.findCity("Duckburg") != null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClosedOpenBoardIsReadonly() {
        OpenBoard board = factory.newBoard(edition);
        board.close();
        board.getCities().add(factory.newCity("Duckburg", 1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBoardIsReadonly() {
        Board board = factory.newBoard(edition);
        board.getCities().add(factory.newCity("Duckburg", 1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBoardIsDeeplyReadonly() {
        Board board = factory.newBoard(edition);
        City other = factory.newCity("Mouseton", 1);
        City sut = board.getCities().iterator().next();
        sut.getConnections().put(other, 1);
    }

    @Test
    public void testMutability() {
        PlantMarket market;
        OpenPlantMarket openMarket;

        Game game = factory.newGame(edition);
        market = game.getPlantMarket();
        // openMarket = game.getPlantMarket(); // Compilerfehler

        OpenGame openGame = factory.newGame(edition);
        market = openGame.getPlantMarket();
        openMarket = openGame.getPlantMarket();
    }

    @Test
    public void testCollectionMutability() {
        List<? extends Player> players;
        List<OpenPlayer> openPlayers;

        Game game = factory.newGame(edition);
        players = game.getPlayers();
        // players.add(null);                      // Laufzeitfehler
        // openPlayers = game.getPlayers();        // Compilerfehler

        OpenGame openGame = factory.newGame(edition);
        players = openGame.getOpenPlayers();
        players.add(null);                          // ok
        openPlayers = openGame.getOpenPlayers();    // ok
        openPlayers.add(null);                      // ok
    }

}

