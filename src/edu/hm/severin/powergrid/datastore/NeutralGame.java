package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenBoard;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlantMarket;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenResourceMarket;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;


import java.util.ArrayList;
import java.util.List;

/**
 * creates a game.
 */

public class NeutralGame implements OpenGame {

    /**
     * edition of game.
     */
    private final Edition edition;

    /**
     * board object of game.
     */
    private final OpenBoard board;

    /**
     * current round.
     */
    private int round;

    /**
     * current phase.
     */
    private Phase phase;

    /**
     * lsit of all player.
     */
    private final List<OpenPlayer> players;

    /**
     * current level.
     */
    private int level;

    /**
     * plantmarket object of game.
     */
    private final OpenPlantMarket plantMarket;

    /**
     * resourcemarket object of game.
     */
    private final OpenResourceMarket resourceMarket;

    /**
     * auction object of game.
     */
    private OpenAuction auction;

    /**
     * current numMoves.
     */
    private int numMoves;

    /**
     * Factory of game.
     */
    private final OpenFactory factory;

    /**
     * creates a NeutralGame.
     *
     * @param edition edition of game
     * @param factory factory of game
     */
    public NeutralGame(final Edition edition, OpenFactory factory) {
        this.edition = edition;

        this.factory = factory;

        board = factory.newBoard(this.edition);
        plantMarket = factory.newPlantMarket(this.edition);
        resourceMarket = factory.newResourceMarket(this.edition);

        round = 0;
        level = 0;
        numMoves = 0;
        phase = Phase.Opening;
        players = new ArrayList<>();
    }

    @Override
    public Edition getEdition() {
        return edition;
    }

    @Override
    public OpenBoard getBoard() {
        return board;
    }

    @Override
    public int getRound() {
        return round;
    }

    @Override
    public void setRound(int round) {
        this.round = round;
    }

    @Override
    public Phase getPhase() {
        return phase;
    }

    @Override
    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    @Override
    public List<OpenPlayer> getOpenPlayers() {
        return players;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public OpenPlantMarket getPlantMarket() {
        return plantMarket;
    }

    @Override
    public OpenResourceMarket getResourceMarket() {
        return resourceMarket;
    }

    @Override
    public OpenAuction getAuction() {
        return auction;
    }

    @Override
    public void setAuction(OpenAuction auction) {
        this.auction = auction;
    }

    @Override
    public OpenPlayer findPlayer(String secret) {
        OpenPlayer result = null;
        for (OpenPlayer player : players)
            if (player.hasSecret(secret))
                result = player;

        return result;
    }

    @Override
    public int getNumMoves() {
        return numMoves;
    }

    @Override
    public void setNumMoves(int numMoves) {
        this.numMoves = numMoves;
    }

    @Override
    public OpenFactory getFactory() {
        return factory;
    }
}
