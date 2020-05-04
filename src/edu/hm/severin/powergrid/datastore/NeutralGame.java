package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.*;

import java.util.ArrayList;
import java.util.List;

public class NeutralGame implements Game {

    /**
     * edition of game.
     */
    private Edition edition;

    /**
     * board object of game.
     */
    private Board board;

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
    private List<Player> players;

    /**
     * current level.
     */
    private int level;

    /**
     * plantmarket object of game.
     */
    private PlantMarket plantMarket;

    /**
     * resourcemarket object of game.
     */
    private ResourceMarket resourceMarket;

    /**
     * auction object of game.
     */
    private Auction auction;

    /**
     * current numMoves.
     */
    private int numMoves;

    /**
     * Factory of game.
     */
    private final Factory factory;

    public NeutralGame(final Edition edition, Factory factory) {
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
    public Board getBoard() {
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
    public List<Player> getPlayers() {
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
    public PlantMarket getPlantMarket() {
        return plantMarket;
    }

    @Override
    public ResourceMarket getResourceMarket() {
        return resourceMarket;
    }

    @Override
    public Auction getAuction() {
        return auction;
    }

    @Override
    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    @Override
    public Player findPlayer(String secret) {
        for ( Player player: players) {
            if(player.getSecret() == secret)
                return player;
        }
        return null;
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
    public Factory getFactory() {
        return factory;
    }
}
