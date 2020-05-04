package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Factory;

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
public class NeutralBoard implements Board {
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
        connectAll();
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
     * @complexity: 3
     */
    @Override
    public void closeRegions(final int remaining) {
        isBoardOpen();
        final Set<City> result = getCities();
        final Set<City> remove = new HashSet<>();
        for (City city : result) {
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
        for (City city : getCities()) {
            final Set<City> keySet = city.getConnections().keySet();
            final Set<City> remove = new HashSet<>();
            for (City connection : keySet) {
                final City found = findCity(connection.getName());
                if (found == null) {
                    remove.add(connection);
                }
            }
            for (City toRemove : remove) {
                city.getConnections().remove(toRemove);
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
    public City findCity(final String name) {
        City found = null;
        final City[] cityArray = getCities().toArray(new City[getCities().size()]);
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
            final Set<City> allCities = getCities();
            for (City city : allCities) {
                city.close();
            }
        } else {
            throw new IllegalStateException("Board is already closed.");
        }
        assert !this.open;
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
         final HashSet<City> citySet = new HashSet<>();

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
            final City fromCity = findCity(specific
                    .substring(0, specific.indexOf(' ')));
            final String[] specArray = specific.split(" ");
            int cityIndex = 2;
            int costIndex = MAGIC_NUMBER;
            while (costIndex <= specArray.length - 1) {
                if(specArray[costIndex].length()<=2){
                    final int cost = Integer.parseInt(specArray[costIndex]);
                    final City toCity = findCity(specArray[cityIndex]);
                    fromCity.connect(toCity, cost);
                    toCity.connect(fromCity, cost);
                }
                cityIndex += 2;
                costIndex += 2;
            }
        }
    }


}
