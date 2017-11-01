import java.io.*;
import java.net.*;

public class GClient {

	static String IP = "127.0.0.1";
	static int PORT = 7171;
	public static void main(String[] args) throws IOException {
		Socket s = null;
	    String line = null;
	    BufferedReader br = null;
	    BufferedReader input = null;
	    PrintWriter output = null;
	    String response = null;
	    
	    if (args.length < 2) {
	      System.out.println("Connecting to '127.0.0.1' using default portNumber: " + PORT);
	    } else {
	      PORT = Integer.valueOf(args[0]).intValue();
	      IP = args[1];
	    }

	    try {
			s = new Socket(IP, PORT);
			br = new BufferedReader(new InputStreamReader(System.in));
	        input = new BufferedReader(new InputStreamReader(s.getInputStream()));
	        output = new PrintWriter(s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    try {
	        line = br.readLine(); 
	        while (line.compareTo(":q") != 0) {
                output.println(line);
                output.flush();
                response = input.readLine();
                System.out.println("Server Response : " + response);
                line = br.readLine();
            }
	    } catch (IOException e) {
	    	// TODO Auto-generated catch block
	        e.printStackTrace();
	    } finally {
	    	if(s != null)
	        	s.close();
	        System.out.println("Connection Closed");
	    }
	}
	
}