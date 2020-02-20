package DataStore;

import java.util.List;
import java.util.Set;

/**
 * Ein Spiel.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-01-03
 */
public interface Game {
    Edition getEdition();

    Board getBoard();

    /**
     * Die Spielstufe.
     * @return Spielstufe als Index 0, 1, 2 fuer Stufen 1, 2, 3.
     */
    int getLevel();

    /**
     * Setzt die Spielstufe fest.
     * @param level Spielstufe als Index 0, 1, 2 fuer Stufen 1, 2, 3.
     */
    void setLevel(int level);

    Phase getPhase();

    /**
     * Legt die Spielphase neu fest.
     * @param phase Phase. Nicht null.
     */
    void setPhase(Phase phase);

    /**
     * Die Spieler.
     * @return Liste der Spieler. Nicht null, aber eventuell leer.
     */
    Set<Player> getPlayers();

    /**
     * Setzt eine Auktion an.
     * @param auction Auktion oder null, wenn es keine gibt.
     */
    void setAuction(Auction auction);

    PlantMarket getPlantMarket();

    ResourceMarket getResourceMarket();

    int getRound();

    void setRound(int round);

    /**
     * Spieler mit einem Geheimnis.
     * Diese Methode antwortet eventuell nicht sofort.
     * @param secret Ein Geheimnis.
     * @return Spieler oder null, wenn es keinen mit dem Geheimnis gibt.
     */
    Player findPlayer(String secret);

    /**
     * Factory, die dieses Spiel erzeugt hat.
     * @return Factory. Nicht null.
     */
    Factory getFactory();
}