package DataStore;

import java.util.List;
import java.util.Objects;

/**
 * creates neutral cities/ boards / players /Plants / markets / auctions / games
 */
public class NeutralFactory implements Factory {


    @Override
    public City newCity(String name, int area) {
        Objects.requireNonNull(name);
        if (name == "") throw new NullPointerException("cities name has to exist ");
        if (area < 1) throw new IllegalArgumentException(" area identifyer is bigger than 0");
        return newCity(name, area);
    }

    @Override
    public Player newPlayer(String secret, String color) {
        return null;
    }

    @Override
    public Plant newPlant(int id, Type type, int resources, int cities) {
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
        return newBoard(edition);
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
