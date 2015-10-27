//Carsten Scholz, 11218504, Guyan De Saram, 11828507 Assignment 1 Pool
import java.awt.*;      
import java.awt.geom.*; 
import java.awt.event.*;

import javax.swing.*;   
import javax.swing.event.*;

import java.util.*;
import java.io.*;

import javax.sound.sampled.*;
import javax.imageio.*;


public class Pool extends JPanel implements MouseMotionListener, MouseListener, KeyListener{
	//game states
	enum GameState {Menu, Playing, HowToPlay};
	GameState state = GameState.Menu;
	
	//Jframe
	JFrame frame;
	
	//sound Clips 
	Clip clip = null;
	Clip shortclip = null;
	boolean start = false ;
	
	
	int menuOption = 0;
	
	class Que{
		public double m_power = 0.0f;
		public double m_velX = 0.0f;
		public double m_velY = 0.0f;
		public double m_queX;
		public double m_queY;
		public boolean m_mousepressed = false;
		Que(double power, double queX, double queY){
			m_power = power;
			m_queX = queX;
			m_queY = queY;
		}
		void moveQue(boolean mousepress){
			m_mousepressed = mousepress;
		}
		void setPower(double power){
			m_power = power;
		}
		void setVelX(double _vx){
			m_velX = _vx;
		}
		void setVelY(double _vy){
			m_velY = _vy;
		}
		void setX(double _x){
			m_queX = _x;
		}
		void setY(double _y){
			m_queY = _y;
		}
		double getVelX(){
			return m_velX;
		}
		double getVelY(){
			return m_velY;
		}
		double getPower(){
			return m_power;
		}
		double getX(){
			return m_queX;
		}
		double getY(){
			return m_queY;
		}
		boolean getPressed(){
			return m_mousepressed;
		}
	}
	class Poolball{
		public double x;
		public double y;
		public double velocityX;
		public double velocityY;
		public char type;
		public double friction = 0.1f;
		public boolean bSunk = false;
		
		Poolball(double _x, double _y, double _vX, double _vY, char _type){
			x = _x;
			y = _y;
			velocityX = _vX;
			velocityY = _vY;
			type = _type;
		}
		double getX(){
			return x;
		}
		double getY(){
			return y;
		}
		double getVelocityX(){
			return velocityX;
		}
		double getVelocityY(){
			return velocityY;
		}
		void setX(double _x){
			x = _x;
		}
		void setY(double _y){
			y = _y;
		}
		void setVelocityX(double _vx){
			velocityX = _vx;
		}
		void setVelocityY(double _vy){
			velocityY = _vy;
		}
		char getType(){
			return type;
		}
		void friction(){
			velocityX /= 1.03f;
			velocityY /= 1.03f;
			if(velocityX < 0.05f && velocityX > -0.05f){
				velocityX = 0.0f;
			}
			if(velocityY < 0.05f && velocityY > -0.05f){
				velocityY = 0.0f;
			}
				
			
		}
		void sunk(){
			bSunk = true;
		}
		boolean isSunk(){
			return bSunk;
		}
	}
	class Barrier{
		public int width;
		public int height;
		public char type;
		public Barrier(int _width, int _height, char _type){
			width = _width;
			height = _height;
			type = _type;
		}
		int getWidth(){
			return width;
		}
		int getHeight(){
			return height;
		}
		char getType(){
			return type;
		}
	}
	class Pocket{
		public int x;
		public int y;
		public char type;
		Pocket(int _x, int _y, char _type){
			x = _x;
			y = _y;
			type = _type;
		}
		int getX(){
			return x;
		}
		int getY(){
			return y;
		}
		char getType(){
			return type;
		}
	}
	class Player{
		public char type = 'N';
		public boolean turn = false;
		public int numballsunk = 0;
		public double ballpos = 30;
		void NextTurn(){
			if(turn == false){
				turn = true;
			}else{
				turn = false;
			}
		}
		void setType(char _type){
			type = _type;
		}
		void ballsunk(){
			numballsunk++;
		}
	}
	
     int DISPLAY_WIDTH   = 1200;
     int DISPLAY_HEIGHT  = 800;
    
    int poolTable_width = 905;
    int poolTable_height = 500;
    
    int widthoffset = DISPLAY_WIDTH - poolTable_width;
    int heightoffset = DISPLAY_HEIGHT - poolTable_height;
    
	Color black = Color.BLACK;
	Color white = Color.WHITE;
	
	//Start point for racked pool balls
	int rackpointX = 655;
	int rackpointY = 250;
	
	//Start point for white pool ball
	int whiterackpX = 250;
	int whiterackpY = 250;
	
	boolean bRack = true;
	boolean poolShot = false;
	
	int mouseX = 0;
	int mouseY = 0;
	
	int ballposBM = 30;
	Que poolQue; 
	double angle;
	
	double power = 1.0f;
	boolean mousePressed = false;
	boolean bGamestart = false;
	boolean bGameWon = false;
	boolean bGameLost = false;

	int MAX_POOLBALLS = 16;
	Poolball[] ball = new Poolball[MAX_POOLBALLS];
	
	int MAX_BARRIERS = 6;
	Barrier[] barrier = new Barrier[MAX_BARRIERS];
	
	int MAX_POCKETS = 6;
	Pocket[] pocket = new Pocket[MAX_POCKETS];
	
	Player player1 = new Player();
	Player player2 = new Player();
	
	//The image holding all the sprites
	Image spriteSheet;
	Image poolTable;
	Image que;
	//image holding the menu button images 
	Image play;
	Image playHighlighted;
	Image exit;
	Image exitHighlighted;
	Image menuBackground;
	Image howtoplayBackground;
	Image howtoplay;
	Image howtoplayHighlighted;
	Image back;
	Image backHighlighted;

	int barrierHeight= 350;
	int barrierWidth = 11;
	

	
	//Keeps track of how many sprites are in the image
	int numSprites = 16;

	//Determines how many frames to display each sprite for
	int numFramesPerSprite = 4;

	//Keeps a track of how many sprites have been drawn
	int spriteCounter = 0;
	
	

	//Variables for the (x,y) and (width,height) of each
	//of the sprites inside the image
	int[] spriteX = new int[numSprites];
	int[] spriteY = new int[numSprites];
	int[] spriteW = new int[numSprites];
	int[] spriteH = new int[numSprites];
	
	//resize the window 
	
	
	//Changes the drawing Color to the color c
	public void changeColor(Graphics g, Color c) {
		g.setColor(c);
	}
	 //Functions to Draw Text on a window
    //Takes a Graphics g, position (x,y) and some text
    public void drawText(Graphics g, double x, double y, String s) {
        Graphics2D g2d = (Graphics2D)g;
		 changeColor(g,white);
        g2d.setFont(new Font("Broadway", Font.BOLD, 40));
        g2d.drawString(s, (int)x, (int)y);
    }

    //Converts an integer to a string
    public String convertIntegerToString(int i) {
        return new Integer(i).toString();
    }
	//This function translates the drawing context by (x,y)
	void translate(Graphics g, double x, double y) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.translate(x,y);
	}
	//This function rotates the drawing context by a degrees
	void rotate(Graphics g, double a) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.rotate(Math.toRadians(a));
	}
    
	//Function to create the window and display it
	public void setupWindow(int width, int height) {
		 frame = new JFrame();
        frame.setSize(width, height);
        frame.setLocation(200,200);
        frame.setTitle("Pool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);
        frame.addMouseMotionListener(this);
        frame.addMouseListener(this);
        frame.setVisible(true);
        setDoubleBuffered(true);

        //Resize the window (insets are just the boards that the Operating System puts on the board)
        Insets insets = frame.getInsets();
        frame.setSize(width + insets.left + insets.right,
        	height + insets.top + insets.bottom);
	}
	//resize the window
	public void resizeWindow(int width, int height) {
		frame.setSize(width, height);
        Insets insets = frame.getInsets();
        frame.setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);
	}
	
	//Returns the time in milliseconds
  	public long getTime() {
          return System.currentTimeMillis();
      }

  	//Waits for ms milliseconds
  	public void sleep(double ms) {
  		try {
  			Thread.sleep((long)ms);
  		} catch(Exception e) {
  			//Do Nothing
  		}
  	}
	public static void main( String args[] ){
		Pool p = new Pool();
	}
	
	//Two variables to keep track of how long has passed since each frame
	long time = 0, oldTime = 0;
	
	//Fixed way of controlling the framerate
	//Works out how long has passed since the last frame
	//and then sleeps for the extra time. This way the
	//game is updated at a fixed rate and doesn't bother
	//drawing extra frames that humans can't possibly see
	//anyway.
	public double fixedFramerate() {
		//Make sure oldTime is set up properly
		if(oldTime == 0) {
			oldTime = getTime();
		}

		//Get the current time
		time = getTime();
		
		//Work out how long has passed since the last frame
		double passed = time - oldTime;
		double expected = dt;

		if(expected > passed) {
			double extra = expected - passed;
			sleep(extra);
		}

		//Set the oldTime to the current time;
		oldTime = getTime();

		return dt;
	}
	
	
	//-------------------------------------------------------
	// Pool Program
	//-------------------------------------------------------
	//Used to keep track of how many ms have passed since last update;
	double dt = 1000.0 / 60;
	
	public Pool(){
		//super("Pool");
		setupWindow(DISPLAY_WIDTH,DISPLAY_HEIGHT);
		//Load the sprite sheet from the image
		poolTable = loadImage("PoolTable.png");
		que = loadImage("Que.png");
		//Load the menu buttons from the image
		play               = loadImage("playimage.png");
		playHighlighted    = loadImage("playimage_highlighted.png");
		howtoplay			 = loadImage("htp.png");
		howtoplayHighlighted = loadImage("htp_highlighted.png");
		exit               = loadImage("exitimage.png");
		exitHighlighted    = loadImage("exit_highlighted.png");
		back					 = loadImage("backimage.png");
		backHighlighted	 = loadImage("backimage_highlighted.png");
		
		menuBackground     = loadImage("backgroundimage.png");
		howtoplayBackground = loadImage("howtoplay_background.png");
		
		//Load the sprite sheet from the image
		spriteSheet = loadImage("SpriteSheet.png");
		
		//Set all of the positions and sizes of the sprites inside the 
		//sprite sheet image
		spriteX[0] = 0;   spriteY[0] = 0;   spriteW[0] = ballposBM; spriteH[0] = ballposBM;
		spriteX[1] = 0; spriteY[1] = ballposBM;   spriteW[1] = ballposBM; spriteH[1] = ballposBM;
		spriteX[2] = 0;   spriteY[2] = ballposBM * 2; spriteW[2] = ballposBM; spriteH[2] = ballposBM;
		spriteX[3] = 0; spriteY[3] = ballposBM * 3; spriteW[3] = ballposBM; spriteH[3] = ballposBM;
		spriteX[4] = 0;   spriteY[4] = ballposBM * 4; spriteW[4] = ballposBM; spriteH[4] = ballposBM;
		spriteX[5] = 0; spriteY[5] = ballposBM * 5; spriteW[5] = ballposBM; spriteH[5] = ballposBM;
		spriteX[6] = 0;   spriteY[6] = ballposBM * 6; spriteW[6] = ballposBM; spriteH[6] = ballposBM;
		spriteX[7] = 0; spriteY[7] = ballposBM * 7; spriteW[7] = ballposBM; spriteH[7] = ballposBM;
		spriteX[8] = 0; spriteY[8] = ballposBM * 8;   spriteW[8] = ballposBM; spriteH[8] = ballposBM;
		spriteX[9] = 0;   spriteY[9] = ballposBM * 9; spriteW[9] = ballposBM; spriteH[9] = ballposBM;
		spriteX[10] = 0; spriteY[10] = ballposBM * 10; spriteW[10] = ballposBM; spriteH[10] = ballposBM;
		spriteX[11] = 0;   spriteY[11] = ballposBM * 11; spriteW[11] = ballposBM; spriteH[11] = ballposBM;
		spriteX[12] = 0; spriteY[12] = ballposBM * 12; spriteW[12] = ballposBM; spriteH[12] = ballposBM;
		spriteX[13] = 0;   spriteY[13] = ballposBM * 13; spriteW[13] = ballposBM; spriteH[13] = ballposBM;
		spriteX[14] = 0; spriteY[14] = ballposBM * 14; spriteW[14] = ballposBM; spriteH[14] = ballposBM;
		spriteX[15] = 0; spriteY[15] = ballposBM * 15; spriteW[15] = ballposBM; spriteH[15] = ballposBM;
		
		//Initialise Pool Balls in rack position
		ball[0] = new Poolball((whiterackpX+widthoffset/2-ballposBM/2),(whiterackpY+heightoffset/2-ballposBM/2),0,0,'W');
		ball[1] = new Poolball((rackpointX+widthoffset/2-ballposBM/2+ballposBM*2),(rackpointY+heightoffset/2-ballposBM/2-ballposBM),0,0,'F');
		ball[2] = new Poolball((rackpointX+widthoffset/2-ballposBM/2+ballposBM*4),(rackpointY+heightoffset/2-ballposBM/2+ballposBM),0,0,'F');
		ball[3] = new Poolball((rackpointX+widthoffset/2-ballposBM/2+ballposBM*3),(rackpointY+heightoffset/2-ballposBM/2-ballposBM/2),0,0,'F');
		ball[4] = new Poolball((rackpointX+widthoffset/2-ballposBM/2+ballposBM*4),(rackpointY+heightoffset/2-ballposBM/2-ballposBM),0.0f,0.0f,'F');
		ball[5] = new Poolball((rackpointX+widthoffset/2-ballposBM/2+ballposBM*4),(rackpointY+heightoffset/2-ballposBM/2-(ballposBM*2)),0.0f,0.0f,'F');
		ball[6] = new Poolball((rackpointX+widthoffset/2-ballposBM/2+ballposBM*3),(rackpointY+heightoffset/2-ballposBM/2+(ballposBM/2) *3),0.0f,0.0f,'F');
		ball[7] = new Poolball((rackpointX+widthoffset/2-ballposBM/2+ballposBM),(rackpointY+heightoffset/2),0.0f,0.0f,'F');
		ball[8] = new Poolball((rackpointX+widthoffset/2-ballposBM/2+ballposBM*2),(rackpointY+heightoffset/2-ballposBM/2),0.0f,0.0f,'E');
		ball[9] = new Poolball((rackpointX+widthoffset/2-ballposBM/2),(rackpointY+heightoffset/2-ballposBM/2),0.0f,0.0f,'S');
		ball[10] = new Poolball((rackpointX+widthoffset/2-ballposBM/2+ballposBM*3),(rackpointY+heightoffset/2-ballposBM/2+ballposBM/2),0.0f,0.0f,'S');
		ball[11] = new Poolball((rackpointX+widthoffset/2-ballposBM/2+ballposBM*4),(rackpointY+heightoffset/2-ballposBM/2+(ballposBM*2)),0.0f,0.0f,'S');
		ball[12] = new Poolball((rackpointX+widthoffset/2-ballposBM/2+ballposBM),(rackpointY+heightoffset/2-ballposBM),0.0f,0.0f,'S');
		ball[13] = new Poolball((rackpointX+widthoffset/2-ballposBM/2+ballposBM*4),(rackpointY+heightoffset/2-ballposBM/2),0.0f,0.0f,'S');
		ball[14] = new Poolball((rackpointX+widthoffset/2-ballposBM/2+ballposBM*3),(rackpointY+heightoffset/2-ballposBM/2-(ballposBM/2) * 3),0.0f,0.0f,'S');
		ball[15] = new Poolball((rackpointX+widthoffset/2-ballposBM/2+ballposBM*2),(rackpointY+heightoffset/2-ballposBM/2+ballposBM),0.0f,0.0f,'S');
		
		barrier[0] = new Barrier(203,0,'L');
		barrier[1] = new Barrier(0,230,'T');
		barrier[2] = new Barrier(0,632,'B');
		barrier[3] = new Barrier(0,632,'B');
		barrier[4] = new Barrier(0,230,'T');
		barrier[5] = new Barrier(1000,0,'R');
		poolQue = new Que(0.0f,((ball[0].getX()+ballposBM)),(ball[0].getY()+ballposBM/2)-7);
		
		pocket[0] = new Pocket(210,210,'A');
		pocket[1] = new Pocket(210,590,'B');
		pocket[2] = new Pocket(600,200,'C');
		pocket[3] = new Pocket(600,600,'D');
		pocket[4] = new Pocket(992,210,'E');
		pocket[5] = new Pocket(992,590,'F');
		
		player2.NextTurn();
		while(true) {
				//read through rules
				rules();
			
				//Tell the window to paint itself
				repaint();	
			
				//Control the framerate
				dt = fixedFramerate();
			
		}
	
	}
	//Keeps track of how many frames have been drawn
	int numberOfFrames = 0;

	//*******************************************************************************************************************************************
	//
	//	Image Sprite Functions Below
	//
	//*******************************************************************************************************************************************
    //Loads an image from file
    public Image loadImage(String s) {
    	Image image = null;
    	try {
		    image = ImageIO.read(new File(s));
		} catch (IOException e) {
			System.out.println("Error: could not load image " + s);
			System.exit(1);
		}
		return image;
    }
    
    //Draws an image on the screen at position (x,y)
    public void drawImage(Graphics g, Image image, double x, double y) {
    	Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(image, (int)x, (int)y, null);
    }

    //Draws a sprite on the screen at position (x,y) with (width, height)
    //Reads from the Image image
    //Sprite is read from the position (x1,y1) and with (width1, height1)
    public void drawImage(Graphics g, Image image, double x, double y, double width, double height, int x1, int y1, int width1, int height1) {
    	Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(image, (int)x, (int)y, (int)(x+width), (int)(y+height), (int)x1, (int)y1, (int)(x1+width1), (int)(y1+height1), null, null);
    }
	//*******************************************************************************************************************************************
	//
	//	Sound Functions Below
	//
	//*******************************************************************************************************************************************
	//This function plays a sound file
    //Takes the name of the sound file and a volume in Decibels
		 
	 
    public void playSoundFile(String file_name, float volume) {
        try {
            File clipFile = new File(file_name);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(clipFile);
            shortclip = AudioSystem.getClip();
            shortclip.open(audioIn);
            FloatControl volumeControl = (FloatControl)shortclip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volume);

            shortclip.start();

        } catch(Exception e) {
            //clip.close();
        }
    }
	 //looping sound
	 public void playSoundFileLoop(String file_name, float volume) {
        try {
            File clipFile = new File(file_name);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(clipFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            
            FloatControl volumeControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volume);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch(Exception e) {
            //clip.close();
        }
    }
	 
	 //close sound
	 public void stopSound(Clip c){
		 if(c != null){
			 c.stop();
		 }
	 }
	 

	 
	//*******************************************************************************************************************************************
	//
	//	Game Rules Functions Below
	//
	//*******************************************************************************************************************************************
	public void rules(){
		if(bGamestart == true){
			if(poolQue.getPressed() == true){
				poolQue.setPower(power);
				if(poolQue.getPower() >= 50.0f){
					poolQue.setPower(50.0f);
				}else{
					power++;
				}	
			}
			if(poolQue.getPressed() == false && poolShot == true){
				double velX = (float)Math.cos(angle) * (poolQue.getPower()/2);
				double velY = (float)Math.sin(angle) * (poolQue.getPower()/2);
				ball[0].setVelocityX(-velX);
				ball[0].setVelocityY(-velY);
				poolShot = false;
				poolQue.setPower(0.0f);
				power = 0;
				player1.NextTurn();
				player2.NextTurn();
			}
			for(int i = 0; i < 16; i++){
				PocketCollision(i);
				BarrierCollision(i);
				BallCollision(i);
			}
			for(int j = 0; j < 16; j++){
				if(ball[j].getVelocityX() != 0.0f || ball[j].getVelocityY() != 0.0f){
					ball[j].setX(ball[j].getX()+(ball[j].getVelocityX()));
					ball[j].setY(ball[j].getY()+(ball[j].getVelocityY()));
					ball[j].friction();
				}
			}
			
			poolQue.setX(ball[0].getX()+ballposBM);
			poolQue.setY((ball[0].getY()+ballposBM/2)-7);
			
		}else{
			for(int j = 0; j < 16; j++){
				ball[j].setVelocityX(0);
				ball[j].setVelocityY(0);
			}
		}
	}
	//Collision detection between ball and pocket happens here
	public void PocketCollision(int i){
		boolean bsunk = false;
		boolean tempBsunk = false;
		int soundcount = 0;
			for(int j = 0; j < 6; j++){
				if((ball[i].getX() <= pocket[j].getX()) && ball[i].getY() <= pocket[j].getY() && ball[i].isSunk() != true && pocket[j].getType() == 'A'){
					ball[i].setVelocityX(0.0f);
					ball[i].setVelocityY(0.0f);
					ball[i].sunk();
					ball[i].setX(1000);
					ball[i].setY(80);
					bsunk = true;
					tempBsunk = true;
					soundcount++;
					
				}
				if(ball[i].getX() <= pocket[j].getX() && (ball[i].getY()+30) >= pocket[j].getY() && ball[i].isSunk() != true && pocket[j].getType() == 'B'){
					ball[i].setVelocityX(0.0f);
					ball[i].setVelocityY(0.0f);
					ball[i].sunk();
					ball[i].setX(1000);
					ball[i].setY(80);
					tempBsunk = true;
					bsunk = true;
					soundcount++;
				}
			
				if((ball[i].getX()+15) > (pocket[j].getX()-20) && (ball[i].getX()+15) < (pocket[j].getX()+20) && ball[i].getY() <= pocket[j].getY() && ball[i].isSunk() != true && pocket[j].getType() == 'C'){

					ball[i].sunk();
					tempBsunk = true;
					bsunk = true;
					soundcount++;
				}
			
				if((ball[i].getX()+15) > (pocket[j].getX()-20) && (ball[i].getX()+15) < (pocket[j].getX()+20) && (ball[i].getY()+30) >= pocket[j].getY() && ball[i].isSunk() != true && pocket[j].getType() == 'D'){
					ball[i].setVelocityX(0.0f);
					ball[i].setVelocityY(0.0f);
					ball[i].sunk();
					ball[i].setX(1000);
					ball[i].setY(80);
					tempBsunk = true;
					bsunk = true;
					soundcount++;
				}
				
				if((ball[i].getX()+30) >= pocket[j].getX() && ball[i].getY() <= pocket[j].getY() && ball[i].isSunk() != true && pocket[j].getType() == 'E'){
					ball[i].setVelocityX(0.0f);
					ball[i].setVelocityY(0.0f);
					ball[i].sunk();
					ball[i].setX(1000);
					ball[i].setY(80);
					tempBsunk = true;
					bsunk = true;
					soundcount++;
				}
				
				if((ball[i].getX()+30) >= pocket[j].getX() && (ball[i].getY()+30) >= pocket[j].getY() && ball[i].isSunk() != true && pocket[j].getType() == 'F'){
					ball[i].setVelocityX(0.0f);
					ball[i].setVelocityY(0.0f);
					ball[i].sunk();
					tempBsunk = true;
					bsunk = true;
					soundcount++;
				}
				
				if(ball[i].isSunk()== true && ball[i].getType() == 'W'){
					ball[i].setVelocityX(0.0f);
					ball[i].setVelocityY(0.0f);
					ball[i].setX(whiterackpX+widthoffset/2-ballposBM/2);
					ball[i].setY(whiterackpY+heightoffset/2-ballposBM/2);
					ball[i].bSunk = false;
				}else if(ball[i].isSunk()== true && ball[i].getType() == 'E'){
					if(player2.turn == true && player1.numballsunk == 7){
						bGameWon = true;
					}else if(player1.turn == true && player2.numballsunk == 7){
						bGameWon = true;
					}else if(player2.turn == true && player1.numballsunk != 7){
						bGameLost = true;
					}else if(player1.turn == true && player2.numballsunk != 7){
						bGameLost = true;
					}
				}else{
					if(player1.turn == true && player1.type == 'N' && player2.type != ball[i].getType() && bsunk == true){
						player1.setType(ball[i].getType());
						if(player1.type == 'F'){
							player2.type = 'S';
						}else{
							player2.type = 'F';
						}
					}else if(player2.turn == true && player2.type == 'N' && player1.type != ball[i].getType() && bsunk == true){
						player2.setType(ball[i].getType());
						if(player2.type == 'F'){
							player1.type = 'S';
						}else{
							player1.type = 'F';
						}
					}
					if(ball[i].getType() == player1.type && bsunk == true){
						player1.ballsunk();
						if(player2.turn == true){
							player1.NextTurn();
							player2.NextTurn();
						}
						ball[i].setVelocityX(0.0f);
						ball[i].setVelocityY(0.0f);
						ball[i].setX(100);
						ball[i].setY(player1.ballpos);
						player1.ballpos += 30;
					}else if(ball[i].getType() == player2.type && bsunk == true){
						player2.ballsunk();
						if(player1.turn == true){
							player1.NextTurn();
							player2.NextTurn();
						}
						ball[i].setVelocityX(0.0f);
						ball[i].setVelocityY(0.0f);
						ball[i].setX(1100);
						ball[i].setY(player2.ballpos);
						player2.ballpos += 30;
					}
				}
				bsunk = false;
			}
		
		//sound when ball is sunk
		if(tempBsunk == true){
			for(int s = 0;s < soundcount; s++){
				playSoundFile("ballinpocket.wav", 5);
			}
			tempBsunk = false;
		}
		
	}
	//Collision detection between ball and barrier happens here
	public void BarrierCollision(int i){
		
		boolean bconnected = false;
		for(int j = 0; j < 6; j++){
			if(barrier[j].getType() == 'T' && bconnected == false){
				if((ball[i].getY()+ballposBM) <= (barrier[j].getHeight())){
					ball[i].setVelocityY(ball[i].getVelocityY()* -1.0f);
					bconnected = true;
					if(ball[i].getVelocityY() == 0.0f && ball[i].isSunk() == false){
						ball[i].setVelocityY(0.7f);
					}
				}
			}else if(barrier[j].getType() == 'B' && bconnected == false){
				if(ball[i].getY()+ballposBM*2 >= (barrier[j].getHeight())){
					ball[i].setVelocityY(ball[i].getVelocityY()* -1.0f);
					bconnected = true;
					if(ball[i].getVelocityY() == 0.0f && ball[i].isSunk() == false){
						ball[i].setVelocityY(-0.7f);
					}
				}
			}else if(barrier[j].getType() == 'R'){
				if(ball[i].getX()+ballposBM >= (barrier[j].getWidth())){
					ball[i].setVelocityX(ball[i].getVelocityX()* -1.0f);
					if(ball[i].getVelocityX() == 0.0f && ball[i].isSunk() == false){
						ball[i].setVelocityX(-0.7f);
					}
				}
			}else if(barrier[j].getType() == 'L'){
				if(ball[i].getX() <= (barrier[j].getWidth())){
					ball[i].setVelocityX(ball[i].getVelocityX()* -1.0f);
					if(ball[i].getVelocityX() == 0.0f && ball[i].isSunk() == false){
						ball[i].setVelocityX(0.7f);
					}
				}
			}
		
		}
		
	}

	//Collision detection between balls happens here
	public void BallCollision(int i){

		if(ball[i].isSunk() != true){
			for(int j = 0; j < 16; j++){
					if(ball[j].isSunk() != true){
					double deltaXSquared = ((ball[i].getX()+15)+ ball[i].getVelocityX()) - ((ball[j].getX()+15)+ ball[j].getVelocityX()); // calc. delta X
					deltaXSquared *= deltaXSquared; // square delta X

					double deltaYSquared = ((ball[i].getY()+15)+ ball[i].getVelocityY()) - ((ball[j].getY()+15)+ ball[j].getVelocityY()); // calc. delta Y
					deltaYSquared *= deltaYSquared; // square delta Y

					// Calculate the sum of the radii, then square it
					double sumRadiiSquared = 15 + 15;
					sumRadiiSquared *= sumRadiiSquared;
					
					if(deltaXSquared + deltaYSquared <= sumRadiiSquared){
					// Ball1 and Ball2 are touching
						if(i != j){
							//checks and plays sound when white ball hits another ball
							if(start == true && ball[i].getType() == 'W' && (ball[i].getVelocityX() > 0.0f || ball[i].getVelocityY() > 0.0f)){
								playSoundFile("breaksmall.wav", 2);
								start = false;
							}else if((ball[i].getVelocityX() > 0.0f || ball[i].getVelocityY() > 0.0f)){
								playSoundFile("singleballhit.wav", 2);
							}
							double temp;
							if(ball[i].getVelocityY() < 0.0f){
								if(ball[j].getVelocityY() > 0.0f){
									temp = ball[i].getVelocityY() + ball[j].getVelocityY();
									if(temp <= 0.0f){
										ball[j].setVelocityY(ball[i].getVelocityY()/1.2f);
										ball[i].setVelocityY((ball[i].getVelocityY()/2.0f));
									}else{
										ball[i].setVelocityY(ball[j].getVelocityY()/1.2f);
										ball[j].setVelocityY((ball[j].getVelocityY()/2.0f));
									}
								}else{
									temp = ball[i].getVelocityY() - ball[j].getVelocityY();
									if(temp <= 0.0f){
										ball[j].setVelocityY(ball[i].getVelocityY()/1.2f);
										ball[i].setVelocityY((ball[i].getVelocityY()/2.0f));
									}else{
										ball[i].setVelocityY(ball[j].getVelocityY());
										ball[j].setVelocityY((ball[j].getVelocityY()/2.0f));
									}
								}
							}else if(ball[i].getVelocityY() >= 0.0f){
								if(ball[j].getVelocityY() > 0.0f){
									temp = ball[i].getVelocityY() - ball[j].getVelocityY();
									if(temp <= 0.0f){
										ball[i].setVelocityY(ball[j].getVelocityY()/1.2f);
										ball[j].setVelocityY((ball[j].getVelocityY()/2.0f));
									}else{
										ball[j].setVelocityY(ball[i].getVelocityY()/1.2f);
										ball[i].setVelocityY((ball[i].getVelocityY()/2.0f));
									}
								}else{
									temp = ball[i].getVelocityY() + ball[j].getVelocityY();
									if(temp <= 0.0f){
										ball[i].setVelocityY(ball[j].getVelocityY()/1.2f);
										ball[j].setVelocityY((ball[j].getVelocityY()/2.0f));
									}else{
										ball[j].setVelocityY(ball[i].getVelocityY());
										ball[i].setVelocityY((ball[i].getVelocityY()/2.0f));
									}
								}							
							}
							
							if(ball[i].getVelocityX() < 0.0f){
								if(ball[j].getVelocityX() > 0.0f){
									temp = ball[i].getVelocityX() + ball[j].getVelocityX();
									if(temp <= 0.0f){
										ball[j].setVelocityX(ball[i].getVelocityX()/1.2f);
										ball[i].setVelocityX((ball[i].getVelocityX()/2.0f));
									}else{
										ball[i].setVelocityX(ball[j].getVelocityX()/1.2f);
										ball[j].setVelocityX((ball[j].getVelocityX()/2.0f));
									}
								}else{
									temp = ball[i].getVelocityX() - ball[j].getVelocityX();
									if(temp <= 0.0f){
										ball[j].setVelocityX(ball[i].getVelocityX()/1.2f);
										ball[i].setVelocityX((ball[i].getVelocityX()/2.0f));
									}else{
										ball[i].setVelocityX(ball[j].getVelocityX());
										ball[j].setVelocityX((ball[j].getVelocityX()/2.0f));
									}
								}
							}else if(ball[i].getVelocityX() >= 0.0f){
								if(ball[j].getVelocityX() > 0.0f){
									temp = ball[i].getVelocityX() - ball[j].getVelocityX();
									if(temp <= 0.0f){
										ball[i].setVelocityX(ball[j].getVelocityX()/1.2f);
										ball[j].setVelocityX((ball[j].getVelocityX()/2.0f));
									}else{
										ball[j].setVelocityX(ball[i].getVelocityX()/1.2f);
										ball[i].setVelocityX((ball[i].getVelocityX()/2.0f));
									}
								}else{
									temp = ball[i].getVelocityX() + ball[j].getVelocityX();
									if(temp <= 0.0f){
										ball[i].setVelocityX(ball[j].getVelocityX()/1.2f);
										ball[j].setVelocityX((ball[j].getVelocityX()/2.0f));
									}else{
										ball[j].setVelocityX(ball[i].getVelocityX()/1.2f);
										ball[i].setVelocityX((ball[i].getVelocityX()/2.0f));
									}
								}							
							}
						}
					}
				}
			
			}
		}
	}
	//This gets called any time the Operating System
	//tells the program to paint itself
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		if(state == GameState.Menu) {
			paintMenu(g);
		} else if(state == GameState.Playing) {
				g2d.setBackground(black);
				g2d.clearRect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
				drawImage(g,poolTable,widthoffset/2,heightoffset/2);
		
				if(bGamestart == false && bGameWon == false && bGameLost == false){
					start = true;
					drawText(g, DISPLAY_WIDTH/4, 60, "Press Space to Start");
				}else if(bGameWon == false && bGameLost == false){
					if(player1.turn == true){
						drawText(g, DISPLAY_WIDTH/4, 60, "Player 1's Turn");
						drawText(g, DISPLAY_WIDTH/4, 120, "Num Balls Sunk");
						drawText(g, DISPLAY_WIDTH/1.5, 120, convertIntegerToString(player1.numballsunk));
					}else if(player2.turn == true){
						drawText(g, DISPLAY_WIDTH/4, 60, "Player 2's Turn");
						drawText(g, DISPLAY_WIDTH/4, 120, "Num Balls Sunk");
						drawText(g, DISPLAY_WIDTH/1.5, 120, convertIntegerToString(player2.numballsunk));
					}
				}else if(bGameWon == true){
					if(player2.turn == true){
						drawText(g, DISPLAY_WIDTH/6, 60, "PLAYER ONE HAS WON THE GAME!!!");
					}else if(player1.turn == true){
						drawText(g, DISPLAY_WIDTH/6, 60, "PLAYER TWO HAS WON THE GAME!!!");
					}
				}else if(bGameLost == true){
					if(player2.turn == true){
						drawText(g, DISPLAY_WIDTH/6, 60, "PLAYER ONE HAS LOST THE GAME!!!");
					}else if(player1.turn == true){
						drawText(g, DISPLAY_WIDTH/6, 60, "PLAYER TWO HAS LOST THE GAME!!!");
					}
				}
			//Draw that sprite on the screen
			if(bRack == true){
				//********************************************************************************************************************************
				//	Sets pool balls from 1-15 in triangle formation
				//				()
				//		  	   ()()
				//		 	  ()()() 
				//      	 ()()()()
				//      	()()()()()
				//********************************************************************************************************************************
				//White Ball sperate
				for(int j = 0; j < 16; j++){
					
						drawImage(g, spriteSheet, ball[j].getX(), ball[j].getY(), ballposBM, ballposBM, spriteX[j], spriteY[j], spriteW[j], spriteH[j]);
					
				}
				
			}
			
			if(ball[0].getVelocityX() == 0.0f && ball[0].getVelocityY() == 0.0f ){
				double centerX = (ball[0].getX()+ballposBM/2); 
				double centerY = (ball[0].getY()+ballposBM/2);
				angle = Math.atan2(centerY - mouseY, centerX - mouseX) - Math.PI / 2;

				((Graphics2D)g).rotate(angle,centerX, centerY);
				drawImage(g,que,(poolQue.getX()+poolQue.getPower()),poolQue.getY());
				((Graphics2D)g).rotate(-angle, centerX, centerY);
			}
			

				
		} else if(state == GameState.HowToPlay){
			paintHTP(g);
		}
		
	}
	//paint the menu
	public void paintMenu(Graphics g) {
		drawImage(g, menuBackground, 0, 0);
		//Play
		if(menuOption == 0) {
			drawImage(g, playHighlighted, 50, 200);
		} else {
			drawImage(g, play,            50, 200);
		}
		
		//How To Play
		if(menuOption == 1){
			drawImage(g, howtoplayHighlighted, 50, 250);
		}else{
			drawImage(g,howtoplay, 50, 250);
		}
		//Exit
		if(menuOption == 2) {
			drawImage(g, exitHighlighted, 50, 300);
		} else {
			drawImage(g, exit,            50, 300);
		}
		
	}
	public void paintHTP(Graphics g){
		drawImage(g, howtoplayBackground, 0, 0);
		changeColor(g, white);
		if((DISPLAY_WIDTH == 1200) && (DISPLAY_HEIGHT == 800)){
			drawText(g, 100, 275, "->Use the Mouse to interact with the White ball");
			drawText(g, 100, 325, "->Holding the Left Mouse Button Down "); 
			drawText(g, 130, 375, "increases the Velocity of the Que");
			drawText(g, 100, 425, "->rotate the mouse to change the angle");
			drawText(g, 130, 475, "of the Que");
			drawImage(g, backHighlighted, 50, 480);
		}
	}
	
	//*******************************************************************************************************************************************
	//
	//	Keyboard/mouse input  Functions Below
	//
	//*******************************************************************************************************************************************
    //Called whenever a key is pressed
    public void keyPressed(KeyEvent e) {
		if(state == GameState.Menu) {
    		keyPressedMenu(e);
    	} else if(state == GameState.HowToPlay){
    		keyPressedHTP(e);
    	} else if(state == GameState.Playing){
			keyPressedPlaying(e);
		}
    }
	 
	  //Called whenever a key is pressed in the menu
    public void keyPressedMenu(KeyEvent e) {
		//Up Arrow
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			if(menuOption > 0) {
				menuOption--;
			}
		}
		//Down Arrow
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(menuOption < 2) {
				menuOption++;
			}
		}
		//Enter Key
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(menuOption == 0) {
				//Start Game
				state = GameState.Playing;
				if(state == GameState.Playing){
					playSoundFileLoop("Jazz.wav", -15);
				}
				menuOption = 0;
			}else if(menuOption == 1) {
				state = GameState.HowToPlay;
				menuOption = 0;
			} else {
				//Exit
				System.exit(0);
			}
		}
	 }
	 public void keyPressedHTP(KeyEvent e) {
		//Enter Key
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				state = GameState.Menu;
				menuOption = 0;
		}
    }
	 
	 public void keyPressedPlaying(KeyEvent e){
		 //Enter Key
		 if((e.getKeyCode() == KeyEvent.VK_ENTER)) {
			 state = GameState.Menu;
			 stopSound(clip);
			 menuOption = 0;
		 }
	 }
	 
    //Called whenever a key is released
    public void keyReleased(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_SPACE ) {
            bGamestart = true;
    	}    	
    }
    
    //Called whenever a key is pressed and immediately released
    public void keyTyped(KeyEvent e) {
        
    }
    public void mouseMoved(MouseEvent e) {
		mouseX=e.getX();
		mouseY=e.getY();
		if(state == GameState.Menu) {
			mouseOnMenu(e);
		} 
    }
	 //highlights the menu button when moused over
	 public void mouseOnMenu(MouseEvent e){
		 
		 if((mouseX > 42 && mouseX < 242) && (mouseY > 230 && mouseY < 280)){
			menuOption = 0;
		 }
		 if((mouseX > 42 && mouseX < 242) && (mouseY > 280 && mouseY < 330)){
			menuOption = 1;
		 }
		 if((mouseX > 42 && mouseX < 242) && (mouseY > 330 && mouseY < 380)){
			menuOption = 2;
		 }
	 }
			 
    public void mouseReleased(MouseEvent e) {
    	if(ball[0].getVelocityX() == 0.0f && ball[0].getVelocityY() == 0.0f ){
			if (e.getButton() == MouseEvent.BUTTON1) {
				mousePressed = false;
				poolQue.moveQue(mousePressed);
				poolShot = true;
			}
    	}
    }
    public void mousePressed(MouseEvent e) {
    	if(ball[0].getVelocityX() == 0.0f && ball[0].getVelocityY() == 0.0f ){
			if (e.getButton() == MouseEvent.BUTTON1) {
				mousePressed = true;
				poolQue.moveQue(mousePressed);
				poolQue.setPower(0.0f);
			}
    	}
    }
	public void mouseClicked(MouseEvent e) {
		if(state == GameState.Menu) {
    		mouseClickedMenu(e);
    	} else if(state == GameState.HowToPlay){
    		mouseClickedBackHTP(e);
    	}
		// TODO Auto-generated method stub
		
	}
	//mouse clicked in menu 
	public void mouseClickedMenu(MouseEvent e) {
		if((mouseX > 42 && mouseX < 242) && (mouseY > 230 && mouseY < 280)){
			state = GameState.Playing;
			playSoundFileLoop("Jazz.wav", -15);
			menuOption = 0;
		}else if((mouseX > 42 && mouseX < 242) && (mouseY > 280 && mouseY < 330)){
			state = GameState.HowToPlay;
			menuOption = 0;
			
		} else if((mouseX > 42 && mouseX < 242) && (mouseY > 330 && mouseY < 380)){
			//Exit
			System.exit(0);
		}
	}

	//mouse clicked in how to play
	public void mouseClickedBackHTP(MouseEvent e){
		if(state == GameState.HowToPlay){
			if((mouseX > 42 && mouseX < 242) && (mouseY > 510 && mouseY < 560)){
				state = GameState.Menu;
				menuOption = 0;
			}
		}
	}
	
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
