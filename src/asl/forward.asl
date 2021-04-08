
//Initial Facts
!start.

//Plans

// if the game hasn't started yet, then we can place the agent somewhere
+!start : connected & started_at_before_kick_off <-
	.random(RandX); .random(RandY);
	move(-1 * RandX * 15, 34 - RandY * 68.0);
	!play.
	
// if the game has already started once the agent is connected, we don't move it anywhere
+!start : connected & not started_at_before_kick_off <-
	!play.
	
// if the agent hasn't connected to the server, wait until this happens
+!start : not connected <-
	skip; // this line doesn't do anything besides wait for the next simulation cycle to see if the agent is now connected
	!start.

// if the ball is not visible, turn to try and find it
+!play : not ballVisible(BallDist, BallDir) & not ballClose <-
	if (ballRightOfPlayer){
		turn(30);
	} else {
		turn(-30);
	};!play.

// if the ball is visible, then we can determine if it is near or far,
// and we can determine if we are facing the ball (or should turn to the ball)
+!play : ballVisible(BallDist, BallDir) & not ballClose & not hasBall <-
	if (BallDir < 15 & BallDir > -15){
		if (BallDist < 1){
			+ballClose;
			+hasBall;
		} else {
			dash(100);
		}
	} elif (BallDir > 0) {
		+ballRightOfPlayer;
		turn(BallDir);
	} elif (BallDir < 0) {
		-ballRightOfPlayer;
		turn(BallDir);
	};!play.
	
// if the ball is close and opponent goal is visible, kick the ball at the goal
// if the goal is close enough. Otherwise dribble the ball
+!play : ballClose & hasBall & lside & goalrVisible(GoalDist, GoalDir) & not opponentClose<- 
	if (GoalDist > 25){
		kick(20, 0);
	} else {
		kick(100, GoalDir);
	}
	-ballClose;
	-hasBall
	!play.
	
+!play : ballClose & hasBall & rside & goallVisible(GoalDist, GoalDir) & not opponentClose <- 
	if (GoalDist > 25){
		kick(20, 0);
	} else {
		kick(100, GoalDir);
	}
	-ballClose;
	-hasBall;
	!play.
	
// if the ball is close and opponent goal is not visible, turn to look for the goal
+!play : ballClose & lside & not goalrVisible(GoalDist, GoalDir)  <- 
	turn(30);
	!play.
+!play : ballClose & rside & not goallVisible(GoalDist, GoalDir)  <- 
	turn(30);
	!play.
	

//if the ball is close and player has the ball and the player sees an opponent close to him
//so if he can see his team-mate, try to pass him
+!play : ballClose & hasBall & playerVisible(PlayerTeam, PlayerNum, PlayerDist, PlayerDir) <-
	if ( PlayerTeam == "test"){ //if they are in the same team
		if(PlayerNum > 2 ){  // I assume player number less than 2 are defenders, and player numbers greater than 2 are forwards (should ask)
			if (not PlayerDir == 0) {
				turn(PlayerDir);
				+readyToPass
				!passBall;
			}
			else  {
				+readyToPass
		   		!passBall;
			}
		}
	}
	else{
	 	 if(PlayerDist < 10){
	 	 	+opponentClose
	 	 }else {
	 	 	-opponentClose
	 	 }
	}; !play.



//pass the ball to an attacker
+!passBall : readyToPass & opponentClose & playerVisible(PlayerTeam, PlayerNum, PlayerDist, PlayerDir) <-
	kick(2*PlayerDist, PlayerDir); 
	-ballClose;
	-hasBall;
	-readyTopass;
	-opponentClose;
	!play.	
		
