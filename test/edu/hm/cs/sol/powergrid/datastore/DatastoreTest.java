package edu.hm.cs.sol.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @author Pietsch
 * @version last modified 2020-04-09
 */
public class DatastoreTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    private final String fqcn = "edu.hm.severin.powergrid.datastore.NeutralFactory";

    private final Factory factory = Factory.newFactory(fqcn);

    @Test
    public void newGame() {
        // arrange
        Game sut = factory.newGame(new EditionGermany());
        // act
        Game have = factory.newGame(new EditionGermany());
        // assert
        assertSame(sut, have);
        assertSame(factory, sut.getFactory());
    }

    @Test
    public void GameGetEdition() {
        // arrange
        Edition sut = new EditionGermany();
        Game game = factory.newGame(sut);
        // act
        Edition have = game.getEdition();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void GameGetBoard() {
        // arrange
        Edition edition = new EditionGermany();
        Board sut = factory.newBoard(edition);
        // act
        Game game = factory.newGame(edition);
        Board have = game.getBoard();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void GameGetAndSetRound() {
        // arrange
        Edition edition = new EditionGermany();
        Game game = factory.newGame(edition);
        int sut = 10;
        game.setRound(sut);
        // act
        int have = game.getRound();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void GameGetAndSetPhase() {
        // arrange
        Edition edition = new EditionGermany();
        Game game = factory.newGame(edition);
        Phase sut = Phase.Building;
        game.setPhase(sut);
        // act
        Phase have = game.getPhase();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void GameGetPlayers() {
        // arrange
        Edition edition = new EditionGermany();
        Game game = factory.newGame(edition);

        List <Player> sut = new ArrayList<>();
        // act
        List <Player> have = game.getPlayers();
        // assert
        Assert.assertTrue(sut.equals(have));
    }

    @Test
    public void GameGetAndSetLevel() {
        // arrange
        Edition edition = new EditionGermany();
        Game game = factory.newGame(edition);
        int sut = 2;
        game.setLevel(sut);
        // act
        int have = game.getLevel();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void GameGetPlantMarket() {
        // arrange
        Edition edition = new EditionGermany();
        PlantMarket sut = factory.newPlantMarket(edition);
        // act
        Game game = factory.newGame(edition);
        PlantMarket have = game.getPlantMarket();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void GameGetResourceMarket() {
        // arrange
        Edition edition = new EditionGermany();
        ResourceMarket sut = factory.newResourceMarket(edition);
        // act
        Game game = factory.newGame(edition);
        ResourceMarket have = game.getResourceMarket();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void GameGetAndSetAuction() {
        // arrange
        Edition edition = new EditionGermany();
        ResourceMarket resourceMarket = factory.newResourceMarket(edition);
        Plant plant = factory.newPlant(2, Plant.Type.Coal, 3, 3);
        List<Player> players = List.of(factory.newPlayer("123", "red"),
                factory.newPlayer("456", "blue"));
        Auction sut = factory.newAuction(plant, players);

        // act
        Game game = factory.newGame(edition);
        game.setAuction(sut);
        Auction have = game.getAuction();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void GameGetAndSetNumMoves() {
        // arrange
        Edition edition = new EditionGermany();
        Game game = factory.newGame(edition);
        int sut = 14;
        game.setNumMoves(sut);
        // act
        int have = game.getNumMoves();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void newResourceMarket() {
        // arrange
        ResourceMarket sut = factory.newResourceMarket(new EditionGermany());
        // act
        ResourceMarket have = factory.newResourceMarket(new EditionGermany());
        // assert
        assertSame(sut, have);
    }

    @Test
    public void ResourceMarketGetPrice() {
        // arrange
        ResourceMarket sutRM = factory.newResourceMarket(new EditionGermany());
        // act
        int sut = sutRM.getPrice(Resource.Uranium);
        int have = 14;
        // assert
        assertSame(sut, have);
    }

    @Test
    public void ResourceMarketGetSupply() {
        Map<Resource, Integer> sut = new HashMap<>();
        sut = Map.of(
                Resource.Oil, 6,
                Resource.Garbage, 18,
                Resource.Uranium, 10);

        Map<Resource, Integer> have = new HashMap<>();
        Bag<Resource> bagOfResources = factory.newResourceMarket(new EditionGermany()).getSupply();
        for (Resource resource : bagOfResources)
            have.put(resource, bagOfResources.count(resource));

        // assert
        Assert.assertTrue(sut.equals(have));
    }

    @Test
    public void ResourceMarketGetAvailable() {
        // arrange
        Map<Resource, Integer> sut = new EditionGermany().getResourcesInitiallyAvailable();
        Map<Resource, Integer> have = new HashMap<>();
        Bag<Resource> bagOfResources = factory.newResourceMarket(new EditionGermany()).getAvailable();
        for (Resource resource : bagOfResources)
            have.put(resource, bagOfResources.count(resource));
        // assert
        Assert.assertTrue(sut.equals(have));
    }

    @Test
    public void newPlantMarket() {
        // arrange
        PlantMarket sut = factory.newPlantMarket(new EditionGermany());
        // act
        PlantMarket have = factory.newPlantMarket(new EditionGermany());
        // assert
        assertSame(sut, have);
    }

    @Test
    public void PlantMarketGetActual() {
        // arrange
        Set<Plant> sut = new HashSet<>();
        // act
        PlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        Set<Plant> have = plantMarket.getActual();
        // assert
        Assert.assertTrue(sut.equals(have));
    }

    @Test
    public void PlantMarketGetFuture() {
        // arrange
        Set<Plant> sut = new HashSet<>();
        // act
        PlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        Set<Plant> have = plantMarket.getFuture();
        // assert
        Assert.assertTrue(sut.equals(have));
    }

    @Test
    public void PlantMarketGetNumberHidden() {
        // arrange
        int sut = 42;
        // act
        PlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        int have = plantMarket.getNumberHidden();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void PlantMarketFindPlantIn() {
        // arrange
        Plant sut = factory.newPlant(7, Plant.Type.Oil, 3, 2);
        // act
        PlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        Plant have = plantMarket.findPlant(7);
        // assert
        assertEquals(sut, have);
    }

    @Test
    public void PlantMarketFindPlantOut() {
        // arrange
        Plant sut = null;
        // act
        PlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        Plant have = plantMarket.findPlant(55);
        // assert
        assertEquals(sut, have);
    }
    @Test
    public void plantMarketFindPlant(){
        PlantMarket m = factory.newPlantMarket(new EditionGermany());
        Plant found = m.findPlant(4);
        assertEquals(found.getType(), Plant.Type.Coal);
    }
    @Test
    public void GameFindPlayers() {
        // arrange
        Edition edition = new EditionGermany();
        Game game = factory.newGame(edition);
        String secret = "hello";
        Player sut = game.findPlayer(secret);
        // act
        Player have = null;
        // assert
        assertSame(sut, have);
    }

    @Test
    public void PlantMarketRemovePlantWithNullPlant() {
        // arrange
        Plant sut = null;
        // act
        PlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        Plant have = plantMarket.removePlant(55);
        // assert
        assertEquals(sut, have);
    }

    @Test
    public void PlantMarketRemovePlantWithRealPlant() {
        // arrange
        Plant sut = factory.newPlant(7, Plant.Type.Oil, 3, 2);
        // act
        PlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        Plant have = plantMarket.removePlant(7);
        // assert
        assertEquals(sut, have);
    }

    @Test
    public void PlantMarketGetHidden() {
        // arrange
        List <Plant> sut = List.of(
                factory.newPlant(3, Plant.Type.Oil, 2, 1),
                factory.newPlant(4, Plant.Type.Coal, 2, 1),
                factory.newPlant(5, Plant.Type.Hybrid, 2, 1),
                factory.newPlant(6, Plant.Type.Garbage, 1, 1),
                factory.newPlant(7, Plant.Type.Oil, 3, 2),
                factory.newPlant(8, Plant.Type.Coal, 3, 2),
                factory.newPlant(9, Plant.Type.Oil, 1, 1),
                factory.newPlant(10, Plant.Type.Coal, 2, 2),
                factory.newPlant(11, Plant.Type.Uranium, 1, 2),
                factory.newPlant(12, Plant.Type.Hybrid, 2, 2),
                factory.newPlant(13, Plant.Type.Eco, 1, 1),
                factory.newPlant(14, Plant.Type.Garbage, 2, 2),
                factory.newPlant(15, Plant.Type.Coal, 2, 3),
                factory.newPlant(16, Plant.Type.Oil, 2, 3),
                factory.newPlant(17, Plant.Type.Uranium, 1, 2),
                factory.newPlant(18, Plant.Type.Eco, 1, 2),
                factory.newPlant(19, Plant.Type.Garbage, 2, 3),
                factory.newPlant(20, Plant.Type.Coal, 3, 5),
                factory.newPlant(21, Plant.Type.Hybrid, 2, 4),
                factory.newPlant(22, Plant.Type.Eco, 1, 2),
                factory.newPlant(23, Plant.Type.Uranium, 1, 3),
                factory.newPlant(24, Plant.Type.Garbage, 2, 4),
                factory.newPlant(25, Plant.Type.Coal, 2, 5),
                factory.newPlant(26, Plant.Type.Oil, 2, 5),
                factory.newPlant(27, Plant.Type.Eco, 1, 3),
                factory.newPlant(28, Plant.Type.Uranium, 1, 4),
                factory.newPlant(29, Plant.Type.Hybrid, 1, 4),
                factory.newPlant(30, Plant.Type.Garbage, 3, 6),
                factory.newPlant(31, Plant.Type.Oil, 3, 6),
                factory.newPlant(32, Plant.Type.Coal, 3, 6),
                factory.newPlant(33, Plant.Type.Eco, 1, 4),
                factory.newPlant(34, Plant.Type.Uranium, 1, 5),
                factory.newPlant(35, Plant.Type.Oil, 1, 5),
                factory.newPlant(36, Plant.Type.Coal, 3, 7),
                factory.newPlant(37, Plant.Type.Eco, 1, 4),
                factory.newPlant(38, Plant.Type.Garbage, 3, 7),
                factory.newPlant(39, Plant.Type.Uranium, 1, 6),
                factory.newPlant(40, Plant.Type.Oil, 2, 6),
                factory.newPlant(42, Plant.Type.Coal, 2, 6),
                factory.newPlant(44, Plant.Type.Eco, 1, 5),
                factory.newPlant(46, Plant.Type.Hybrid, 3, 7),
                factory.newPlant(50, Plant.Type.Fusion, 1, 6)
        );
        // act
        PlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        List<Plant> have = plantMarket.getHidden();
        // assert
        assertEquals(sut, have);
    }

    @Test
    public void newAuction() {
        // arrange
        Auction sut = factory.newAuction(factory.newPlant(3, Plant.Type.Eco, 1, 1),
                List.of(factory.newPlayer("123", "red"),
                        factory.newPlayer("456", "blue")));
        // act
        Auction have = factory.newAuction(factory.newPlant(3, Plant.Type.Coal, 2, 2),
                List.of(factory.newPlayer("789", "yellow"),
                        factory.newPlayer("012", "green")));
        // assert
        assertSame(sut, have);
    }

    @Test
    public void auctionGetAndSetAmount() {
        // arrange
        Auction auction = factory.newAuction(factory.newPlant(3, Plant.Type.Eco, 1, 1),
                List.of(factory.newPlayer("123", "red"),
                        factory.newPlayer("456", "blue")));
        // act
        auction.setAmount(20);
        int sut = auction.getAmount();
        int have = 20;
        // assert
        assertSame(sut, have);
    }

    @Test
    public void auctionGetAndSetPlayer() {
        // arrange
        Auction auction = factory.newAuction(factory.newPlant(3, Plant.Type.Eco, 1, 1),
                List.of(factory.newPlayer("123", "red"),
                        factory.newPlayer("456", "blue")));
        // act
        Player have = factory.newPlayer("1234", "blue");
        auction.setPlayer(have);
        Player sut = auction.getPlayer();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void auctionGetPlayers() {
        // arrange
        List<Player> have = List.of(
                factory.newPlayer("123", "red"),
                factory.newPlayer("456", "blue"));
        Auction auction = factory.newAuction(factory.newPlant(3, Plant.Type.Eco, 1, 1), have);
        // act
        List<Player> sut = auction.getPlayers();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void auctionGetPlant() {
        // arrange
        Plant have = factory.newPlant(3, Plant.Type.Eco, 1, 1);
        Auction auction = factory.newAuction(have,
                List.of(factory.newPlayer("123", "red"),
                        factory.newPlayer("456", "blue")));
        // act
        Plant sut = auction.getPlant();
        // assert
        assertSame(sut, have);
    }

}

