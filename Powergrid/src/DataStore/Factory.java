package DataStore;
import java.util.List;

/**
 * Produziert neue Bausteine des Spieles.
 * Kapselt die konkreten Implementierungen.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-01-02
 */
public interface Factory {
    static Factory newFactory() {
        //TODO: write code here
        return new NeutralFactory();
    };

    /**
     * Eine Stadt, noch ohne Verbindungen.
     * Erst ist wenigstens ein connect-Aufruf noetig, dann einmal close.
     * @param name Name. Nicht null und nicht leer.
     * @param area Gebiet, in dem diese Stadt liegt. Wenigstens 1.
     */
    City newCity(String name, int area);


    /**
     * Ein Spieler.
     * @param secret Geheimnis des Spielers. Nicht null.
     * @param color  Farbe des Spielers. Nicht null.
     */
    Player newPlayer(String secret, String color);

    /**
     * Ein Kraftwerk.
     * @param id        Kennung des Kraftwerks. Nicht negativ.
     * @param type      Kraftwerkstyp. Nicht null.
     * @param resources Anzahl Rohstoffe, die das Kraftwerk verbraucht. Nicht negativ.
     * @param cities    Anzahl Staedte, die das Kraftwerk versorgen kann. Echt positiv.
     */
    Plant newPlant(int id, Type type, int resources, int cities);

    /**
     * Ein Kraftwerksmarkt.
     * @param edition Ausgabe des Spieles.
     */
    PlantMarket newPlantMarket(Edition edition);

    /**
     * Ein Rohstoffmarkt.
     */
    ResourceMarket newResourceMarket(Edition edition);

    /**
     * Ein Spielplan.
     * Fuegt die Staedte der Edition und ihre Verbidnungen in den Spielplan ein.
     * @param edition Ausgabe des Spieles.
     */
    Board newBoard(Edition edition);

    /**
     * Eine Auktion.
     * @param plant   Kraft, um das es geht. Nicht null.
     * @param players Spieler, die an der Auktion teilnehmen. Nicht null, nicht leer.
     */
    Auction newAuction(Plant plant, List<Player> players);

    /**
     * Ein Spiel, noch ohne Spieler.
     * @param edition Ausgabe des Spieles.
     */
    Game newGame(Edition edition);

}