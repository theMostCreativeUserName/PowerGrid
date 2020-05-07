package edu.hm.cs.rs.powergrid.datastore.mutable;

import edu.hm.cs.rs.powergrid.datastore.Plant;

/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-02-22
 */
public interface OpenPlant extends Plant, Checksumed {
    void setOperated(boolean operated);
}
