// Environment code for project roboCupTeam

import jason.asSyntax.*;
import jason.environment.*;
import jason.asSyntax.parser.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.*;

public class RoboCupGame extends Environment {
    private Logger logger = Logger.getLogger("roboCupTeam."+RoboCupGame.class.getName());

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(args);
        try {
			addPercept(ASSyntax.parseLiteral("percept(demo)"));
			Player player = new Player(InetAddress.getByName("localhost"), 8000, "test");
			new Thread(() -> {
                try {
                    player.mainLoop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        logger.info("executing: "+action+", but not implemented!");
        if (true) { // you may improve this condition
             informAgsEnvironmentChanged();
        }
        return true; // the action was executed with success
    }

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
        super.stop();
    }
}
