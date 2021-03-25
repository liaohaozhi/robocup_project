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
    static final Literal NOT_SEE_BALL = Literal.parseLiteral("notSeeBall");

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(args);
        addPlayer("player1");
        updatePlayerPercepts("player1", NOT_SEE_BALL);
    }

    private void addPlayer(String playerName) {
        try {
            Player player = new Player(InetAddress.getByName("localhost"), 6000, "test", playerName);
            PLAYERS.put(playerName, player);
            player.start();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    //updating player's percepts
    void updatePlayerPercepts(String player, Literal literal) {
        addPercept(player, literal);
        logger.info("update " + player + " percepts: noSeeBall");
    }

    void updatePlayerPerceptsFromVisual(String player, VisualInfo visualInfo) {
        //todo
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        logger.info(agName + " executing: " + action);
        Player player = PLAYERS.get(agName);
        player.doAction(action.toString());
        updatePlayerPerceptsFromVisual(agName, player.visualInfo);
        return true; // the action was executed with success
    }

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
        super.stop();
    }
}
