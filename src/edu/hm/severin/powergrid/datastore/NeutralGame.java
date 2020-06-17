package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenBoard;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlantMarket;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenResourceMarket;


import java.util.ArrayList;
import java.util.List;

/**
 * Implements class Game.
 */

// PMD has problems, we noticed it and ignored it - the second time
// Checkstyle has a problem too, but we need 11 variables instead of 10 or you have an broken code
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
     * List of all player.
     */
    private final List<OpenPlayer> players;

    /**
     * current level.
     */
    private int level;

    /**
     * PlantMarket object of game.
     */
    private final OpenPlantMarket plantMarket;

    /**
     * ResourceMarket object of game.
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
     * Constructor of game.
     * @param edition used edition
     * @param factory used factory
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
        if (round < 0)
            throw new IllegalArgumentException();
        this.round = round;
    }

    @Override
    public Phase getPhase() {
        return phase;
    }

    @Override
    public void setPhase(Phase phase) {
        if (phase == null)
            throw new IllegalArgumentException();
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
        if (level < 0)
            throw new IllegalArgumentException();
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
        return players.stream().filter(player -> player.hasSecret(secret)).findFirst().orElse(null);
    }

    @Override
    public int getNumMoves() {
        return numMoves;
    }

    @Override
    public void setNumMoves(int numMoves) {
        if (numMoves < 0)
            throw new IllegalArgumentException();
        this.numMoves = numMoves;
    }

    @Override
    public OpenFactory getFactory() {
        return factory;
    }
}
