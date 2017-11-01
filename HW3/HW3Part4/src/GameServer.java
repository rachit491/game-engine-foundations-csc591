import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import eventManagementSystem.Event;
import eventManagementSystem.EventType;

public class GameServer {
 
	static ServerSocket serverSocket;
	static Socket socket;
	static ObjectOutputStream out;
	static CopyOnWriteArrayList<ServerThread> wt = new CopyOnWriteArrayList<ServerThread>();
	static ObjectInputStream in;
	static Factory fct = new Factory();
	static GameObject go;
	static List<GameObject> plt = new ArrayList<GameObject>();
	static int[] x = new int[10];	//no of platforms visible at once
	 
	public static void main (String[] args) {
		try {
			serverSocket = new ServerSocket(7172);
			System.out.println("Server started...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		x[0] = 0;
		for(int i=1; i<10; i++)
			x[i] = new Random().nextInt(220);
		
		while(true) {
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ServerThread st = createElement(wt.size(), x);
			synchronized(wt) {
				wt.add(st);
				System.out.println(wt);
			}
			st.setList(wt);
			
			Thread thread = new Thread(st);
			thread.start();
			
			//ServerWriteThread swt = new ServerWriteThread(out, plt);
			//Thread thread2 = new Thread(swt);
			//thread2.start();
		}
	} 
	
	public static ServerThread createElement(int id, int[] x) {
		ServerThread st = null;
				
		try {
			out = new ObjectOutputStream(socket.getOutputStream());			
			
			//create a player object
			out.writeObject(new Event(EventType.INIT_PLAYER, new ArrayList<Object>() {{
				add(System.nanoTime());
				add(id);
				add(fct.createPlayer(id, 30, 35));
			}}));
			out.reset();
			
			//Platform
			for(int i=0; i<10; i++) {
				if(i == 6)
					plt.add(fct.createMovingPlatform(id, i, 10, 580-(i*60)));
				else
					plt.add(fct.createPlatform(id, i, x[i], 580-(i*60)));
				
				final int index = i;
				out.writeObject(new Event(EventType.INIT_PLATFORM, new ArrayList<Object>() {{
					add(System.nanoTime());
					add(id);
					add(plt.get(index));
				}}));
				out.reset();
			}
			
			in = new ObjectInputStream(socket.getInputStream());
			
			st = new ServerThread(out, in, id);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return st;
	}
}


/*class ServerWriteThread implements Runnable {
	ObjectOutputStream out;
	List<GameObject> platform;
	
	public ServerWriteThread(ObjectOutputStream out, List<GameObject> platform) {
		this.out = out;
		this.platform = platform;
	}
	
	public void run() {
		while(true) {
			try {
				platform.get(6).getAnimator().play();
				out.writeObject(platform.get(6));
				out.reset();
				
				Thread.sleep(1000);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}*/

class ServerThread implements Runnable {
 
	ObjectOutputStream out;
	ObjectInputStream in;
	CopyOnWriteArrayList<ServerThread> user = new CopyOnWriteArrayList<ServerThread>();
	int playerid;
	
	int pid;
	
	public ServerThread(ObjectOutputStream out, ObjectInputStream in, int clientID) {
		this.out = out;
		this.in = in;
		this.playerid = clientID;
	}
	
	public void setList(CopyOnWriteArrayList<ServerThread> user) {
		this.user = user;
	}
	
	public void run() {		
		Event ev;
		try {
			while((ev = (Event) in.readObject()) != null) {
				try {
					for (int i = 0; i < this.user.size(); i++) {
						if(this.user.get(i)!= null) {
							this.user.get(i).out.writeObject(ev);
							this.user.get(i).out.reset();
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