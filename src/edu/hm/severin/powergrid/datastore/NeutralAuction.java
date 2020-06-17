package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;

import java.util.List;

/**
 * creates Auction.
 *
 * @author Pietsch
 */

// PMD has problems, we noticed it and ignored it
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

    /**
     * constructor of NeutralAuction.
     *
     * @param plant   plant for auction
     * @param players all players participating
     */
    NeutralAuction(OpenPlant plant, List<OpenPlayer> players) {
        if (players == null)
            throw new IllegalArgumentException();
        if (players.isEmpty())
            throw new IllegalArgumentException();
        this.player = players.get(0);
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
        if (player == null)
            throw new IllegalArgumentException();
        this.player = player;
    }

    @Override
    public void setAmount(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException();
        this.amount = amount;
    }
}
