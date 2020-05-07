package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;

import java.util.List;
/**
 * creates Auction.
 * @author Pietsch
 * @complexity: 8
 */

class NeutralAuction implements OpenAuction {

    /**
     * Highest Bidder.
     */
    private OpenPlayer player;

    /**
     * List of participating players.
     */
    private final List<OpenPlayer> players;

    /**
     * current maximum.
     */
    private int amount;

    /**
     * Plant of auction.
     */
    private final OpenPlant plant;

    NeutralAuction( OpenPlant plant, List<OpenPlayer> players) {
        this.plant = plant;
        this.players = players;
        this.amount = plant.getNumber();
    }

    @Override
    public OpenPlayer getPlayer() {
        return this.player;
    }

    @Override
    public List<OpenPlayer> getOpenPlayers() {
        return this.players;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public OpenPlant getPlant() {
        return this.plant;
    }

    @Override
    public void setPlayer(OpenPlayer player) {
        this.player = player;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
