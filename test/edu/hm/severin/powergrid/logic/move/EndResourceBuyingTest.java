package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
}