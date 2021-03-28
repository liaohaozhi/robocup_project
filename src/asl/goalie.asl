//intents

//plans

//idle
+gameStart <- !waitSaveBall.
+!waitSaveBall: atGoal <- turnToBall; .wait(200); !waitSaveBall.
+!waitSaveBall: not atGoal <- runBackToGoal; .wait(200); !waitSaveBall.

//save ball
+saveBall <- !saveBall.
+!saveBall : not ballClose <- runToBall; .wait(200); !saveBall.
+!saveBall : ballClose <- kickBall; .wait(200); !waitSaveBall.