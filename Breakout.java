/*
 * File: Breakout.java
 * -------------------
 * This file will eventually implement the game of Breakout.
 *
 * TODO: Update this file with a description of what your program
 * actually does!
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;
	public void init() {
		setSize(APPLICATION_WIDTH,APPLICATION_HEIGHT);
	} //set size end
	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;
	private static final int BALL_DIAM = 20;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;
	
	//fields
	GRect paddle = new GRect(0, 0, PADDLE_WIDTH,PADDLE_HEIGHT);
	private double vx, vy;
	
	private void addRect(double x, double y, double width, double height, int l) {
		GRect brick = new GRect(x,y,width,height);
		brick.setFilled(true);
		if (l<2) {
			brick.setColor(Color.red);
		} else if(l<4) {
			brick.setColor(Color.orange);
		} else if (l<6) {
			brick.setColor(Color.yellow);
		} else if (l<8) {
			brick.setColor(Color.green);

		} else if (l<10) {
			brick.setColor(Color.cyan);
		}
		add(brick); 
		
		//end if else
	}// end add rect
	private boolean linearSearch(GObject target, GObject[] itemList) {
		for (int i=0; i<itemList.length;i++) {
			if (itemList[i] == target) {
				return true; 
			}
		}
		return false;
	}
	private boolean linSearchPow(GObject target, GObject[] items) {
		for (int i= 0; i<items.length;i++) {
			if (items[i] == target) {
				return true;
			}
		}
		return false;
	  }
	 
	public void run() {
		/* You fill this in, along with any subsidiary methods */
		addMouseListeners();
		
		paddle.setLocation((getWidth()-PADDLE_WIDTH)/2, getHeight()-PADDLE_Y_OFFSET);
		paddle.setFilled(true);
		add(paddle); 
		GOval ball = new GOval((getWidth()/2-BALL_RADIUS), getHeight()/2-BALL_RADIUS,BALL_RADIUS*2,BALL_RADIUS*2);
		ball.setFilled(true);
		add(ball);
		
		for ( int i = 0; i<NBRICK_ROWS; i++) {
			for (int j = 0; j<NBRICKS_PER_ROW; j++) {
				addRect(i*(BRICK_WIDTH+BRICK_SEP),(j*(BRICK_HEIGHT+BRICK_SEP))+BRICK_Y_OFFSET,BRICK_WIDTH,BRICK_HEIGHT,j);
			}
		}
		
		GObject[] unbreakable = new GObject[5];
		GObject[] powerUps = new GObject[2];
		int listIndex = 0;
		int listIndexPow = 0;
		
		RandomGenerator rgen = RandomGenerator.getInstance();
		vx = rgen.nextDouble(1.0,3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		vy=3;
		
		int brickCount = NBRICK_ROWS * NBRICKS_PER_ROW;
		boolean gameOn = true;
		int gameCount = NTURNS;
		
		GLabel livesCounter = new GLabel("Lives: " +gameCount,10,35);
		livesCounter.setFont("Comic Sans-35");
		add(livesCounter);
		
		int pauseTime = 20;
		int paddleCounter = 0;
		
		for( int i = 0; i< NBRICKS_PER_ROW; i++) {
			boolean isPow = rgen.nextBoolean(0.5); 
			if (isPow == true) {
				
				GRect powers = new GRect(i*(BRICK_WIDTH+BRICK_SEP),(9*(BRICK_HEIGHT+BRICK_SEP))+BRICK_Y_OFFSET,BRICK_WIDTH,BRICK_HEIGHT);
				powers.setColor(Color.magenta);
				powers.setFilled(true);
				add(powers);
			}
		}
//		if (isPow == true && listIndexPow<2 && linSearchPow(collider,powerUps)== false) {
//			collider.setColor(Color.magenta);
//			powerUps[listIndexPow] = collider;
//			listIndexPow +=1;
//			remove(collider);
//			
//		} 
		while (gameOn) {
			ball.move(vx, vy);
			pause(pauseTime);
			GObject collider = getCollideObject(ball.getX(),ball.getY());
			
			
			
			// tests if it hit the sides
			if(ball.getX()>getWidth()-BALL_DIAM || ball.getX() < 0) {
				vx = -vx;
			} 
			//tests if it hit the top
			if( ball.getY() < 0   ) {
				vy= -vy;
				ball.setColor(Color.black);
				
			} 
			//tests if it hit the bottom and makes it so that the game ends if it does
			if ( ball.getY() > getHeight()-BALL_DIAM) {
				gameCount--;
				pauseTime = 15;
				ball.setLocation((getWidth()/2-BALL_RADIUS), getHeight()/2-BALL_RADIUS);
				livesCounter.setLabel("Lives: "+gameCount);
				ball.setColor(Color.black);
				if( gameCount == 0) {
					gameOn = false;
					GLabel lose = new GLabel("You Lose");
					lose.setFont("Comic Sans-56");
					lose.setLocation((WIDTH-lose.getWidth())/2,getHeight()/2);
					add(lose);
					ball.setVisible(false);
				}// tests if you lose
				waitForClick();
				
			}// end of if-elseif
			
			//tests if you hit something else
			if (collider !=null && collider != livesCounter ) {
				
				if (collider == paddle) {
					paddleCounter++;
					ball.setColor(Color.black);
					if (vy > 0) {
						vy=-vy;
					}
					if (paddleCounter % 5 ==0 && pauseTime >=5) {
						pauseTime-=2.5;
					}
				}
				if (collider != paddle  ) {
						if (collider.getColor()!=Color.magenta) {
							if (ball.getColor() != Color.red) {
								vy = -vy;
								boolean isUnbreakable = rgen.nextBoolean(1);
								
								if (isUnbreakable == true && listIndex<5&& linearSearch(collider, unbreakable) == false) {
									collider.setColor(Color.gray);
									unbreakable[listIndex] = collider;
									listIndex += 1;
							
								}
								if (linearSearch(collider, unbreakable) == false) {
									brickCount--;
									remove(collider);
								}
								if (linearSearch(collider, unbreakable) ==true) {
									brickCount--;
									remove(collider);
								}
							} else {
								remove(collider);
								brickCount--;
							}
							
							
						} else {
							ball.setColor(Color.red);
							remove(collider);
						}
						
				
						
					
					
					
						

					
				}
			} // ends if 
			
			// tests if you win and ends game
			if (brickCount == 0) {
				gameOn = false;
				GLabel win = new GLabel("You Win");
				win.setFont("Comic Sans-56");
				win.setLocation((WIDTH-win.getWidth())/2,getHeight()/2);
				add(win);
			}// end of win tester
			
			
		}//while for movement end
	}// run end
	public void mouseMoved(MouseEvent e) {
		if (e.getX()+PADDLE_WIDTH < getWidth()) {
			paddle.setLocation(e.getX(),getHeight()-PADDLE_Y_OFFSET);

		} else {
			paddle.setLocation(getWidth()-PADDLE_WIDTH,getHeight()-PADDLE_Y_OFFSET);
		}//if -  else end
	} //mouse moved end
	private GObject getCollideObject(double x, double y) {
		GObject coll = getElementAt(x,y);
		GObject coll1 = getElementAt(x+BALL_DIAM,y);
		GObject coll2 = getElementAt(x+BALL_DIAM,y+BALL_DIAM);
		GObject coll3 = getElementAt(x,y+BALL_DIAM);
		
		if (coll != null) {
			return coll;
		}else if (coll1 != null) {
			return coll1;
		} else if(coll2 != null) {
			return coll2;
		} else if(coll3 != null) {
			return coll3;
		} else {
			return null;
		} //end if else for collissions
		
		
	}// end collisions tester
	
}//class end
