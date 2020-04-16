package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Factory;

import java.util.*;

/**
 * the board of the game.
 *
 * @author Severin
 * @complexity: 30
 */
public class NeutralBoard implements Board {
    /**
     * constant to correct the offset of unicode numbers.
     */
    private static int htmlCorrecter = 48;

    /**
     * Factory used for this board.
     */
    private final Factory factory;

    /**
     * edition of the board.
     */
    private final Edition edition;

    /**
     * state of the bord.
     * if open: board can be changed
     * if closed: no more changes valid
     */
    private boolean open;

    /**
     * includes all possible cities names.
     */
    private Set<City> cityNames;

    /**
     * A new board.
     *
     * @param edition Edition of the board. Not Null.
     */
    NeutralBoard(final Edition edition) {
        Objects.requireNonNull(edition);
        this.edition = edition;
        this.factory = new NeutralFactory();
        //default state
        open = true;
        // method exported. Collects all City-Names of Edition
        this.cityNames = getCityNamesFromEdition();
        // connects all cities in cityNames
        connectionSpecifics();

    }

    private void isBoardOpen() {
        if (!open) {
            throw new IllegalStateException("Board is closed");
        }
    }

    /**
     * removes cities and connections, if area > remaining.
     * also updates the Variable cityNames
     *
     * @param remaining highest area that should remain
     * @complexity: 5
     */
    @Override
    public void closeRegions(final int remaining) {
        isBoardOpen();
        Set<City> allCities = getCities();
        int originalCities = allCities.size();
        // converts city Set to array
        // and goes through every element of the new Array (for each loop)
        // (this is needed to not be offset by the changing cityNames)
        City[] cityArray = allCities.toArray(new City[getCities().size()]);
        for (City city : cityArray) {
            // deletes city from citySet
            if (city.getRegion() > remaining) {
                getCities().remove(city);
            }
            else {
                // creates a Set of connections,
                // of the current city (the ArrayElement)
                Set<City> connectedCities = city.getConnections().keySet();
                // same thing as before-> Set to Array
                City[] connectedArray = connectedCities.toArray(
                        new City[city.getConnections().size()]);
                // deletes invalid cities from a valid cities connections
                for (City connected : connectedArray) {
                    if (connected.getRegion() > remaining) {
                        city.getConnections().remove(connected);
                    }
                }
            }
        }
        // originalCities == Number of cities in CitySpecifications
        assert originalCities > getCities().size();
    }


    /**
     * finds a city in the set cityNames.
     *
     * @param name Name of the requested city
     * @return found city or null
     * @complexity: 3
     */
    @Override
    public City findCity(final String name) {
        City found = null;
        City[] cityArray = getCities().toArray(new City[getCities().size()]);
        for (City city : cityArray) {
            if (city.getName().contains(name)) {
                found = city;
            }
        }
        assert getCities().contains(found) || found == null;
        return found;
    }

    /**
     * get all city names.
     *
     * @return cityNames
     */
    @Override
    public Set<City> getCities() {
        return cityNames;
    }

    /**
     * closes board and all its cities.
     *
     * @throws IllegalStateException if board is already closed
     * @complexity 3
     */
    @Override
    public void close() {
        if (open) {
            open = false;
            Set<City> allCities = getCities();
            for (City city : allCities) {
                city.close();
            }
        } else {
            throw new IllegalStateException("Board is already closed.");
        }
        assert this.open == false;
    }

    private Edition getEdition() {
        return edition;
    }


    /**
     * gets List from edition.getCitySpecifications (citySpecs).
     * and sorts out the first word(=CityName) of every String
     * and reorders into a Set
     *
     * @return Set of all cities
     * @complexity: 2
     */
    private Set<City> getCityNamesFromEdition() {
        HashSet<City> citySet = new HashSet<>();

        for (int counter = 0; counter < getEdition().getCitySpecifications().size(); counter++) {
            String citySpecElement = getEdition().getCitySpecifications().get(counter);
            // first word of String
            String cityName = citySpecElement.substring(0, citySpecElement.indexOf(' '));
            // java automatically converts chars to their html number code
            // for numbers this has to be corrected by distracting the html- numberCode of 0 (= 48)
            int area = citySpecElement.charAt(cityName.length() + 1) - htmlCorrecter;
            citySet.add(factory.newCity(cityName, area));
        }
        assert getEdition().getCitySpecifications().size() == citySet.size();
        return citySet;
    }

    /**
     * catches exception thrown from connect.
     * @param from city to be connected
     * @param to city to connect to
     * @param cost of connection
     */
    private void connectionHandler(final City from,final City to, final int cost) {
        try {
            from.connect(to, cost);
        } catch (RuntimeException e) {

        }
    }

    /**
     * loops through all cities and specifics.
     * @complexity: 4
     */
    private void connectionSpecifics() {
        List<String> citySpecs = getEdition().getCitySpecifications();
        Set<City> allCityNames = getCities();

        //goes through all cities
        for (City from : allCityNames) {
            String fromCityName = from.getName();
            //goes through city specifics
            for (String specElement : citySpecs) {
                if (specElement.contains(fromCityName)) {
                    connectAll(from, specElement);
                }
            }
        }
    }

    /**
     * connects all cities, creates their connections.
     * @param from city to connect to
     * @param specElement specifics for the connection
     * @complexity: 5
     */
    private void connectAll(final City from,final String specElement) {
        String fromCityName = from.getName();
        City toCity;
        int cost = 0;
        String[] specArray = specElement.split(" ");
        // cityname is first element
        if (specElement.startsWith(fromCityName)) {
            for (int index = 2; index < specArray.length; index += 2) {
                toCity = findCity(specArray[index]);
                connectionHandler(from, toCity, cost);
            }
        } else {
            // cityName not first element
            String sCity = specArray[0];
            toCity = findCity(sCity);
            for (int index = 0; index < specArray.length; index++) {
                String arrayElement = specArray[index];
                if (arrayElement.contains(fromCityName)) {
                    // int costIndex = index;
                    cost = Integer.parseInt(specArray[index + 1]);
                }
            }
            connectionHandler(from, toCity, cost);
        }
        assert from.getConnections() != null;
    }

}
