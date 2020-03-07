package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Factory;

import java.util.*;

public class NeutralBoard implements Board {
    private boolean open;
    private final Edition edition;
    private Set<City> cityNames;

    NeutralBoard(Edition edition) {
        Objects.requireNonNull(edition);
        this.edition = edition;
        open = true;
        this.cityNames = getCityNamesFromEdition();
        connectAll();
    }

    /**
     * removes cities and connections of area > remaining
     * complexity: 6
     */
    @Override
    public void closeRegions(int remaining) {
        if (!open) throw new IllegalStateException("Board is closed");
        Set<City> allCities = getCities();
        // city Set as Array to go through each single city
        City[] cityArray = allCities.toArray(new City[getCities().size()]);
        for (City city : cityArray) {
            // deletes city from citySet and its connections
            if (city.getRegion() > remaining)
                getCities().remove(city);
            else {
                Set<City> connectedCities = city.getConnections().keySet();
                City[]connectedArray = connectedCities.toArray(new City[city.getConnections().size()]);
                // deletes invalid cities from a valid cities connections
                for (City connected : connectedArray) {
                    if (connected.getRegion() > remaining) city.getConnections().remove(connected);
                }
            }
        }
    }


    /**
     * finds a city in the set of all cities
     *
     * @return found city or null
     * complexity: 3
     */
    @Override
    public City findCity(String name) {
        City found = null;
        City[] cityArray = getCities().toArray(new City[getCities().size()]);
        for (City city : cityArray) {
            if (city.getName().contains(name)) {
                found = city;
            }
        }
        return found;
    }

    @Override
    public Set<City> getCities() {
        return cityNames;
    }

    @Override
    public void close() {
        open = false;
    }

    private Edition getEdition() {
        return edition;
    }


    /**
     * gets List from edition.getCitySpecifications
     * and sorts out the first word(=CityName) of every String
     * and reorders into a Set
     * complexity: 2
     *
     * @return Set of all cities
     */
    private Set<City> getCityNamesFromEdition() {
        // city Specs. as Array, so you can actually do stuff
        City[] cityArray = new City[getEdition().getCitySpecifications().size()];
        Factory factory = new NeutralFactory();
        int index = 0;
        while (index != getEdition().getCitySpecifications().size()) {
            String citySpecElement = getEdition().getCitySpecifications().get(index);
            // first word of String
            String cityName = citySpecElement.substring(0, citySpecElement.indexOf(' '));
            // java automatically converts chars to their html number code
            // for numbers this has to be corrected by distracting the html- numberCode of 0 (= 48)
            int area = citySpecElement.charAt(cityName.length() + 1) - 48;
            cityArray[index] = factory.newCity(cityName, area);
            index++;
        }
        return new HashSet<>(Arrays.asList(cityArray));
    }

    /**
     * connects all cities, creates these connections
     * complexity: 12
     * TODO: reduce complexity
     * (maybe export inner stuff from second loop to new method; eg find cost)
     */
    private void connectAll() {
        List<String> citySpecs = getEdition().getCitySpecifications();
        Set<City> cityName = getCities();
        City toCity;
        int cost = 0;
        //goes through all cities
        for (City city : cityName) {
            String fromCityName = city.getName();
            //goes through city specifics
            for (String specElement : citySpecs) {
                if (specElement.contains(fromCityName)) {

                    String[] specArray = specElement.split(" ");
                    // cityname is first element
                    if (specElement.startsWith(fromCityName)) {
                        for (int index = 2; index < specArray.length; index += 2) {
                            toCity = findCity(specArray[index]);
                            if (!city.getConnections().containsKey(toCity) && toCity != null)
                                city.connect(toCity, cost);
                        }
                    } else {

                        String sCity = specArray[0];
                        toCity = findCity(sCity);
                        for (int index = 0; index < specArray.length; index++) {
                            String arrayElement = specArray[index];
                            if (arrayElement.contains(fromCityName)) {
                                // int costIndex = index;
                                cost = Integer.parseInt(specArray[index + 1]);
                            }
                        }
                        if (!city.getConnections().containsKey(toCity) && toCity != null) {
                            city.connect(toCity, cost);
                        }
                    }
                }
            }
        }
    }

}
