package edu.hm.cs.rs.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import java.util.List;

/**
 * Ein Spiel.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-10
 */
public interface Game {
    /**
     * Ausgabe des Spieles.
     * @return Ausgabe. nicht null.
     */
    Edition getEdition();

    Board getBoard();

    /**
     * Rundennummer.
     * Zaehlt am Beginn jeder Runde hoch.
     * @return Runde, in der das Spiel gerade ist. Bei Spielbeginn 0. Nicht negativ.
     */
    int getRound();

    /**
     * Legt eine neue Rundennummer fest.
     * @param round Rundennummer. Nicht negativ.
     */
    void setRound(int round);

    /**
     * Spielphase.
     * @return Phase. Nicht null.
     */
    Phase getPhase();

    /**
     * Legt die Spielphase fest.
     * @param phase Neu Spielphase. Nicht null.
     */
    void setPhase(Phase phase);

    /**
     * Liste der Spieler. mit dem besten zuerst.
     * Geordnet nach fallender Anzahl angeschlossener Staedte und bei gleicher Anzahl
     * nach fallender Nummer des groessten Kraftwerks.
     * @return Spieler. Nicht null.
     */
    List<Player> getPlayers();

    /**
     * Index der Spielstufe.
     * @return Stufe, 0-based (0 = Stufe 1, 1 = Stufe 2, ...). Nicht negativ.
     */
    int getLevel();

    void setLevel(int level);

    PlantMarket getPlantMarket();

    ResourceMarket getResourceMarket();

    Auction getAuction();

    /**
     * Setzt eine Auktion fest.
     * @param auction Auktion oder null, wenn gerade keine laeuft.
     */
    void setAuction(Auction auction);

    /**
     * Sucht einen Spieler mit dem gegebenen Geheimnis.
     * Diese Methode kehrt eventuell nicht sofort zurueck, sondern braucht eine Weile.
     * @param secret Ein String.
     *               Kann null sein. Dann liefert die Methode immer null.
     * @return Spieler mit diesem Geheimnis oder null, wenn es keinen gibt.
     */
    Player findPlayer(String secret);

    /**
     * Anzahl der bisher ausgefuehrten Spielzuege.
     * @return Anzahl Zuege. Bei Spielbeginn 0. Nicht negativ.
     */
    int getNumMoves();

    void setNumMoves(int numMoves);

    /**
     * Factory, die dieses Spiel erzeugt hat.
     * @return Factory. Nicht null.
     */
    Factory getFactory();

}
