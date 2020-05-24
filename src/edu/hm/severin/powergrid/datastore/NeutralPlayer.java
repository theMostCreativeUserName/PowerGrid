package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.severin.powergrid.ListBag;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * creates player.
 *
 * @author Severin, Pietsch
 * @complexity: 25
 */

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
     * money of player.
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
     *
     * @param color  color of player
     * @param secret color of secret
     */
    public NeutralPlayer(final String secret, final String color) {

        electro = 0;
        passed = false;
        secretCalls = 0;
        this.secret = secret;
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
     * money of player.
     */
    @Override
    public int getElectro() {
        return electro;
    }

    /**
     * new money value.
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
     * @param theSecret Ein String.
     * @return true, if secret is correct: else false
     */
    @Override
    public boolean hasSecret(final String theSecret) {
        boolean knowsSecret = false;
        if (theSecret.equals(this.secret)) knowsSecret = true;
        return knowsSecret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColor(), getElectro());
    }

    @Override
    public boolean equals(Object obj) {
        //if (this == obj) return true;
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

        final Plant elsePlant = new NeutralPlant(1, Plant.Type.Coal, 1, 1);
        return player.getPlants().stream().max(Comparator.comparingInt(Plant::getNumber)).orElse(elsePlant).getNumber();
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
