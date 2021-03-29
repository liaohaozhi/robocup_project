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
	private static final int PORT_NUMBER = 6000;
	
    private Logger logger = Logger.getLogger("roboCupTeam."+RoboCupGame.class.getName());

    static final Map<String, Player> PLAYERS = new HashMap<String, Player>();

    static final Map<PlayerRole, Perceptor> PERCEPTORS = new HashMap<>();

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(args);
        
        //add perceptors
        PERCEPTORS.put(PlayerRole.goalie, new Perceptor.Goalie());

        //add players
        addPlayer("player1", PlayerRole.forward);
        addPlayer("goalie", PlayerRole.goalie);
    }

    private void addPlayer(String playerName, PlayerRole role) {
        try {
            Player player = new Player(InetAddress.getByName("localhost"), PORT_NUMBER, "test",
            		playerName, role);
            PLAYERS.put(playerName, player);
            player.start();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    //updating player's percepts
    void addPlayerPercept(String player, Literal literal) {
        addPercept(player, literal);
        logger.info("update " + player + " percepts: " + literal);
    }

    private void updatePlayerPerceptsFromHearing(String player, MessageInfo messageInfo) {
        //pattern: message(sender, uttered, time)
        Literal msgLiteral = ASSyntax.createLiteral("message",
                ASSyntax.createString(messageInfo.getSender()),
                ASSyntax.createString(messageInfo.getUttered()),
                ASSyntax.createNumber(messageInfo.getTime()));
        addPlayerPercept(player, msgLiteral);
    }

    /** update player percepts with visualInfo contents*/
    private void updatePlayerPerceptsFromVisual(String player, VisualInfo visualInfo) {   	
    	addPerceptsForObjectInfos(player, visualInfo.getBallList());
    	addPerceptsForObjectInfos(player, visualInfo.getGoalList());
    	addPerceptsForObjectInfos(player, visualInfo.getFlagList());	
    }
    
    /** add the direction and distances for a list of visible objects to a player's percepts*/
    private void addPerceptsForObjectInfos(String player, Vector<?> objects) {
    	for(Object o: objects) {
    		Literal literal = ASSyntax.createLiteral(
    				((ObjectInfo)o).getType().replaceAll("\\s","")+"Visible", 
            		ASSyntax.createNumber(((ObjectInfo)o).getDistance()), 
            		ASSyntax.createNumber(((ObjectInfo)o).getDirection()));
    		addPlayerPercept(player, literal);
    	}
    }
    
    public void setPlayerPercepts(String playerName, VisualInfo visualInfo, MessageInfo messageInfo) {
    	Player player = PLAYERS.get(playerName);
    	
    	clearPercepts(playerName);

    	addPlayerPercept(playerName, ASSyntax.createAtom(player.getM_side()+"side"));

        if (messageInfo != null)
        	updatePlayerPerceptsFromHearing(playerName, messageInfo);
        if (visualInfo != null)
        	updatePlayerPerceptsFromVisual(playerName, visualInfo);
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        Player player = PLAYERS.get(agName);
        player.doAction(action.toString());

        //wait 2 steps for action affect
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PlayerRole role = player.playerRole;
        //Run custom perceptor for the role
        //Otherwise run default setPlayerPercepts which adds all visualInfo and messageInfo percepts
        if (PERCEPTORS.containsKey(role)) {
            Perceptor perceptor = PERCEPTORS.get(role);
            perceptor.accept(this, player);
        }
        else {
            setPlayerPercepts(agName,  player.visualInfo,  player.messageInfo);
        }
        return true; // the action was executed with success
    }

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
        super.stop();
        for(Player player : PLAYERS.values()) {
            player.finalize();
        }
    }
}
