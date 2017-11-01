import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import eventManagementSystem.Event;

public class GameServer {
 
	static ServerSocket serverSocket;
	static Socket socket;
	static ObjectOutputStream out;
	static CopyOnWriteArrayList<ServerThread> wt = new CopyOnWriteArrayList<ServerThread>();
	static ObjectInputStream in;
	static Factory fct = new Factory();
	static GameObject go;
	static List<GameObject> plt = new ArrayList<GameObject>();
	private static int NUM_ENEMIES = 31;
	 
	public static void main (String[] args) {
		try {
			serverSocket = new ServerSocket(7172);
			System.out.println("Server started...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true) {
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ServerThread st = createElement(wt.size());
			synchronized(wt) {
				wt.add(st);
				System.out.println(wt);
			}
			st.setList(wt);
			
			Thread thread = new Thread(st);
			thread.start();
			
			ServerWriteThread swt = new ServerWriteThread(out, plt);
			Thread thread2 = new Thread(swt);
			thread2.start();
		}
	} 
	
	public static ServerThread createElement(int id) {
		ServerThread st = null;
				
		try {
			out = new ObjectOutputStream(socket.getOutputStream());			
			
			//create a player object
			go = fct.createPlayer(id, 30, 30);
			out.writeObject(go);		
			out.reset();
			
			//Platform
			for(int i=0; i<NUM_ENEMIES; i++) {
				int x = 120 + 50*(i%10);
				int y = 50 + 60*(int)(Math.floor(i/10));
				
				plt.add(fct.createMovingPlatform(id, i, x, y, 40, 30));
				
				out.writeObject(plt.get(i));
				out.reset();
			}
			
			in = new ObjectInputStream(socket.getInputStream());
			
			st = new ServerThread(out, in, id, go);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return st;
	}
}


class ServerWriteThread implements Runnable {
	private static final int NUM_ENEMIES = 31;
	ObjectOutputStream out;
	List<GameObject> platform;
	
	public ServerWriteThread(ObjectOutputStream out, List<GameObject> platform) {
		this.out = out;
		this.platform = platform;
		
		//Try using JavaScript Timer function
		Timer t = new Timer();
		t.schedule(new TimerTask() {
		    @Override
		    public void run() {
		       for(int i=0; i<NUM_ENEMIES; i++) {
					int y = platform.get(i).getTransform().getPositionY();
					platform.get(i).getTransform().setPositionY(y + 40);
		       }
		    }
		}, 0, 15000);
	}
	
	public void run() {
		while(true) {
			try {
				for(int i=0; i<NUM_ENEMIES; i++) {
					platform.get(i).getAnimator().playX(true);
					out.writeObject(platform.get(i));
					out.reset();
				}
			} catch (IOException /*| InterruptedException*/ e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}

class ServerThread implements Runnable {
 
	ObjectOutputStream out;
	ObjectInputStream in;
	CopyOnWriteArrayList<ServerThread> user = new CopyOnWriteArrayList<ServerThread>();
	int playerid;
	GameObject go;
	
	int pid;
	
	public ServerThread(ObjectOutputStream out, ObjectInputStream in, int clientID, GameObject go) {
		this.out = out;
		this.in = in;
		this.playerid = clientID;
		this.go = go;
	}
	
	public void setList(CopyOnWriteArrayList<ServerThread> user) {
		this.user = user;
	}
	
	public void run() {
		Object obj;
		try {
			while((obj = (Object) in.readObject()) != null) {
				try {
					if(obj instanceof GameObject) {
						for (int i = 0; i < this.user.size(); i++) {
							if(this.user.get(i)!= null) {
								this.user.get(i).out.writeObject(obj);
								this.user.get(i).out.reset();
							}
						}
					}
					if(obj instanceof Event) {
						System.out.println("Event- Here");
						for (int i = 0; i < this.user.size(); i++) {
							if(this.user.get(i)!= null) {
								this.user.get(i).out.writeObject(obj);
								this.user.get(i).out.reset();
							}
						}
					}
				} catch (IOException e) {
					user.remove(this);
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}