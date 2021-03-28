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

    static final Map<PlayerRole, Perceptor> PERCEPTORS = new HashMap<>();

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(args);
        
        //add perceptors
        PERCEPTORS.put(PlayerRole.goalie, new Perceptor.Goalie());

        //add players
        addPlayer("player1", PlayerRole.forward);
        addPlayer("player2", PlayerRole.forward);
        addPlayer("goalie", PlayerRole.goalie);

        //start game
        addPercept(Literals.GAME_START);
    }

    private void addPlayer(String playerName, PlayerRole role) {
        try {
            Player player = new Player(InetAddress.getByName("localhost"), 6000, "test", playerName, role);
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
    	clearPercepts(player);    	
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
        
        clearPercepts(agName);

        PlayerRole role = player.playerRole;
        Perceptor perceptor = PERCEPTORS.get(role);
        if (perceptor != null) {
            perceptor.accept(this, player);
        }
        else {
            updatePlayerPercepts(agName, ASSyntax.createAtom(player.getM_side()+"side"));
            if(player.visualInfo != null)
                updatePlayerPerceptsFromVisual(agName, player.visualInfo);
            if(player.messageInfo != null) {
                updatePlayerPerceptsFromHearing(agName, player.messageInfo);
            }
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
