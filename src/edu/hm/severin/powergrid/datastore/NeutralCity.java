package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Factory;

import java.util.*;

public class NeutralCity implements City {
    private final String name;
    private final int area;
    private boolean open;
    private Map<City, Integer> connections;

    NeutralCity(String name, int area) {
        Objects.requireNonNull(name);
        if (area < 1 || name == "") {
            throw new IllegalArgumentException("name or area of city is invalid");
        } else {
            this.name = name;
            this.area = area;
            this.open = true;
            this.connections = new HashMap<>();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getArea() {
        return area;
    }

    /**
     * complexity: 4
     */
    @Override
    public void connect(City to, int cost) {
        if (open) {
            Objects.requireNonNull(to);
            if(to == this || cost < 0) throw new IllegalArgumentException("city or cost are invalid");
            if (getConnections().containsKey(to)) throw new IllegalArgumentException("connection exists already");
            getConnections().put(to, cost);
        }else throw new IllegalStateException("city is closed already");

    }

    @Override
    public Map<City, Integer> getConnections() {
        return connections;
    }

    @Override
    public void close() {
        open = false;
    }

    @Override
    public String toString() {
        return getName() + " " + getArea();
    }

}
