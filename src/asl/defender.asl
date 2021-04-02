//beliefs
atOwnPosition.

//intentions
!waitDefendBall.

//plans

//Waiting at own position
+!waitDefendBall : atOwnPosition & not ball(BallDist, BallDir) <-
    turn(40);
    !waitDefendBall.
+!waitDefendBall : atOwnPosition & ball(BallDist, BallDir) <-
    if (BallDist > 20 | not BallDir == 0) {
        turn(BallDir);
		!waitDefendBall;
    }
    else {
		//reach ball
        dash(100);
        -atOwnPosition;
		!reachBall;
    }.

//Reach ball
+!reachBall : ball(BallDist, BallDir) <-
	if (not BallDir == 0) {
		turn(BallDir);
		!reachBall;
	}
	elif (BallDist > 1) {
		dash(100);
		!reachBall;
	}
	elif (BallDist <= 1) {
		!findOpponentGoal;
	}.
+!reachBall : not ball(BallDist, BallDir) <-
	turn(40);
	!reachBall.
			
	
//looking for Opponent Goal 
+!findOpponentGoal : not opponentGoal(GoalDist, GoalDir) <-
     turn(40);
     !findOpponentGoal.
     
+!findOpponentGoal : opponentGoal(GoalDist, GoalDir) <-
     !findCenterPlayer.
	
//looking for other player in order to pass the ball

// trying to find nearest player to the opponent goal
+!findCenterPlayer : not ownPlayer(PlayerDist,PlayerDir) <-
	 turn(40);
	 !findRightPlayer.
+!findCenterPlayer :  ownPlayer(PlayerDist,PlayerDir) <-
	if (not PlayerDir == 0) {
		turn(PlayerDir);
		!passBall;
	}
	else  {
   		!passBall;
	}.
// trying to find player in the right side 
+!findRightPlayer : not ownPlayer(PlayerDist,PlayerDir) <-
	 turn(280);
	 !findLeftPlayer.
+!findRightPlayer :  ownPlayer(PlayerDist,PlayerDir) <-
	if (not PlayerDir == 0) {
		turn(PlayerDir);
		!passBall;
	}
	else  {
   		!passBall;
	}.
// trying to find player in the left side
+!findLeftPlayer : not ownPlayer(PlayerDist,PlayerDir) <-
	!findOpponentGoaltoKick.
+!findLeftPlayer :  ownPlayer(PlayerDist,PlayerDir) <-
	if (not PlayerDir == 0) {
		turn(PlayerDir);
		!passBall;
	}
	else  {
   		!passBall;
	}.
//Kick the ball to the goal
//this state occurs if Defender couldn't find any player to pass the ball
+!findOpponentGoaltoKick : not opponentGoal(GoalDist, GoalDir) <-
     turn(40);
     !findOpponentGoaltoKick.
     
+!findOpponentGoaltoKick : opponentGoal(GoalDist, GoalDir) <-
     !kickBall.
     
//pass the ball to attacker
+!passBall <-
	kick(10*PlayerDist, 0);  //kick the ball according to the distant of other player
	!runBackAtOwnPosition.
	
//Kick ball
+!kickBall <- 
	kick(100, 0);
	!runBackAtOwnPosition.

//run back
+!runBackAtOwnPosition : not ownPosition(PositionDist, PositionDir) <-
	turn(40);
	!runBackAtOwnPosition.
	
+!runBackAtOwnPosition : ownPosition(PositionDist, PositionDir) <-
	if (not PositionDir == 0) {
		turn(PositionDir);
		!runBackAtOwnPosition;
	}
	elif (PositionDist > 3) {
		dash(100);
		!runBackAtOwnPosition;
	}
	else {
		+atOwnPosition;
		!waitDefendBall;
	}.