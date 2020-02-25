package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.*;

import java.util.List;

public class NeutralFactory implements Factory {
    @Override
    public City newCity(String name, int area) {
        return new NeutralCity(name, area);
    }

    @Override
    public Player newPlayer(String secret, String color) {
        return null;
    }

    @Override
    public Plant newPlant(int number, Plant.Type type, int resources, int cities) {
        return null;
    }

    @Override
    public PlantMarket newPlantMarket(Edition edition) {
        return null;
    }

    @Override
    public ResourceMarket newResourceMarket(Edition edition) {
        return null;
    }

    @Override
    public Board newBoard(Edition edition) {
        return new NeutralBoard(edition);
    }

    @Override
    public Auction newAuction(Plant plant, List<Player> players) {
        return null;
    }

    @Override
    public Game newGame(Edition edition) {
        return null;
    }
}
