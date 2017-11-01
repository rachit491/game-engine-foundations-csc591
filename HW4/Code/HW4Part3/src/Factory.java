import java.util.Random;

/*
 * Class Responsible to create objects from Components
 */

public class Factory {
	
	public Factory() {
		
	}
	
	//creating a player gameObject 
	public GameObject createPlayer(int clientID, int pWidth, int pHeight) {
		GameObject obj = new GameObject(clientID);
		obj.setName("player-" + clientID);
		
		int y = (clientID % 2 == 0) ? 570: 15;
		Transform transform = new Transform(obj);
		Box box = new Box(obj);
		
        box.setSize(pWidth, pHeight);
        box.setColor(this.getRandomInt(256));
        
        transform = obj.getTransform();
        transform.setPosition(this.getRandomInt(300)[0], y);	//default spawn
        transform.setVisible(true);
        
		return obj;
	}
	
	public int[] getRandomInt(int max) {
		Random r = new Random();
		int[] color = new int[3];
		
		color[0] = r.nextInt(max);
		color[1] = r.nextInt(max);
		color[2] = r.nextInt(max);
		
		return color;
	}
	
	// creating a platform gameObject 
	public GameObject createPlatform(int clientID, int index, int x, int y) {
		GameObject obj = new GameObject(clientID);
		obj.setName("platform-" + clientID + "-" + index);
		Transform transform = new Transform(obj);
		Box box = new Box(obj);
		int[] pColor = {0, 0, 0};
		
		box.setSize(100, 15);
		box.setColor(pColor);
		
		transform = obj.getTransform();
        transform.setPosition(x, y);
        transform.setVisible(true);
        
		return obj;
	}
	
	// creating a moving platform gameObject 
	public GameObject createMovingPlatform(int clientID, int index, int x, int y, int pWidth, int pHeight) {
		GameObject obj = new GameObject(clientID);
		obj.setName("platform-" + clientID + "-" + index);
		
		Transform transform = new Transform(obj);
		Box box = new Box(obj);
		Animator anime = new Animator(box, 10, 0);
		Physics phy = new Physics(obj, null);
		
		int[] pColor = {255, 0, 0};
		
		box.setSize(pWidth, pHeight);
		box.setColor(pColor);
		
		transform = obj.getTransform();
        transform.setPosition(x, y);
        transform.setVisible(true);
        
		return obj;
	}
	
}
