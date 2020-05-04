package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * creates Factory.
 * @author Severin,Pietsch
 * @complexity: 17
 */
public class NeutralFactory implements Factory {

    /**
     * Map of already generated cities.
     */
    private Map<String, City> nameOfCity = new HashMap<>();

    /**
     * Map of already generated players.
     */
    private Map<String, Player> colorOfPlayer = new HashMap<>();

    /**
     * Map of already generated plants.
     */
    private Map<Integer, Plant> numberOfPlant = new HashMap<>();

    /**
     * generated plantmarket.
     */
    private PlantMarket plantMarket;

    /**
     * generated resourcemarket.
     */
    private ResourceMarket resourceMarket;

    /**
     * generated board.
     */
    private Board board;

    /**
     * Map of already generated auctions.
     */
    private Map<Plant, Auction> plantforAuction = new HashMap<>();

    /**
     * generated game.
     */
    private Game game;

    /**
     * new City.
     * @param name Name. Not Null, not empty.
     * @param area Area. Bigger 0.
     * @return new City
     */
    @Override
    public City newCity(final String name, final int area) {
        final City result;
        if(nameOfCity.containsKey(name))
            result = nameOfCity.get(name);
        else {
            result = new NeutralCity(name, area);
            nameOfCity.put(name, result);
        }
        return result;
    }

    /**
     * new Player.
     * @param secret Geheimnis des Spielers. Nicht null.
     * @param color  Farbe des Spielers. Nicht null.
     * @return new Player
     */
    @Override
    public Player newPlayer(final String secret, final String color) {
        final Player result;
        if(colorOfPlayer.containsKey(color))
            result = colorOfPlayer.get(color);
        else {
            result = new NeutralPlayer(secret, color);
            colorOfPlayer.put(color, result);
        }
        return result;
    }

    /**
     * new Plant.
     * @param number    Nummer des Kraftwerks. Nicht negativ.
     * @param type      Kraftwerkstyp. Nicht null.
     * @param resources Anzahl Rohstoffe, die das Kraftwerk verbraucht.
     *                  Nicht negativ.
     * @param cities    Anzahl Staedte, die das Kraftwerk versorgen kann.
     *                  Echt positiv.
     * @return new Plant
     */
    @Override
    public Plant newPlant(final int number, final Plant.Type type, final int resources, final int cities) {
        final Plant result;
        if(numberOfPlant.containsKey(number))
            return numberOfPlant.get(number);
        else {
            result = new NeutralPlant(number, type, resources, cities);
            numberOfPlant.put(number, result);
        }
        return result;
    }

    /**
     * new PlantMarket.
     * @param edition Ausgabe des Spieles.
     * @return new PlantMarket
     */
    @Override
    public PlantMarket newPlantMarket(final Edition edition) {
        if (plantMarket == null) {
            plantMarket = new NeutralPlantMarket(edition, this);
        }
        return plantMarket;
    }

    /**
     * new ResourceMarket.
     * @param edition Ausgabe des Spieles.
     * @return new ResourceMarket
     */
    @Override
    public ResourceMarket newResourceMarket(final Edition edition) {
        if (resourceMarket == null){
            resourceMarket = new NeutralResourceMarket(edition);
        }
        return resourceMarket;
    }

    /**
     * new Board.
     * @param edition Ausgabe des Spieles.
     * @return new Board
     */
    @Override
    public Board newBoard(final Edition edition) {
        if (board == null) {
            board = new NeutralBoard(edition);
        }
        return board;
    }
    /**
     * Eine Auktion.
     * Das Hoechstgebot ist gleich der Nummer des Kraftwerkes.
     * Der erste Spieler der Liste ist der Hoechstbietende.
     * @param plant   Kraftwerk, das zum Verkauf steht. Nicht null.
     * @param players Spieler, die an der Auktion teilnehmen. Nicht null, nicht leer.
     *                Die Spieler bieten in der Reihenfolge dieser Liste.
     * @return Auktion. Nicht null.
     */
    @Override
    public Auction newAuction(final Plant plant, final List<Player> players) {
        final Auction result;
        if(plantforAuction.containsKey(plant))
            result = plantforAuction.get(plant);
        else {
            result = new NeutralAuction(plant, players);
            plantforAuction.put(plant, result);
        }
        return result;
    }

    /**
     * new Game.
     * @param edition Ausgabe des Spieles.
     * @return new Game
     */
    @Override
    public Game newGame(final Edition edition) {
        if (game == null) {
            game = new NeutralGame(edition, this);
        }
        return game;
    }
}
