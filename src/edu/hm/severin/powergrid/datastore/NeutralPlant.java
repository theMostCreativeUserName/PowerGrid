package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Resource;

import java.util.Set;

public class NeutralPlant implements Plant {
    /** number of plant */
    private final int number;
    /** type of plant*/
    private final Plant.Type type;
    /**number of resources used by plant*/
    private final int resources;
    /** number of cities that can be provided*/
    private final int cities;
    /** is plant operating?*/
    private boolean operated;

    public NeutralPlant(final int number, final Type type, final int resources, final int cities) {
        if (number<0) throw new IllegalArgumentException("number of plant cannot be negative");
        if (cities<0) throw new IllegalArgumentException("plant provides at least one city");
        if (resources<0) throw new IllegalArgumentException("plant uses at least one resource");

        this.number = number;
        this.type = type;
        this.resources = resources;
        this.cities = cities;
    }

    /**
     * identifying number.
     * @return number. not negativ
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * number of cities that can be provided.
     * @return cities
     */
    @Override
    public int getCities() {
        return cities;
    }

    /**
     * Number of resources.
     * @return resources
     */
    @Override
    public int getNumberOfResources() {
        return resources;
    }

    /**
     * type of the plant.
     * @return type
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * tests if plant has provided energy.
     * @return true, if plant produced energy
     */
    @Override
    public boolean hasOperated() {
        return operated;
    }

    /**
     * Legt fest, ob  dieses Kraftwerk Strom produziert hat.
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
     * @return Verschiedene Rohstoffsammlungen. Nicht null und nicht leer.
     * Menge und Elemente unveraenderlich.
     */
    @Override
    public Set<Bag<Resource>> getResources() {
        return null;
    }
}
