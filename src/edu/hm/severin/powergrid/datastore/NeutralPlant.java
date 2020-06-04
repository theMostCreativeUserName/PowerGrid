package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.severin.powergrid.ListBag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * creates the Factory of the game.
 *
 * @author Severin, Pietsch
 * @complexity: 20
 */
public class NeutralPlant implements OpenPlant {
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
    /**
     * resource thar can be used by plant.
     */
    private final Set<Bag<Resource>> usableResources;

    /**
     * creates new Neutral Plant.
     *
     * @param number            identifying number
     * @param type              type of plant
     * @param numberofResources Number of Resources used in one turn
     * @param cities            the plant provides to
     */

    public NeutralPlant(final int number, final Type type, final int numberofResources, final int cities) {
        if (number <= 0) throw new IllegalArgumentException("number of plant cannot be negative or Zero");
        if (cities <= 0) throw new IllegalArgumentException("plant provides at least one city");
        if (numberofResources <= 0) throw new IllegalArgumentException("plant uses at least one resource");

        this.number = number;
        this.type = type;
        this.numberOfResources = numberofResources;
        this.cities = cities;
        this.usableResources = getUsableResources(this.numberOfResources);
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
     * resources, that can be used by a plant.
     *
     * @return unmodifiable set of resources, non null
     */
    @Override
    public Set<Bag<Resource>> getResources() {
        return this.usableResources;
    }

    private Set<Bag<Resource>> getUsableResources(int amount) {
        final Plant.Type plantType = getType();
        final Set<Bag<Resource>> plantCanUse = new HashSet<>();
        Bag<Resource> usable = new ListBag<>();

       switch (plantType) {
            case Coal -> usable = usable.add(Resource.Coal, amount);
            case Oil -> usable = usable.add(Resource.Oil, amount);
            case Garbage -> usable = usable.add(Resource.Garbage, amount);
            case Uranium -> usable = usable.add(Resource.Uranium, amount);
        }
        if (plantType == Type.Hybrid){
            plantCanUse.addAll(generateHybridBags(amount));
        }else {
            final Bag<Resource> immutableUsable = usable.immutable();
            plantCanUse.add(immutableUsable);
        }
        return plantCanUse;
    }

    private Set<Bag<Resource>> generateHybridBags(int amount) {
        Set<Bag<Resource>> allBags = new HashSet<>();
        allBags.add(new ListBag<Resource>().add(Resource.Coal, amount));
        allBags.add(new ListBag<Resource>().add(Resource.Oil, amount));
        int currentAmount = amount-1;
        while (currentAmount != 0){
            Bag<Resource> oneBagOfCombination = new ListBag<>();
            oneBagOfCombination = oneBagOfCombination.add(Resource.Coal, currentAmount);
            oneBagOfCombination = oneBagOfCombination.add(Resource.Oil, amount-currentAmount);
            allBags.add(oneBagOfCombination);
            currentAmount--;
        }
        return allBags;
    }

    @Override
    public int compareTo(Plant other) {
        return this.getNumber() - other.getNumber();
    }
}
