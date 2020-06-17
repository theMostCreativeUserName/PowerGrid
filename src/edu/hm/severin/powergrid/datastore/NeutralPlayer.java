package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.severin.powergrid.ListBag;

import java.util.HashSet;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;

/**
 * creates player.
 *
 * @author Severin, Pietsch
 */
// PMD declares this as a pure Dataclass, this tough can't be changed
public class NeutralPlayer implements OpenPlayer {

    /**
     * counts calls for secret.
     */
    private int secretCalls;
    /**
     * secret of the player.
     */
    private final String secret;
    /**
     * player color.
     */
    private final String color;
    /**
     * turn of player.
     * true, if past already
     * false, if not yet passed
     */
    private boolean passed;
    /**
     * electro of player.
     */
    private int electro;
    /**
     * cities the player owns.
     */
    private final Set<OpenCity> cities = new HashSet<>();
    /**
     * plants the player owns.
     */
    private final Set<OpenPlant> plants = new HashSet<>();
    /**
     * resources the player stores.
     */
    private final Bag<Resource> resources = new ListBag<>();

    /**
     * A new player.
     * @param playerSecret secret of player
     * @param color color of player
     */
    public NeutralPlayer(final String playerSecret, final String color) {
        Objects.requireNonNull(playerSecret);
        Objects.requireNonNull(color);
        electro = 0;
        passed = false;
        secretCalls = 0;
        this.secret = playerSecret;
        this.color = color;
    }

    /**
     * color of player.
     */
    @Override
    public String getColor() {
        return color;
    }

    /**
     * all cities of player.
     */
    @Override
    public Set<OpenCity> getOpenCities() {
        return cities;
    }

    /**
     * all plants of player.
     */
    @Override
    public Set<OpenPlant> getOpenPlants() {
        return plants;
    }

    /**
     * all resources of player.
     */
    @Override
    public Bag<Resource> getOpenResources() {
        return resources;
    }

    /**
     * electro of player.
     */
    @Override
    public int getElectro() {
        return electro;
    }

    /**
     * new electro value.
     */
    @Override
    public void setElectro(int electro) {
        if (electro < 0) throw new IllegalArgumentException();
        this.electro = electro;
    }

    /**
     * tests if players turn has passed.
     *
     * @return true, if players turn has passed already
     */
    @Override
    public boolean hasPassed() {
        return passed;
    }

    /**
     * players turn status.
     */
    @Override
    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    /**
     * gives players secret.
     * BUT only once; all consecutive calls return null
     *
     * @return secret or null
     */
    @Override
    public String getSecret() {
        String result = null;
        if (secretCalls <= 0) result = secret;
        secretCalls++;

        return result;
    }

    /**
     * tests if player knows the secret.
     *
     * @param secrets Ein String.
     * @return true, if secret is correct: else false
     */
    @Override
    public boolean hasSecret(final String secrets) {
        boolean knowsSecret = false;
        if (this.secret.equals(secrets))
            knowsSecret = true;
        return knowsSecret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColor(), getElectro());
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || getClass() != obj.getClass()) return false;
        final NeutralPlayer that = (NeutralPlayer) obj;
        return getColor().equals(that.getColor());
    }

    /**
     * search for the plant with the highest number in a player.
     *
     * @param player Player where highest plant should be found
     * @return number of highest plant
     */
    private int biggestPlantOfPlayer(Player player) {
        final OptionalInt numberOfBiggestPlant = player.getPlants().stream().mapToInt(Plant::getNumber).max();
        if (numberOfBiggestPlant.isEmpty())
            return 0;
        return numberOfBiggestPlant.getAsInt();
    }

    /**
     * new compareTo, compare Players. 1) Connected Cities (des), 2) highest number of plant (des) 3) Color alphabetic.
     *
     * @param other Other Player to compare with
     * @return CompareValue
     */
    @Override
    public int compareTo(Player other) {
        int result;
        result = other.getCities().size() - this.getCities().size();
        if (result == 0)
            result = biggestPlantOfPlayer(other) - biggestPlantOfPlayer(this);
        if (result == 0)
            result = this.getColor().compareTo(other.getColor());
        return result;
    }
}
