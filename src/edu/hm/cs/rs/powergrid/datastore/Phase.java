package edu.hm.cs.rs.powergrid.datastore;

/**
 * Phasen, die ein Spiel durchlaueft.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-02-08
 */
public enum Phase {
    /** Spieler aufnehmen. Das Spiel laeuft noch nicht. */
    Opening,
    /** Beginn einer Runde: Spieler ordnen. */
    PlayerOrdering,
    /** Kraftwerke erwerben. */
    PlantBuying,
    /** Ein Kraftwerk versteigern. */
    PlantAuction,
    /** Rohstoffe kaufen. */
    ResourceBuying,
    /** Staedte anschliessen. */
    Building,
    /** Kraftwerke betreiben und Staedte mit Strom versorgen. */
    PlantOperation,
    /** Einkommen erhalten und Rohstoffmarkt auffuellen. */
    Bureaucracy,
    /** Spiel beendet. */
    Terminated
}
