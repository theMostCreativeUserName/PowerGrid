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

public class OperateNoPlantTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); //max sec for test

    private final Game game;
    private final Rules sut;
    private final String NO_SECRET = "";
    private static BiConsumer<Integer, Runnable> times = (n, runnable) -> IntStream.range(0, n).forEach(__ -> runnable.run());

    public OperateNoPlantTest(){
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.severin.powergrid.datastore.NeutralFactory");
        System.setProperty("powergrid.rules", "edu.hm.severin.powergrid.logic.StandardRules");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    //Hold my spaghetti code
    //This doesn't require a lot of testing
    //It doesnt affect other players or the electro count of the player itself

    @Test
    public void OperateNoPlantOnePlayer(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.PlantOperation);

        OpenPlayer player = factory.newPlayer("ReadyPlayerOne", "red");
        player.setElectro(420);
        player.setPassed(false);
        opengame.getOpenPlayers().add(player);

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());

        //assert
        assertTrue(moveTypes.contains(MoveType.OperateNoPlant));
        assertEquals(moveTypes.size(),1); //Buy no Resource
    }

    @Test
    public void OperateNoPlantTwoPlayers(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.PlantOperation);

        OpenPlayer player1 = factory.newPlayer("ReadyPlayerOne", "red");
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player1.setElectro(420);
        player2.setElectro(69);
        player1.setPassed(false);
        player2.setPassed(false);
        opengame.getOpenPlayers().add(player1);
        opengame.getOpenPlayers().add(player2);

        //act
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<MoveType> moveTypes = haveMove.stream().map(Move::getType).collect(Collectors.toList());

        //assert
        assertTrue(moveTypes.contains(MoveType.OperateNoPlant));
        assertEquals(moveTypes.size(),1); //Buy no Resource
    }

    @Test
    public void OperateNoPlantFire(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.PlantOperation);

        OpenPlayer player1 = factory.newPlayer("ReadyPlayerOne", "red");
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player1.setElectro(420);
        player2.setElectro(69);
        player1.setPassed(false);
        player2.setPassed(false);
        opengame.getOpenPlayers().add(player1);
        opengame.getOpenPlayers().add(player2);


        //assert
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.OperateNoPlant).collect(Collectors.toList());
        opengame.setLevel(2);
        Optional<Problem> problem =  sut.fire(Optional.of("ReadyPlayerOne"), moves.get(0));

        assertTrue(problem.isEmpty());
        assertTrue(player1.hasPassed());
    }

    @Test
    public void OperateNoPlantFire2(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.PlantOperation);

        OpenPlayer player1 = factory.newPlayer("ReadyPlayerOne", "red");
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player1.setElectro(420);
        player2.setElectro(69);
        player1.setPassed(false);
        player2.setPassed(false);
        opengame.getOpenPlayers().add(player1);
        opengame.getOpenPlayers().add(player2);


        //assert
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.OperateNoPlant).collect(Collectors.toList());
        player1.setPassed(true);
        Optional<Problem> problem =  sut.fire(Optional.of("ReadyPlayerOne"), moves.get(0));

        assertSame(problem.get(), Problem.NotYourTurn);
    }

    @Test
    public void OperateNoPlantFire3(){
        //arrange
        OpenGame opengame = (OpenGame) sut.getGame();
        OpenFactory factory = opengame.getFactory();
        opengame.setPhase(Phase.PlantOperation);

        OpenPlayer player1 = factory.newPlayer("ReadyPlayerOne", "red");
        OpenPlayer player2 = factory.newPlayer("ReadyPlayerTwo", "blue");
        player1.setElectro(420);
        player2.setElectro(69);
        player1.setPassed(false);
        player2.setPassed(false);
        opengame.getOpenPlayers().add(player1);
        opengame.getOpenPlayers().add(player2);


        //assert
        final Set<Move> haveMove = sut.getMoves(Optional.of("ReadyPlayerOne"));
        List<Move> moves = haveMove.stream().filter(Move -> Move.getType() == MoveType.OperateNoPlant).collect(Collectors.toList());
        player1.setPassed(true);
        player2.setPassed(true);
        Optional<Problem> problem =  sut.fire(Optional.of("ReadyPlayerOne"), moves.get(0));

        assertSame(problem.get(), Problem.NotYourTurn);
    }




}

