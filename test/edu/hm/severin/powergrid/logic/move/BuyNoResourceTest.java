package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.junit.Assert.*;

public class BuyNoResourceTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); //max sec for test

    private final Game game;
    private final Rules sut;
    private final String NO_SECRET = "";
    private static BiConsumer<Integer, Runnable> times = (n, runnable) -> IntStream.range(0, n).forEach(__ -> runnable.run());

    public BuyNoResourceTest(){
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    @Test
    public void BuyNoResourceOnePlayer(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.ResourceBuying);

        OpenPlayer player = factory.newPlayer("ReadyPlayerOne", "red");
        player.setElectro(420);
        player.setPassed(false);
        opengame.getOpenPlayers().add(player);

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());

        //assert
        assertTrue(moveTypes.contains(MoveType.BuyNoResource));
        assertEquals(moveTypes.size(),1); //Buy no Resource
    }

    @Test
    public void BuyNoResourceTwoPlayers(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.ResourceBuying);

        OpenPlayer player1 = factory.newPlayer("ReadyPlayerOne", "red");
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player1.setElectro(420);
        player2.setElectro(69);
        player1.setPassed(false);
        player2.setPassed(true);
        opengame.getOpenPlayers().add(player1);
        opengame.getOpenPlayers().add(player2);

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.BuyNoResource).collect(Collectors.toList());

        //assert
        assertEquals(moves.size(),1); //Buy no Resource
    }

    @Test
    public void BuyNoResourceProperties(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.ResourceBuying);

        OpenPlayer player1 = factory.newPlayer("ReadyPlayerOne", "red");
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player1.setElectro(420);
        player2.setElectro(69);
        player1.setPassed(false);
        player2.setPassed(true);
        opengame.getOpenPlayers().add(player1);
        opengame.getOpenPlayers().add(player2);

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.BuyNoResource).collect(Collectors.toList());
        Move move = moves.get(0);
        //assert
        assertSame(move.getProperties().getProperty("type"), MoveType.BuyNoResource.toString());
        assertSame(move.getProperties().getProperty("player"), player1.getColor() );
    }

    @Test
    public void BuyNoResourceTwoPlayers2(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.ResourceBuying);

        OpenPlayer player1 = factory.newPlayer("ReadyPlayerOne", "red");
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player1.setElectro(420);
        player2.setElectro(69);
        player1.setPassed(true);
        player2.setPassed(true);
        opengame.getOpenPlayers().add(player1);
        opengame.getOpenPlayers().add(player2);

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.BuyNoResource).collect(Collectors.toList());

        //assert
        assertEquals(moves.size(),0); //Buy no Resource
    }

    @Test
    public void BuyNoResourceTwoPlayersFire(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.ResourceBuying);

        OpenPlayer player1 = factory.newPlayer("ReadyPlayerOne", "red");
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player1.setElectro(420);
        player2.setElectro(69);
        player1.setPassed(false);
        player2.setPassed(false);
        opengame.getOpenPlayers().add(player1);
        opengame.getOpenPlayers().add(player2);

        player1.getOpenPlants().add(factory.newPlant(333, Plant.Type.Coal, 12, 12));

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerTwo"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.BuyNoResource).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.of("ReadyPlayerTwo"), moves.get(0));

        //assert
        assertTrue(problem.isEmpty());
        assertEquals(player2.hasPassed(), true);
    }

    @Test
    public void BuyNoResourceTwoPlayersFire2(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.ResourceBuying);

        OpenPlayer player1 = factory.newPlayer("ReadyPlayerOne", "red");
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player1.setElectro(420);
        player2.setElectro(69);
        player1.setPassed(false);
        player2.setPassed(false);
        opengame.getOpenPlayers().add(player1);
        opengame.getOpenPlayers().add(player2);

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerTwo"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.BuyNoResource).collect(Collectors.toList());
        player2.setPassed(true);
        Optional<Problem> problem =  sut.fire(Optional.of("ReadyPlayerTwo"), moves.get(0));

        //assert
        assertSame(problem.get(), Problem.NotYourTurn);
    }

    @Test (expected = IllegalStateException.class)
    public void BuyNoResourceTwoPlayersFireException(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.ResourceBuying);

        OpenPlayer player1 = factory.newPlayer("ReadyPlayerOne", "red");
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player1.setElectro(420);
        player2.setElectro(69);
        player1.setPassed(false);
        player2.setPassed(false);
        opengame.getOpenPlayers().add(player1);
        opengame.getOpenPlayers().add(player2);

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerTwo"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.BuyNoResource).collect(Collectors.toList());
        HotMove move = (HotMove)moves.get(0);
        move.collect(opengame, Optional.of(factory.newPlayer("Irgendwas", "Mir egal")));
    }
}
