import java.io.*;
import java.net.*;
import java.util.concurrent.*;

class ServerThread extends Thread {
	
	Socket s = null;
    String line = null, threadName = null;
    BufferedReader input = null, x = null;
    PrintWriter output = null;
    String response = null;
    CopyOnWriteArrayList<Socket> list = new CopyOnWriteArrayList<Socket>();
    
	public ServerThread(Socket s, CopyOnWriteArrayList<Socket> list) {
		this.s = s;
		this.list = list;
	}
	
	public synchronized void addToList(Socket s) {
		list.add(s);
	}
	
	public void run() {
		
		synchronized(list) {
			//Adding socket to the list
			addToList(s);
			System.out.println("Client connection list: " + list);
		}
		
        try {
        	input = new BufferedReader(new InputStreamReader(s.getInputStream()));
			output = new PrintWriter(s.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	    try {
	        line = input.readLine();
	        while (line.compareTo(":q") != 0) {	//:q means client has terminated connection
	        	line = line.toUpperCase();	//Changing to upper case
	            output.println(line);
	            output.flush();
	            System.out.println("Response to Client " + this.getName() + " : " + line);
	            line = input.readLine();
	        }   
	    } catch (NullPointerException | IOException e) {
	    	// TODO Auto-generated catch block
	        //e.printStackTrace();
	    	System.out.println(this.getName() + " has terminated connection");
	    } finally{    
		    try{
		        System.out.println("Connection Closing..");
		        if (s != null) {
			        s.close();
			        System.out.println("Socket Closed");
			    }
		    } catch (IOException e2) {
		    	// TODO Auto-generated catch block
		    	e2.printStackTrace();
		    }
	    }//end finally
	}
}

public class GEServer {
	
	static int PORT = 7171;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Socket s = null;
		ServerSocket ss = null;
		CopyOnWriteArrayList<Socket> list = new CopyOnWriteArrayList<Socket>();
		
		if (args.length < 1) {
	      System.out.println("Using default portNumber: " + PORT);
	    } else {
	      PORT = Integer.valueOf(args[0]).intValue();
	    }

		try {
			ss = new ServerSocket(PORT);
    		while (true) {
    			try {
    				s = ss.accept();
    				ServerThread sThread = new ServerThread(s, list);
    				sThread.start();    				
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				System.out.println("Connection Error!");
    			}
    		}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
