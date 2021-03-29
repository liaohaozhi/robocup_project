
//Initial Facts
!start.


//Plans

// if the ball is not visible, turn to try and find it
+!start : not ballVisible(BallDist, BallDir) & not ballClose <-
	if (ballRightOfPlayer){
		turn(30);
	} else {
		turn(-30);
	};!start.

// if the ball is visible, then we can determine if it is near or far,
// and we can determine if we are facing the ball (or should turn to the ball)
+!start : ballVisible(BallDist, BallDir) & not ballClose <-
	if (BallDir < 15 & BallDir > -15){
		if (BallDist < 1){
			+ballClose;
		} else {
			dash(100);
		}
	} elif (BallDir > 0) {
		+ballRightOfPlayer;
		turn(BallDir);
	} elif (BallDir < 0) {
		-ballRightOfPlayer;
		turn(BallDir);
	};!start.
	
// if the ball is close and opponent goal is visible, kick the ball at the goal
+!start : ballClose & lside & goalrVisible(GoalDist, GoalDir) <- 
	kick(100, GoalDir);
	-ballClose;
	!start.
+!start : ballClose & rside & goallVisible(GoalDist, GoalDir) <- 
	kick(100, GoalDir);
	-ballClose;
	!start.
	
// if the ball is close and opponent goal is not visible, turn to look for the goal
+!start : ballClose & lside & not goalrVisible(GoalDist, GoalDir)  <- 
	turn(30);
	!start.
+!start : ballClose & rside & not goallVisible(GoalDist, GoalDir) <- 
	turn(30);
	!start.