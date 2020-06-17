package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.severin.powergrid.ListBag;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * creates the Plants of the game.
 *
 * @author Severin, Pietsch
 */
// PMD has problem "Data Classes are simple data holders". Cant be fixed.
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
     * resource that can be used by plant.
     */
    private final Set<Bag<Resource>> usableResources;

    /**
     * creates new Neutral Plant.
     *
     * @param number            identifying number
     * @param type              type of plant
     * @param numberOfResources Number of Resources used in one turn
     * @param cities            the plant provides to
     */

    public NeutralPlant(final int number, final Type type, final int numberOfResources, final int cities) {
        if (number < 0) throw new IllegalArgumentException("number of plant cannot be negative");
        if (cities <= 0) throw new IllegalArgumentException("plant provides at least one city");
        if (numberOfResources < 0) throw new IllegalArgumentException("plant uses at least one resource");
        if (type == null) throw new IllegalArgumentException("type is never null");

        this.number = number;
        this.type = type;
        this.numberOfResources = numberOfResources;
        this.cities = cities;
        this.usableResources = getUsableResources(this.numberOfResources);
    }

    /**
     * identifying number.
     *
     * @return number. not negative
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
     * sets if plant has produced energy.
     *
     * @param operated true if plant produced energy
     */
    @Override
    public void setOperated(boolean operated) {
        this.operated = operated;
    }

    /**
     * set of resources, that this plant can use.
     * if plant uses no resources at all set is empty.
     *
     * @return different resources. not null, not empty.
     * sets and elements are immutable.
     */
    @Override
    public Set<Bag<Resource>> getResources() {
        return this.usableResources;
    }

    /**
     * creates the set of bags for usableResources.
     *
     * @param amount amount of resources needed by plant
     * @return set of bags with specification from getResources
     */
    private Set<Bag<Resource>> getUsableResources(int amount) {
        final Plant.Type plantType = getType();
        final Set<Bag<Resource>> plantCanUse = new HashSet<>();
        final Bag<Resource> usable = new ListBag<>();

        if (plantType == Type.Hybrid) {
            plantCanUse.addAll(generateHybridBags(amount));
        } else if (plantType.getResources().size() == 0)
            plantCanUse.add(usable.immutable());
        else {
            final Bag<Resource> immutableUsable = usable.add(plantType.getResources().iterator().next(), amount).immutable();
            plantCanUse.add(immutableUsable);
        }
        return Collections.unmodifiableSet(plantCanUse);
    }


    /**
     * Generate Set of Bags with all combination of Resources for hybrid plants.
     * @param amount maximal count of resources per bag
     * @return a set of bags with all combination of Resources
     */
    private Set<Bag<Resource>> generateHybridBags(int amount) {
        int currentAmount = 0;
        final Set<Bag<Resource>> allBags = new HashSet<>();
        while (currentAmount <= amount) {
            final Bag<Resource> oneBagOfCombination = new ListBag<>();
            oneBagOfCombination.add(Resource.Coal, amount - currentAmount);
            oneBagOfCombination.add(Resource.Oil, currentAmount);
            allBags.add(oneBagOfCombination);
            currentAmount++;
        }
        return allBags;
    }

    // SpotBugs: Medium Confidence Bad Practise because of not implementing equals, which isn´t needed
    @Override
    public int compareTo(Plant other) {
        return this.getNumber() - other.getNumber();
    }
}
