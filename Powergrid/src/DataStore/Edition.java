package DataStore;

import java.util.List;
import java.util.Map;

/**
 * Ausgabe des Spieles.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-01-08
 */
public interface Edition {
    /** Mindestanzahl Spieler. */
    int PLAYERS_MINIMUM = 2;

    /** Hoechstanzahl Spieler. */
    int PLAYERS_MAXIMUM = 6;

    /** Startkapital bei Spielbeginn. */
    int INITIAL_ELECTRO = 50;

    /** Anzahl Kraftwerke im aktuellen und zukuenftigen Markt zusammen in Stufen 1, 2. */
    int PLANTS_LEVEL_1_2 = 8;

    /** Anzahl Kraftwerke im aktuellen Markt in Stufe 3. */
    int PLANTS_LEVEL_3 = 6;

    /**
     * Anzahl Rohstoffe pro Art, die insgesamt im Spiel sind.
     * Rohstoffe koennen in einem Kraftwerk lagern,
     * im Markt verfuegbar oder momentan nicht verfuegbar sein.
     */
    Map<Resource, Integer> getResourceToNumber();

    /** Kosten des billigsten Rohstoffs im Markt, abhaengig von der Anzahl verfuegbarer Rohstoffe. */
    Map<Resource, List<Integer>> getResourceAvailableToCost();

    /** Anzahl Rohstoffe pro Art, die beim Spielstart im Markt verfuegbar sind. */
    Map<Resource, Integer> getResourcesInitiallyAvailable();

    /**
     * Kosten einer des Anschlusses einer Stadt, abhaengig von der Stufe.
     * Die Verbindungskosten kommen noch dazu.
     */
    List<Integer> levelToCityCost();

    /**
     * Anzahl neuer Rohstoffe am Rundenende,
     * abhaengig von der Anzahl Spieler (erster Index) und von der Stufe (zweiter Index).
     */
    Map<Resource, List<List<Integer>>> getResourcePlayersToSupply();

    /**
     * Einkommen in Elektro mit Index = Anzahl betriebener Kw.
     * Das erste Element (Index 0) ist das Grundeinkommen fuer Spieler ohne Kraftwerk.
     */
    List<Integer> getOperatedPlantsIncome();

    /**
     * Farben der Spieler.
     * Spielt nur fuer UIs eine Rolle.
     */
    List<String> getPlayerColors();

    /**
     * Anzahl Kraftwerke im Spiel.
     * Index = Spieleranzahl.
     */
    List<Integer> getPlayersPlantsInitiallyRemoved();

    /**
     * Maximale Anzahl Kraftwerke, die ein Spieler besitzen darf mit Index = Spieleranzahl.
     * Index = Spieleranzahl.
     */
    List<Integer> getPlayersPlantsLimit();

    /**
     * Anzahl angeschlossener Staedte eines Spielers, die Level 2 ausloesen.
     * Index = Spieleranzahl.
     */
    List<Integer> getPlayersLevel2Cities();

    /**
     * Anzahl angeschlossener Staedte eines Spielers, die das Spielende ausloesen.
     * Index = Spieleranzahl.
     */
    List<Integer> getPlayersEndgameCities();

    /**
     * Spezifikationen der Kraftwerke.
     * Jeder String hat den Aufbau "Nummer Typ Staedte".
     * Die Nummer sind eindeutig, positiv und nicht fortlaufend.
     * Der Typ besteht aus lauter gleichen Buchstaben C, O, G, U, E, F, H.
     * Die Laenge entspricht der verbrauchten Anzahl Rohstoffe.
     * Bei E und F ist Anzahl ohne Bedeutung; sie brauchen keine Rohstoffe.
     * H steht fuer Kohle oder Oel.
     *
     * Beispiele: "4 CC 1", "21 HH 4", "50 F 6".
     */
    List<String> getPlantSpecifications();

    /**
     * Spezifikationen der Staedt und Verbindungen.
     * Jeder String hat den Aufbau "Name Gebiet (Name Kosten)+".
     * Der erste Name ist der Name einer Stadt (nicht null, nicht leer, eindeutig).
     * Das Gebiet ist eine Nummer (fortlaufend ab 1).
     * Die Paare "Name Kosten" stehen fuer eine Verbindung zu einer anderen Stadt mit den
     * gegebenen Kosten.
     *
     * Verbindungen sind nur einmal spezifiziert, verlaufen aber immer in beiden Richtungen.
     *
     * Beispiel "Cuxhaven 1 Bremen 8 Hamburg 11":
     * Cuxhafen liegt in Region 1.
     * Die Verbindung zwischen Cuxhaven und Bremen kostet 8, zwischen Cuxhaven und Hamburg 11.
     */
    List<String> getCitySpecifications();

    default boolean assertValid() {
        assert getResourceToNumber().entrySet()
                .stream()
                .mapToInt(Map.Entry::getValue)
                .allMatch(n -> n >= 0): "total resources not negative";
        assert getResourceAvailableToCost().entrySet()
                .stream()
                .allMatch(entry -> getResourceToNumber().get(entry.getKey())
                        == entry.getValue().size()): "price functions cover all existing resources";
        assert getResourcesInitiallyAvailable().entrySet()
                .stream()
                .allMatch(entry -> entry.getValue() >= 0): "resources initially availabe not negative";
        assert getResourcesInitiallyAvailable().entrySet()
                .stream()
                .allMatch(entry -> entry.getValue()
                        <= getResourceToNumber().get(entry.getKey())): "resources initially available exist";
        return true;
    }
}