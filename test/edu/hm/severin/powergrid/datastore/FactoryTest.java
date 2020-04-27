package edu.hm.severin.powergrid.datastore;


import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Factory;
import edu.hm.cs.rs.powergrid.datastore.Player;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Smoketest for factory.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @author Pietsch
 * @version last modified 2020-04-07
 */
public class FactoryTest {
    @Rule public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    private final String fqcn = "edu.hm.yourname.powergrid.datastore.SillyFactory";

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

    @Test public void comparePlayers() {
        // arrange
        Player sut = factory.newPlayer("-", "a");
        Player other = factory.newPlayer("-", "b");
        // act
        // assert
        //assertTrue("players w/o cities&plants compare by color", sut.compareTo(other) < 0);
    }
}