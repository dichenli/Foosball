import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class CIT_591_HW8 extends PApplet {

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

	public void drawBalls() {
	    for (Ball ball : balls) {
	    ball.drawBall();
	  }
	}

	public void moveBalls() {
	    for (Ball ball : balls) {
	    ball.moveBall(players);
	  }
	}

	public void drawScore() {
	  fill(0, 0, 0);
	  textSize(24);
	  text("Your Score: " + p1.score, 20, height - 30);
	  text("Computer's Score: " + p2.score, 20, 30);
	}

	public void drawEnding() {
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

	public void drawTime(){
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
	
	//start of class keeper and class ball
	class Keeper extends PApplet  {
		float x, y, dx, dy;
		int diameter = 40;
		int radius = diameter / 2;
		int upperBound;
		int lowerBound;
		int score;
		
		public Keeper(float initX, float initY, int upper, int lower, float initDX, float initDY) {
			x = initX;
			y = initY;
			dx = initDX;
			dy = initDY;
			upperBound = upper;
			lowerBound = lower;
			score = 0;
		}	
		
		public void drawKeeper() {
		    fill(255, 100, 100);
	    		ellipse(x, y, diameter, diameter);
		}
		
		public void moveByMouse() {
	    		x += dx;
	    		y += dy;
			dx = ease(mouseX - x);
			dy = ease(mouseY - y);
			if (x <= radius || x >= width - radius) dx = -dx;
		    if (y <= upperBound - radius || y >= lowerBound + radius) dy = -dy;
	  	}
		
		private float ease(float n) {
			return n / 15;
		}
	  	
		public void moveComputer() {
	  		x += dx;
	    		y += dy;
	    		if (x <= gateLeft || x >= gateRight) dx = - dx;
	  	}
	} //End of class Keeper
	
	
	
	
	class Ball extends PApplet {
		  float x, y, dx, dy;
		  int diameter = 10;
		  int radius = diameter / 2;
		  boolean isGoal = false;
		  
		  public Ball(int lowerBound, int upperBound) {
		    x = random(width);
		    y = random(lowerBound, upperBound);
		    dx = 0;
		    dy = 0;
		    do {
		      dx = random(-5, +5);
		      dy = random(-5, +5);
		    } while (dx == 0 && dy == 0);
		  }
		  
		  public void drawBall() {
		  	if (isGoal) return;
		    fill(255);
		    ellipse(x, y, diameter, diameter);
		  }
		  
		  public void moveBall(ArrayList<Keeper> players) { //
		    x += dx;
		    y += dy;
		    isGoal = goal();
		    if (isGoal) return;
		    for (Keeper player : players) {
		    		if (sqrt((player.x - this.x) * (player.x - this.x) + (player.y - this.y) * (player.y - this.y)) <= player.radius + this.radius) {
		    			this.dx = -(this.dx + player.dx * 2);
		    			this.dy = -(this.dy + player.dy * 2);
		    		}
		    }	
		    if (x <= radius || x >= width - radius) dx = -dx;
		    if (y <= radius || y >= height - radius) dy = -dy;
		  }
		  
		  public boolean goal() {
		  	if ((y >= height - radius || y <= radius) && x <= gateRight && x >= gateLeft) 
		  	{
		  		if (y >= height - radius) p1.score += 1;
		  		else p2.score += 1;
		  		return true;
		  	}
		  	else return false; //can we remove it?
		  }
		  
		  public void removeBall() {
		  	assert isGoal;
		  	x = random(-300,-100);
		  	y = random(-300, -100);
		  	dx = 0;
		  	dy = 0;
		  }
	} //End of class Ball
    static public void main(String args[]) {
        PApplet.main(new String[] { "--bgcolor=#ECE9D8", "CIT_591_HW8" });
    }
}
