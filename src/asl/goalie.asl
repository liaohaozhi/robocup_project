//beliefs
atGoal.

//intentions
!waitSaveBall.

//plans

//Waiting at goal
+!waitSaveBall : atGoal & not ball(BallDist, BallDir) <-
    turn(40);
    !waitSaveBall.
+!waitSaveBall : atGoal & ball(BallDist, BallDir) <-
    if (BallDist > 20 | not BallDir == 0) {
        turn(BallDir);
		!waitSaveBall;
    }
    else {
		//reach ball
        dash(100);
        -atGoal;
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
		!clearBall;
	}.
+!reachBall : not ball(BallDist, BallDir) <-
	turn(40);
	!reachBall.
	
//clear ball
//todo: add clear plan, pass the nearest player or kick to opposite side
+!clearBall <- 
	kick(100, 0);
	!runBackAtOwnGoal.

//run back
+!runBackAtOwnGoal : not ownGoal(GoalDist, GoalDir) <-
	turn(40);
	!runBackAtOwnGoal.
	
+!runBackAtOwnGoal : ownGoal(GoalDist, GoalDir) <-
	if (not GoalDir == 0) {
		turn(GoalDir);
		!runBackAtOwnGoal;
	}
	elif (GoalDist > 5) {
		dash(100);
		!runBackAtOwnGoal;
	}
	else {
		turn(180); //turn back
		+atGoal;
		!waitSaveBall;
	}.