import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameServer {
 
	static ServerSocket serverSocket;
	static Socket socket;
	static ObjectOutputStream out;
	static CopyOnWriteArrayList<ServerThread> wt = new CopyOnWriteArrayList<ServerThread>();
	static ObjectInputStream in;
	static Factory fct = new Factory();
	static GameObject go;
	 
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
		}
	} 
	
	public static ServerThread createElement(int id) {
		ServerThread st = null;
				
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			
			go = fct.createPlayer(id, 30, 35);
			out.writeObject(go);		
			out.reset();
			
			//Platform
			
			in = new ObjectInputStream(socket.getInputStream());
			
			st = new ServerThread(out, in, id, go);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return st;
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
		try {
			while((go = (GameObject) in.readObject()) != null) {
				try {
					for (int i = 0; i < this.user.size(); i++) {
						if(this.user.get(i)!= null) {
							this.user.get(i).out.writeObject(go);
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