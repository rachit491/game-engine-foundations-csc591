import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ServerReadWriteThread extends Thread{  

    String line = null;
    BufferedReader br = null;
    PrintWriter pw = null;
    Socket s = null;
    int pref;
    ServerReadWriteThread other;
    Boolean busy;

    public ServerReadWriteThread(Socket s, int pref, ServerReadWriteThread other) {
        this.s = s;
        this.pref = pref;
        if(pref == 0) {
        	busy = true;
        } else { this.other = other; }
    }
    
    public synchronized Boolean isBusy() { return busy; }

    public void run() {	
	    try {
	    	if(pref == 0) {
	    		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    		pw = new PrintWriter(s.getOutputStream());
	    		synchronized(this) {
	    			busy = true;
	    		}
	    		
	    		while(line == null)
	    			line = br.readLine();
	    		
	    		synchronized(this) {
	    			busy = false;
	    			notify();
	    		}
	    	} 
	    	else {
	    		while(other.isBusy()) {
    				try {
    					synchronized(other) {
    						other.wait();
    					}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    		if(other.line != null) {
		            other.pw.println(other.line.toUpperCase());
		            other.pw.flush();
		            System.out.println("Response to Client " + other.getName() + " : " + other.line);
		            other.line = other.br.readLine();
		            synchronized(other) {
		            	busy = true;
		            }
	    		}
	    	}
	    } catch (IOException | NullPointerException e){
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
	}
}


public class AsyncServer {
	
	public static void main(String args[]) {
	
	    Socket s = null;
	    ServerSocket ss = null;
	    int PORT = 7881;
	    
	    if (args.length < 1) {
	    	System.out.println("Using default portNumber: " + PORT);
	    } else {
	    	PORT = Integer.valueOf(args[0]).intValue();
	    }
	    
	    try {
	        ss = new ServerSocket(PORT); // can also use static final PORT_NUM , when defined	
	    } catch(IOException e) {
	    	// TODO Auto-generated catch block
		    e.printStackTrace();
	    }
	
	    while(true) {
	        try {
	            s = ss.accept();
	            ServerReadWriteThread rt = new ServerReadWriteThread(s, 0, null);
	            ServerReadWriteThread wt = new ServerReadWriteThread(s, 1, rt);
	            wt.start();
	            rt.start();
	        } catch(Exception e) { 
		        e.printStackTrace();
		        System.out.println("Connection Error");
		    }
	    }
	
	}

}