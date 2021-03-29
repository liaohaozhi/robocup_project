import java.util.regex.Pattern;

class Action {

    static void execute(Player player, String action) {
    	if (Pattern.matches("^turn\\(-?[0-9]+\\)$", action)) {
    		player.turn(Double.parseDouble(action.substring(5, action.length()-1)));
		} else if (Pattern.matches("^dash\\(-?[0-9|\\.]+\\)$", action)) {
			player.dash(Double.parseDouble(action.substring(5, action.length()-1)));
		} else if (Pattern.matches("^kick\\(-?[0-9|\\.]+,-?[0-9|\\.]+\\)$", action)) {
			player.kick(Double.parseDouble(action.substring(5, action.indexOf(","))), 
					Double.parseDouble(action.substring(action.indexOf(",")+1, action.length()-1)));
		}
    }
}
