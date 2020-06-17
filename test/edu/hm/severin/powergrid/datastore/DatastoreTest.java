package edu.hm.severin.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
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

    private final OpenFactory factory = OpenFactory.newFactory(fqcn);

    @Test
    public void newGame() {
        // arrange
        OpenGame sut = factory.newGame(new EditionGermany());
        // act
        OpenGame have = factory.newGame(new EditionGermany());
        // assert
        assertSame(sut, have);
        assertSame(factory, sut.getFactory());
    }

    @Test
    public void GameGetEdition() {
        // arrange
        Edition sut = new EditionGermany();
        OpenGame game = factory.newGame(sut);
        // act
        Edition have = game.getEdition();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void GameGetBoard() {
        // arrange
        Edition edition = new EditionGermany();
        OpenBoard sut = factory.newBoard(edition);
        // act
        OpenGame game = factory.newGame(edition);
        OpenBoard have = game.getBoard();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void GameGetAndSetRound() {
        // arrange
        Edition edition = new EditionGermany();
        OpenGame game = factory.newGame(edition);
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
        OpenGame game = factory.newGame(edition);
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
        OpenGame game = factory.newGame(edition);

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
        OpenGame game = factory.newGame(edition);
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
        OpenPlantMarket sut = factory.newPlantMarket(edition);
        // act
        OpenGame game = factory.newGame(edition);
        OpenPlantMarket have = game.getPlantMarket();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void GameGetResourceMarket() {
        // arrange
        Edition edition = new EditionGermany();
        OpenResourceMarket sut = factory.newResourceMarket(edition);
        // act
        OpenGame game = factory.newGame(edition);
        OpenResourceMarket have = game.getResourceMarket();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void GameGetAndSetAuction() {
        // arrange
        Edition edition = new EditionGermany();
        OpenResourceMarket resourceMarket = factory.newResourceMarket(edition);
        OpenPlant plant = factory.newPlant(2, Plant.Type.Coal, 3, 3);
        List<OpenPlayer> players = List.of(factory.newPlayer("123", "red"),
                factory.newPlayer("456", "blue"));
        OpenAuction sut = factory.newAuction(plant, players);

        // act
        OpenGame game = factory.newGame(edition);
        game.setAuction(sut);
        OpenAuction have = game.getAuction();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void GameGetAndSetNumMoves() {
        // arrange
        Edition edition = new EditionGermany();
        OpenGame game = factory.newGame(edition);
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
        OpenResourceMarket sut = factory.newResourceMarket(new EditionGermany());
        // act
        OpenResourceMarket have = factory.newResourceMarket(new EditionGermany());
        // assert
        assertSame(sut, have);
    }

    @Test
    public void ResourceMarketGetPrice() {
        // arrange
        OpenResourceMarket sutRM = factory.newResourceMarket(new EditionGermany());
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
        OpenPlantMarket sut = factory.newPlantMarket(new EditionGermany());
        // act
        OpenPlantMarket have = factory.newPlantMarket(new EditionGermany());
        // assert
        assertSame(sut, have);
    }

    @Test
    public void PlantMarketGetActual() {
        // arrange
        Set<OpenPlant> sut = new HashSet<>();
        // act
        OpenPlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        Set<OpenPlant> have = plantMarket.getOpenActual();
        // assert
        Assert.assertTrue(sut.equals(have));
    }

    @Test
    public void PlantMarketGetFuture() {
        // arrange
        Set<OpenPlant> sut = new HashSet<>();
        // act
        OpenPlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        Set<OpenPlant> have = plantMarket.getOpenFuture();
        // assert
        Assert.assertTrue(sut.equals(have));
    }

    @Test
    public void PlantMarketGetNumberHidden() {
        // arrange
        int sut = 42;
        // act
        OpenPlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        int have = plantMarket.getNumberHidden();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void PlantMarketFindPlantIn() {
        // arrange
        OpenPlant sut = factory.newPlant(7, Plant.Type.Oil, 3, 2);
        // act
        OpenPlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        OpenPlant have = plantMarket.findPlant(7);
        // assert
        assertEquals(sut, have);
    }

    @Test
    public void PlantMarketFindPlantOut() {
        // arrange
        OpenPlant sut = null;
        // act
        OpenPlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        OpenPlant have = plantMarket.findPlant(55);
        // assert
        assertEquals(sut, have);
    }
    @Test
    public void plantMarketFindPlant(){
        OpenPlantMarket m = factory.newPlantMarket(new EditionGermany());
        OpenPlant found = m.findPlant(4);
        assertEquals(found.getType(), Plant.Type.Coal);
    }
    @Test
    public void GameFindPlayers() {
        // arrange
        Edition edition = new EditionGermany();
        OpenGame game = factory.newGame(edition);
        String secret = "hello";
        OpenPlayer sut = game.findPlayer(secret);
        // act
        OpenPlayer have = null;
        // assert
        assertSame(sut, have);
    }

    @Test
    public void PlantMarketRemovePlantWithNullPlant() {
        // arrange
        OpenPlant sut = null;
        // act
        OpenPlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        OpenPlant have = plantMarket.removePlant(55);
        // assert
        assertEquals(sut, have);
    }

    @Test
    public void PlantMarketRemovePlantWithRealPlant() {
        // arrange
        OpenPlant sut = factory.newPlant(7, Plant.Type.Oil, 3, 2);
        // act
        OpenPlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        OpenPlant have = plantMarket.removePlant(7);
        // assert
        assertEquals(sut, have);
    }

    @Test
    public void PlantMarketGetHidden() {
        // arrange
        List <OpenPlant> sut = List.of(
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
        OpenPlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        List<OpenPlant> have = plantMarket.getOpenHidden();
        // assert
        assertEquals(sut, have);
    }

    @Test
    public void newAuction() {
        // arrange
        OpenAuction sut = factory.newAuction(factory.newPlant(3, Plant.Type.Eco, 1, 1),
                List.of(factory.newPlayer("123", "red"),
                        factory.newPlayer("456", "blue")));
        // act
        OpenAuction have = factory.newAuction(factory.newPlant(3, Plant.Type.Coal, 2, 2),
                List.of(factory.newPlayer("789", "yellow"),
                        factory.newPlayer("012", "green")));
        // assert
        assertSame(sut, have);
    }

    @Test
    public void auctionGetAndSetAmount() {
        // arrange
        OpenAuction auction = factory.newAuction(factory.newPlant(3, Plant.Type.Eco, 1, 1),
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
        OpenAuction auction = factory.newAuction(factory.newPlant(3, Plant.Type.Eco, 1, 1),
                List.of(factory.newPlayer("123", "red"),
                        factory.newPlayer("456", "blue")));
        // act
        OpenPlayer have = factory.newPlayer("1234", "blue");
        auction.setPlayer(have);
        OpenPlayer sut = auction.getPlayer();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void auctionGetPlayers() {
        // arrange
        List<OpenPlayer> have = List.of(
                factory.newPlayer("123", "red"),
                factory.newPlayer("456", "blue"));
        OpenAuction auction = factory.newAuction(factory.newPlant(3, Plant.Type.Eco, 1, 1), have);
        // act
        List<OpenPlayer> sut = auction.getOpenPlayers();
        // assert
        assertSame(sut, have);
    }

    @Test
    public void auctionGetPlant() {
        // arrange
        OpenPlant have = factory.newPlant(3, Plant.Type.Eco, 1, 1);
        OpenAuction auction = factory.newAuction(have,
                List.of(factory.newPlayer("123", "red"),
                        factory.newPlayer("456", "blue")));
        // act
        OpenPlant sut = auction.getPlant();
        // assert
        assertSame(sut, have);
    }
    @Test
    public void GameFindPlayerOK() {
        // arrange
        Edition edition = new EditionGermany();
        OpenGame game = factory.newGame(edition);
        OpenPlayer sut = factory.newPlayer("678", "gelb");
        OpenPlayer player = factory.newPlayer("777", "rot");
        List<OpenPlayer> players = game.getOpenPlayers();
        players.add(sut);
        players.add(player);
        // act
        OpenPlayer have = game.findPlayer("678");
        // assert
        assertSame(sut, have);
    }

    @Test
    public void PlantMarketActualRemoveAndGet() {
        // arrange
        OpenPlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        Set<OpenPlant> actual = plantMarket.getOpenActual();
        OpenPlant sut = factory.newPlant(70, Plant.Type.Eco, 3, 3);
        actual.add(sut);
        Set<OpenPlant> sut2 = new HashSet<>();
        // act
        OpenPlant have = plantMarket.removePlant(70);
        Set<OpenPlant> have2 = plantMarket.getOpenActual();
        // assert
        assertEquals(sut, have);
        assertEquals(sut2, have2);
    }

    @Test
    public void PlantMarketFutureRemoveAndGet() {
        // arrange
        OpenPlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        Set<OpenPlant> future = plantMarket.getOpenFuture();
        OpenPlant sut = factory.newPlant(70, Plant.Type.Eco, 3, 3);
        future.add(sut);
        Set<OpenPlant> sut2 = new HashSet<>();
        // act
        OpenPlant have = plantMarket.removePlant(70);
        Set<OpenPlant> have2 = plantMarket.getOpenFuture();
        // assert
        assertEquals(sut, have);
        assertEquals(sut2, have2);
    }

    @Test
    public void PlantMarketRemovePlantWithNullPlant2() {
        // arrange
        OpenPlant sut = null;
        // act
        OpenPlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        OpenPlant have = plantMarket.removePlant(55);
        // assert
        assertEquals(sut, have);
    }

    @Test
    public void PlantMarketRemovePlantWithRealPlant3() {
        OpenPlantMarket plantMarket = factory.newPlantMarket(new EditionGermany());
        List<OpenPlant> hidden = plantMarket.getOpenHidden();
        hidden.removeAll(hidden);
        OpenPlant sut = factory.newPlant(70, Plant.Type.Eco, 3, 3);
        hidden.add(sut);
        List <OpenPlant> sut2 = new ArrayList<>();
        // act
        OpenPlant have = plantMarket.removePlant(70);
        List<OpenPlant> have2 = plantMarket.getOpenHidden();
        // assert
        assertEquals(sut, have);
        assertEquals(sut2, have2);
    }

    // New Crap because changes
    @Test (expected =  IllegalArgumentException.class)
    public void resourceMarketSorryNothingToBuy(){
        OpenResourceMarket resourceMarket = factory.newResourceMarket(new EditionGermany());
        resourceMarket.getOpenAvailable().remove(resourceMarket.getOpenAvailable());
        resourceMarket.getPrice(Resource.Uranium);
    }

    // x.14
    @Test
    public void gameSetRoundZero(){
        OpenGame game = factory.newGame(new EditionGermany());
        game.setRound(0);
        assertSame(game.getRound(), 0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void gameSetRoundNegative(){
        OpenGame game = factory.newGame(new EditionGermany());
        game.setRound(-1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void gameSetPhaseNull(){
        OpenGame game = factory.newGame(new EditionGermany());
        game.setPhase(null);
    }

    @Test
    public void gameSetLevelZero(){
        OpenGame game = factory.newGame(new EditionGermany());
        game.setLevel(0);
        assertSame(game.getRound(), 0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void gameSetLevelNegative(){
        OpenGame game = factory.newGame(new EditionGermany());
        game.setLevel(-1);
    }

    @Test
    public void GameFindPlayerStream(){
        OpenGame game = factory.newGame(new EditionGermany());
        OpenPlayer player = factory.newPlayer("456", "blau");
        game.getOpenPlayers().clear();
        game.getOpenPlayers().add(factory.newPlayer("123", "rot"));
        game.getOpenPlayers().add(player);

        assertSame(game.findPlayer("456"), player);
    }

    @Test
    public void gameSetNumMovesZero(){
        OpenGame game = factory.newGame(new EditionGermany());
        game.setNumMoves(0);
        assertSame(game.getNumMoves(), 0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void gameSetNumMovesNegative(){
        OpenGame game = factory.newGame(new EditionGermany());
        game.setNumMoves(-1);
    }

    @Test
    public void auctionSetAmountZero(){
        OpenAuction auction = factory.newAuction(factory.newPlant(1, Plant.Type.Coal, 2, 2), List.of(factory.newPlayer("123", "rot")));
        auction.setAmount(0);
        assertSame(auction.getAmount(), 0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void auctionSetAmountNegative(){
        OpenAuction auction = factory.newAuction(factory.newPlant(1, Plant.Type.Coal, 2, 2), List.of(factory.newPlayer("123", "rot")));
        auction.setAmount(-1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void auctionSetPlayerNull(){
        OpenAuction auction = factory.newAuction(factory.newPlant(1, Plant.Type.Coal, 2, 2), List.of(factory.newPlayer("123", "rot")));
        auction.setPlayer(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void auctionSetPlayersNull(){
        OpenAuction auction = factory.newAuction(factory.newPlant(1, Plant.Type.Coal, 2, 2), null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void auctionSetPlayerEmpty(){
        OpenAuction auction = factory.newAuction(factory.newPlant(1, Plant.Type.Coal, 2, 2), new ArrayList<>());
    }

    // x.15
    @Test
    public void auctionConstructorFirstPlayerIsHighestBidder(){
        OpenPlayer player = factory.newPlayer("123", "rot");
        OpenAuction auction = factory.newAuction(factory.newPlant(1, Plant.Type.Coal, 2, 2), List.of(player, factory.newPlayer("456", "pink")));
        assertSame(auction.getPlayer(), player);
    }
}

