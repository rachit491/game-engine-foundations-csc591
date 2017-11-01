package object;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerObject {

	static ServerSocket serverSocket;
	static Socket socket;
	static ObjectOutputStream out;
	static CopyOnWriteArrayList<ServerObjectThread> wt = new CopyOnWriteArrayList<ServerObjectThread>();
	static ObjectInputStream in;
	
	///////////// EDIT THESE VALUES //////////////
	static int maxClients = 2;
	static int iterations = 1000;
	static int size = 10;
	/////////////////////////////////////////////
	
	public static void main (String[] args) {
		try {
			serverSocket = new ServerSocket(7172);
			System.out.println("Server started...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = 0;
		while(i < maxClients) {
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ServerObjectThread st = createElement(wt.size());
			
			synchronized(wt) {
				wt.add(st);
				System.out.println(wt);
			}
			st.setList(wt);
			
			Thread thread = new Thread(st);
			thread.start();
			i++;
		}
	} 
	
	public static List<GameObject> createGameObject(int id, int size) {
		List<GameObject> obj = new ArrayList<GameObject>();
		for(int i=0; i < size; i++) {
			obj.add(new GameObject(id));
			obj.get(i).setName("player-" + id);
			Transform transform = new Transform(obj.get(i));
			Box box = new Box(obj.get(i));
			
	        box.setSize(30, 30);
	        
	        transform = obj.get(i).getTransform();
	        transform.setPosition(20, 545);	//default spawn
	        transform.setVisible(true);
		}
        
		return obj;
	}
	
	public static ServerObjectThread createElement(int id) {
		ServerObjectThread st = null;
				
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			
			//create a player object
			List<GameObject> go =  createGameObject(id, size);	//
			out.writeObject(go);
			out.reset();
			
			in = new ObjectInputStream(socket.getInputStream());
			
			st = new ServerObjectThread(out, in, iterations, go);	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return st;
	}
}

class ServerObjectThread implements Runnable {
	 
	ObjectOutputStream out;
	ObjectInputStream in;
	CopyOnWriteArrayList<ServerObjectThread> user = new CopyOnWriteArrayList<ServerObjectThread>();
	int iterations;
	List<GameObject> go;
	int count;
	int newX, oldX;
	
	public ServerObjectThread(ObjectOutputStream out, ObjectInputStream in, int iterations, List<GameObject> go) {
		this.out = out;
		this.in = in;
		this.iterations = iterations;
		this.go = go;
		this.count = 1;
	}
	
	public void setList(CopyOnWriteArrayList<ServerObjectThread> user) {
		this.user = user;
	}
	
	public long getMilliseconds() {
		return System.currentTimeMillis();
	}
	
	public void run() {
		try {
			long prevTime = getMilliseconds();
			while((go = (List<GameObject>) in.readObject()) != null && count < iterations) {
				//edit x values
				for(int i=0; i< go.size(); i++) {
					oldX = go.get(i).getTransform().getPositionX();
					newX = (oldX + 10) > 500 ? 0: oldX+10;
					go.get(i).getTransform().setPositionX(newX);
				}
				
				out.writeObject(go);
				out.reset();
				count++;
			}
			long currTime = getMilliseconds();
			System.out.println(currTime-prevTime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}