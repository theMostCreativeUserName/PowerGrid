package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlantMarket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * creates the plantMarket.
 */
public class NeutralPlantMarket implements OpenPlantMarket {

    /**
     * set of actual plants.
     */
    private final Set<OpenPlant> actual;

    /**
     * set of future plants.
     */
    private final Set<OpenPlant> future;

    /**
     * list of hidden plants, representing the card deck.
     */
    private final List<OpenPlant> hidden;

    /**
     * constructor of NeutralPlantMarket.
     *
     * @param edition edition of the game
     * @param factory factory which is used
     */
    public NeutralPlantMarket(final Edition edition, OpenFactory factory) {
        actual = new HashSet<>();
        future = new HashSet<>();
        hidden = new ArrayList<>();

        final List<String> plantsFromEdition = edition.getPlantSpecifications();
        for (String plantSpecification : plantsFromEdition) {
            final String numberOfPlant = plantSpecification.substring(0, plantSpecification.indexOf(' '));
            final String typeAndConsumption = plantSpecification.substring(plantSpecification.indexOf(' ') + 1, plantSpecification.lastIndexOf(' '));
            final String citySupply = plantSpecification.substring(plantSpecification.lastIndexOf(' ') + 1);

            final int number = Integer.parseInt(numberOfPlant);
            final int consumption = typeAndConsumption.length();
            final int cities = Integer.parseInt(citySupply);

            final Plant.Type typeOfPlant;
            switch (typeAndConsumption.charAt(0)) {
                case 'O' -> typeOfPlant = Plant.Type.Oil;
                case 'C' -> typeOfPlant = Plant.Type.Coal;
                case 'H' -> typeOfPlant = Plant.Type.Hybrid;
                case 'G' -> typeOfPlant = Plant.Type.Garbage;
                case 'F' -> typeOfPlant = Plant.Type.Fusion;
                default -> typeOfPlant = Plant.Type.Eco;
            }
            final OpenPlant plant = factory.newPlant(number, typeOfPlant, consumption, cities);
            hidden.add(plant);
        }
    }

    @Override
    public Set<OpenPlant> getOpenActual() {
        return actual;
    }

    @Override
    public OpenPlant removePlant(int number) {
        final OpenPlant plant = findPlant(number);
        if (actual.contains(plant))
            actual.remove(plant);
        if (future.contains(plant))
            future.remove(plant);
        if (hidden.contains(plant))
            hidden.remove(plant);

        return plant;
    }

    @Override
    public Set<OpenPlant> getOpenFuture() {
        return future;
    }

    @Override
    public int getNumberHidden() {
        return hidden.size();
    }

    @Override
    public OpenPlant findPlant(int number) {
        final Set<OpenPlant> allPlantSets = new HashSet<>();
        OpenPlant result = null;
        allPlantSets.addAll(actual);
        allPlantSets.addAll(future);
        allPlantSets.addAll(hidden);
        for (OpenPlant plant : allPlantSets) {
            if (plant.getNumber() == number) {
                result = plant;
            }
        }
        return result;
    }

    @Override
    public List<OpenPlant> getOpenHidden() {
        return hidden;
    }

}
