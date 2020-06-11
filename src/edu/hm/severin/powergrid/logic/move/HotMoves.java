package edu.hm.severin.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Set;

/**
 * Companions class to collect all possible moves of the game.
 * @author Severin
 */
public class HotMoves {

    /**
     * collects Prototypes of all possible moves.
     * @return list of possible hotMoves
     */
    public static Set<HotMove> getPrototypes() {
        return Set.of(new NewPlayerJoins(),
                new UpdatePlantMarket(),
                new EndBuilding(),
                new EndGame(),
                new ConnectNoCity(),
                new TurnOver(),
                new Build1stCity(),
                new OrderPlayers(),
                new SupplyElectricity(),
                new OperateNoPlant(),
                new BuyNoResource(),
                new EndResourceBuying(),
                new PassAuction(),
                new RaiseBid(),
                new BuyResource(),
                new DropPlant(),
                new EnterLevel2(),
                new EnterLevel3(),
                new ScrapPlant(),
                new DropResource(),
                new ConnectCity(),
                new OperatePlant(),
                new EndAuctions(),
                new GameBegins(),
                new LeaveAuction(),
                new StartAuction(),
                new CloseAuction()
        );
    }
}