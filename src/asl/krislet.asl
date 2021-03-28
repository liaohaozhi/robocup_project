
//Initial Facts
!start.

//Rules

//Plans

// if the ball is visible, then we can determine if it is near or far,
// and we can determine if we are facing the ball (or should turn to the ball)
+!start : ballVisible(BallDist, BallDir) <-
	if (BallDir == 0) {
		if (BallDist > 1){
			// if we are facing the ball and it is far away, we want to run towards it.
			dash(100);.wait(200);
		} else {
			// if we are facing the ball and close to it, we can kick it
			kick(100, 0);.wait(200);
		};
	} else {
		turn(BallDir);.wait(200);
	};!start.

// if the ball is not visible, we should turn to look for it
+!start : not ballVisible(BallDist, BallDir) <-
	turn(40);.wait(200); !start.
