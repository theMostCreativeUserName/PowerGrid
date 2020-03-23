package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Resource;

import java.util.Set;

public class NeutralPlant implements Plant {
    private final int number;
    private final Plant.Type type;
    private final int resources;
    private final int cities;
    private boolean operated;

    public NeutralPlant(int number, Type type, int resources, int cities) {
        this.number = number;
        this.type = type;
        this.resources = resources;
        this.cities = cities;
    }

    @Override
    public int getNumber() {
        return 0;
    }

    @Override
    public int getCities() {
        return 0;
    }

    @Override
    public int getNumberOfResources() {
        return 0;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean hasOperated() {
        return false;
    }

    @Override
    public void setOperated(boolean operated) {

    }

    @Override
    public Set<Bag<Resource>> getResources() {
        return null;
    }
}
