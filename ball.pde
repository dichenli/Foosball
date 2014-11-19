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