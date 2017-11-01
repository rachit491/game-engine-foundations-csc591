import java.io.IOException;
import java.io.ObjectInputStream;

import eventManagementSystem.Event;

/*
 * Client Read Write Thread
 */
public class ClientReadThread implements Runnable {
	ObjectInputStream in;
	GameClient client;
	Object obj = null;
	
	public ClientReadThread(ObjectInputStream in, GameClient c) {
		this.in = in;
		this.client = c;
	}
	
	public void run() {
		try {
			while((obj = (Object) in.readObject()) != null) {
				if(obj instanceof GameObject) {
					if(((GameObject) obj).getName().contains("player")) {
						//System.out.println("Reading obj - " + obj.getPID() + obj.getTransform().getPositionX());
						client.updateCoordinates((GameObject) obj, ((GameObject) obj).getPID());
					}
					if(((GameObject) obj).getName().contains("platform")) {
						//Send moving platform data
						String[] x = ((GameObject) obj).getName().split("-");
						//System.out.println("Read Platform: " + x[2]);
						client.updatePlatformCoordinates((GameObject) obj, Integer.parseInt(x[2]));
					}
				}	
				
				if(obj instanceof Event) {
					client.updateEvent((Event) obj, ((Event) obj).getClientID());
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
} 