package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenBoard;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;

import java.util.Set;
import java.util.Objects;
import java.util.HashSet;
import java.util.List;

/**
 * the board of the game.
 *
 * @author Severin
 * @complexity: 25
 */
public class NeutralBoard implements OpenBoard {
    /**
     * constant to correct the offset of unicode numbers.
     */
    private static final int HTML_CORRECTER = 48;
    /**
     * initializes the search for connection cost in connectAll().
     */
    private static final int MAGIC_NUMBER = 3;

    /**
     * Factory used for this board.
     */
    private final OpenFactory factory;

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
    private Set<OpenCity> cityNames;

    /**
     * A new board. Electro
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
        connectAll();
    }


    private void isBoardOpen() {
        if (!open) {
            throw new UnsupportedOperationException("Board is closed");
        }
    }

    /**
     * removes cities and connections, if area > remaining.
     * also updates the Variable cityNames
     *
     * @param remaining highest area that should remain
     * @complexity: 3
     */
    @Override
    public void closeRegions(final int remaining) {
        final Set<OpenCity> result = getOpenCities();
        final Set<OpenCity> remove = new HashSet<>();
        for (OpenCity city : result) {
            if (city.getRegion() > remaining) {
                remove.add(city);
            }
            //city.getConnections();
        }
        result.removeAll(remove);
        this.cityNames = result;
        removeClosedConnections();
    }

    /**
     * removes connections to non existing cities.
     *
     * @complexity: 5
     */
    private void removeClosedConnections() {
        for (OpenCity city : getOpenCities()) {
            final Set<City> keySet = city.getOpenConnections().keySet();
            final Set<City> remove = new HashSet<>();
            for (City connection : keySet) {
                final OpenCity found = findCity(connection.getName());
                if (found == null) {
                    remove.add(connection);
                }
            }
            for (City toRemove : remove) {
                city.getOpenConnections().remove(toRemove);
            }
        }
    }

    /**
     * finds a city in the set cityNames.
     *
     * @param name Name of the requested city
     * @return found city or null
     * @complexity: 3
     */
    @Override
    public OpenCity findCity(final String name) {
        OpenCity found = null;
        final OpenCity[] cityArray = getOpenCities().toArray(new OpenCity[getOpenCities().size()]);
        for (OpenCity city : cityArray) {
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
    public Set<OpenCity> getOpenCities() {
        isBoardOpen();
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
            for (City city : getCities()) {
               city.close();
            }
        open = false;
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
    private Set<OpenCity> getCityNamesFromEdition() {
        final Set<OpenCity> citySet = new HashSet<>();

        for (int counter = 0; counter < getEdition()
                .getCitySpecifications()
                .size(); counter++) {
            final String citySpecElement = getEdition()
                    .getCitySpecifications()
                    .get(counter);
            // first word of String
            final String cityName = citySpecElement
                    .substring(0, citySpecElement.indexOf(' '));
            // java automatically converts chars to their html number code
            // for numbers this has to be corrected
            // by distracting the html- numberCode of 0 (= 48)
            final int area = citySpecElement
                    .charAt(cityName.length() + 1) - HTML_CORRECTER;
            citySet.add(factory.newCity(cityName, area));
        }
        assert getEdition().getCitySpecifications().size() == citySet.size();
        return citySet;
    }

    /**
     * connects all cities, creates their connections.
     *
     * @complexity: 4
     */
    public void connectAll() {
        final List<String> citySpecs = getEdition().getCitySpecifications();
        for (String specific : citySpecs) {
            final OpenCity fromCity = findCity(specific
                    .substring(0, specific.indexOf(' ')));
            final String[] specArray = specific.split(" ");
            int cityIndex = 2;
            int costIndex = MAGIC_NUMBER;
            while (costIndex <= specArray.length - 1) {
                if (specArray[costIndex].length() <= 2) {
                    final int cost = Integer.parseInt(specArray[costIndex]);
                    final OpenCity toCity = findCity(specArray[cityIndex]);
                    fromCity.connect(toCity, cost);
                    toCity.connect(fromCity, cost);
                }
                cityIndex += 2;
                costIndex += 2;
            }
        }
    }


}
