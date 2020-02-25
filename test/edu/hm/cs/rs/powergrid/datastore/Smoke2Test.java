package edu.hm.cs.rs.powergrid.datastore;

import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;


/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-02-19
 */
public class Smoke2Test {
    @Rule public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    private final String fqcn = "example.powergrid.datastore.TheObjectMill";

    private final Factory factory = Factory.newFactory(fqcn);

    @Test public void newPlant() {
        // arrange
        Plant plant = factory.newPlant(3, Plant.Type.Oil, 2, 1);
        // act
        // assert
        Assert.assertEquals(3, plant.getNumber());
        Assert.assertEquals(Plant.Type.Oil, plant.getType());
        Assert.assertEquals(2, plant.getNumberOfResources());
        Assert.assertEquals(1, plant.getCities());
    }

    @Test public void newPlayer() {
        // arrange
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        // act
        // assert
        assertTrue(sut.hasSecret("hush - don't tell!"));
    }

}
