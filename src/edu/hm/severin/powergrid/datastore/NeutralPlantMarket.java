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

public class NeutralPlantMarket implements OpenPlantMarket {

    /**
     * set of actual plants.
     */
    private Set<OpenPlant> actual;

    /**
     * set of future plants.
     */
    private Set<OpenPlant> future;

    /**
     * list of hidden plants.
     */
    private List<OpenPlant> hidden;

    /**
     * factory of plantmarket.
     */
    private final OpenFactory factory;
    public NeutralPlantMarket(final Edition edition, OpenFactory factory) {
        this.factory = factory;

        actual = new HashSet<OpenPlant>();
        future = new HashSet<OpenPlant>();
        hidden = new ArrayList<OpenPlant>();

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
            OpenPlant plant = factory.newPlant(number, typOfPLant, consumption, cities);
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
        allPlantSets.addAll(actual);
        allPlantSets.addAll(future);
        allPlantSets.addAll(hidden);
        for ( OpenPlant plant: allPlantSets) {
            if (plant.getNumber() == number){
                return plant;
            }
        }
        return null;
    }

    @Override
    public List<OpenPlant> getOpenHidden() {
        return hidden;
    }

}
