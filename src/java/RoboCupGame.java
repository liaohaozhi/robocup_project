// Environment code for project roboCupTeam

import jason.asSyntax.*;
import jason.environment.*;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.*;

public class RoboCupGame extends Environment {
    private Logger logger = Logger.getLogger("roboCupTeam."+RoboCupGame.class.getName());

    static final Map<String, Player> PLAYERS = new HashMap<String, Player>();
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
    void updatePlayerPercepts(String player, Literal literal) {
        addPercept(player, literal);
        logger.info("update " + player + " percepts: " + literal);
    }

    void updatePlayerPerceptsFromHearing(String player, MessageInfo messageInfo) {
        //pattern: message(sender, uttered, time)
        Literal msgLiteral = ASSyntax.createLiteral("message",
                ASSyntax.createString(messageInfo.getSender()),
                ASSyntax.createString(messageInfo.getUttered()),
                ASSyntax.createNumber(messageInfo.getTime()));
        updatePlayerPercepts(player, msgLiteral);
    }

    /** update player percepts with visualInfo contents*/
    void updatePlayerPerceptsFromVisual(String player, VisualInfo visualInfo) {
    	addObjectPercepts(player, visualInfo.getBallList());
    	addObjectPercepts(player, visualInfo.getGoalList());
    	addObjectPercepts(player, visualInfo.getFlagList());	
    }
    
    /** add the direction and distances for a list of visible objects to a player's percepts*/
    void addObjectPercepts(String player, Vector<?> objects) {
    	for(Object o: objects) {
    		Literal literal = ASSyntax.createLiteral(
    				((ObjectInfo)o).getType().replaceAll("\\s","")+"Visible", 
            		ASSyntax.createNumber(((ObjectInfo)o).getDistance()), 
            		ASSyntax.createNumber(((ObjectInfo)o).getDirection()));
    		updatePlayerPercepts(player, literal);
    	}
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        logger.info(agName + " executing: " + action);
        Player player = PLAYERS.get(agName);
        player.doAction(action.toString());
        try {
            Thread.sleep(200);
        } catch (Exception e) {}
        clearPercepts(agName);
    	updatePlayerPercepts(agName, NOT_SEE_BALL);	//to do: remove this line once we have a proper asl
    	updatePlayerPercepts(agName, ASSyntax.createAtom(player.getM_side()+"side"));
    	if(player.visualInfo != null)
    		updatePlayerPerceptsFromVisual(agName, player.visualInfo);
    	if(player.messageInfo != null) {
            updatePlayerPerceptsFromHearing(agName, player.messageInfo);
        }
        return true; // the action was executed with success
    }

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
        super.stop();
        for(Player player : PLAYERS.values()) {
            player.m_playing = false;
        }
    }
}
