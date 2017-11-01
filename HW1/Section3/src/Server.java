import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Serializable {

	public static void main(String[] args) throws IOException {
		
		String line;
		
		//Creating Server Socket
		ServerSocket ss = new ServerSocket(9399);
		
		//////////////////////////////  Client 1   /////////////////////////////
		Socket s1 = ss.accept();
		
		//Creating its InputStream Reader
		BufferedReader br1 = new BufferedReader(new InputStreamReader(s1.getInputStream()));
		//Scanner br1 = new Scanner(s1.getInputStream());
		line = br1.readLine();
		System.out.println("Client 1 sent : " + line);
		
		PrintStream w1 = new PrintStream(s1.getOutputStream());
		//Server multiplies the number to itself and responds
		w1.println(line.toUpperCase());
		
		//////////////////////////////Client 2   /////////////////////////////
		Socket s2 = ss.accept();
		
		//Creating its InputStream Reader
		BufferedReader br2 = new BufferedReader(new InputStreamReader(s2.getInputStream()));
		line = br2.readLine();
		System.out.println("Client 2 sent : " + line);
		
		PrintStream w2 = new PrintStream(s2.getOutputStream());
		//Server multiplies the number to itself and responds
		w2.println(line.toUpperCase());
		
		//////////////////////////////  Client 3   /////////////////////////////
		Socket s3 = ss.accept();
		
		//Creating its InputStream Reader
		BufferedReader br3 = new BufferedReader(new InputStreamReader(s3.getInputStream()));
		line = br3.readLine();
		System.out.println("Client 3 sent : " + line);
		
		PrintStream w3 = new PrintStream(s3.getOutputStream());
		//Server multiplies the number to itself and responds
		w3.println(line.toUpperCase());
		
	}
}