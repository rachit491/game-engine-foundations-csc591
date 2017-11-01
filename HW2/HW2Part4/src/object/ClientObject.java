package object;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientObject {
	
	private static Socket socket;
	private static ObjectInputStream in;
	private static ObjectOutputStream out;

	public static void main(String[] args) {
		try {
			socket = new Socket("127.0.0.1", 7172);
			System.out.println("Connection successful...");
			
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			
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
	ObjectInputStream in;
	List<GameObject> obj;
	private ObjectOutputStream out;
	
	public ClientReadThread(ObjectInputStream in, ObjectOutputStream out) {
		this.in = in;
		this.out = out;
	}
	
	public void run() {
		try {
			while((obj = (List<GameObject>) in.readObject()) != null) {
				for(int i=0; i<obj.size(); i++)
					System.out.println(obj.get(i).getTransform().getPositionX());
				out.writeObject(obj);
				out.reset();
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
} 