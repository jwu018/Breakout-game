import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*; 


public class flappym extends GraphicsProgram{
	
	//fields
	private GImage dinor = new GImage("./dino.png");
	private GImage ptero = new GImage("./ptero.png");
	private GImage terr = new GImage("./dinoTerr.png");
	boolean gameOn = true;
	private GLabel lose = new GLabel("You Lose");
	private GLabel score = new GLabel("Score: 0000",-20,-20);
	private GLabel powerTime = new GLabel("Power time left: 0s");
	private GRect newScreen = new GRect(800,500);
	private GImage dvd = new GImage("./steve.jpg");
	private GLabel toStart = new GLabel("Click To Start");
	private boolean inAir = false;
	private boolean started = false;
	
	boolean up = false;
	boolean down = false;
	RandomGenerator rgen = RandomGenerator.getInstance();
	
	private int moveSpeed = -2;

	
	public void init() {
		setSize(800,500);
	}//ends the init
	
	public void run() {
		addMouseListeners();
		
		//adds the background
		terr.setLocation(0,(getHeight()-terr.getHeight())/2-35);
		add(terr);
		
		//adds the dinasour image to the screen with a function
		dinor.scale(0.2);
		dinor.setLocation(40,(getHeight()-dinor.getHeight())/2);
		add(dinor);
		
		//add the pterodactyl
		ptero.scale(0.4);
		ptero.setLocation((getWidth())/2+50,0-ptero.getHeight());
		add(ptero);
		
		//adds the beginning label to prompt the user to click
		toStart.setFont("Comic Sans-52");
		toStart.setLocation((getWidth()-toStart.getWidth())/2,getHeight()-toStart.getHeight());
		add(toStart);
		
		// sets up the labels that will be used later
		lose.setFont("Comic Sans-64");
		powerTime.setFont("Comic Sans-24");
		score.setFont("Comic Sans-24");
		score.setLocation(getWidth()-score.getWidth()-50, 50);
		powerTime.setLocation(0+50, 50);
		add(score);
		add(powerTime);
		
		int ticks = 0;
		int powerTicks = 0;
		int cactCount = 0;
		boolean powerUp = false;
		int scoreCount = 0;
		//boolean for later to check if it is in air - work on it 
		
		
		//initializes the object for cactus later on
		GObject it = null;
		println(it);
		
		waitForClick();
		
		remove(toStart);
		started=true;
		while (gameOn) {
			if (cactCount <2) {
				it = addCacti();
				cactCount++;
			}// ends if
			
			if (it != null && it.getX() > 0 - it.getWidth()) {
				it.move(moveSpeed, 0);
				pause(1.5);
			} else {
				cactCount--;
				scoreCount += 100;
				if(scoreCount< 1000) {
					score.setLabel("Score: 0"+scoreCount);
				} else {
					score.setLabel("Score: " +scoreCount);
					if (scoreCount % 500 ==0) {
						moveSpeed--;
					}
				}
				
			}
			
			//moves the dinosaur up and down if it is true
			if(up && !down) {
				inAir = true;
				if (ticks <65) {
					dinor.move(0,-3);
					pause(0.5);
					ticks+=1;
				} else {
					up = false;
					down = true;
					ticks = 0;
				} //ends the if else statement
				
			}//ends the if
			
			//for moving down
			
			if (down && !up) {
				if (ticks <65) {
					dinor.move(0, 3);
					pause(0.5);
					ticks+=1;
				} else {
					down = false;
					inAir = false;
					ticks = 0;
				}//ends the if else statement 
				
			}//ends the if for down

			//to check if the dinosour hits anything while running
			boolean check = getCollisionObject(dinor);
			if (check && powerUp == false) {
				gameOn = false;
				lose.setLocation((getWidth()-lose.getWidth())/2,getHeight()-lose.getHeight());
				add(lose);
				pause(2000);
			}// ends if to check for collisions and setting game to lose
			
			//gives a chance of gaining a power up and spans ptero
			if (!powerUp) {
				powerUp = rgen.nextBoolean(0.001);

			}//ends if to check if power up has already been activated
			if (powerUp) {
				
				
				if (powerTicks <1500) {
					powerTicks++;
					powerTime.setLabel("Power Time Left: "+ (15-(powerTicks/100)) + "s");
					if (ptero.getX()>getWidth()/2-80){
						ptero.move(-2, 2);
						
					}
				} else {
					
					if (ptero.getX()<(getWidth())/2+50) {
						ptero.move(2, -2);
						println("Hi");
						
					} else {
						powerUp = false;
					}
				} // ends if for checkking how long the powerup lasts
				
				
			} // ends if for powerups
			
			
		}//ends the while to see if for gameOn
		
		if(!gameOn) {
			
			//adds the new screen for an end game animation
			newScreen.setFilled(true);
			newScreen.setColor(Color.black);
			add(newScreen);
			dvd.scale(0.5);
			dvd.setLocation(0,0);
			add(dvd);
			
			double dx = 1;
			double dy = 1.5;
			
			while(true) {
				dvd.move(dx,dy);
				pause(15);
				if (dvd.getX() <0 || dvd.getX()>getWidth()-dvd.getWidth()) {
					dx = -dx;
				} else if(dvd.getY()<0 || dvd.getY() > getHeight()-dvd.getHeight()) {
					dy= -dy;
				}
			}
		}
		
		
	}// ends the run
	
	//function to see which object the dinosaur will hit. 
	private boolean getCollisionObject(GImage image) {
		for (int j = (int)image.getY(); j<image.getY()+image.getHeight();j++) {
			for (int i= (int) image.getX(); i<image.getX()+image.getWidth();i++)  {
				GObject coll = getElementAt(i,j);
				if (coll != null && coll != terr && coll != dinor && coll != ptero && coll!=powerTime) {
					return true;
				}
			}
		}//ends the if else to see whhich object it hit
		return false;

	}// ends the method to get the collision object
	
	private GObject addCacti() {
		GImage SCact = new GImage("./cactS.png");
		GImage LCact = new GImage("./cactL.png");
		GImage Cact2 = new GImage("./cact2.png");
		
		LCact.scale(0.4);
		SCact.scale(0.6);
		Cact2.scale(0.6);
		//creates booleans to see which one to generate
		int cactI = rgen.nextInt(0,2);
		GImage[] cactuss = {SCact, Cact2, LCact};
		if(cactuss[cactI]== Cact2) {
			cactuss[cactI].setLocation(getWidth()+100, getHeight()/2-10);

		} else if(cactuss[cactI]==LCact) {
			cactuss[cactI].setLocation(getWidth()+100, getHeight()/2-25);

		} else {
			cactuss[cactI].setLocation(getWidth()+100, getHeight()/2-5);

		} // end of if else to set the location specific to the different size of cactuses
		add(cactuss[cactI]);
		
		return cactuss[cactI];
		 // ends if else to see which cacti to add
		
		
	}//ends addCacti
	
	@Override public void mousePressed(MouseEvent e) {
			
			//sets a variable to tell whether it is supposed to be moving up or not
		if(started) {
			if (!inAir) {
				up = true;

			}
		}
			
		
	}// ends the mouse clicked event
	
}//ends the whole class
