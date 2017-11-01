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
	int playerid;
	
	List<GameObject> player = new ArrayList<GameObject>();
	
	int gravity = 1;
	PVector velocity =  new PVector(0, 0);
	int speed = 5;
	int bounceY = -1;
	
	ArrayList<PShape> object = new ArrayList<PShape>();
	
	boolean left, right;
	int up;
	
	List<Integer> x = new ArrayList<>();
	List<Integer> y = new ArrayList<>();
	
	int newX, oldY, newY;
	
	PShape platform;
	
	GameObject p;
	int[] color;
	
	public void settings() {
		size(400, 600);
		
		try {
			socket = new Socket("127.0.0.1", 7172);
			System.out.println("Connection successful...");
			
			in = new ObjectInputStream(socket.getInputStream());
			while((p = (GameObject) in.readObject()) != null) {
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
				
				out = new ObjectOutputStream(socket.getOutputStream());
				
				ClientReadThread input = new ClientReadThread(in, this);
				Thread thread = new Thread(input); 
				thread.start();
				
				//Thread thread2 = new Thread(this);
				//thread2.start();
				
				break;
			}
			
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
		
		for(int i=0; i < player.size(); i++) {
			if(player.get(i) != null) {
				
				stroke(25, 55, 36);
				color = player.get(i).getBox().getColor();
				fill(color[0], color[1], color[2]);
				
				updateMovements(i);
			}
			else {
				object.add(i, null);
			}
			
		}
		
		//Write to server on every draw call
		if(left || right || up != 0) {
			try {
				p.setPID(playerid);
				p.getTransform().setPositionX(x.get(playerid));
				p.getTransform().setPositionY(y.get(playerid));
				
				out.writeObject(p);
				out.reset();
				//System.out.println("P Sent - " + p.getPID() + " " + p.getTransform().getPositionX());
			} catch (IOException e) {
				System.out.println("Unable to send coordinates");
			}
		}
		
	}
	
	private void updateMovements(int i) {
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
			
			if (y.get(i) < 570)
				velocity.y += gravity;
			else
				velocity.y = 0;
			
			if (y.get(i) >= 570 && up != 0) {
			    velocity.y = -10;
			}
			  
			// We check the nextPosition before actually setting the position so we can
			PVector nextPosition = new PVector(x.get(i), y.get(i));
			nextPosition.add(velocity);
			  
			//if (nextPosition.x > offset && nextPosition.x < (width - offset))
			x.set(i, (int) nextPosition.x);
			 
			//if (nextPosition.y > offset && nextPosition.y < (height - offset))
			y.set(i, (int) nextPosition.y);
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
	
	public void keyPressed() {
		if(key == CODED) {
			if(keyCode == 37) {
				left = true;
			}
			if(keyCode == 39) {
				right = true;
			}
		}	
		if (key == ' ') {
			up = -1;
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
		}
	}
	
	public static void main(String[] args) {
		PApplet.main("GameClient");
	}
 
}


class ClientReadThread implements Runnable {
	ObjectInputStream in;
	GameClient client;
	GameObject obj = null;
	
	public ClientReadThread(ObjectInputStream in, GameClient c) {
		this.in = in;
		this.client = c;
	}
	
	public void run() {
		try {
			while((obj = (GameObject) in.readObject()) != null) {
				//System.out.println("Reading obj - " + obj.getPID() + obj.getTransform().getPositionX());
				client.updateCoordinates(obj, obj.getPID());
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
} 