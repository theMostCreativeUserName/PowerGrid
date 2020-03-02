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
   // private Map<City, City> cityConnections;

    NeutralBoard(Edition edition) {
        Objects.requireNonNull(edition);
        this.edition = edition;
        open = true;
        this.cityNames = getCityNamesFromEdition();
        connectAll();
    }

    /**
     *removes cities and connections of area > remaining
     * complexity: 2
     */
    @Override
    public void closeRegions(int remaining) {
    if (!open) throw new IllegalStateException("Board is closed");
    Set<City> allCities = getCities();
        for (City city:allCities) {
            if (city.getArea() > remaining) {
                getCities().remove(city);
                city.getConnections().remove(city);
            }
        }
    }

    /**
     * finds a city in a set of all cities
     * @return found city or null
     * complexity: 2
     */
    @Override
    public City findCity(String name) {
        City found = null;
        City[] cityArray = getCities().toArray(new City[getCities().size()]);
        for (City city:cityArray) {
            if(city.getName().contains(name)){
                found = new NeutralCity(city.getName(),city.getArea());
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

    private Edition getEdition(){
        return edition;
    }



    /**
     * gets List from edition.getCitySpecifications
     * and sorts out the first word(=CityName) of every String
     * and reorders into a Set
     * complexity: 2
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
            String cityName = citySpecElement.substring(0,citySpecElement.indexOf(' '));
            // java automatically converts chars to their html number code
            // for numbers this has to be corrected by distracting the html- numberCode of 0 (= 48)
            int area = citySpecElement.charAt(cityName.length()+1) -48;
            cityArray[index]=  factory.newCity(cityName,area);

            index++;
        }
        Set<City> allCityNames = new HashSet<City>();
        for(City element : cityArray) allCityNames.add(element);
        return allCityNames;
    }

    /**
     * connects all cities, creates these connections and cities
     * complexity: 9
     */
    private void connectAll(){
        List<String> citySpecs = getEdition().getCitySpecifications();
        Set<City> cityName = getCities();
        City toCity = new NeutralCity("n",1);
        int cost = 0;
        //goes through all cities
        for (City city: cityName) {
            String fromCityName = city.getName();
            //goes through city specifics
            for (String specElement: citySpecs){
                if (specElement.contains(fromCityName)) {
                    String[] specArray = specElement.split(" ");
                    // cityname is first element
                    if (specElement.startsWith(fromCityName)) {
                        for (int index = 2; index < specArray.length; index += 2) {
                            toCity = findCity(specArray[index]);
                            if (specArray.length < 3)
                                cost = Integer.valueOf(specArray[index + 1]);
                        }
                    } else {

                        String sCity = specArray[0];
                        toCity = findCity(sCity);
                        for (int index = 0; index < specArray.length; index++) {
                            String arrayElement = specArray[index];
                            if (arrayElement.contains(fromCityName)) {
                                // int costIndex = index;
                                cost = Integer.valueOf(specArray[index + 1]);
                            }
                        }
                    }
                    if (!city.getConnections().containsKey(toCity) && toCity!=null) {
                        city.connect(toCity, cost);
                        System.out.println(city.getName()+
                                city.getConnections());
                    }
                }
            }
        }
    }
}
