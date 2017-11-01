import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

public class GameClient extends PApplet { 
	static Socket socket;
	static ObjectInputStream in;
	static ObjectOutputStream out;
	
	static int width = 400, height = 600;
	int playerid;
	
	List<GameObject> player = new ArrayList<GameObject>();
	List<GameObject> platform = new ArrayList<GameObject>();
	
	int gravity = 1;
	PVector velocity =  new PVector(0, 0);
	int bounceY = -15, speed = 10;
	int maxYVel = 10;
	
	ArrayList<PShape> object = new ArrayList<PShape>();
	
	//keyboard input flags
	int up = 0;
	boolean onPlatform;
	boolean left, right, down;
	boolean draw = false;
	
	//x y coordinates for clients
	List<Integer> x = new ArrayList<>();
	List<Integer> y = new ArrayList<>();
	
	int newX, oldY, newY;
	
	//PShape platform;
	
	GameObject obj, p;
	int[] color;
	
	int readCount = 0;
	
	public void settings() {
		size(width, height);
		onPlatform = true;
		
		try {
			socket = new Socket("127.0.0.1", 7172);
			System.out.println("Connection successful...");
			
			in = new ObjectInputStream(socket.getInputStream());
			while((obj = (GameObject) in.readObject()) != null && readCount < 10) {
				if(obj.getName().contains("player")) {
					p = obj;
					playerid = p.getPID();
					System.out.println(playerid + " This");
					for(int i=0; i < playerid && playerid >= player.size(); i++) {
						player.add(i, null);
						x.add(i, 0);
						y.add(i, 570);
					}
					player.add(playerid, p);
					x.add(p.getTransform().getPositionX());
					y.add(p.getTransform().getPositionY());
				}
				
				if(obj.getName().contains("platform")) {
					platform.add(obj);
				}
				
				//Thread thread2 = new Thread(this);
				//thread2.start();
				
				readCount++;
				//break;
			}

			out = new ObjectOutputStream(socket.getOutputStream());
			
			ClientReadThread input = new ClientReadThread(in, this);
			Thread thread = new Thread(input); 
			thread.start();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to start client");
		}
	}
	
	public void updateCoordinates(GameObject obj, int playerid) {
		if(playerid >= player.size()) {
			this.player.add(playerid, obj);
			x.add(playerid, obj.getTransform().getPositionX());
			y.add(playerid, obj.getTransform().getPositionY());
		}
		else {
			this.player.set(playerid, obj);
			x.set(playerid, obj.getTransform().getPositionX());
			y.set(playerid, obj.getTransform().getPositionY());
		}
		
		//this.playerid = playerid;
	}
	
	public void setup() {
		fill(120, 120, 250);
	}
	
	public void draw() {
		background(12, 214, 201);
		
		//Draw Platforms
		renderPlatforms();
		//Draw Players
		renderPlayer();
		sendPlayerCoordinates();
	}
	
	public void reset(int i) {
		//send to spawn points 
		x.set(i, 20);
		y.set(i, 545);
	}
 	
	private void renderPlatforms() {
		stroke(255);
		
		for(int i=0; i < platform.size(); i++) {
			renderPlatform(platform.get(i));
		}
	}
	
	public void renderPlatform(GameObject obj) {
		int x, y, w, h;
		PShape plt;
		
		if(obj != null) {
			color = obj.getBox().getColor();
			fill(color[0], color[1], color[2]);
			
			x = obj.getTransform().getPositionX();
			y = obj.getTransform().getPositionY();
			
			w = obj.getBox().getWidth();
			h = obj.getBox().getHeight();
			
			plt = createShape(RECT, x, y, w, h);
			shape(plt);
		}
	}
	
	public void updatePlatformCoordinates(GameObject obj, int playerid) {
		if(playerid < platform.size() && obj != null) {
			//renderPlatform(obj);
			platform.set(playerid, obj);
			redraw();
			System.out.println(platform.size() + " " + obj.getPID());
		}
	}
	
	
	private void renderPlayer() {
		for(int i=0; i < player.size(); i++) {
			if(player.get(i) != null) {
				
				stroke(25, 55, 36);
				color = player.get(i).getBox().getColor();
				fill(color[0], color[1], color[2]);
				
				collide(i, platform);
				updatePlayerMovements(i);
				
				if(y.get(i) > height) {
					reset(i);
					break;
				}
			}
			else {
				object.add(i, null);
			}
		}
	}
	
	private void updatePlayerMovements(int i) {
		if(i == playerid) {
			if(right == true) {
				newX = x.get(i) + speed;
				//velocity.x = newX;
				x.set(i, newX);
			}
			if(left == true) {
				newX = x.get(i) - speed;
				//velocity.x = newX;
				x.set(i, newX);
			}
		    
			y.set(i, (int) (y.get(i)+velocity.y));
			
			//System.out.println("Y POS: " + y.get(i));
			//if(up == 0) {
				velocity.y += gravity;
				//onPlatform = false;
			//}
			
		    velocity.y = min(maxYVel, velocity.y);
		    velocity.y = max(-maxYVel, velocity.y);	
		    
		    //if(up == -1) 
		    	//velocity.y = bounceY;
		    //System.out.println(velocity.y);
		}
		
		//x-boundaries
		if(x.get(i) > 399)
			x.set(i, 0);
		if(x.get(i) < -30)
			x.set(i, 365);
		
		if(object.isEmpty() || i == object.size())
			object.add(i, createShape(RECT, x.get(i), y.get(i), 35, 30));
		else {
			object.set(i, createShape(RECT, x.get(i), y.get(i), 35, 30));
		}
		shape(object.get(i));

	}
	
	void collide(int index, List<GameObject> platform) {
	    // standard rectangle intersections, but only for our lowest quarter
		int x = this.x.get(index);
		int y = this.y.get(index);
		int i;
		Rectangle plt;
		
		for(i=0; i < platform.size(); i++) {
			plt = platform.get(i).getBox().getRectangle();
			if(x < (plt.x + plt.width) && (x + 30) > plt.x && 
			   (y + 37) < (plt.y + plt.height) && (y + 35) > plt.y) {
				// but we only care about platforms when falling
		    	if (velocity.y > 0) {
		    		// for bouncing
		    		velocity.y = 0;
		    		onPlatform = true;
		    		break;
		    	}
		    }
		}
	}
	
	private void sendPlayerCoordinates() {
		//Write to server on every draw call
		if(left || right || up != 0) {
			try {
				p.setPID(playerid);
				p.getTransform().setPositionX(x.get(playerid));
				p.getTransform().setPositionY(y.get(playerid));
				
				out.writeObject(p);
				out.reset();
				//if(down)	down = false;
				//System.out.println("P Sent - " + p.getPID() + " " + p.getTransform().getPositionX());
			} catch (IOException e) {
				System.out.println("Unable to send coordinates");
			}
		}
	}
	
	public void keyPressed() {
		if(key == CODED) {
			if(keyCode == 37) {
				left = true;
			}
			if(keyCode == 39) {
				right = true;
			}
		}	
		if (key == ' ' && up == 0 && onPlatform) {
			up = -1;
			down = false;
			velocity.y = bounceY;
		}
	}
	
	public void keyReleased() {
		if(key == CODED) {
			if(keyCode == 37) {
				left = false;
			}
			if(keyCode == 39) {
				right = false;
			}
		}
		if (key == ' ') {
			up = 0;
			down = true;
			onPlatform = false;
		}
	}
	
	public static void main(String[] args) {
		PApplet.main("GameClient");
	}
 
}