package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlantMarket;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenResourceMarket;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenBoard;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * creates Factory.
 *
 * @author Severin, Pietsch
 * @complexity: 17
 */
public class NeutralFactory implements OpenFactory {

    /**
     * Map of already generated cities.
     */
    private final Map<String, OpenCity> nameOfCity = new HashMap<>();

    /**
     * Map of already generated players.
     */
    private final Map<String, OpenPlayer> colorOfPlayer = new HashMap<>();

    /**
     * Map of already generated plants.
     */
    private final Map<Integer, OpenPlant> numberOfPlant = new HashMap<>();

    /**
     * generated plantmarket.
     */
    private OpenPlantMarket plantMarket;

    /**
     * generated resourcemarket.
     */
    private OpenResourceMarket resourceMarket;

    /**
     * generated board.
     */
    private OpenBoard board;

    /**
     * Map of already generated auctions.
     */
    private final Map<Plant, OpenAuction> plantforAuction = new HashMap<>();

    /**
     * generated game.
     */
    private OpenGame game;

    /**
     * new City.
     *
     * @param name Name. Not Null, not empty.
     * @param area Area. Bigger 0.
     * @return new City
     */
    @Override
    public OpenCity newCity(final String name, final int area) {
        final OpenCity result;
        if (nameOfCity.containsKey(name))
            result = nameOfCity.get(name);
        else {
            result = new NeutralCity(name, area);
            nameOfCity.put(name, result);
        }
        return result;
    }

    /**
     * new Player.
     *
     * @param secret Geheimnis des Spielers. Nicht null.
     * @param color  Farbe des Spielers. Nicht null.
     * @return new Player
     */
    @Override
    public OpenPlayer newPlayer(final String secret, final String color) {
        final OpenPlayer result;
        if (colorOfPlayer.containsKey(color))
            result = colorOfPlayer.get(color);
        else {
            result = new NeutralPlayer(secret, color);
            colorOfPlayer.put(color, result);
        }
        return result;
    }

    /**
     * new Plant.
     *
     * @param number    Nummer des Kraftwerks. Nicht negativ.
     * @param type      Kraftwerkstyp. Nicht null.
     * @param resources Anzahl Rohstoffe, die das Kraftwerk verbraucht.
     *                  Nicht negativ.
     * @param cities    Anzahl Staedte, die das Kraftwerk versorgen kann.
     *                  Echt positiv.
     * @return new Plant
     */
    @Override
    public OpenPlant newPlant(final int number, final Plant.Type type, final int resources, final int cities) {
        final OpenPlant result;
        if (numberOfPlant.containsKey(number))
            return numberOfPlant.get(number);
        else {
            result = new NeutralPlant(number, type, resources, cities);
            numberOfPlant.put(number, result);
        }
        return result;
    }

    /**
     * new PlantMarket.
     *
     * @param edition Ausgabe des Spieles.
     * @return new PlantMarket
     */
    @Override
    public OpenPlantMarket newPlantMarket(final Edition edition) {
        if (plantMarket == null) {
            plantMarket = new NeutralPlantMarket(edition, this);
        }
        return plantMarket;
    }

    /**
     * new ResourceMarket.
     *
     * @param edition Ausgabe des Spieles.
     * @return new ResourceMarket
     */
    @Override
    public OpenResourceMarket newResourceMarket(final Edition edition) {
        if (resourceMarket == null) {
            resourceMarket = new NeutralResourceMarket(edition);
        }
        return resourceMarket;
    }

    /**
     * new Board.
     *
     * @param edition Ausgabe des Spieles.
     * @return new Board
     */
    @Override
    public OpenBoard newBoard(final Edition edition) {
        if (board == null) {
            board = new NeutralBoard(edition);
        }
        return board;
    }

    /**
     * Eine Auktion.
     * Das Hoechstgebot ist gleich der Nummer des Kraftwerkes.
     * Der erste Spieler der Liste ist der Hoechstbietende.
     *
     * @param plant   Kraftwerk, das zum Verkauf steht. Nicht null.
     * @param players Spieler, die an der Auktion teilnehmen. Nicht null, nicht leer.
     *                Die Spieler bieten in der Reihenfolge dieser Liste.
     * @return Auktion. Nicht null.
     */
    @Override
    public OpenAuction newAuction(final OpenPlant plant, final List<OpenPlayer> players) {
        final OpenAuction result;
        if (plantforAuction.containsKey(plant))
            result = plantforAuction.get(plant);
        else {
            result = new NeutralAuction(plant, players);
            plantforAuction.put(plant, result);
        }
        return result;
    }

    /**
     * new Game.
     *
     * @param edition Ausgabe des Spieles.
     * @return new Game
     */
    @Override
    public OpenGame newGame(final Edition edition) {
        if (game == null) {
            game = new NeutralGame(edition, this);
        }
        return game;
    }
}
