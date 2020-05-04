
package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.datastore.City;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * a city of the game.
 * @author Severin, pietsch
 * @complexity: 14
 * */
public class NeutralCity implements City {
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
    private final  Map<City, Integer> connections = new HashMap<>();

    /**
     * a new city.
     * @param name Name of City. Not null; not empty
     * @param area Area of City. Bigger 0
     * @complexity: 3
     */
    NeutralCity(final String name, final int area) {
        if (name.isEmpty())
            throw new IllegalArgumentException("name of city is invalid");
        if (area < 1)
            throw new IllegalArgumentException("area of city is invalid");
        this.name = name;
        this.area = area;
        this.open = true;
    }

    /**
     * Name getter.
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Region getter.
     * @return region
     */
    @Override
    public int getRegion() {
        return area;
    }

    /**
     * connects two cities.
     * @complexity: 4
     * @param cost cost of the connection
     * @param toCity City to connect to
     */
    @Override
    public void connect(final City toCity, final int cost) {
        if (open) {
            Objects.requireNonNull(toCity);
            if (toCity == this || cost < 0){
                throw new IllegalArgumentException("city or cost are invalid");
            }
            if (getConnections().containsKey(toCity))
                throw new IllegalArgumentException("connection exists already");
            getConnections().put(toCity, cost);
        } else throw new IllegalStateException("city is closed already");

        assert toCity.getConnections() != null;
    }

    /**
     * Connection Getter.
     * @return connections
     */
    @Override
    public Map<City, Integer> getConnections() {
        return connections;
    }

    /**
     * closes this city.
     */
    @Override
    public void close() {
        open = false;
    }

    /**
     * new toString for better testing.
     * @return city.toString()
     */
    @Override
    public String toString() {
        return getName() + " " + getRegion();
    }

    /**
     * new compareTo, sort cities after name in alphabetic order.
     * @param other Other City to compare with
     * @return Comparevalue (- this is first, 0 this is same as other, + other is first.)
     */
    @Override
    public int compareTo(City other){
        return this.getName().compareTo(other.getName());
    }

}
