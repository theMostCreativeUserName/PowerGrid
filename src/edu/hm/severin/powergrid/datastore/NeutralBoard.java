package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenBoard;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * the board of the game.
 *
 * @author Severin
 */
public class NeutralBoard implements OpenBoard {

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

    /**
     * checks if the board is open, when not throw Exception.
     */
    private void isBoardOpen() {
        if (!open) {
            throw new IllegalStateException("Board is closed");
        }
    }

    /**
     * removes cities and connections, if area is bigger than remaining.
     * also updates the Variable cityNames.
     *
     * @param remaining highest area that should remain
     */
    @Override
    public void closeRegions(final int remaining) {
        isBoardOpen();
        final Set<OpenCity> result = getOpenCities();
        final Set<OpenCity> remove = new HashSet<>();
        result.stream()
                .filter( city -> city.getRegion() > remaining)
                .forEach(remove::add);
        remove.forEach(city -> city.getOpenConnections().clear());
        result.removeAll(remove);
        this.cityNames = result;
        removeClosedConnections();
    }

    /**
     * removes connections to non existing cities.
     *
     */
    private void removeClosedConnections() {
        for (OpenCity city : getOpenCities()) {
            final Set<City> remove = new HashSet<>();
            city.getOpenConnections().keySet().stream()
                    .filter( connection -> findCity(connection.getName()) == null)
                    .forEach(remove::add);

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
     */
    @Override
    public OpenCity findCity(final String name) {
        OpenCity found = null;
        final OpenCity[] cityArray = getOpenCities().toArray(new OpenCity[getOpenCities().size()]);
        for (OpenCity city : cityArray) {
            if (city.getName().equals(name)) {
                found = city;
            }
        }
        return found;
    }

    /**
     * get all city names.
     *
     * @return cityNames
     */
    @Override
    public Set<OpenCity> getOpenCities() {
        Set<OpenCity> result = cityNames;
        if (!open) {
            result =  Set.copyOf(cityNames);
        }
        return result;
    }

    /**
     * closes board and all its cities.
     *
     * @throws IllegalStateException if board is already closed
     */
    @Override
    public void close() {
        if (open) {
            open = false;
            final Set<OpenCity> allCities = getOpenCities();
            for (OpenCity city : allCities) {
                city.close();
            }
        } else {
            throw new IllegalStateException("Board is already closed.");
        }
        assert !this.open;
    }

    /**
     * getter for edition of board.
     * @return edition of board
     */
    private Edition getEdition() {
        return edition;
    }


    /**
     * gets List from edition.getCitySpecifications (citySpecs).
     * and sorts out the first word(=CityName) of every String
     * and reorders into a Set
     *
     * @return Set of all cities
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
            // area of the city
            final int area = Integer.parseInt(String.valueOf(citySpecElement
                    .charAt(cityName.length() + 1)));
            citySet.add(factory.newCity(cityName, area));
        }
        assert getEdition().getCitySpecifications().size() == citySet.size();
        return citySet;
    }

    /**
     * connects all cities, creates their connections.
     *
     */
    private void connectAll() {
        final List<String> citySpecs = getEdition().getCitySpecifications();
        for (String specific : citySpecs) {
            final OpenCity fromCity = findCity(specific
                    .substring(0, specific.indexOf(' ')));
            final String[] specArray = specific.split(" ");
            final List<String> list = Arrays.stream(specArray).filter(string -> string.length() > 0).collect(Collectors.toList());

            for (int index = 2; index <= specArray.length - 2; index += 2) {
                final int cost = Integer.parseInt(list.get(index + 1));
                final OpenCity toCity = findCity(list.get(index));
                fromCity.connect(toCity, cost);
                toCity.connect(fromCity, cost);
            }
        }
    }


}
