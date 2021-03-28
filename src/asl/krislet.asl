
//Initial Facts
!start.

//Rules

//Plans

// if the ball is visible, then we can determine if it is near or far,
// and we can determine if we are facing the ball (or should turn to the ball)
+!start : ballVisible(ballDist, ballDir) <-
	if (ballDir == 0) {
		+facingBall;
	} else {
		turn(ballDir);
	};
	if (ballDist > 1){
		+ballFar(ballDist);
	} else {
		+ballclose;
	}; !start.
	
// if we are facing the ball and it is far away, we want to run towards it.
//
// the power of the dash is proportional to how far we need to run, 
// so hopefully we do not run past the ball
+!start : facingBall & ballFar(ballDist) <-
	dash(5 * ballDist); !start.

// if we are facing the ball and close to it, we can kick it
+!start : facingBall & ballClose <-
	kick(100, 0); !start.

// if the ball is not visible, we should turn to look for it
+!start : not ballVisible(ballDist, ballDir) <-
	turn(40); !start.
