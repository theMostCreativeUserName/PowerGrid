package DataStore;

import java.util.Set;

public class NeutralBoard implements Board {

    @Override
    public City findCity(String name) {
        return null;
    }

    @Override
    public void closeRegions(int limit) {

    }

    @Override
    public Set<City> getCities() {
        return null;
    }

    @Override
    public void close() {

    }
}
