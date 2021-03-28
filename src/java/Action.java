import java.util.regex.Pattern;

class Action {
    static int DEFAULT_TURN_MOMENT = 40;
    static int DEFAULT_KICK_POWER = 100;

    //ACTION LABELS
    static final String TURN = "turn";
    static final String RUN_BACK_TO_GOAL = "runBackToGoal";
    static final String TURN_TO_BALL = "turnToBall";
    static final String KICK_BALL_TO_OPP_GOAL = "kickBallToOppositeGoal";

    static void execute(Player player, String action) {
    	if (Pattern.matches("^turn\\(-?[0-9]+\\)$", action)) {
    		player.turn(Integer.parseInt(action.substring(5, action.length()-1)));
		} else if (Pattern.matches("^dash\\(-?[0-9|\\.]+\\)$", action)) {
			player.dash(Double.parseDouble(action.substring(5, action.length()-1)));
		} else if (Pattern.matches("^kick\\(-?[0-9|\\.]+,-?[0-9|\\.]+\\)$", action)) {
			player.kick(Double.parseDouble(action.substring(5, action.indexOf(","))), 
					Double.parseDouble(action.substring(action.indexOf(",")+1, action.length()-1)));
		}
    	
        switch (action) {
            case TURN:
                player.turn(DEFAULT_TURN_MOMENT);
                break;
            case RUN_BACK_TO_GOAL:
                new RunBackToBall().execute(player);
                break;
            case TURN_TO_BALL: {
                new TurnToBall().execute(player);
                break;
            }
            case KICK_BALL_TO_OPP_GOAL: {
                new KickBallToOppGoal().execute(player);
                break;
            }
            default:
                break;
        }
    }

    static abstract class InternalAction {
        abstract void execute(Player player);
    }

    private static class RunBackToBall extends InternalAction {
        @Override
        void execute(Player player) {
            String ownSide = player.m_side == 'l' ? VisualInfoUtil.LEFT_GOAL : VisualInfoUtil.RIGHT_GOAL;
            GoalInfo ownGoal = VisualInfoUtil.getGoalInfo(player.visualInfo, ownSide);
            if (ownGoal != null) {
                if (ownGoal.m_direction != 0) {
                    player.turn(ownGoal.m_direction);
                }
                else {
                    player.dash(DEFAULT_KICK_POWER);
                }
            }
            else {
                player.turn(DEFAULT_TURN_MOMENT);
            }
        }
    }

    private static class TurnToBall extends InternalAction {
        @Override
        void execute(Player player) {
            BallInfo ballInfo = VisualInfoUtil.getBallInfo(player.visualInfo);
            if (ballInfo != null) {
                player.turn(ballInfo.m_direction);
            }
            else {
                player.turn(DEFAULT_TURN_MOMENT);
            }
        }
    }

    private static class KickBallToOppGoal extends InternalAction {

        @Override
        void execute(Player player) {
            String oppSide = player.m_side == 'r' ? VisualInfoUtil.LEFT_GOAL : VisualInfoUtil.RIGHT_GOAL;
            GoalInfo oppGoal = VisualInfoUtil.getGoalInfo(player.visualInfo, oppSide);
            if (oppGoal != null) {
                player.kick(DEFAULT_KICK_POWER, oppGoal.m_direction);
            }
            else {
                player.turn(DEFAULT_TURN_MOMENT);
            }
        }
    }
}
