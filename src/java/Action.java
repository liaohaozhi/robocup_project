import java.util.regex.Pattern;

class Action {
	private static final String TURN_PATTERN = "^turn\\(-?[0-9]+\\)$";
	private static final String DASH_PATTERN = "^dash\\(-?[0-9|\\.]+\\)$";
	private static final String KICK_PATTERN = "^kick\\(-?[0-9|\\.]+,-?[0-9|\\.]+\\)$";
	private static final String MOVE_PATTERN = "^move\\(-?[0-9|\\.]+,-?[0-9|\\.]+\\)$";

	
    static void execute(Player player, String action) {
    	if (Pattern.matches(TURN_PATTERN, action)) {
    		player.turn(Double.parseDouble(action.substring(5, action.length()-1)));
		} else if (Pattern.matches(DASH_PATTERN, action)) {
			player.dash(Double.parseDouble(action.substring(5, action.length()-1)));
		} else if (Pattern.matches(KICK_PATTERN, action)) {
			player.kick(Double.parseDouble(action.substring(5, action.indexOf(","))), 
					Double.parseDouble(action.substring(action.indexOf(",")+1, action.length()-1)));
		} else if (Pattern.matches(MOVE_PATTERN, action)) {
			player.move(Double.parseDouble(action.substring(5, action.indexOf(","))), 
					Double.parseDouble(action.substring(action.indexOf(",")+1, action.length()-1)));
		}
    }
}
