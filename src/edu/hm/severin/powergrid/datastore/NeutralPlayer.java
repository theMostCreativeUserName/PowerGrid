package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.Resource;

import java.util.Set;

public class NeutralPlayer implements Player {
    final String secret;
    final String color;
    boolean passed;
    int electro;
    Set<City> cities;
    Set<Plant> plants;
    Bag<Resource> resources;

    public NeutralPlayer(String secret, String color) {
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
    public boolean hasSecret(String secret) {
        return false;
    }
}
