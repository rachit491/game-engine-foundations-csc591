package string;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerString {

	static ServerSocket serverSocket;
	static Socket socket;
	static PrintWriter out;
	static CopyOnWriteArrayList<ServerThread> wt = new CopyOnWriteArrayList<ServerThread>();
	static BufferedReader in;
	
	///////////// EDIT THESE VALUES //////////////
	static int maxClients = 2;
	static int iterations = 1000;
	////////////////////////////////////////////////
	
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
			
			ServerThread st = createElement(wt.size());
			
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
	
	public static ServerThread createElement(int id) {
		ServerThread st = null;
				
		try {
			out = new PrintWriter(socket.getOutputStream());
			
			//create a player object
			String go =  "Client-" + id + "-";	//
			out.println(go);		
			out.flush();	
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			st = new ServerThread(out, in, iterations, go);	//1000 iterations for gameloop
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return st;
	}
}

class ServerThread implements Runnable {
	 
	PrintWriter out;
	BufferedReader in;
	CopyOnWriteArrayList<ServerThread> user = new CopyOnWriteArrayList<ServerThread>();
	int iterations;
	String go;
	int count;
	
	public ServerThread(PrintWriter out, BufferedReader in, int iterations, String go) {
		this.out = out;
		this.in = in;
		this.iterations = iterations;
		this.go = go;
		this.count = 1;
	}
	
	public void setList(CopyOnWriteArrayList<ServerThread> user) {
		this.user = user;
	}
	
	public long getMilliseconds() {
		return System.currentTimeMillis();
	}
	
	public void run() {
		try {
			long prevTime = getMilliseconds();
			while((go = in.readLine()) != null && count < iterations) {				
				out.println(go+count);
				out.flush();
				count++;
			}
			long currTime = getMilliseconds();
			System.out.println(currTime-prevTime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}