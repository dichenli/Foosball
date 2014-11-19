	ArrayList<Ball> balls = new ArrayList();
	ArrayList<Keeper> players = new ArrayList();
	int time = 0;
	int minute, second;
	int scoreP1 = 0;
	int scoreP2 = 0;
	int ballNum = 10;
	int fps = 60;							//frames per second
	int maxTime = 3 * 60 * fps;		//game time (3 minutes by default)
	int gateLeft = 250;					// left boundaries of goal
	int gateRight = 350;					// right boundaries of goal
	int p2Bound = 200;					// Upper boundary for player 1, player1 cannot move across this line
	int p1Bound = height - p2Bound; // Lower boundary for player 2.
	boolean gameOver = false;
	//float p2X = 0;  							// Computer's mouse coordinates
	//float p2Y = 0;
	Keeper p1;
	Keeper p2;


	public void setup() {
	  	size(800, 600);
		p1 = new Keeper(width / 2, p1Bound / 2, p1Bound, height, 0, 0);
		//player 1 can only move between 600 and 800
		p2 = new Keeper(width / 2, height- p1Bound / 2,  p2Bound, 0, 10, 0);   
		//player2 can only move between 0 and 200
		//p2 initial speed: dx = 10.0
		players.add(p1);
		players.add(p2);
	  	frameRate(fps);
	  	//p2X = random(-200, width + 200);
	  	//p2Y = random(p2Bound -100, height + 100);
	}

	public void draw() {
	  if (gameOver) {
	    drawEnding(); // to draw what it is like when game over
	    return;
	  }
	  background(83, 214, 43); // green grass background
	  if (time % (maxTime * (2 / 3) / ballNum ) == 0) { 
	  	// Balls are created by the same time interval. All 10 balls will show up by 2/3 of the game time
	    balls.add(new Ball(p2Bound, p1Bound));
	  }
	  time += 1; 
	  fill(0); // Draw the two goals in black
	  rect(gateLeft, 10, gateRight - gateLeft, 10);
	  rect(gateLeft, height, gateRight - gateLeft, 10);  //Each goal is 10 in height
	  p1.drawKeeper();
	  p1.moveByMouse();
	  p2.drawKeeper();
	  p2.moveComputer();  // Computer makes move
	  //where we left out
	  
	  drawBalls();
	  moveBalls();
	  drawScore();
	  drawTime();
	  
	}

	void drawBalls() {
	    for (Ball ball : balls) {
	    ball.drawBall();
	  }
	}

	void moveBalls() {
	    for (Ball ball : balls) {
	    ball.moveBall(players);
	  }
	}

	void drawScore() {
	  fill(0, 0, 0);
	  textSize(24);
	  text("Your Score: " + p1.score, 20, height - 30);
	  text("Computer's Score: " + p2.score, 20, 30);
	}

	void drawEnding() {
		fill(0, 0, 0);
		textSize(24);
		drawScore();
		textSize(40);
		textAlign(CENTER);
		text("Game Over!", width / 2, height / 2);
		if (p1.score > p2.score) text("Congratulations! You Win!", width / 2, height / 2 + 50);
		else if (p2.score > p1.score) text("The Computer Wins!", width / 2, height / 2 + 50);
		else text("The Game Is A Draw!", width / 2, height / 2 + 50);
	    drawBalls();
	    p1.drawKeeper();
	    p2.drawKeeper();
	}

	void drawTime(){
		fill(0, 0, 0);
		textSize(24);
		minute = time/3600;
		second = time / 60 - minute * 60;
		text(minute + ":" + second, 20, height / 2);
	}
//	boolean collision() {
//	  for (Ball ball : list) {
//	    if (dist(meX, meY, ball.x, ball.y) < 20) return true;
//	  }
//	  return false;
//	}
	