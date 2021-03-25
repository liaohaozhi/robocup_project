// Environment code for project roboCupTeam

import jason.asSyntax.*;
import jason.environment.*;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

public class RoboCupGame extends Environment {
    private Logger logger = Logger.getLogger("roboCupTeam."+RoboCupGame.class.getName());

    static final Map<String, Player> PLAYERS = new HashMap<>();

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(args);
        addPlayer("player1");
        updatePlayerPercepts("player1");
    }

    private void addPlayer(String playerName) {
        try {
            Player player = new Player(InetAddress.getByName("localhost"), 8000, "test", playerName);
            PLAYERS.put(playerName, player);
            player.start();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    //updating player's percepts
    void updatePlayerPercepts(String player) {
        addPercept(player, Literal.parseLiteral("noSeeBall"));
        logger.info("update " + player + " percepts: noSeeBall");
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        logger.info(agName + " executing: " + action);
        PLAYERS.get(agName).doAction(action.toString());
        return true; // the action was executed with success
    }

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
        super.stop();
    }
}
