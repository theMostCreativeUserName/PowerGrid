package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Factory;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.PlantMarket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NeutralPlantMarket implements PlantMarket {

    /**
     * set of actual plants.
     */
    private Set<Plant> actual;

    /**
     * set of future plants.
     */
    private Set<Plant> future;

    /**
     * list of hidden plants.
     */
    private List<Plant> hidden;

    /**
     * factory of plantmarket.
     */
    private final Factory factory;

    public NeutralPlantMarket(final Edition edition, Factory factory) {
        this.factory = factory;

        actual = new HashSet<Plant>();
        future = new HashSet<Plant>();
        hidden = new ArrayList<Plant>();

        List <String> plantsFromEdition = edition.getPlantSpecifications();
        for (String plantspecification : plantsFromEdition) {
            final String numberOfPlant = plantspecification.substring(0, plantspecification.indexOf(' '));
            final String typeAndConsumption = plantspecification.substring(plantspecification.indexOf(' ') + 1, plantspecification.lastIndexOf(' '));
            final String citySupply = plantspecification.substring(plantspecification.lastIndexOf(' ') + 1);

            final int number = Integer.parseInt(numberOfPlant);
            final int consumption = typeAndConsumption.length();
            final int cities = Integer.parseInt(citySupply);

            final Plant.Type typOfPLant;
            switch (typeAndConsumption.charAt(0)) {
                case 'O' -> typOfPLant = Plant.Type.Oil;
                case 'C' -> typOfPLant = Plant.Type.Coal;
                case 'H' -> typOfPLant = Plant.Type.Hybrid;
                case 'G' -> typOfPLant = Plant.Type.Garbage;
                case 'F' -> typOfPLant = Plant.Type.Fusion;
                default -> typOfPLant = Plant.Type.Eco;
            }
            Plant plant = factory.newPlant(number, typOfPLant, consumption, cities);
            hidden.add(plant);
        }
    }

    @Override
    public Set<Plant> getActual() {
        return actual;
    }

    @Override
    public Plant removePlant(int number) {
        Plant plant = findPlant(number);
        if (actual.contains(plant))
            actual.remove(plant);
        if (future.contains(plant))
            future.remove(plant);
        if (hidden.contains(plant))
            hidden.remove(plant);

        return plant;
    }

    @Override
    public Set<Plant> getFuture() {
        return future;
    }

    @Override
    public int getNumberHidden() {
        return hidden.size();
    }

    @Override
    public Plant findPlant(int number) {
        Set<Plant> allPlantSets = new HashSet<>();
        allPlantSets.addAll(actual);
        allPlantSets.addAll(future);
        allPlantSets.addAll(hidden);
        for ( Plant plant: allPlantSets) {
            if (plant.getNumber() == number){
                return plant;
            }
        }
        return null;
    }

    @Override
    public List<Plant> getHidden() {
        return hidden;
    }

}
