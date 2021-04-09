
//intentions
!start.

// if the game hasn't started yet, then we can place the agent somewhere
+!start : connected & started_at_before_kick_off <-
	.random(RandX); .random(RandY);
	move(-1 * RandX * 15, 34 - RandY * 68.0);
	!reachBall.
	
// if the game has already started once the agent is connected, we don't move it anywhere
+!start : connected & not started_at_before_kick_off <-
	!reachBall.
	
// if the agent hasn't connected to the server, wait until this happens
+!start : not connected <-
	skip; // this line doesn't do anything besides wait for the next simulation cycle to see if the agent is now connected
	!start.


//plans

	
// if the ball is not visible, turn to try and find it
+!reachBall : not ballVisible(BallDist, BallDir) & not ballClose <-
	if (ballRightOfPlayer){
		turn(30);
	} else {
		turn(-30);
	};!reachBall.
			
+!reachBall : ballVisible(BallDist, BallDir) & not ballClose  <-
	if (BallDir < 15 & BallDir > -15){
		if (BallDist < 1){
			+ballClose;
			!passOrKeppBall;
		} else {
			dash(100);
		}
	} elif (BallDir > 0) {
		+ballRightOfPlayer;
		turn(BallDir);
	} elif (BallDir < 0) {
		-ballRightOfPlayer;
		turn(BallDir);
	};!reachBall.
	

//decide whether keep the ball or pass it
+!passOrKeepBall : ballClose &	not playerVisible(PlayerTeam, PlayerNum, PlayerDist, PlayerDir) <-
	!keepBall.
	
+!passOrKeepBall : ballClose & playerVisible(PlayerTeam, PlayerNum, PlayerDist, PlayerDir) <-
	if(not PlayerTeam == "test"){
		if(PlayerDist < 10){
			+opponentclose;
			!passBall;
		}else{
			-opponentclose;
			!keepBall;
		}
	}.
	
// if the ball is close and opponent goal is visible, kick the ball at the goal
// if the goal is close enough. Otherwise dribble the ball
+!keepBall : ballClose & lside & goalrVisible(GoalDist, GoalDir) & not opponentClose <- 
	if (GoalDist > 25){
		kick(20, 0);
	} else {
		kick(100, GoalDir);
	}
	-ballClose;
	!reachBall.
	
+!keepBall : ballClose & rside & goallVisible(GoalDist, GoalDir) & not opponentClose <- 
	if (GoalDist > 25){
		kick(20, 0);
	} else {
		kick(100, GoalDir);
	}
	-ballClose;
	!reachBall.	

//now player should look for a team-mate	
+!passBall : ballClose & lside & not goalrVisible(GoalDist, GoalDir)<-
	turn(40);
	!passBall.
+!passBall : ballClose & rside & not goallVisible(GoalDist, GoalDir)<-
	turn(40);
	!passBall.  

+!passBall : ballClose & lside & goalrVisible(GoalDist, GoalDir) <-
     !findCenterPlayer. 
+!passBall : ballClose & rside & goallVisible(GoalDist, GoalDir) <-
     !findCenterPlayer.     
	
// try to find a center attacker in order to pass the ball
+!findCenterPlayer : ballClose & not playerVisible(PlayerTeam, PlayerNum, PlayerDist, PlayerDir) <-
	 turn(40);
	 !findRightPlayer.
	 
+!findCenterPlayer : ballClose & playerVisible(PlayerTeam, PlayerNum, PlayerDist, PlayerDir) <-
	if ( PlayerTeam=="test"){    // checking if the player is team mate or not
		if (not PlayerDir == 0) {
			turn(PlayerDir);
			!passBallTeammate;
		}
		else  {
	   		!passBallTeammate;
		}
	}
	else{
		 turn(40);
	 	!findRightPlayer;
	}.
	
// try to find an attacker on right-hand side in order to pass the ball
+!findRightPlayer : ballClose & not playerVisible(PlayerTeam, PlayerNum, PlayerDist, PlayerDir) <-
	 turn(-80);
	 !findLeftPlayer.
	 
+!findRightPlayer : ballClose & playerVisible(PlayerTeam, PlayerNum, PlayerDist, PlayerDir) <-
	if ( PlayerTeam=="test"){
		if (not PlayerDir == 0) {
			turn(PlayerDir);
			!passBallTeammate;
		}	
		else  {
   			!passBallTeammate;
   		}
   	
   	}
   	else{
   		turn(-80);
	 	!findLeftPlayer;
	}.
	
// try to find an attacker on left-hand side in order to pass the ball
+!findLeftPlayer : ballClose & not playerVisible(PlayerTeam, PlayerNum, PlayerDist, PlayerDir) <-
	!findOpponentGoaltoKick.
+!findLeftPlayer : ballClose & playerVisible(PlayerTeam, PlayerNum, PlayerDist, PlayerDir) <-
	if ( PlayerTeam=="test"){
		if (not PlayerDir == 0) {
			turn(PlayerDir);
			!passBallTeammate;
		}
		else  {
	   		!passBallTeammate; 		
	   		}
	 }
	 else{
		!keepBall; //keep it and then kick it towards the goal
	}.
	  
     
//pass the ball to team mate
+!passBallTeammate : playerVisible(PlayerTeam, PlayerNum, PlayerDist, PlayerDir) <-
	kick(3*PlayerDist, 0); 
	!reachBall.
	


