package edu.hm.severin.powergrid.datastore;


import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;


/**
 * test for city and board classes.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @author Severin
 */
public class CityBoardTest{
  @Rule public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    private final String fqcn = "edu.hm.severin.powergrid.datastore.NeutralFactory"; // package path

    private final OpenFactory factory = OpenFactory.newFactory(fqcn);

    private OpenCity getCity(String name, int region){  // specific City
        return factory.newCity(name, region);
    }
    private OpenBoard getBoardGermany(){  // specific Board
        return factory.newBoard(new EditionGermany());
    }

// ------------------------------------------- Tests -----------------------------------------------------
    @Test public void newCity() {
        // arrange
        City sut = getCity("Entenhausen", 1);
        // act
        // assert
        Assert.assertEquals("Entenhausen", sut.getName());
        Assert.assertEquals(1, sut.getRegion());
    }
    @Test (expected =  IllegalArgumentException.class)
    public void newIllegalCity1() {
        // arrange
        City sut = getCity("", 1);
    }
    @Test (expected =  NullPointerException.class)
    public void newIllegalCity2() {
        // arrange
        City sut = getCity(null, 1);
    }
    @Test (expected =  IllegalArgumentException.class)
    public void newIllegalCity3() {
        // arrange
        City sut = getCity("", 0);
    }
    @Test (expected =  IllegalArgumentException.class)
    public void newIllegalCity4() {
        // arrange
        City sut = getCity("AnotherCity", -1);
    }
    @Test public void getCityName() {
        // arrange
        City sut = getCity("Entenhausen", 1);
        Assert.assertEquals(sut.getName(), "Entenhausen");
    }

    @Test public void getCityArea() {
        // arrange
        City sut = getCity("Entenhausen", 1);
        Assert.assertEquals(sut.getRegion(), 1);
    }
    @Test public void connectCity1(){
        OpenCity sut = getCity("city", 1);
        OpenCity sat = getCity("city2", 2);
        sut.connect(sat, 30);
        Assert.assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalArgumentException.class)
    public void connectCity2(){
        OpenCity sut = getCity("city", 1);
        OpenCity sat = getCity("city2", 2);
        sut.connect(sat, -1);
        Assert.assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalArgumentException.class)
    public void connectCity3(){
        OpenCity sut = getCity("city", 1);
        sut.connect(sut, 30);
        Assert.assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalArgumentException.class)
    public void connectCity4(){
        OpenCity sut = getCity("city", 1);
        OpenCity sat = getCity("city2", 2);
        sut.connect(sat, 30);
        sut.connect(sat, 30);
        Assert.assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalArgumentException.class)
    public void connectCity5(){
        OpenCity sut = getCity("city", 1);
        OpenCity sat = getCity("city2", 2);
        sut.connect(sat, 30);
        sut.connect(sat, 50);
        Assert.assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalStateException.class)
    public void connectCity6(){
        OpenCity sut = getCity("city", 1);
        OpenCity sat = getCity("city2", 2);
        sut.close();
        sut.connect(sat, 30);
        Assert.assertEquals("{city2 2=30}",sut.getConnections().toString());
    }

    @Test (expected = IllegalStateException.class)
    public void closeBoard(){
        OpenBoard sut = getBoardGermany();
        sut.close();
        sut.close();
    }
    @Test (expected = IllegalStateException.class)
    public void closeBoard2(){
        OpenBoard sut = getBoardGermany();
        sut.close();
        OpenCity m = sut.findCity("Bremen");
        m.close();
        m.connect(sut.findCity("München"), 666);
        Assert.assertEquals(50, m.getConnections().size());
    }
    @Test (expected = IllegalStateException.class)
    public void closeBoard3(){
        OpenBoard sut = getBoardGermany();
        sut.close();
        OpenCity m = sut.findCity("Bremen");
        m.connect(sut.findCity("München"), 666);
        Assert.assertEquals(m.getConnections().size(), 60);
    }

    @Test public void newBoard() {
        // arrange
        OpenBoard sut = getBoardGermany();
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
        OpenBoard sut = getBoardGermany();
        // act
        sut.close();
        // assert
        Assert.assertEquals(getCity("Würzburg",4).toString(), sut.findCity("Würzburg").toString());
    }
    @Test public void newBoardFindCity2() {
        // arrange
        OpenBoard sut = getBoardGermany();
        // act
        sut.close();
        // assert
        Assert.assertEquals(null, sut.findCity("Japan"));
    }
    @Test public void closeRegionsBoard1(){
        OpenBoard sut = getBoardGermany();
        sut.closeRegions(2);
        Assert.assertEquals(null, sut.findCity("München"));
    }
    @Test (expected = NullPointerException.class)
    public void closeRegionsBoard2(){
        OpenBoard sut = getBoardGermany();
        sut.closeRegions(2);
        Assert.assertEquals(null, sut.findCity("München").getConnections());
    }
    @Test (expected = IllegalStateException.class)
    public void closeRegionsBoard4(){
        OpenBoard sut = getBoardGermany();
        sut.close();
        sut.closeRegions(2);
    }
    @Test public void closeRegionsBoard3(){
        OpenBoard sut = getBoardGermany();
        sut.closeRegions(2);
        Assert.assertEquals(14,sut.getCities().size() );
    }
    @Test public void closeRegionsBoard5(){
        OpenBoard sut = getBoardGermany();
        sut.closeRegions(2);
        City city = sut.findCity("Berlin");
        System.out.println(city.getName()+city.getConnections());
        Assert.assertTrue(sut.findCity("Berlin").getConnections().size() == 4);
    }
    @Test public void getCitiesOfBoard1(){
        Board sut = getBoardGermany();
        Assert.assertEquals(42,sut.getCities().size());
    }
    @Test public void getCitiesOfBoard2(){
        Board sut = getBoardGermany();
        Assert.assertEquals(factory.newCity("Würzburg",4).toString(), sut.findCity("Würzburg").toString());
    }
    @Test public void getCitiesOfBoard3(){
        Board sut = getBoardGermany();
        System.out.println(sut.findCity("Würzburg").getConnections().isEmpty());
        Assert.assertFalse( sut.findCity("Würzburg").getConnections().isEmpty());
    }


}
