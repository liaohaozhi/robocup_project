
//Plans

//want to see ball if not see ball
+noSeeBall <- !see(ball).
//keep turning to see ball 
+!see(ball) <- turn ; .wait(200); !see(ball).

