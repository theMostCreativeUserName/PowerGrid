package edu.hm.severin.powergrid.datastore;


import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.severin.powergrid.ListBag;
import org.junit.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * test for plant and player classes.
 *
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @author Severin
 */

public class PlantPlayerTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    private final String fqcn = "edu.hm.severin.powergrid.datastore.NeutralFactory";

    private final OpenFactory factory = OpenFactory.newFactory(fqcn);


    // -------------------------------- Tests ------------------------------------------------------------

    @Test
    public void newPlant() {
        // arrange
        OpenPlant plant = factory.newPlant(3, Plant.Type.Oil, 2, 1);
        // act
        // assert
        Assert.assertEquals(3, plant.getNumber());
        Assert.assertEquals(Plant.Type.Oil, plant.getType());
        Assert.assertEquals(2, plant.getNumberOfResources());
        Assert.assertEquals(1, plant.getCities());
    }

    @Test(expected = IllegalArgumentException.class)
    public void newIllegalPlant1() {
        OpenPlant plant = factory.newPlant(-1, Plant.Type.Coal, 2, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newIllegalPlant2() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Coal, -1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newIllegalPlant3() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Coal, 2, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newIllegalPlant6() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Coal, 2, 0);
    }

    @Test
    public void newPlant2(){
        OpenPlant plant = factory.newPlant(0, Plant.Type.Coal, 2,2);
        int have = plant.getNumber();
        int sut = 0;
        assertSame(have, sut);
    }

    @Test
    public void newPlant3(){
        OpenPlant plant = factory.newPlant(2, Plant.Type.Coal, 0,2);
        int have = plant.getNumberOfResources();
        int sut = 0;
        assertSame(have, sut);
    }

    @Test
    public void newPlant4(){
        OpenPlant plant = factory.newPlant(2, Plant.Type.Coal, 2,1);
        int have = plant.getCities();
        int sut = 1;
        assertSame(have, sut);
    }


    @Test
    public void plantHasOperated1() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Coal, 2, 1);
        assertFalse(plant.hasOperated());
    }

    @Test
    public void plantSetOperated2() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Coal, 2, 1);
        plant.setOperated(true);
        assertTrue(plant.hasOperated());
    }

    @Test
    public void plantSetOperated3() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Coal, 2, 1);
        plant.setOperated(true);
        plant.setOperated(false);
        assertFalse(plant.hasOperated());
    }

    @Test
    public void newPlayer() {
        // arrange
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        // act
        // assert
        assertTrue(sut.hasSecret("hush - don't tell!"));
        assertFalse(sut.hasSecret("hush - 't tell!"));
    }

    @Test
    public void newPlayer2() {
        // arrange
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");

        assertEquals("red", sut.getColor());
    }

    @Test
    public void playerEqual1() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");

        assertTrue(sut.equals(sut));
    }

    @Test
    public void playerEqual2() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        OpenPlayer sat = factory.newPlayer("hush - don't tell!", "blue");

        assertFalse(sut.equals(sat));
        assertFalse(sat.equals(sut));
    }

    @Test
    public void playerEqual3() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        String sat = "orange";

        assertFalse(sut.equals(sat));
    }

    @Test
    public void playerEqual4() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        String sat = "orange";

        assertFalse(sut.equals(null));
    }

    @Test
    public void playerEqual6() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        String sat = "orange";
        OpenPlayer m = factory.newPlayer("m", sat);
        OpenPlayer n = factory.newPlayer("hush - don't tell!", "red");
        n.setElectro(30);

        assertFalse(sut.equals(sat));
        assertFalse(sut.equals(m));
    }

    @Test
    public void playerHash() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        OpenPlayer sat = factory.newPlayer("hush - don't tell!", "blue");
        assertEquals(sat.hashCode(), sat.hashCode());
        assertEquals(sut.hashCode(), sut.hashCode());
        assertFalse(sat.hashCode() == sut.hashCode());
    }

    @Test
    public void playerGetSecret1() throws InterruptedException {
        // arrange
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        Thread.sleep(200);
        // act
        // assert
        assertEquals("hush - don't tell!", sut.getSecret());
    }

    @Test
    public void playerGetSecret2() throws InterruptedException {
        // arrange
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        String s = sut.getSecret();
        Thread.sleep(200);
        assertEquals(null, sut.getSecret());
    }

    @Test
    public void playerGetSecret3() throws InterruptedException {
        // arrange
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        String s = sut.getSecret();
        assertEquals(s, "hush - don't tell!");
        Thread.sleep(200);
        s = sut.getSecret();
        Thread.sleep(200);
        assertEquals(null, sut.getSecret());
    }

    @Test
    public void playerGetColor() {
        // arrange
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");

        assertEquals("red", sut.getColor());
    }

    @Test
    public void playerGetPassed() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        assertFalse(sut.hasPassed());
    }

    @Test
    public void playerSetPassed1() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        sut.setPassed(true);
        assertTrue(sut.hasPassed());
    }

    @Test
    public void playerSetPassed2() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        sut.setPassed(true);
        sut.setPassed(false);
        assertFalse(sut.hasPassed());
    }

    @Test
    public void playerGetElectro() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        assertTrue(sut.getElectro() == 0);
    }

    @Test
    public void playerSetElectro1() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        sut.setElectro(100);
        assertEquals(100, sut.getElectro());
    }

    @Test
    public void playerSetElectro2() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        sut.setElectro(100);
        sut.setElectro(3);
        assertEquals(3, sut.getElectro());
    }

    @Test(expected = IllegalArgumentException.class)
    public void playerSetElectro3() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        sut.setElectro(100);
        sut.setElectro(-1);
    }

    @Test
    public void playerGetPlants() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        assertTrue(sut.getPlants().isEmpty());
    }

    @Test
    public void playerGetCities() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        assertTrue(sut.getCities().isEmpty());
    }

    @Test
    public void PlayerGetCities() {
        OpenPlayer player = factory.newPlayer("hush - don't tell!", "red");
        OpenCity city = factory.newCity("Unicorncity", 1000);
        Set<OpenCity> sut = player.getOpenCities();
        sut.add(city);
        Set<OpenCity> have = player.getOpenCities();
        assertSame(sut, have);
    }

    @Test
    public void PlayerGetPlants() {
        OpenPlayer player = factory.newPlayer("hush - don't tell!", "red");
        OpenPlant plant = factory.newPlant(666, Plant.Type.Eco, 1, 333);
        Set<OpenPlant> sut = player.getOpenPlants();
        sut.add(plant);
        Set<OpenPlant> have = player.getOpenPlants();
        assertSame(sut, have);
    }


    @Test (expected = IllegalArgumentException.class)
    public void plantIllegal(){
        OpenPlant sut = factory.newPlant(2, Plant.Type.Coal, 3,0);
    }

    @Test (expected = NullPointerException.class)
    public void playerIllegal1(){
        OpenPlayer sut = factory.newPlayer(null, "fghjklö");
    }
    @Test (expected = NullPointerException.class)
    public void playerIllegal2(){
        OpenPlayer sut = factory.newPlayer("null", null);
    }
    @Test
    public void playerHasSecret(){
        OpenPlayer sut = factory.newPlayer("null", "fghjklö");
        assertFalse(sut.hasSecret(null));
    }
    @Test
    public void playerSecret(){
        OpenPlayer sut = factory.newPlayer("", "fghjklö");
        assertFalse(sut.hasSecret(null));

    }
    @Test
    public void playerGetResources() {
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        assertTrue(sut.getResources().isEmpty());
    }

    @Test
    public void plantGetResources1() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Coal, 2, 1);
        assertEquals("[ListBag{elements=[Coal, Coal], readOnly=true}]", plant.getResources().toString());
    }

    @Test
    public void plantGetResources2() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Oil, 2, 1);
        assertEquals("[ListBag{elements=[Oil, Oil], readOnly=true}]", plant.getResources().toString());
    }

    @Test
    public void plantGetResources3() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Garbage, 2, 1);
        assertEquals("[ListBag{elements=[Garbage, Garbage], readOnly=true}]", plant.getResources().toString());
    }

    @Test
    public void plantGetResources4() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Uranium, 2, 1);
        assertEquals("[ListBag{elements=[Uranium, Uranium], readOnly=true}]", plant.getResources().toString());
    }

    @Test
    public void plantGetResources5() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Hybrid, 2, 1);
        Set<Bag<Resource>> have = new HashSet<>();
        have.add(new ListBag<Resource>(Resource.Coal, Resource.Coal));
        have.add(new ListBag<Resource>(Resource.Oil, Resource.Oil));
        have.add(new ListBag<Resource>(Resource.Coal, Resource.Oil));
        assertEquals(have, plant.getResources());
    }

    @Test
    public void plantGetResources6() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Eco, 2, 1);
        assertEquals("[ListBag{elements=[], readOnly=true}]", plant.getResources().toString());
    }

    @Test
    public void plantGetResources7() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Fusion, 2, 1);
        assertEquals("[ListBag{elements=[], readOnly=true}]", plant.getResources().toString());
    }

    // Neue Tests
    @Test (expected = IllegalArgumentException.class)
    public void playerSetElectroMinusOne() {
        OpenPlayer player = factory.newPlayer("Ohno", "rainbow");
        player.setElectro(-1);
    }

    @Test
    public void playerSetElectroZero() {
        OpenPlayer player = factory.newPlayer("Ohno", "rainbow");
        player.setElectro(0);
        assertSame(player.getElectro(), 0);
    }

    // x.14
    @Test
    public void plantGetResources8() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Hybrid, 1, 1);
        Set<Bag<Resource>> have = new HashSet<>();
        have.add(new ListBag<Resource>(Resource.Coal));
        have.add(new ListBag<Resource>(Resource.Oil));
        assertEquals(have, plant.getResources());
    }

    @Test
    public void plantGetResources9() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Hybrid, 2, 1);
        Set<Bag<Resource>> have = new HashSet<>();
        have.add(new ListBag<Resource>(Resource.Coal, Resource.Coal));
        have.add(new ListBag<Resource>(Resource.Oil, Resource.Oil));
        have.add(new ListBag<Resource>(Resource.Coal, Resource.Oil));
        assertEquals(have, plant.getResources());
    }

    @Test
    public void plantGetResources10() {
        OpenPlant plant = factory.newPlant(1, Plant.Type.Hybrid, 0, 1);
        assertEquals("[ListBag{elements=[], readOnly=false}]", plant.getResources().toString());
    }

    //x.17
    @Test (expected = UnsupportedOperationException.class)
    public void PlantGetResources(){
        OpenPlant plant = factory.newPlant(12, Plant.Type.Oil, 3, 3);
        Bag<Resource> bag = new ListBag<>();
        bag.add(Resource.Oil, 4);
        plant.getResources().add(bag);
    }

    //x.18
    @Test (expected = IllegalArgumentException.class)
    public void plantTypeNUll(){
        OpenPlant plant = factory.newPlant(1, null, 2, 3);
    }

    @Test
    public void playerSecretNull(){
        OpenPlayer player = factory.newPlayer("lol", "rot");
        assertFalse(player.hasSecret(null));
    }

}