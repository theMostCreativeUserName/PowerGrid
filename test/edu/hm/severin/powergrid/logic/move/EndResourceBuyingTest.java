package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Phase;
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

public class EndResourceBuyingTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); //max sec for test

    private final Game game;
    private final Rules sut;
    private final String NO_SECRET = "";
    private static BiConsumer<Integer, Runnable> times = (n, runnable) -> IntStream.range(0, n).forEach(__ -> runnable.run());

    public EndResourceBuyingTest(){
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    //Lets Test

    @Test
    public void EndResourceBuyingOnePlayer(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.ResourceBuying);

        //Player1
        OpenPlayer player = factory.newPlayer("ReadyPlayerOne", "red");
        player.setElectro(420);
        player.setPassed(false);
        opengame.getOpenPlayers().add(player);

        //Player2
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player2.setElectro(520);
        player2.setPassed(true);
        opengame.getOpenPlayers().add(player2);

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.EndResourceBuying).collect(Collectors.toList());

        //assert
        assertEquals(moves.size(),0);
    }

    @Test
    public void EndResourceBuyingOnePlayer2(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.ResourceBuying);

        //Player1
        OpenPlayer player = factory.newPlayer("ReadyPlayerOne", "red");
        player.setElectro(420);
        player.setPassed(true);
        opengame.getOpenPlayers().add(player);

        //Player2
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player2.setElectro(520);
        player2.setPassed(true);
        opengame.getOpenPlayers().add(player2);

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.EndResourceBuying).collect(Collectors.toList());

        //assert
        assertEquals(moves.size(),1);
    }

    @Test
    public void EndResourceBuyingProperties(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.ResourceBuying);

        //Player1
        OpenPlayer player = factory.newPlayer("ReadyPlayerOne", "red");
        player.setElectro(420);
        player.setPassed(true);
        opengame.getOpenPlayers().add(player);

        //Player2
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player2.setElectro(520);
        player2.setPassed(true);
        opengame.getOpenPlayers().add(player2);

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.EndResourceBuying).collect(Collectors.toList());
        Move move = moves.get(0);
        assertSame(move.getProperties().getProperty("type"), MoveType.EndResourceBuying.toString());

    }

    @Test
    public void EndResourceBuyingFire(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.ResourceBuying);

        //Player1
        OpenPlayer player = factory.newPlayer("ReadyPlayerOne", "red");
        player.setElectro(420);
        player.setPassed(true);
        opengame.getOpenPlayers().add(player);

        //Player2
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player2.setElectro(520);
        player2.setPassed(true);
        opengame.getOpenPlayers().add(player2);

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.EndResourceBuying).collect(Collectors.toList());
        Optional<Problem> problem =  sut.fire(Optional.of("ReadyPlayerOne"), moves.get(0));

        //assert
        assertTrue(problem.isEmpty());
        assertFalse(player.hasPassed());
        assertFalse(player2.hasPassed());
        assertSame(opengame.getPhase(), Phase.Building);
    }

    @Test
    public void EndResourceBuyingFire2(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.ResourceBuying);

        //Player1
        OpenPlayer player = factory.newPlayer("ReadyPlayerOne", "red");
        player.setElectro(420);
        player.setPassed(true);
        opengame.getOpenPlayers().add(player);

        //Player2
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player2.setElectro(520);
        player2.setPassed(true);
        opengame.getOpenPlayers().add(player2);

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.EndResourceBuying).collect(Collectors.toList());
        player.setPassed(false);
        Optional<Problem> problem =  sut.fire(Optional.of("ReadyPlayerOne"), moves.get(0));

        //assert
        assertSame(problem.get(), Problem.PlayersRemaining);
    }

    @Test (expected = IllegalStateException.class)
    public void EndResourceBuyingFireException(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.ResourceBuying);

        //Player1
        OpenPlayer player = factory.newPlayer("ReadyPlayerOne", "red");
        player.setElectro(420);
        player.setPassed(true);
        opengame.getOpenPlayers().add(player);

        //Player2
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player2.setElectro(520);
        player2.setPassed(true);
        opengame.getOpenPlayers().add(player2);

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.EndResourceBuying).collect(Collectors.toList());
        HotMove move = (HotMove)moves.get(0);
        move.collect(opengame, Optional.of(factory.newPlayer("Irgendwas", "Mir egal")));
    }


}
