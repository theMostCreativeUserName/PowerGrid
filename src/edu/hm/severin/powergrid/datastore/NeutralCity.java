package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.datastore.City;

import java.util.Map;

public class NeutralCity implements City {
    private final String name;
    private final int area;
    private boolean open;

    NeutralCity(String name, int area){
        this.name = name;
        this.area = area;
        this.open = true;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getArea() {
        return area;
    }

    @Override
    public void connect(City to, int cost) {

    }

    @Override
    public Map<City, Integer> getConnections() {
        return null;
    }

    @Override
    public void close() {
        open = false;
    }

}
