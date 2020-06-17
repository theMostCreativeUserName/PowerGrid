
package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * a city of the game.
 *
 * @author Severin, pietsch
 */
public class NeutralCity implements OpenCity {
    /**
     * Name of city.
     */
    private final String name;

    /**
     * area of city.
     */
    private final int area;

    /**
     * state of the bord.
     * if open: board can be changed
     * if closed: no more changes valid
     */
    private boolean open;

    /**
     * connections of this city.
     */
    private final Map<City, Integer> connections = new HashMap<>();

    /**
     * a new city.
     *
     * @param name Name of City. Not null; not empty
     * @param area Area of City. Bigger 0
     */
    NeutralCity(final String name, final int area) {
        if (name.isEmpty())
            throw new IllegalArgumentException("name of city is invalid");
        if(!Character.isLetter(name.charAt(0)))
            throw new IllegalArgumentException("name starts with letter");
        if (area < 1)
            throw new IllegalArgumentException("area of city is invalid");
        this.name = name;
        this.area = area;
        this.open = true;
    }

    /**
     * Name getter.
     *
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Region getter.
     *
     * @return region
     */
    @Override
    public int getRegion() {
        return area;
    }

    /**
     * connects two cities.
     *
     * @param cost   cost of the connection
     * @param toCity City to connect to
     */
    @Override
    public void connect(final City toCity, final int cost) {
        if (open) {
            Objects.requireNonNull(toCity);
            if (toCity ==  this) {
                throw new IllegalArgumentException("city is invalid");
            }
            if(cost < 0)
                throw new IllegalArgumentException("cost is invalid");
            if (getOpenConnections().containsKey(toCity))
                throw new IllegalArgumentException("connection exists already");
            getOpenConnections().put(toCity, cost);
        } else throw new IllegalStateException("city is closed already");

        assert toCity.getConnections() != null;
    }

    /**
     * Connection Getter.
     *
     * @return connections
     */
    @Override
    public Map<City, Integer> getOpenConnections() {
        Map<City, Integer> result = connections;
        if(!open) {
            result = Map.copyOf(connections);
        }
        return result;
    }

    /**
     * closes this city.
     */
    @Override
    public void close() {
        if (this.getConnections().isEmpty()) throw new IllegalStateException("city has no connections");
        if (!open) {
            throw new IllegalStateException("Board is closed");
        }
        open = false;
    }

    /**
     * new toString for better testing.
     *
     * @return city.toString()
     */
    @Override
    public String toString() {
        return getName() + " " + getRegion();
    }

    // SpotBugs: Medium Confidence Bad Practise because of not implementing equals, which isn´t needed
    @Override
    public int compareTo(City other) {
        return this.getName().compareToIgnoreCase(other.getName());
    }
}
