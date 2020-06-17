package edu.hm.severin.powergrid.datastore;


import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import org.junit.Assert;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.List;

/**
 *
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @author Pietsch
 * @version last modified 2020-04-07
 */
public class FactoryTest {
    @Rule public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    private final String fqcn = "edu.hm.severin.powergrid.datastore.NeutralFactory";

    private final OpenFactory factory = OpenFactory.newFactory(fqcn);

    @Test public void newSameCity() {
        // arrange
        OpenCity sut = factory.newCity("Duckburg", 1);
        // act
        final OpenCity have = factory.newCity("Duckburg", 2); // region ignored
        // assert
        Assert.assertSame("factory supplies one City object per name", sut, have);
    }

    @Test public void newOtherCity() {
        // arrange
        OpenCity sut = factory.newCity("Duckburg", 1);
        // act
        final OpenCity have = factory.newCity("Mouseton", 1);
        // assert
        Assert.assertNotSame("factory returns different City objects for different names", sut, have);
    }

    @Test public void newSamePlayer() {
        // arrange
        OpenPlayer sut = factory.newPlayer("geheim", "a");
        // act
        final OpenPlayer have = factory.newPlayer("geheim2", "a");
        // assert
        Assert.assertSame("factory supplies one Player object per color", sut, have);
    }

    @Test public void newOtherPlayer() {
        // arrange
        OpenPlayer sut = factory.newPlayer("geheim", "a");
        // act
        final OpenPlayer have = factory.newPlayer("geheim", "b");
        // assert
        Assert.assertNotSame("factory returns different Player objects for different colors", sut, have);
    }

    @Test public void newSamePlant() {
        // arrange
        OpenPlant sut = factory.newPlant(1, Plant.Type.Coal, 2, 2 );
        // act
        final OpenPlant have = factory.newPlant(1, Plant.Type.Eco, 5, 4 );
        // assert
        Assert.assertSame("factory supplies one Plant object per number", sut, have);
    }

    @Test public void newSameBoard() {
        // arrange
        Edition test = new EditionGermany();
        OpenBoard sut = factory.newBoard(test);
        // act
        final OpenBoard have = factory.newBoard(test);
        // assert
        Assert.assertSame("factory supplies one Board object per edition", sut, have);
    }

    @Test public void newOtherPlant() {
        // arrange
        OpenPlant sut = factory.newPlant(1, Plant.Type.Coal, 2, 2 );
        // act
        final OpenPlant have = factory.newPlant(2, Plant.Type.Coal, 2, 2 );
        // assert
        Assert.assertNotSame("factory returns different Plant objects for different numbers", sut, have);
    }

    @Test public void compareCities1() {
        // arrange
        OpenCity sut = factory.newCity("Mu", 1);
        OpenCity other = factory.newCity("Nu", 2);
        // act
        // assert
        assertTrue("Cities compared by name, sut before other", sut.compareTo(other) < 0);
    }

    @Test public void compareCities2() {
        // arrange
        OpenCity sut = factory.newCity("Nu", 1);
        OpenCity other = factory.newCity("Mu", 2);
        // act
        // assert
        assertTrue("Cities compared by name, sut after other", sut.compareTo(other) > 0);
    }

    @Test public void compareCities3() {
        // arrange
        OpenCity sut = factory.newCity("Nu", 1);
        OpenCity other = factory.newCity("Nu", 2);
        // act
        // assert
        assertTrue("Cities compared by name, sut and other are the same", sut.compareTo(other) == 0);
    }

    @Test public void comparePlants1() {
        // arrange
        OpenPlant sut = factory.newPlant(1, Plant.Type.Coal, 2, 2);
        OpenPlant other = factory.newPlant(2, Plant.Type.Coal, 2, 2);
        // act
        // assert
        assertTrue("Plants compared by there number in ascending order (sut before other)", sut.compareTo(other) < 0);
    }

    @Test public void comparePlants2() {
        // arrange
        OpenPlant sut = factory.newPlant(3, Plant.Type.Coal, 2, 2);
        OpenPlant other = factory.newPlant(2, Plant.Type.Coal, 2, 2);
        // act
        // assert
        assertTrue("Plants compared by there number in ascending order (other before sut)", sut.compareTo(other) > 0);
    }

    @Test public void comparePlants3() {
        // arrange
        OpenPlant sut = factory.newPlant(2, Plant.Type.Coal, 2, 2);
        OpenPlant other = factory.newPlant(2, Plant.Type.Coal, 2, 2);
        // act
        // assert
        assertTrue("Plants compared by there number in ascending order (sut same as other)", sut.compareTo(other) == 0);
    }

    @Test public void comparePlayers1() {
        // arrange
        OpenPlayer sut = factory.newPlayer("-", "a");
        OpenPlayer other = factory.newPlayer("-", "b");
        // act
        // assert
        assertTrue("players w/o cities&plants compare by color (sut first)", sut.compareTo(other) < 0);
    }

    @Test public void comparePlayers2() {
        // arrange
        OpenPlayer sut = factory.newPlayer("-", "b");
        OpenPlayer other = factory.newPlayer("-", "b");
        // act
        // assert
        assertTrue("players w/o cities&plants compare by color (sut and other same)", sut.compareTo(other) == 0);
    }

    @Test public void comparePlayers3() {
        // arrange
        OpenPlayer sut = factory.newPlayer("-", "c");
        OpenPlayer other = factory.newPlayer("-", "b");
        // act
        // assert
        assertTrue("players w/o cities&plants compare by color (other first)", sut.compareTo(other) > 0);
    }

    @Test public void comparePlayers4() {
        // arrange
        OpenPlayer sut = factory.newPlayer("-", "c");
        sut.getOpenCities().add(factory.newCity("ItsToEarlyForThat", 333));
        OpenPlayer other = factory.newPlayer("-", "b");
        // act
        // assert
        assertTrue(sut.compareTo(other) < 0);
    }

    @Test public void comparePlayers5() {
        // arrange
        OpenPlayer sut = factory.newPlayer("-", "c");
        sut.getOpenPlants().add(factory.newPlant(333, Plant.Type.Fusion, 666, 999));
        OpenPlayer other = factory.newPlayer("-", "b");
        // act
        // assert
        assertTrue(sut.compareTo(other) < 0);
    }

}