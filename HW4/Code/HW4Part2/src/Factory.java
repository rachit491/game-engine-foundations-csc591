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
		Transform transform = new Transform(obj);
		Box box = new Box(obj);
		int[] pColor = {0, 255, 0};
		
        box.setSize(pWidth, pHeight);
        box.setColor(pColor);
        
        transform = obj.getTransform();
        transform.setPosition(350-pWidth, 545);	//default spawn
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
	public GameObject createPlatform(int clientID, int index, int x, int y, int width, int height) {
		GameObject obj = new GameObject(clientID);
		obj.setName("platform-" + clientID + "-" + index);
		Transform transform = new Transform(obj);
		Box box = new Box(obj);
		int[] pColor = {0, 0, 0};
		
		box.setSize(width, height);
		box.setColor(pColor);
		
		transform = obj.getTransform();
        transform.setPosition(x, y);
        transform.setVisible(true);
        
		return obj;
	}
	
	// creating a moving platform gameObject 
	public GameObject createMovingPlatform(int clientID, int index, int x, int y, int width, int height) {
		GameObject obj = new GameObject(clientID);
		obj.setName("platform-" + clientID + "-" + index);
		Transform transform = new Transform(obj);
		Box box = new Box(obj);
		Animator anime = new Animator(box, 1, 0);
		
		int[] pColor = {0, 0, 0};
		
		box.setSize(width, height);
		box.setColor(pColor);
		
		transform = obj.getTransform();
        transform.setPosition(x, y);
        transform.setVisible(true);
        
        anime.setupInitialValues(120, 80, 30, 330);
		return obj;
	}
	
	public GameObject createBullet(int clientID, GameObject player) {
		int x = player.getTransform().getPositionX() + player.getBox().getWidth()/2 - 2;
		int y = player.getTransform().getPositionY();
		
		GameObject obj = new GameObject(clientID);
		obj.setName("bullet-" + clientID);
		
		Transform transform = new Transform(obj);
		Box box = new Box(obj);
		Animator anime = new Animator(box, 0, -7);
		
		int[] color = {255, 255, 255};
		
		box.setSize(5, 10);
		box.setColor(color);
		
		transform = obj.getTransform();
		transform.setPosition(x, y);
		transform.setVisible(true);
		
		anime.setupInitialValues(0, 0, 0, 0);
		
		return obj;
	}

	public GameObject createAlienBullet(int alienIndex, GameObject alien) {
		int x = alien.getTransform().getPositionX() + alien.getBox().getWidth()/2 - 2;
		int y = alien.getTransform().getPositionY();
		
		GameObject obj = new GameObject(alienIndex);
		obj.setName("alien-bullet-" + alienIndex);
		
		Transform transform = new Transform(obj);
		Box box = new Box(obj);
		Animator anime = new Animator(box, 0, 5);
		
		int[] color = {255, 0, 255};
		
		box.setSize(3, 15);
		box.setColor(color);
		
		transform = obj.getTransform();
		transform.setPosition(x, y);
		transform.setVisible(true);
		
		anime.setupInitialValues(0, 0, 0, 0);
		
		return obj;
	}
	
}
