package DataStore;

import java.util.Map;
import java.util.Set;

/**
 * creates general, separate City-Objects
 */

public class NeutralCity implements City{
    private  String name;
    private  int area;
    private  Set<Connection> connections;
    private boolean cityOpen;

    /**
     * constructs cities from a list in edition
     * sorts out the list elements; and initializes the object variables
     */
    NeutralCity(String name, int area, Set<Connection> connections){
        this.name = name;
        this.area = area;
        this.connections = connections;
        this.cityOpen = true;
    }

    public void setName(String name){
        if (cityOpen) this.name = name;
    }

    public void setArea(int area){
        if (cityOpen) this.area = area;
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
    public Map<City, Integer> getConnections() {
        return null;
    }

    @Override
    public void connect(City to, int cost) {

    }

    @Override
     public void close(){

     }
}
