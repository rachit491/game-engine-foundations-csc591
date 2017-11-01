import java.util.Random;

/*
 * Class Responsible to create objects from Components
 */

public class Factory {
	
	public Factory() {
		
	}
	
	public GameObject createPlayer(int clientID, int pWidth, int pHeight) {
		GameObject obj = new GameObject(clientID);
		Transform transform = new Transform(obj);
		Box box = new Box(obj);
		
        box.setSize(pWidth, pHeight);
        box.setColor(this.getRandomInt(256));
        
        transform = obj.getTransform();
        transform.setPosition(0, 570);	//default spawn
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
	
}
