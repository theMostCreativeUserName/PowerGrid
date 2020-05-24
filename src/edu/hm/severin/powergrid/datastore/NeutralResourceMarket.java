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
 * @author Pietsch
 * @complexity: 9
 */
class NeutralResourceMarket implements OpenResourceMarket {

    /**
     * bag of available resources.
     */
    private final Bag<Resource> available;

    /**
     * bag of remaining supply resources.
     */
    private final Bag<Resource> supply;

    /**
     * edition of game/resource market.
     */
    private final Edition edition;

    NeutralResourceMarket(final Edition edition) {
        this.edition = edition;
        available = new ListBag<>();
        final Map<Resource, Integer> availableStart = edition.getResourcesInitiallyAvailable();

        for ( Resource resource: availableStart.keySet()) {
            for ( int index=0; index < availableStart.get(resource); index++)
                available.add(resource);
        }

        supply = new ListBag<>();
        final Map<Resource, Integer> supplyStart = edition.getResourceToNumber();
        for ( Resource resource: supplyStart.keySet()) {
            final int remainingSupply = supplyStart.get(resource) - availableStart.get(resource);
            for (int index=0; index < remainingSupply; index++)
                supply.add(resource);
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
        return costForResource.get(resource).get(amount-1);
    }
}
