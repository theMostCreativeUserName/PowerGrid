package DataStore;
import java.util.Set;

/**
 * Ein Spieler.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-01-14
 */
public interface Player {
    /**
     * Unveraenderliche und eindeutige Farbe des Spielers.
     * Die Farbe spielt fuer den Spielverlauf keine Rolle.
     * @return Farbe. Nicht null.
     */
    String getColor();

    /**
     * Nummer des groessten Kraftwerks, das der Spieler besitzt.
     * @return Kraftwerksnummer.
     * @throws IllegalStateException wenn der Spieler kein Kraftwerk besitzt.
     */
    int largestPlantId();

    /**
     * Staedte, die der Spieler an sein Stromnetz angeschlossen hat.
     * @return Staedte. Nicht null.
     */
    Set<City> getCities();

    /**
     * Vermoegen des Spielers.
     * @return Anzahl Elektro. Nicht negativ.
     */
    int getElectro();

    /**
     * Legt das Vermoegen des Spielers fest.
     * @return Neue Anzahl Elektro. Nicht negativ.
     * @throws IllegalArgumentException wenn die Anzahl negativ ist.
     */
    void setElectro(int electro);

    /**
     * Test, ob der Spieler an der Reihe war.
     * @return true = Spieler war an der Reihe; false ansonsten.
     */
    boolean hasPassed();

    /**
     * Legt fest, ob der Spieler an der Reihe war.
     * @param passed true = Spieler war an der Reihe; false ansonsten.
     */
    void setPassed(boolean passed);

    /**
     * Liefert das Geheimnis des Spielers.
     * Diese Methode liefert beim ersten Aufruf das Geheimnis
     * und ab dem zweiten Aufruf nur noch null.
     * @return Geheimnis oder null.
     */
    String getSecret();

    /**
     * Testet, ob der Spieler dieses Geheimnis hat.
     * @param secret Moegliches Geheimnis.
     * @return true = das moegliche Geheimnis stimmt; false ansonsten.
     */
    boolean hasSecret(String secret);

    /**
     * Kraftwerke, die der Spieler besitzt.
     * @return Kraftwerke. Nicht null.
     */
    Set<Plant> getPlants();

    /**
     * Rohstoffe, die der Spieler in seinen Kraftwerken lagert.
     * @return Rohstoff. Nicht null.
     */
    Bag<Resource> getResources();
}