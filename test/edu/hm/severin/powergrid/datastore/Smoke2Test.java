package edu.hm.severin.powergrid.datastore;


import edu.hm.cs.rs.powergrid.datastore.Factory;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import org.junit.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-03-01
 */
public class Smoke2Test {
    @Rule public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    private final String fqcn = "edu.hm.severin.powergrid.datastore.NeutralFactory";

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

    @Test (expected = IllegalArgumentException.class)
    public void newIllegalPlant1(){
        Plant plant = factory.newPlant(-1, Plant.Type.Coal, 2,2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void newIllegalPlant2(){
        Plant plant = factory.newPlant(1, Plant.Type.Coal, -1,2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void newIllegalPlant3(){
        Plant plant = factory.newPlant(1, Plant.Type.Coal, 2,-1);
    }

    @Test public void plantHasOperated1(){
        Plant plant = factory.newPlant(1, Plant.Type.Coal, 2,1);
        assertFalse(plant.hasOperated());
    }
    @Test public void plantSetOperated2(){
        Plant plant = factory.newPlant(1, Plant.Type.Coal, 2,1);
        plant.setOperated(true);
        assertTrue(plant.hasOperated());
    }

    @Test public void plantSetOperated3(){
        Plant plant = factory.newPlant(1, Plant.Type.Coal, 2,1);
        plant.setOperated(true);
        plant.setOperated(false);
        assertFalse(plant.hasOperated());
    }

    @Test public void newPlayer() {
        // arrange
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        // act
        // assert
        assertTrue(sut.hasSecret("hush - don't tell!"));
    }
    @Test public void newPlayer2() {
        // arrange
        Player sut = factory.newPlayer("hush - don't tell!", "red");

        assertEquals("red", sut.getColor());
    }
    @Test public void playerGetSecret() throws InterruptedException {
        // arrange
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        Thread.sleep(200);
        // act
        // assert
        assertEquals("hush - don't tell!", sut.getSecret());
    }

    @Test public void playerGetSecret2() throws InterruptedException {
        // arrange
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        sut.getSecret();
        Thread.sleep(200);
        assertEquals(null, sut.getSecret());
    }

    @Test public void playerGetColor() {
        // arrange
        Player sut = factory.newPlayer("hush - don't tell!", "red");

        assertEquals("red", sut.getColor());
    }
    @Test public void playerGetPassed(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        assertFalse(sut.hasPassed());
    }
    @Test public void playerSetPassed1(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        sut.setPassed(true);
        assertTrue(sut.hasPassed());
    }
    @Test public void playerSetPassed2(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        sut.setPassed(true);
        sut.setPassed(false);
        assertFalse(sut.hasPassed());
    }

    @Test public void playerGetElectro(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        assertTrue(sut.getElectro() == 0);
    }
    @Test public void playerSetElectro1(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        sut.setElectro(100);
        assertEquals(100, sut.getElectro());
    }
    @Test public void playerSetElectro2(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        sut.setElectro(100);
        sut.setElectro(3);
        assertEquals(3, sut.getElectro());
    }
    @Test public void playerGetPlants(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        assertTrue(sut.getPlants().isEmpty());
    }
    @Test public void playerGetCities(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        assertTrue(sut.getCities().isEmpty());
    }
}