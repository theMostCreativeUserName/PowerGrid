package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.Resource;

import java.util.Set;

public class NeutralPlayer implements Player {
    private final String secret;
    private final String color;
    private boolean passed;
    private int electro;
    private Set<City> cities;
    private Set<Plant> plants;
    private Bag<Resource> resources;

    public NeutralPlayer(final String secret, final String color) {
        this.secret = secret;
        this.color = color;
    }

    @Override
    public String getColor() {
        return null;
    }

    @Override
    public Set<City> getCities() {
        return null;
    }

    @Override
    public Set<Plant> getPlants() {
        return null;
    }

    @Override
    public Bag<Resource> getResources() {
        return null;
    }

    @Override
    public int getElectro() {
        return 0;
    }

    @Override
    public void setElectro(int electro) {

    }

    @Override
    public boolean hasPassed() {
        return false;
    }

    @Override
    public void setPassed(boolean passed) {

    }

    @Override
    public String getSecret() {
        return null;
    }

    @Override
    public boolean hasSecret(final String secret) {
        return false;
    }
}
