package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenResourceMarket;
import edu.hm.severin.powergrid.ListBag;

import java.util.List;
import java.util.Map;

/**
 * creates ResourceMarket.
 *
 * @author Pietsch
 */
class NeutralResourceMarket implements OpenResourceMarket {

    /**
     * bag of available resources.
     */
    private Bag<Resource> available;

    /**
     * bag of remaining supply resources.
     */
    private final Bag<Resource> supply;

    /**
     * edition of resourceMarket.
     */
    private final Edition edition;

    /**
     * constructor of NeutralResourceMarket.
     * @param edition edition of the game
     */
    NeutralResourceMarket(final Edition edition) {
        this.edition = edition;
        available = new ListBag<>();
        final Map<Resource, Integer> availableStart = edition.getResourcesInitiallyAvailable();

        for (Map.Entry<Resource, Integer> resource: availableStart.entrySet()) {
            available = available.add(resource.getKey(), resource.getValue());
        }

        supply = new ListBag<>();
        final Map<Resource, Integer> supplyStart = edition.getResourceToNumber();
        for (Map.Entry<Resource, Integer> resource : supplyStart.entrySet()) {
            final int remainingSupply = resource.getValue() - availableStart.get(resource.getKey());
            for (int currentAmountOfResources = 0; currentAmountOfResources < remainingSupply; currentAmountOfResources++)
                supply.add(resource.getKey());
        }
    }

    @Override
    public Bag<Resource> getOpenAvailable() {
        return available;
    }

    @Override
    public Bag<Resource> getOpenSupply() {
        return supply;
    }

    @Override
    public int getPrice(Resource resource) {
        final Map<Resource, List<Integer>> costForResource = edition.getResourceAvailableToCost();
        final int amount = available.count(resource);
        final int cost;
        if (amount > 0)
            cost = costForResource.get(resource).get(amount - 1);
        else
            throw new IllegalArgumentException();
        return cost;
    }
}
