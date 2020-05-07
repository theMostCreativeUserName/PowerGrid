package edu.hm.severin.powergrid.datastore;


import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.severin.powergrid.BagTest;
import org.junit.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * test for plant and player classes.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @author Severin
 */

public class PlantPlayerTest {
    @Rule public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    private final String fqcn = "edu.hm.severin.powergrid.datastore.NeutralFactory";

    private final Factory factory = Factory.newFactory(fqcn);


    // -------------------------------- Tests ------------------------------------------------------------

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

    @Test (expected = IllegalArgumentException.class)
    public void newIllegalPlant4(){
        Plant plant = factory.newPlant(0, Plant.Type.Coal, 2,2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void newIllegalPlant5(){
        Plant plant = factory.newPlant(1, Plant.Type.Coal, 0,2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void newIllegalPlant6(){
        Plant plant = factory.newPlant(1, Plant.Type.Coal, 2,0);
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
    @Test public void plantGetResources1(){
        Plant plant = factory.newPlant(1, Plant.Type.Coal, 2,1);
        assertEquals("[[Coal]]", plant.getResources().toString());
    }
    @Test public void plantGetResources2(){
        Plant plant = factory.newPlant(1, Plant.Type.Oil, 2,1);
        assertEquals("[[Oil]]", plant.getResources().toString());
    }
    @Test public void plantGetResources3(){
        Plant plant = factory.newPlant(1, Plant.Type.Garbage, 2,1);
        assertEquals("[[Garbage]]", plant.getResources().toString());
    }
    @Test public void plantGetResources4(){
        Plant plant = factory.newPlant(1, Plant.Type.Uranium, 2,1);
        assertEquals("[[Uranium]]", plant.getResources().toString());
    }
    @Test public void plantGetResources5(){
        Plant plant = factory.newPlant(1, Plant.Type.Hybrid, 2,1);
        assertEquals("[[Coal, Oil]]", plant.getResources().toString());
    }
    @Test public void plantGetResources6(){
        Plant plant = factory.newPlant(1, Plant.Type.Eco, 2,1);
        assertEquals("[[]]", plant.getResources().toString());
    }

    @Test public void plantGetResources7(){
        Plant plant = factory.newPlant(1, Plant.Type.Fusion, 2,1);
        assertEquals("[[]]", plant.getResources().toString());
    }
    @Test public void newPlayer() {
        // arrange
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        // act
        // assert
        assertTrue(sut.hasSecret("hush - don't tell!"));
        assertFalse(sut.hasSecret("hush - 't tell!"));
    }
    @Test public void newPlayer2() {
        // arrange
        Player sut = factory.newPlayer("hush - don't tell!", "red");

        assertEquals("red", sut.getColor());
    }
    @Test public void playerEqual1(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");

        assertTrue(sut.equals(sut));
    }
    @Test public void playerEqual2(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        Player sat = factory.newPlayer("hush - don't tell!", "blue");

        assertFalse(sut.equals(sat));
        assertFalse(sat.equals(sut));
    }
    @Test public void playerEqual3(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        String sat = "orange";

        assertFalse(sut.equals(sat));
    }
    @Test public void playerEqual4(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        String sat = "orange";

        assertFalse(sut.equals(null));
    }
    @Test public void playerEqual6(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        String sat = "orange";
        Player m = factory.newPlayer("m", sat);
        Player n = factory.newPlayer("hush - don't tell!", "red");
        n.setElectro(30);

        assertFalse(sut.equals(sat));
        assertFalse(sut.equals(m));
    }
    @Test public void playerHash(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        Player sat = factory.newPlayer("hush - don't tell!", "blue");
        assertEquals(sat.hashCode(), sat.hashCode());
        assertEquals(sut.hashCode(), sut.hashCode());
        assertFalse(sat.hashCode() == sut.hashCode());
    }

    @Test public void playerGetSecret1() throws InterruptedException {
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
        String s = sut.getSecret();
        Thread.sleep(200);
        assertEquals(null, sut.getSecret());
    }
    @Test public void playerGetSecret3() throws InterruptedException {
        // arrange
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        String s = sut.getSecret();
        assertEquals(s, "hush - don't tell!");
        Thread.sleep(200);
        s = sut.getSecret();
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
    @Test public void playerSetElectro3(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        sut.setElectro(100);
        sut.setElectro(0);
        assertEquals(0, sut.getElectro());
    }
    @Test public void playerGetPlants(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        assertTrue(sut.getPlants().isEmpty());
    }
    @Test public void playerGetCities(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        assertTrue(sut.getCities().isEmpty());
    }
    @Test public void playerGetResources(){
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        assertTrue(sut.getResources().isEmpty());
    }
    @Test public void PlayerGetCities(){
        Player player = factory.newPlayer("hush - don't tell!", "red");
        City city = factory.newCity("Unicorncity", 1000);
        Set<City> sut = player.getCities();
        sut.add(city);
        Set<City> have = player.getCities();
        assertSame(sut, have);
    }

    @Test public void PlayerGetPlants(){
        Player player = factory.newPlayer("hush - don't tell!", "red");
        Plant plant= factory.newPlant(666, Plant.Type.Eco, 1, 333);
        Set<Plant> sut = player.getPlants();
        sut.add(plant);
        Set<Plant> have = player.getPlants();
        assertSame(sut, have);
    }


}