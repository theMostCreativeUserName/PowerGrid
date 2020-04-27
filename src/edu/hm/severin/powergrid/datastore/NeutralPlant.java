package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.severin.powergrid.ListBag;

import java.util.HashSet;
import java.util.Set;

/**
 * creates the Factory of the game.
 * @author Severin, Pietsch
 * @complexity: 20
 */
public class NeutralPlant implements Plant {
    /**
     * number of plant.
     */
    private final int number;
    /**
     * type of plant.
     */
    private final Plant.Type type;
    /**
     * number of resources used by plant.
     */
    private final int numberOfResources;
    /**
     * number of cities that can be provided.
     */
    private final int cities;
    /**
     * is plant operating?
     */
    private boolean operated;
    /** resource thar can be used by plant.*/
    private Set<Bag<Resource>> usableResources;

    public NeutralPlant(final int number, final Type type, final int numberofResources, final int cities) {
        if (number < 0) throw new IllegalArgumentException("number of plant cannot be negative");
        if (cities < 0) throw new IllegalArgumentException("plant provides at least one city");
        if (numberofResources < 0) throw new IllegalArgumentException("plant uses at least one resource");

        this.number = number;
        this.type = type;
        this.numberOfResources = numberofResources;
        this.cities = cities;
        this.usableResources = setUsableResources();
    }

    /**
     * identifying number.
     *
     * @return number. not negativ
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * number of cities that can be provided.
     *
     * @return cities
     */
    @Override
    public int getCities() {
        return cities;
    }

    /**
     * Number of resources.
     *
     * @return resources
     */
    @Override
    public int getNumberOfResources() {
        return numberOfResources;
    }

    /**
     * type of the plant.
     *
     * @return type
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * tests if plant has provided energy.
     *
     * @return true, if plant produced energy
     */
    @Override
    public boolean hasOperated() {
        return operated;
    }

    /**
     * Legt fest, ob  dieses Kraftwerk Strom produziert hat.
     *
     * @param operated true genau dann, wenn dieses Kw Strom produziert hat.
     */
    @Override
    public void setOperated(boolean operated) {
        this.operated = operated;
    }

    /**
     * Rohstoffsammlungen, die dieses Kraftwerk verbrennen kann.
     * Wenn das Kw nur eine Sorte verbraucht, hat die Menge nur ein Element.
     * Wenn das Kw verschiedene Sorten akzeptiert,
     * enthaelt die Menge alle zulaessigen Kombinationen.
     * Wenn das Kw nichts braucht, enthaelt die Menge eine leere Sammlung als einziges Element.
     *
     * @return Verschiedene Rohstoffsammlungen. Nicht null und nicht leer.
     * Menge und Elemente unveraenderlich.
     */
    @Override
    public Set<Bag<Resource>> getResources() {
      return this.usableResources;
    }

    private Set<Bag<Resource>> setUsableResources(){
        Plant.Type type = getType();
        Set<Bag<Resource>> canUse = new HashSet<>();
        Bag<Resource> usable = new ListBag<>();
        switch (type) {
            case Coal:
                usable.add(Resource.Coal);
                break;
            case Oil:
                usable.add(Resource.Oil);
                break;
            case Garbage:
                usable.add(Resource.Garbage);
                break;
            case Uranium:
                usable.add(Resource.Uranium);
                break;
            case Hybrid: {
                usable.add(Resource.Coal);
                usable.add(Resource.Oil);
                break;
            }
            default: {
            }
        }
        Bag<Resource> imutableUsable = usable.immutable();
        canUse.add(imutableUsable);
        return canUse;
    }

    @Override
    public int compareTo(Plant other){
        return this.getNumber()-other.getNumber();
    }
}
