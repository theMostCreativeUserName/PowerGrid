package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.*;

import java.util.HashMap;
import java.util.Map;

/**
 * creates Factory.
 * @author Severin,Pietsch
 * @complexity: 12
 */
public class NeutralFactory implements Factory {
    /**
     * new City.
     * @param name Name. Not Null, not empty.
     * @param area Area. Bigger 0.
     * @return new City
     */
    private Map<String, City> NameOfCity = new HashMap<>();
    @Override
    public City newCity(final String name, final int area) {
        final City result;
        if(NameOfCity.containsKey(name))
            result = NameOfCity.get(name);
        else {
            result = new NeutralCity(name, area);
            NameOfCity.put(name, result);
        }
        return result;
    }

    /**
     * new Player.
     * @param secret Geheimnis des Spielers. Nicht null.
     * @param color  Farbe des Spielers. Nicht null.
     * @return
     */
    private Map<String, Player> ColorOfPlayer = new HashMap<>();
    @Override
    public Player newPlayer(final String secret, final String color) {
        final Player result;
        if(ColorOfPlayer.containsKey(color))
            result = ColorOfPlayer.get(color);
        else {
            result = new NeutralPlayer(secret, color);
            ColorOfPlayer.put(color, result);
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
    private Map<Integer, Plant> NumberOfPlant = new HashMap<>();
    @Override
    public Plant newPlant(final int number, final Plant.Type type, final int resources, final int cities) {
        final Plant result;
        if(NumberOfPlant.containsKey(number))
            return NumberOfPlant.get(number);
        else {
            result = new NeutralPlant(number, type, resources, cities);
            NumberOfPlant.put(number, result);
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
        return null;
    }

    /**
     * new ResourceMarket.
     * @param edition Ausgabe des Spieles.
     * @return new ResourceMarket
     */
    @Override
    public ResourceMarket newResourceMarket(final Edition edition) {
        return null;
    }

    /**
     * new Board.
     * @param edition Ausgabe des Spieles.
     * @return new Board
     */
    private Map<Edition, Board> EditionOfBoard = new HashMap<>();
    @Override
    public Board newBoard(final Edition edition) {
        final Board result;
        if(EditionOfBoard.containsKey(edition))
            result = EditionOfBoard.get(edition);
        else {
            result = new NeutralBoard(edition);
            EditionOfBoard.put(edition, result);
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
        return null;
    }
}
