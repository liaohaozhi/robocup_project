import java.util.regex.Pattern;

class Action {
	private static final String TURN_PATTERN = "^turn\\(-?[0-9]+\\)$";
	private static final String DASH_PATTERN = "^dash\\(-?[0-9|\\.]+\\)$";
	private static final String KICK_PATTERN = "^kick\\(-?[0-9|\\.]+,-?[0-9|\\.]+\\)$";
	private static final String MOVE_PATTERN = "^move\\(-?[0-9|\\.]+,-?[0-9|\\.]+\\)$";
	private static final String CATCH_PATTERN = "^catch\\(-?[0-9]+\\)$";
	private static final String CHANGEVIEW_PATTERN = "^change_view\\([narrow|normal|wide],[high|low]\\)$";
	

	
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
		} else if (Pattern.matches(CATCH_PATTERN, action)) {
			player.catch_ball(Double.parseDouble(action.substring(6, action.length()-1)));
		} else if (Pattern.matches(CHANGEVIEW_PATTERN, action)){
			player.changeView(action.substring(12, action.indexOf(",")), 
					action.substring(action.indexOf(",")+1, action.length()-1));
		} 
    }
}
