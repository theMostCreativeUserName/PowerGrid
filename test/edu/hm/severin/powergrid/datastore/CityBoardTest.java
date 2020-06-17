package edu.hm.severin.powergrid.datastore;


import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenBoard;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import org.junit.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Set;

import static org.junit.Assert.*;


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
        OpenCity sut = getCity("Entenhausen", 1);
        // act
        // assert
        assertEquals("Entenhausen", sut.getName());
        assertEquals(1, sut.getRegion());
    }
    @Test (expected =  IllegalArgumentException.class)
    public void newIllegalCity1() {
        // arrange
        OpenCity sut = getCity("", 1);
    }
    @Test (expected =  NullPointerException.class)
    public void newIllegalCity2() {
        // arrange
        OpenCity sut = getCity(null, 1);
    }
    @Test (expected =  IllegalArgumentException.class)
    public void newIllegalCity3() {
        // arrange
        OpenCity sut = getCity("", 0);
    }
    @Test (expected =  IllegalArgumentException.class)
    public void newIllegalCity4() {
        // arrange
        OpenCity sut = getCity("AnotherCity", -1);
    }
    @Test public void getCityName() {
        // arrange
        OpenCity sut = getCity("Entenhausen", 1);
        assertEquals(sut.getName(), "Entenhausen");
    }

    @Test public void getCityArea() {
        // arrange
        OpenCity sut = getCity("Entenhausen", 1);
        assertEquals(sut.getRegion(), 1);
    }
    @Test public void connectCity1(){
        OpenCity sut = getCity("city", 1);
        OpenCity sat = getCity("city2", 2);
        sut.connect(sat, 30);
        assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalArgumentException.class)
    public void connectCity2(){
        OpenCity sut = getCity("city", 1);
        OpenCity sat = getCity("city2", 2);
        sut.connect(sat, -1);
        assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalArgumentException.class)
    public void connectCity3(){
        OpenCity sut = getCity("city", 1);
        sut.connect(sut, 30);
        assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalArgumentException.class)
    public void connectCity4(){
        OpenCity sut = getCity("city", 1);
        OpenCity sat = getCity("city2", 2);
        sut.connect(sat, 30);
        sut.connect(sat, 30);
        assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalArgumentException.class)
    public void connectCity5(){
        OpenCity sut = getCity("city", 1);
        OpenCity sat = getCity("city2", 2);
        sut.connect(sat, 30);
        sut.connect(sat, 50);
        assertEquals("{city2 2=30}",sut.getConnections().toString());
    }
    @Test (expected = IllegalStateException.class)
    public void connectCity6(){
        OpenCity sut = getCity("city", 1);
        OpenCity sat = getCity("city2", 2);
        sut.close();
        sut.connect(sat, 30);
        assertEquals("{city2 2=30}",sut.getConnections().toString());
    }

    @Test (expected = IllegalStateException.class)
    public void closeBoard(){
        OpenBoard sut = getBoardGermany();
        sut.close();
        sut.close();
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
        assertEquals(getCity("W\u00FCrzburg",4).toString(), sut.findCity("W\u00FCrzburg").toString());
    }
    @Test public void newBoardFindCity2() {

        OpenBoard sut = getBoardGermany();

        assertEquals(null, sut.findCity("Japan"));
    }
    @Test public void closeRegionsBoard1(){
        OpenBoard sut = getBoardGermany();
        sut.closeRegions(2);
        assertEquals(null, sut.findCity("M\u00FCnchen"));
    }
    @Test (expected = NullPointerException.class)
    public void closeRegionsBoard2(){
        OpenBoard sut = getBoardGermany();
        sut.closeRegions(2);
        assertEquals(null, sut.findCity("M\u00FCnchen").getConnections());
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
        assertEquals(14,sut.getCities().size() );
    }
    @Test public void closeRegionsBoard5(){
        OpenBoard sut = getBoardGermany();
        sut.closeRegions(2);
        OpenCity city = sut.findCity("Berlin");
        assertTrue(sut.findCity("Berlin").getConnections().size() == 4);
    }
    @Test public void getCitiesOfBoard1(){
        OpenBoard sut = getBoardGermany();
        assertEquals(42,sut.getCities().size());
    }
    @Test public void getCitiesOfBoard2(){
        OpenBoard sut = getBoardGermany();
        assertEquals(factory.newCity("W\u00FCrzburg",4).toString(), sut.findCity("W\u00FCrzburg").toString());
    }
    @Test public void getCitiesOfBoard3(){
        OpenBoard sut = getBoardGermany();
        Assert.assertFalse( sut.findCity("W\u00FCrzburg").getConnections().isEmpty());
    }
    @Test (expected = IllegalStateException.class)
    public void closeRegionsTwice(){
        OpenCity sut = getCity("mm", 3);
        sut.close();
        sut.close();
    }

    @Test (expected = IllegalStateException.class)
    public void closedAfterCloseMethod(){
        OpenBoard board = getBoardGermany();
        OpenCity city = factory.newCity("Unicorntown", 1000);
        OpenCity city2 = factory.newCity("Softwarehausen", 1000);
        Set<OpenCity> cities = board.getOpenCities();
        cities.add(city);
        cities.add(city2);
        board.close();
        city.connect(city2, 1000);
    }

    @Test public void newBoard2() {
        // arrange
        final EditionGermany edition = new EditionGermany();
        final OpenBoard sut = factory.newBoard(edition);
        // act
        sut.close();
        // assert
        assertEquals("board holds all specified cities",
                (long)edition.getCitySpecifications().size(),
                sut.getCities().size());
    }

    @Test public void closeNew() {
        // arrange
        final EditionGermany edition = new EditionGermany();
        final OpenBoard sut = factory.newBoard(edition);
        // act
        sut.close();
        // assert
        assertEquals("board holds all specified cities",
                (long)edition.getCitySpecifications().size(),
                sut.getCities().size());
    }
    @Test (expected = IllegalStateException.class)
    public void cityCloseNew(){
        final OpenCity sut = factory.newCity("UnicornLand", 4);
        sut.close();
    }

    @Test (expected = UnsupportedOperationException.class)
    public void boardClose2(){
        final OpenBoard sut = factory.newBoard(new EditionGermany());
        sut.close();
        sut.getCities().add(factory.newCity("UnicorCity", 666));
    }
    @Test (expected = IllegalStateException.class)
    public void boardClose3(){
        final OpenBoard sut = factory.newBoard(new EditionGermany());
        sut.close();
        sut.close();
    }
    @Test(expected = IllegalStateException.class)
    public void cityClose(){
        OpenCity sut = factory.newCity("Unicorn", 4);
        OpenCity s = factory.newCity("TheSatanists", 666);
        sut.connect(s, 5);
        sut.close();
        sut.close();
    }
    @Test(expected = IllegalStateException.class)
    public void cityClose2(){
        OpenCity sut = factory.newCity("Unicorn", 4);
        OpenCity s = factory.newCity("TheSatanists", 666);
        s.close();
        sut.connect(s, 5);
        sut.close();
    }
    @Test(expected = IllegalStateException.class)
    public void cityClose3(){
        OpenCity sut = factory.newCity("Unicorn", 4);
        sut.close();
        OpenCity s = factory.newCity("TheSatanists", 666);
        s.close();
        sut.connect(s, 5);

    }

    //Neue Tests 2:
    @Test(expected = IllegalArgumentException.class)
    public void nameStartWithNumber(){
        OpenCity sut = factory.newCity("1Bims", 2);
    }

    @Test(expected = IllegalStateException.class)
    public void connectionAfterCloseCity(){
        OpenCity sut = factory.newCity("Testhausen", 2);
        OpenCity sut2 = factory.newCity("Lolhausen", 3);
        OpenCity sut3 = factory.newCity("UnicornCity", 3);
        sut.connect(sut2, 5);
        sut.close();
        sut.connect(sut3, 10);
    }

    @Test public void getCitiesOfBoard50(){
        OpenBoard sut = getBoardGermany();
        OpenCity mutant = sut.findCity("Flensburg");
        assertEquals(1, mutant.getRegion());
        assertEquals("{Kiel 1=4}", mutant.getConnections().toString());
        assertEquals(42,sut.getCities().size());
    }

    @Test
    public void emptyConnectionsAfterRemoving(){
        OpenBoard sut = getBoardGermany();
        final OpenCity stuttgart = sut.findCity("Stuttgart");
        sut.closeRegions(4);
        assertTrue(stuttgart.getConnections().isEmpty());
    }



}
