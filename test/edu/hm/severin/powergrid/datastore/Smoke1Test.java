package edu.hm.severin.powergrid.datastore;


import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Factory;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Map;


/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @author Severin
 * @version 2020-02-19
 */
public class Smoke1Test {
   // @Rule public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    private final String fqcn = "edu.hm.severin.powergrid.datastore.NeutralFactory";

    private final Factory factory = Factory.newFactory(fqcn);

    @Test public void newCity() {
        // arrange
        City sut = factory.newCity("Entenhausen", 1);
        // act
        // assert
        Assert.assertEquals("Entenhausen", sut.getName());
        Assert.assertEquals(1, sut.getRegion());
    }
    @Test (expected =  IllegalArgumentException.class)
    public void newIllegalCity1() {
        // arrange
        City sut = factory.newCity("", 1);
    }
    @Test (expected =  NullPointerException.class)
    public void newIllegalCity2() {
        // arrange
        City sut = factory.newCity(null, 1);
    }
    @Test (expected =  IllegalArgumentException.class)
    public void newIllegalCity3() {
        // arrange
        City sut = factory.newCity("", 0);
    }
    @Test (expected =  IllegalArgumentException.class)
    public void newIllegalCity4() {
        // arrange
        City sut = factory.newCity("AnotherCity", -1);
    }
    @Test public void getCityName() {
        // arrange
        City sut = factory.newCity("Entenhausen", 1);
        Assert.assertEquals(sut.getName(), "Entenhausen");
    }

    @Test public void getCityArea() {
        // arrange
        City sut = factory.newCity("Entenhausen", 1);
        Assert.assertEquals(sut.getRegion(), 1);
    }
    @Test public void connectCity1(){
        City sut = factory.newCity("city", 1);
        City sat = factory.newCity("city2", 2);
        sut.connect(sat, 30);
        Assert.assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalArgumentException.class)
    public void connectCity2(){
        City sut = factory.newCity("city", 1);
        City sat = factory.newCity("city2", 2);
        sut.connect(sat, -1);
        Assert.assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalArgumentException.class)
    public void connectCity3(){
        City sut = factory.newCity("city", 1);
        sut.connect(sut, 30);
        Assert.assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalArgumentException.class)
    public void connectCity4(){
        City sut = factory.newCity("city", 1);
        City sat = factory.newCity("city2", 2);
        sut.connect(sat, 30);
        sut.connect(sat, 30);
        Assert.assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalArgumentException.class)
    public void connectCity5(){
        City sut = factory.newCity("city", 1);
        City sat = factory.newCity("city2", 2);
        sut.connect(sat, 30);
        sut.connect(sat, 50);
        Assert.assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalStateException.class)
    public void connectCity6(){
        City sut = factory.newCity("city", 1);
        City sat = factory.newCity("city2", 2);
        sut.close();
        sut.connect(sat, 30);
        Assert.assertEquals("{city2 2=30}",sut.getConnections().toString());
    }

    @Test public void newBoard() {
        // arrange
        Board sut = factory.newBoard(new EditionGermany());
        // act
        sut.close();
        // assert
        assertFalse(sut.getCities().isEmpty());
    }
    @Test (expected = NullPointerException.class)
    public void newIllegalBoard(){
        Board sut = factory.newBoard(null);
    }
    @Test public void newBoardFindCity1() {
        // arrange
        Board sut = factory.newBoard(new EditionGermany());
        // act
        sut.close();
        // assert
        Assert.assertEquals(factory.newCity("Würzburg",4).toString(), sut.findCity("Würzburg").toString());
    }
    @Test public void newBoardFindCity2() {
        // arrange
        Board sut = factory.newBoard(new EditionGermany());
        // act
        sut.close();
        // assert
        Assert.assertEquals(null, sut.findCity("Japan"));
    }
    @Test public void closeRegionsBoard1(){
        Board sut = factory.newBoard(new EditionGermany());
        sut.closeRegions(2);
        Assert.assertEquals(null, sut.findCity("München"));
    }
    @Test (expected = NullPointerException.class)
    public void closeRegionsBoard2(){
        Board sut = factory.newBoard(new EditionGermany());
        sut.closeRegions(2);
        Assert.assertEquals(null, sut.findCity("München").getConnections());
    }
    @Test (expected = IllegalStateException.class)
    public void closeRegionsBoard4(){
        Board sut = factory.newBoard(new EditionGermany());
        sut.close();
        sut.closeRegions(2);
    }
    @Test public void closeRegionsBoard3(){
        Board sut = factory.newBoard(new EditionGermany());
        sut.closeRegions(2);
        Assert.assertEquals(14,sut.getCities().size() );
    }
    @Test public void closeRegionsBoard5(){
        Board sut = factory.newBoard(new EditionGermany());
        sut.closeRegions(2);
        City city = sut.findCity("Berlin");
        System.out.println(city.getName()+city.getConnections());
        Assert.assertTrue(sut.findCity("Berlin").getConnections().size() == 4);
    }
    @Test public void getCitiesOfBoard1(){
        Board sut = factory.newBoard(new EditionGermany());
        Assert.assertEquals(42,sut.getCities().size());
    }
    @Test public void getCitiesOfBoard2(){
        Board sut = factory.newBoard(new EditionGermany());
        Assert.assertEquals(factory.newCity("Würzburg",4).toString(), sut.findCity("Würzburg").toString());
    }
    @Test public void getCitiesOfBoard3(){
        Board sut = factory.newBoard(new EditionGermany());
        Assert.assertFalse( sut.findCity("Würzburg").getConnections().isEmpty());
    }

}
