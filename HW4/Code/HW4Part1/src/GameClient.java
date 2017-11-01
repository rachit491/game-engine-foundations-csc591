import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import eventManagementSystem.Event;
import eventManagementSystem.EventHandler;
import eventManagementSystem.EventManager;
import eventManagementSystem.EventType;
import processing.core.PApplet;
import processing.core.PShape;
import scripting.ScriptManager;

public class GameClient extends PApplet implements Serializable, EventHandler { 
	static Socket socket;
	static ObjectInputStream in;
	static ObjectOutputStream out;
	
	static int width = 400, height = 600;
	int playerid;
	
	List<GameObject> player = new ArrayList<GameObject>();
	List<GameObject> platform = new ArrayList<GameObject>();	
	
	List<GameObject> playerCopy1 = new ArrayList<GameObject>();
	List<GameObject> platformCopy1 = new ArrayList<GameObject>();
	List<GameObject> playerCopy2 = new ArrayList<GameObject>();
	List<GameObject> platformCopy2 = new ArrayList<GameObject>();
	
	ArrayList<PShape> object = new ArrayList<PShape>();

	//Game Timeline
	Timeline tl = new Timeline(System.nanoTime());
	
	//Event Manager
	EventManager eventManager = new EventManager();
	
	//Replay System
	ReplaySystem replay = new ReplaySystem(eventManager, tl);
	
	//load default physics
	Physics phy = new Physics(replay);
	
	//keyboard input flags
	UserInputs userInput = new UserInputs(eventManager, tl, replay);
	
	//Collision Detection
	CollisionDetection cd = new CollisionDetection(eventManager, phy, userInput);
		
	//x y coordinates for clients
	List<Integer> x = new ArrayList<>();
	List<Integer> y = new ArrayList<>();	
	
	List<Integer> xCopy1 = new ArrayList<>();
	List<Integer> yCopy1 = new ArrayList<>();
	List<Integer> xCopy2 = new ArrayList<>();
	List<Integer> yCopy2 = new ArrayList<>();
	
	int newX, oldY, newY;
	
	GameObject obj, p;
	int[] color;
	
	int readCount = 0;
	int index = 5;
	
	HashMap<Integer, Queue<Event>> eventDir = new HashMap<Integer, Queue<Event>>();
	
	public void settings() {
		size(width, height);
		
		phy.addEventListener(eventManager);
		userInput.setFlagDefault();
		
		eventManager.addDispatcher(EventType.DEATH_EVENT, this);
		eventManager.addDispatcher(EventType.SPAWN_EVENT, this);
		eventManager.addDispatcher(EventType.RECORD_SAVE_DATA, this);
		eventManager.addDispatcher(EventType.RECORD_RESTORE_DATA, this);
		eventManager.addDispatcher(EventType.REPLAY_SAVE_DATA, this);
		eventManager.addDispatcher(EventType.REPLAY_RESTORE_DATA, this);
		
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
				
				readCount++;
			}

			out = new ObjectOutputStream(socket.getOutputStream());
			this.eventManager.setOutputStream(out);
			
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
		
		if(!tl.getIsPaused()) {			
			//this.tl.update(System.nanoTime());
			this.tl.singleStep();
		}
		else {
			//since my cycle updation is called per frame of the draw function
			//so when fast-forward or slow motion is called, i've updated the tick size for the time as
			//well as the frame rate
			frameRate((float) this.replay.replayTL.getTickSize());
			//replay.replayTL.update(System.nanoTime());
			this.replay.replayTL.singleStep();
			this.replay.replayStarted();
		}
		
		writeText();
		
		//Draw Platforms
		renderPlatforms();
		//Draw Players
		renderPlayer();
		sendPlayerCoordinates();
		if(playerid == 0)
			sendPlatformCoordinates();
	}
	
	public void writeText() {
		//order of key press isn't handled
		String[] str = {"Recording", "Stopped", "1x replay", "0.5x replay", "2x replay",
				"Press a - Record, s - Stop, d - 1x play, f - 2x, g - 0.5x", "Done"};
		
		fill(0);
		textSize(12);
		text(str[this.index], 15, 25);
	}
	
	public void setValues(String str) {
		switch(str) {
		case "record": 
						for(int i=0; i<this.x.size(); i++) {
							this.xCopy1.add(this.x.get(i));
						}
						for(int i=0; i<this.y.size(); i++) {
							this.yCopy1.add(this.y.get(i));
						}
						for(int i=0; i<this.player.size(); i++) {
							this.playerCopy1.add(this.player.get(i));
						}
						for(int i=0; i<this.platform.size(); i++) {
							this.platformCopy1.add(this.platform.get(i));
						}
						this.platformCopy1.get(6).setPID(this.platform.get(6).getPID());
						this.platformCopy1.get(6).getTransform().setPosition(this.platform.get(6).getTransform().getPositionX(),
								this.platform.get(6).getTransform().getPositionY());
						break;
		
		case "replay": 
						for(int i=0; i<this.x.size(); i++) {
							this.xCopy2.add(this.x.get(i));
						}
						for(int i=0; i<this.y.size(); i++) {
							this.yCopy2.add(this.y.get(i));
						}
						for(int i=0; i<this.player.size(); i++) {
							this.playerCopy2.add(this.player.get(i));
						}
						for(int i=0; i<this.platform.size(); i++) {
							this.platformCopy2.add(this.platform.get(i));
						}
						this.platformCopy2.get(6).setPID(this.platform.get(6).getPID());
						this.platformCopy2.get(6).getTransform().setPosition(this.platform.get(6).getTransform().getPositionX(),
								this.platform.get(6).getTransform().getPositionY());
						break;
		}
	}
	
	public void restoreValues(String str) {
		switch(str) {
		case "record": 
						for(int i=0; i<this.xCopy1.size(); i++) {
							this.x.set(i, this.xCopy1.get(i));
						}
						for(int i=0; i<this.yCopy1.size(); i++) {
							this.y.set(i, this.yCopy1.get(i));
						}
						for(int i=0; i<this.playerCopy1.size(); i++) {
							this.player.set(i, this.playerCopy1.get(i));
						}
						for(int i=0; i<this.platformCopy1.size(); i++) {
							this.platform.set(i, this.platformCopy1.get(i));
						}
						this.platform.get(6).setPID(this.platformCopy1.get(6).getPID());
						this.platform.get(6).getTransform().setPosition(this.platformCopy1.get(6).getTransform().getPositionX(),
								this.platformCopy1.get(6).getTransform().getPositionY());
						break;
		
		case "replay": 
						for(int i=0; i<this.xCopy2.size(); i++) {
							this.x.set(i, this.xCopy2.get(i));
						}
						for(int i=0; i<this.yCopy2.size(); i++) {
							this.y.set(i, this.yCopy2.get(i));
						}
						for(int i=0; i<this.playerCopy2.size(); i++) {
							this.player.set(i, this.playerCopy2.get(i));
						}
						for(int i=0; i<this.platformCopy2.size(); i++) {
							this.platform.set(i, this.platformCopy2.get(i));
						}
						this.platform.get(6).setPID(this.platformCopy2.get(6).getPID());
						this.platform.get(6).getTransform().setPosition(this.platformCopy2.get(6).getTransform().getPositionX(),
								this.platformCopy2.get(6).getTransform().getPositionY());
						break;
		}
	}
	
	public void reset(int i) {
		//send to spawn points 
		x.set(i, 20);
		y.set(i, 540);
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
			platform.set(playerid, obj);
			//redraw();
			//System.out.println(platform.size() + " " + obj.getPID());
		}
	}
	
	
	private void renderPlayer() {
		final int index;
		for(int i=0; i < player.size(); i++) {
			if(player.get(i) != null) {
				
				stroke(25, 55, 36);
				color = player.get(i).getBox().getColor();
				fill(color[0], color[1], color[2]);
				
				collide(i, platform);
				updatePlayerMovements(i);
				
				if(y.get(i) > height) {
					//Free-Fall = Death
					index = i;
					this.eventManager.trigger(new Event(EventType.DEATH_EVENT, new ArrayList<Object>() {{
						add(tl.getTimeCycles());
						add(index);
						add("death");
						add(index);
					}}));
					break;
				}
			}
			else {
				object.add(i, null);
			}
		}
	}
	
	//update player data and set in the array
	private void updatePlayerMovements(int i) {
		if(i == playerid) {
			if(userInput.right == true) {
				newX = x.get(i) + phy.getSpeed();
				//velocity.x = newX;
				x.set(i, newX);
			}
			if(userInput.left == true) {
				newX = x.get(i) - phy.getSpeed();
				x.set(i, newX);
			}
		    
			y.set(i, (int) (y.get(i) + phy.getVelocity().y));
			
			this.eventManager.trigger(new Event(EventType.UPDATE_POSITION_EVENT, new ArrayList<Object>() {{
				add(tl.getTimeCycles());
				add(i);
				add("update_position");
			}}));
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
	
	// Collision Detection Component
	void collide(int index, List<GameObject> platform) {
		this.eventManager.trigger(new Event(EventType.COLLISION_EVENT, new ArrayList<Object>() {{
			add(tl.getTimeCycles());
			add(index);
			add(x);
			add(y);
			add(index);
			add(platform);
		}}));
	}
	
	private void sendPlayerCoordinates() {
		//Write to server on every draw call
		if(userInput.left || userInput.right || userInput.up != 0) {
			try {
				p.setPID(playerid);
				p.getTransform().setPositionX(x.get(playerid));
				p.getTransform().setPositionY(y.get(playerid));
				
				out.writeObject(p);
				out.reset();
			} catch (IOException e) {
				System.out.println("Unable to send coordinates");
			}
		}
	}
	
	private void sendPlatformCoordinates() {
		try {
			//Scripting here for updating the color of the moving platform
			ScriptManager.loadScript(System.getProperty("user.dir") + "/src/scripting/platformUpdate.js");
			ScriptManager.bindArgument("platform", platform);
			ScriptManager.bindArgument("factory", new Factory());
			ScriptManager.executeScript("update");
			
			platform.get(6).getAnimator().play();
			out.writeObject(platform.get(6));
			out.reset();
		} catch (IOException e) {
			System.out.println("Unable to send coordinates");
		}
	}
	
	public void updateEvent(Event e, int id) {
		if (this.eventDir.get(id) == null) {
			this.eventDir.put(id, new LinkedList<Event>());
		}
		this.eventDir.get(key).add(e);
	}
	
	public void keyPressed() {
		if(key == CODED) {
			this.eventManager.trigger(new Event(EventType.INPUT_EVENT, new ArrayList<Object>() {{
				add(tl.getTimeCycles());
				add(playerid);
				add("keyPressed");
				add(keyCode);
			}}));
		}	
		
		if(key == ' ') {
			this.eventManager.trigger(new Event(EventType.INPUT_EVENT, new ArrayList<Object>() {{
				add(tl.getTimeCycles());
				add(playerid);
				add("keyPressed");
				add(999);
			}}));
		}
		
		if(key == 'a') {	//Record
			this.eventManager.trigger(new Event(EventType.RECORD_SAVE_DATA, new ArrayList<Object>() {{
				add(tl.getTimeCycles());
				add(playerid);
				add("saveData");
			}}));
			this.eventManager.trigger(new Event(EventType.REPLAY_RECORD, new ArrayList<Object>() {{
				add(tl.getTimeCycles());
				add(playerid);
				add("replayRecord");
			}}));
			this.index = 0;
		}
		
		if(key == 's') {	//Stop		
			this.eventManager.trigger(new Event(EventType.REPLAY_STOP, new ArrayList<Object>() {{
				add(tl.getTimeCycles());
				add(playerid);
				add("replayStop");
			}}));
			this.index = 1;
		}
		
		if(key == 'd') {	//Normal Replay
			this.eventManager.trigger(new Event(EventType.REPLAY_SAVE_DATA, new ArrayList<Object>() {{
				add(tl.getTimeCycles());
				add(playerid);
				add("saveData");
			}}));
			this.eventManager.trigger(new Event(EventType.RECORD_RESTORE_DATA, new ArrayList<Object>() {{
				add(tl.getTimeCycles());
				add(playerid);
				add("saveData");
			}}));
			this.eventManager.trigger(new Event(EventType.REPLAY_NORMAL, new ArrayList<Object>() {{
				add(tl.getTimeCycles());
				add(playerid);
				add("replayNormal");
			}}));
			this.index = 2;
		}
		
		if(key == 'f') {	//Fast Replay
			//only make fast forward
			System.out.println("FF");
			this.eventManager.trigger(new Event(EventType.REPLAY_FF, new ArrayList<Object>() {{
				add(replay.replayTL.getTimeCycles());
				add(playerid);
				add("replayFast");
			}}));
			this.index = 4;
		}
		
		if(key == 'g') {	//Slow Replay
			//only make slow motion
			System.out.println("SM");
			this.eventManager.trigger(new Event(EventType.REPLAY_SM, new ArrayList<Object>() {{
				add(replay.replayTL.getTimeCycles());
				add(playerid);
				add("replaySlow");
			}}));
			this.index = 3;
		}
	}
	
	public void keyReleased() {
		if(key == CODED) {
			this.eventManager.trigger(new Event(EventType.INPUT_EVENT_RELEASE, new ArrayList<Object>() {{
				add(tl.getTimeCycles());
				add(playerid);
				add("keyReleased");
				add(keyCode);
			}}));
		}
		
		if(key == ' ') {
			this.eventManager.trigger(new Event(EventType.INPUT_EVENT_RELEASE, new ArrayList<Object>() {{
				add(tl.getTimeCycles());
				add(playerid);
				add("keyReleased");
				add(999);
			}}));
		}
	}
	
	public static void main(String[] args) {
		PApplet.main("GameClient");
	}

	@Override
	public void onEvent(Event e) {
		/*if(this.replay.getIsReplaying()) {
			System.out.println("Replay-Death");
		}
		else {*/
			if(e.getEventType() == EventType.DEATH_EVENT) {
				//System.out.println("DEATH_EVENT");
				int index = (int) e.getEventArgs().get(1);
				if(!this.replay.getIsReplaying())
					this.eventManager.trigger(new Event(EventType.SPAWN_EVENT, new ArrayList<Object>() {{
						add(tl.getTimeCycles());
						add(playerid);
						add("spawn");
						add(index);
					}}));
			}
			
			if(e.getEventType() == EventType.SPAWN_EVENT) {
				//System.out.println("SPAWN_EVENT");
				int index = (int) e.getEventArgs().get(1);
				this.reset(index);
			}
			
			if(e.getEventType() == EventType.RECORD_SAVE_DATA) {
				this.setValues("record");
			}
			
			if(e.getEventType() == EventType.RECORD_RESTORE_DATA) {
				this.restoreValues("record");
			}
			
			if(e.getEventType() == EventType.REPLAY_SAVE_DATA) {
				this.setValues("replay");
			}
			
			if(e.getEventType() == EventType.REPLAY_RESTORE_DATA) {
				this.restoreValues("replay");
				this.index = 6;
			}
		//}
	}
 
}