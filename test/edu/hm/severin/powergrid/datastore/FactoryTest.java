package edu.hm.severin.powergrid.datastore;


import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Set;

/**
 * Smoketest for factory.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @author Pietsch
 * @version last modified 2020-04-07
 */
public class FactoryTest {
    @Rule public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    private final String fqcn = "edu.hm.severin.powergrid.datastore.NeutralFactory";

    private final Factory factory = Factory.newFactory(fqcn);

    @Test public void newSameCity() {
        // arrange
        City sut = factory.newCity("Duckburg", 1);
        // act
        final City have = factory.newCity("Duckburg", 2); // region ignored
        // assert
        Assert.assertSame("factory supplies one City object per name", sut, have);
    }

    @Test public void newOtherCity() {
        // arrange
        City sut = factory.newCity("Duckburg", 1);
        // act
        final City have = factory.newCity("Mouseton", 1);
        // assert
        Assert.assertNotSame("factory returns different City objects for different names", sut, have);
    }

    @Test public void newSamePlayer() {
        // arrange
        Player sut = factory.newPlayer("geheim", "a");
        // act
        final Player have = factory.newPlayer("geheim2", "a");
        // assert
        Assert.assertSame("factory supplies one Player object per color", sut, have);
    }

    @Test public void newOtherPlayer() {
        // arrange
        Player sut = factory.newPlayer("geheim", "a");
        // act
        final Player have = factory.newPlayer("geheim", "b");
        // assert
        Assert.assertNotSame("factory returns different Player objects for different colors", sut, have);
    }

    @Test public void newSamePlant() {
        // arrange
        Plant sut = factory.newPlant(1, Plant.Type.Coal, 2, 2 );
        // act
        final Plant have = factory.newPlant(1, Plant.Type.Eco, 5, 4 );
        // assert
        Assert.assertSame("factory supplies one Plant object per number", sut, have);
    }

    @Test public void newSameBoard() {
        // arrange
        Edition test = new EditionGermany();
        Board sut = factory.newBoard(test);
        // act
        final Board have = factory.newBoard(test);
        // assert
        Assert.assertSame("factory supplies one Board object per edition", sut, have);
    }

    @Test public void newOtherPlant() {
        // arrange
        Plant sut = factory.newPlant(1, Plant.Type.Coal, 2, 2 );
        // act
        final Plant have = factory.newPlant(2, Plant.Type.Coal, 2, 2 );
        // assert
        Assert.assertNotSame("factory returns different Plant objects for different numbers", sut, have);
    }

    @Test public void compareCities1() {
        // arrange
        City sut = factory.newCity("Mu", 1);
        City other = factory.newCity("Nu", 2);
        // act
        // assert
        assertTrue("Cities compared by name, sut before other", sut.compareTo(other) < 0);
    }

    @Test public void compareCities2() {
        // arrange
        City sut = factory.newCity("Nu", 1);
        City other = factory.newCity("Mu", 2);
        // act
        // assert
        assertTrue("Cities compared by name, sut after other", sut.compareTo(other) > 0);
    }

    @Test public void compareCities3() {
        // arrange
        City sut = factory.newCity("Nu", 1);
        City other = factory.newCity("Nu", 2);
        // act
        // assert
        assertTrue("Cities compared by name, sut and other are the same", sut.compareTo(other) == 0);
    }

    @Test public void comparePlants1() {
        // arrange
        Plant sut = factory.newPlant(1, Plant.Type.Coal, 2, 2);
        Plant other = factory.newPlant(2, Plant.Type.Coal, 2, 2);
        // act
        // assert
        assertTrue("Plants compared by there number in ascending order (sut before other)", sut.compareTo(other) < 0);
    }

    @Test public void comparePlants2() {
        // arrange
        Plant sut = factory.newPlant(3, Plant.Type.Coal, 2, 2);
        Plant other = factory.newPlant(2, Plant.Type.Coal, 2, 2);
        // act
        // assert
        assertTrue("Plants compared by there number in ascending order (other before sut)", sut.compareTo(other) > 0);
    }

    @Test public void comparePlants3() {
        // arrange
        Plant sut = factory.newPlant(2, Plant.Type.Coal, 2, 2);
        Plant other = factory.newPlant(2, Plant.Type.Coal, 2, 2);
        // act
        // assert
        assertTrue("Plants compared by there number in ascending order (sut same as other)", sut.compareTo(other) == 0);
    }

    @Test public void comparePlayers1() {
        // arrange
        Player sut = factory.newPlayer("-", "a");
        Player other = factory.newPlayer("-", "b");
        // act
        // assert
        assertTrue("players w/o cities&plants compare by color (sut first)", sut.compareTo(other) < 0);
    }

    @Test public void comparePlayers2() {
        // arrange
        Player sut = factory.newPlayer("-", "b");
        Player other = factory.newPlayer("-", "b");
        // act
        // assert
        assertTrue("players w/o cities&plants compare by color (sut and other same)", sut.compareTo(other) == 0);
    }

    @Test public void comparePlayers3() {
        // arrange
        Player sut = factory.newPlayer("-", "c");
        Player other = factory.newPlayer("-", "b");
        // act
        // assert
        assertTrue("players w/o cities&plants compare by color (other first)", sut.compareTo(other) > 0);
    }
    @Test public void comparePlayersCities1() {
        // arrange
        Player sut = factory.newPlayer("-", "c");
        Set<City> cities = sut.getCities();
        cities.add(factory.newCity("Unicorncity", 1000));
        Player other = factory.newPlayer("-", "b");
        // act
        // assert
        assertTrue("players with Cities, sut first", sut.compareTo(other) < 0);
    }

    @Test public void comparePlayersCities2() {
        // arrange
        Player sut = factory.newPlayer("-", "c");
        Player other = factory.newPlayer("-", "b");
        Set<City> cities = other.getCities();
        cities.add(factory.newCity("Unicorncity", 1000));
        // act
        // assert
        assertTrue("players with Cities, sut first", sut.compareTo(other) > 0);
    }

    @Test public void comparePlayersPlant1() {
        // arrange
        Player sut = factory.newPlayer("-", "c");
        Set<Plant> plants = sut.getPlants();
        plants.add(factory.newPlant(1000, Plant.Type.Eco, 300, 60000));
        Player other = factory.newPlayer("-", "b");

        // act
        // assert
        assertTrue("players with Cities, sut first", sut.compareTo(other) < 0);
    }

    @Test public void comparePlayersPlant2() {
        // arrange
        Player sut = factory.newPlayer("-", "c");
        Player other = factory.newPlayer("-", "b");
        Set<Plant> plants = other.getPlants();
        plants.add(factory.newPlant(1000, Plant.Type.Eco, 300, 60000));

        // act
        // assert
        assertTrue("players with Cities, sut first", sut.compareTo(other) > 0);
    }

}