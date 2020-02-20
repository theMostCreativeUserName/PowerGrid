package DataStore;

/**
 * Phasen, die ein Spiel durchlaueft.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-09-02
 */
public enum Phase {
    /** Spieler aufnehmen. Das Spiel laeuft noch nicht. */
    Opening,
    /** Beginn einer Runde: Spieler ordnen. */
    PlayerOrder,
    /** Kraftwerke erwerben. */
    PlantBuying,
    /** Ein Kraftwerk versteigern. */
    PlantAuction,
    /** Rohstoffe kaufen. */
    ResourceBuying,
    /** Staedt anschliessen. */
    Building,
    /** Kraftwerke betreiben und Staedte mit Strom versorgen. */
    PlantOperation,
    /** Einkommen erhalten und Rohstoffmarkt auffuellen. */
    Bureaucracy,
    /** Spiel beendet. */
    Terminated
}