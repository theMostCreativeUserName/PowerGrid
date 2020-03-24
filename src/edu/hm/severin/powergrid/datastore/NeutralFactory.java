package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.*;


public class NeutralFactory implements Factory {
    /**
     * new City.
     * @param name Name. Not Null, not empty.
     * @param area Area. Bigger 0.
     * @return new City
     */
    @Override
    public City newCity(final String name,final int area) {
        return new NeutralCity(name, area);
    }

    /**
     * new Player.
     * @param secret Geheimnis des Spielers. Nicht null.
     * @param color  Farbe des Spielers. Nicht null.
     * @return
     */
    @Override
    public Player newPlayer(final String secret, final String color) {
        return new NeutralPlayer(secret, color);
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
        return new NeutralPlant(number,type,resources,cities);
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
    @Override
    public Board newBoard(final Edition edition) {
        return new NeutralBoard(edition);
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
