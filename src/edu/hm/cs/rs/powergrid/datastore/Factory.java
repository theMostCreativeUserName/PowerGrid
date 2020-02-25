package edu.hm.cs.rs.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import java.util.List;

/**
 * Produziert neue Bausteine des Spieles.
 * Kapselt die konkreten Implementierungen.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-02-19
 */
public interface Factory {
    /**
     * Liefert eine neue Factory,
     * deren FQCN die Systemproperty powergrid.factory definiert.
     * Wenn diese Systemproperty undefiniert ist,
     * gilt die Umgebungsvariable POWERGRID_FACTORYTYPE.
     * @return neue Factory.
     */
    static Factory newFactory() {
        return newFactory(System.getProperty("powergrid.factory",
                System.getenv("POWERGRID_FACTORY")));
    }

    /**
     * Liefert eine neue Factory des gegebenen Typs.
     * @param fqcn Fully qualified Classname der konkreten Factoryklasse.
     * @return neue Factory.
     */
    static Factory newFactory(String fqcn) {
        try {
            return (Factory)Class.forName(fqcn)
                    .getConstructor()
                    .newInstance();
        }
        catch(ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

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
     * @param number    Nummer des Kraftwerks. Nicht negativ.
     * @param type      Kraftwerkstyp. Nicht null.
     * @param resources Anzahl Rohstoffe, die das Kraftwerk verbraucht. Nicht negativ.
     * @param cities    Anzahl Staedte, die das Kraftwerk versorgen kann. Echt positiv.
     */
    Plant newPlant(int number, Plant.Type type, int resources, int cities);

    /**
     * Ein Kraftwerksmarkt.
     * @param edition Ausgabe des Spieles.
     * @return Kraftwerksmarkt. Nicht null.
     */
    PlantMarket newPlantMarket(Edition edition);

    /**
     * Ein Rohstoffmarkt.
     * @param edition Ausgabe des Spieles.
     * @return Rohstoffmarkt. Nicht null.
     */
    ResourceMarket newResourceMarket(Edition edition);

    /**
     * Ein Spielplan.
     * Fuegt die Staedte der Edition und ihre Verbidnungen in den Spielplan ein.
     * @param edition Ausgabe des Spieles.
     * @return Spielplan.
     */
    Board newBoard(Edition edition);

    /**
     * Eine Auktion.
     * @param plant   Kraft, um das es geht. Nicht null.
     * @param players Spieler, die an der Auktion teilnehmen. Nicht null, nicht leer.
     * @return Auktion. Nicht null.
     */
    Auction newAuction(Plant plant, List<Player> players);

    /**
     * Ein Spiel, noch ohne Spieler.
     * @param edition Ausgabe des Spieles.
     * @return Spiel. Nicht null.
     */
    Game newGame(Edition edition);

}