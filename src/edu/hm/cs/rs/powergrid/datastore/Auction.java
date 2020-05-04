package edu.hm.cs.rs.powergrid.datastore;

import java.util.List;

/**
 * Eine laufende Kraftwerks-Versteigerung.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-09
 */
public interface Auction {
    /**
     * Spieler, der das bisher hoechste Gebot haelt.
     * @return Spieler. Nicht null.
     */
    Player getPlayer();

    /**
     * Alle verbleibenden Spieler, die noch an dieser Auktion beteiligt sind.
     * Der erste Spieler in der Liste gibt das naechste Gebot ab.
     * Der Hoechstbietende ist immer dabei.
     * @return Spielerliste. Nicht leer (wenigstens ein Element).
     */
    List<Player> getPlayers();

    /**
     * Betrag des momentan hoechsten Gebotes.
     * @return Anzahl Elektro. Nicht negativ.
     */
    int getAmount();

    /**
     * Versteigertes Kraftwerk.
     * @return Kraftwerk. Nicht null.
     */
    Plant getPlant();

    /** Legt einen neuen Hoechstbeitenden fest.
     * @param player Neuer Hoechtsbietender. Nicht null.
     */
    void setPlayer(Player player);

    /** Legt ein neuea Hoechstgebot fest.
     * @param amount Neues Hoechtgetbot. Nicht negativ.
     */
    void setAmount(int amount);
}
