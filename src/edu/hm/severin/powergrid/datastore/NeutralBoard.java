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
     * through Edition.getCitySpecifications connections are organized into Map<City, City>
     *  Complexity: 6
     * @return Map of all city connections
     */
    private Map<City,City> getConnectionsFromEdition() {
        List<String> citySpecs = getEdition().getCitySpecifications();
        Map<City, City> cityConnections = null;
        Set<City> allCities = getCities();
        // go through every city of the city-set
     /*   while(this.getCities().iterator().hasNext()){
            getCities().forEach(city -> {
                City toCity = null;
                while(citySpecs.iterator().hasNext()){
                    citySpecs.forEach(listElement ->{
                        if (listElement.contains(city.getName())){
                            String[] singleCityArray = listElement.split(" ");
                            if(singleCityArray[0] == city.getName()){
                                int index = 0;
                                while (index < singleCityArray.length){
                                    if(index%2==0){
                                        toCity = findCity(singleCityArray[index]);

                                    }
                                }
                            }
                        }
                    });
                }
            });
        }*/
        for (City fromCity : getCities()) {
            //goes through every string of citySpecifications

            for (String SpecElement : citySpecs) {
                if (SpecElement.contains(fromCity.getName())) {
                    String[] singleCityArray = SpecElement.split(" ");
                    if (singleCityArray[0] == fromCity.getName()) {
                        int index = 0;
                        while (index < singleCityArray.length) {
                            if (index % 2 == 0 && index != 0) {
                                 City toCity = findCity(singleCityArray[index]);
                                cityConnections.put(fromCity, toCity);
                            }
                            index++;
                        }
                    } else {
                        City toCity = findCity(singleCityArray[0]);
                        cityConnections.put(fromCity, toCity);
                    }
                }
            }
        }
        return cityConnections;
    }

    /**
     * complexity: 7
     * @param fromCity
     * @return complete connections of one city
     */
    private Map<City, Integer> connectSingleCity(City fromCity) {
        List<String> citySpecs = getEdition().getCitySpecifications();
        String fromCityName = fromCity.getName();
        Map<City,Integer> connections = null;
        City toCity = new NeutralCity("n", 1);
        int cost = 0;

        for (String specElement : citySpecs) {
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
                        // here is a problem somewhere.....
                        String arrayElement = specArray[index];
                        if (arrayElement.contains(fromCityName)) {
                            // int costIndex = index;
                            cost = Integer.valueOf(specArray[index + 1]);
                        }
                    }
                }
                connections.put(toCity,cost);
            }
        }
        return connections;
    }


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
                    if (!city.getConnections().containsKey(toCity)) {
                        city.connect(toCity, cost);
                        System.out.println(city.getConnections());
                    }
                }
            }
        }
    }
    /**
     * trough Edition.citySpecifications costs for connections are found
     * complexity: 4
     * @return int cost of the connection
     */
    private int findConnectionCost(City from, City to){
        int cost = -1;
        for (String singleCity: getEdition().getCitySpecifications()) {
            if (singleCity.contains(from.getName()) && singleCity.contains(to.getName())) {
                if (singleCity.startsWith(from.getName())) {
                    int startIndex = singleCity.indexOf(to.getName());
                    cost = Integer.valueOf(singleCity.charAt(startIndex+to.getName().length()+1));
                }else{
                    int startIndex = singleCity.indexOf(from.getName());
                    cost = Integer.valueOf(singleCity.charAt(startIndex+from.getName().length()+1));
                }
            }
        }
        return cost;
    }

    /**
     * connects all cities, through city.connect()
     * complexity: 1
     */
    private void connectAllCities(){
        Map<City,City> allConnections = getConnectionsFromEdition();
        for(Map.Entry<City, City> entry : allConnections.entrySet()){
            City from = entry.getKey();
            City to = entry.getValue();
            from.connect(to, findConnectionCost(from, to));
            System.out.println(from.getConnections());
        }
    }

}
