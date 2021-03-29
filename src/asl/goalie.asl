//beliefs
atGoal.

//intentions
!waitSaveBall.

//plans

//Waiting at goal
//not see ball
+!waitSaveBall : atGoal & not ball(BallDist, BallDir) <-
    turn(40);
    !waitSaveBall.
//see ball
+!waitSaveBall : atGoal & ball(BallDist, BallDir) <-
    if (BallDist > 100 | not BallDir == 0) {
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
//see ball
+!reachBall : ball(BallDist, BallDir) <-
	if (not BallDir == 0) {
		turn(BallDir);
		!reachBall;
	}
	elif (BallDist > 1) {
		dash(100);
		!reachBall;
	}.
//not see ball
+!reachBall : not ball(BallDist, BallDir) <-
	turn(40);
	!reachBall.