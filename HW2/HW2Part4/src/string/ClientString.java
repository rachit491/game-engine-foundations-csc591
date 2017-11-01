package string;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientString {
	
	private static Socket socket;
	private static BufferedReader in;
	private static PrintWriter out;

	public static void main(String[] args) {
		try {
			socket = new Socket("127.0.0.1", 7172);
			System.out.println("Connection successful...");
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			
			ClientReadThread input = new ClientReadThread(in, out);
			Thread thread = new Thread(input); 
			thread.start();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to start client");
		}
	}
}

class ClientReadThread implements Runnable {
	BufferedReader in;
	String obj = null;
	private PrintWriter out;
	
	public ClientReadThread(BufferedReader in, PrintWriter out) {
		this.in = in;
		this.out = out;
	}
	
	public void run() {
		try {
			while((obj = in.readLine()) != null) {
				System.out.println(obj);
				out.println(obj);
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
} 