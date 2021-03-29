import jason.asSyntax.ASSyntax;

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
            game.clearPercepts(player.playerName);

            char side = player.m_side;
            String playerName = player.playerName;
            VisualInfo visualInfo = player.visualInfo;
            if (visualInfo != null) {
                //update own goal position
                GoalInfo ownGoal = null;
                if (side == 'l') {
                    ownGoal = VisualInfoUtil.getGoalInfo(visualInfo, VisualInfoUtil.LEFT_GOAL);
                }
                else {
                    ownGoal = VisualInfoUtil.getGoalInfo(visualInfo, VisualInfoUtil.RIGHT_GOAL);
                }
                if (ownGoal != null) {
                    logger.info("update " + player + " own goal info : " + ownGoal);
                    game.addPercept(playerName, ASSyntax.createLiteral(
                            "ownGoal",
                            ASSyntax.createNumber(ownGoal.getDistance()),
                            ASSyntax.createNumber(ownGoal.getDirection())));
                }

                //update ball position
                BallInfo ballInfo = VisualInfoUtil.getBallInfo(visualInfo);
                if (ballInfo != null) {
                    logger.info("update " + player + " ball info : " + ballInfo);
                    game.addPercept(playerName, ASSyntax.createLiteral(
                            "ball",
                            ASSyntax.createNumber(ballInfo.getDistance()),
                            ASSyntax.createNumber(ballInfo.getDirection())
                    ));
                }
            }
        }
    }
}
