
//Plans

//want to see ball if not see ball
+gameStart <- !see(ball).
//keep turning to see ball 
+!see(ball) <- turn ; .wait(200); !see(ball).

