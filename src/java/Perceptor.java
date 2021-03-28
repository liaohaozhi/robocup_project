import java.util.Vector;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public abstract class Perceptor implements BiConsumer<RoboCupGame, Player> {
    @Override
    public abstract void accept(RoboCupGame roboCupGame, Player player);

    static class Goalie extends Perceptor {
        Logger logger = Logger.getLogger("Perceptor."+Perceptor.class.getName());

        @Override
        public void accept(RoboCupGame game, Player player) {
            char side = player.m_side;
            String playerName = player.playerName;
            VisualInfo visualInfo = player.visualInfo;
            //update goalie position
            GoalInfo ownGoal = null;
            if (side == 'l') {
                ownGoal = VisualInfoUtil.getGoalInfo(visualInfo, VisualInfoUtil.LEFT_GOAL);
            }
            else {
                ownGoal = VisualInfoUtil.getGoalInfo(visualInfo, VisualInfoUtil.RIGHT_GOAL);
            }
            logger.info("update " + player + " goal info : " + ownGoal);
            if (ownGoal != null && ownGoal.m_distance < 5.0) {
                game.addPlayerPercept(player.getName(), Literals.GOALIE_AT_GOAL);
            }

            //update goalie visual
            Vector<BallInfo> ballList = visualInfo.getBallList();
            if (ballList != null && ballList.size() > 0) {
                BallInfo ballInfo = ballList.iterator().next();
                logger.info("update " + player + " ball info : " + ballInfo);
                if (ballInfo.m_distance < 20.0) {
                    game.addPlayerPercept(playerName, Literals.GOALIE_SAVE_BALL);
                    if (ballInfo.m_distance < 1.0) {
                        game.addPlayerPercept(playerName, Literals.GOALIE_BALL_CLOSE);
                    }
                }
            }
        }
    }
}
