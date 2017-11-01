import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


class ClientReadWriteThread extends Thread {
	String line = null;
    BufferedReader br1 = null, br2 = null;
    PrintWriter pw = null;
    Socket s = null;
    int pref;
    ClientReadWriteThread other;
    Boolean busy;

    public ClientReadWriteThread(Socket s, int pref, ClientReadWriteThread other) {
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
	    		br1 = new BufferedReader(new InputStreamReader(System.in));
	    		br2 = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    		pw = new PrintWriter(s.getOutputStream());
	    		synchronized(this) {
	    			busy = true;
	    		}
	    		
	    		while(line == null)
	    			line = br1.readLine();
	    		
	    		System.out.println("Just read from Client: " + line + " ... now sending to Server");
	    		pw.println(line);
	    		pw.flush();
	    		
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
	    		//System.out.println("Now waiting for response from server, " + other.line);
	    		if(other.line != null) {
	    			other.line = other.br2.readLine();
	                System.out.println("Server Response : " + other.line);
	                other.line = other.br1.readLine();
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

public class AsyncClient {
	
	static int PORT = 7881;
    static String IP = "127.0.0.1";
    
	public static void main(String args[]) {
	    
		Socket s = null;
		
		if (args.length < 2) {
			System.out.println("Connecting to '127.0.0.1' using default portNumber: " + PORT);
	    } else {
	    	PORT = Integer.valueOf(args[0]).intValue();
	    	IP = args[1];
	    }
	
	    try {
	        s = new Socket(IP, PORT);
	        ClientReadWriteThread cw = new ClientReadWriteThread(s, 0, null);
	        ClientReadWriteThread cr = new ClientReadWriteThread(s, 1, cw);
	        cr.start();
	        cw.start();
	    }
	    catch (IOException e){
	    	// TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
}
