import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Serializable {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		String line1, line2;
		
		BufferedReader r1 = new BufferedReader(new InputStreamReader(System.in));
		Socket s = new Socket("127.0.0.1", 9399);
	
		BufferedReader r2 = new BufferedReader(new InputStreamReader(s.getInputStream()));
		System.out.println("Enter a string : ");
		line1 = r1.readLine();
		
		PrintStream w = new PrintStream(s.getOutputStream());
		w.println(line1);
		
		//Read Server Response
		line2 = r2.readLine();
		System.out.println("Server Responded: " + line2);
		
	}

}
