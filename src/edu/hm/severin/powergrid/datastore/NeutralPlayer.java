package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.Resource;

import java.util.HashSet;
import java.util.Set;

public class NeutralPlayer implements Player {

    /** counts calls for secret*/
    private int secretCalls = 0;
    /** secret of the player. */
    private final String secret;
    /** player color. */
    private final String color;
    /** turn of player.
     * true, if past already
     * false, if not yet passed
     */
    private boolean passed = false;
    /** electro of player. */
    private int electro = 0;
    /** cities the player owns. */
    private Set<City> cities = new HashSet<>();
    /** plants the player owns. */
    private Set<Plant> plants = new HashSet<>();
    /** resources the player stores. */
    private Bag<Resource> resources = null;

    /** A new player. */
    public NeutralPlayer(final String secret, final String color) {
        this.secret = secret;
        this.color = color;
    }

    /** color of player. */
    @Override
    public String getColor() {
        return color;
    }

    /** all cities of player. */
    @Override
    public Set<City> getCities() {
        return cities;
    }

    /** all plants of player. */
    @Override
    public Set<Plant> getPlants() {
        return plants;
    }

    /** all ressources of player. */
    @Override
    public Bag<Resource> getResources() {
        return resources;
    }

    /** electro of player. */
    @Override
    public int getElectro() {
        return electro;
    }

    /** new electro value. */
    @Override
    public void setElectro(int electro) {
        this.electro = electro;
    }

    /**
     * tests if players turn has passed.
     * @return true, if players turn has passed already
     */
    @Override
    public boolean hasPassed() {
        return passed;
    }

    /** players turn status. */
    @Override
    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    /**
     * gives players secret.
     * BUT only once; all consecutive calls return null
     * @return secret or null
     */
    @Override
    public String getSecret() {
        String result = null;
        if (secretCalls == 0) result = secret;
        secretCalls++;

        return result;
    }

    /**
     * tests if player knows the secret.
     * @param secret Ein String.
     * @return true, if secret is correct: else false
     */
    @Override
    public boolean hasSecret(final String secret) {
        boolean knowsSecret = false;
        if (secret.equals(this.secret)) knowsSecret = true;
        return knowsSecret;
    }
}
