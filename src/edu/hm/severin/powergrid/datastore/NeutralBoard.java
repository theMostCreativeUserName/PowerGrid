package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;

import java.util.Set;

public class NeutralBoard implements Board {
    private boolean open;
    private Edition edition;

    NeutralBoard(Edition edition){
        this.edition = edition;
        open = true;
    }

    @Override
    public void closeRegions(int remaining) {

    }

    @Override
    public City findCity(String name) {
        return null;
    }

    @Override
    public Set<City> getCities() {
        return null;
    }

    @Override
    public void close() {
        open = false;
    }
}
