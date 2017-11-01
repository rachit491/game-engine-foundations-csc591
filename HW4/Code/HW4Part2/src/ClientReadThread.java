import java.io.IOException;
import java.io.ObjectInputStream;

import eventManagementSystem.Event;

/*
 * Client Read Write Thread
 */
public class ClientReadThread implements Runnable {
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
				if(obj.getName().contains("player")) {
					//System.out.println("Reading obj - " + obj.getPID() + obj.getTransform().getPositionX());
					client.updateCoordinates(obj, obj.getPID());
				}
				if(((GameObject) obj).getName().contains("platform")) {
					//Send moving platform data
					String[] x = obj.getName().split("-");
					//System.out.println("Read Platform: " + x[2]);
					client.updatePlatformCoordinates(obj, Integer.parseInt(x[2]));
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
} 