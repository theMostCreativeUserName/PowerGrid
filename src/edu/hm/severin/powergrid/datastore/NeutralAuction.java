package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.datastore.Auction;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;

import java.util.List;
/**
 * creates Auction.
 * @author Pietsch
 * @complexity: 8
 */

class NeutralAuction implements Auction {

    /**
     * Highest Bidder.
     */
    private Player player;

    /**
     * List of participating players.
     */
    private final List<Player> players;

    /**
     * current maximum.
     */
    private int amount;

    /**
     * Plant of auction.
     */
    private final Plant plant;

    NeutralAuction( Plant plant, List<Player> players) {
        this.plant = plant;
        this.players = players;
        this.amount = plant.getNumber();
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public List<Player> getPlayers() {
        return this.players;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public Plant getPlant() {
        return this.plant;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
