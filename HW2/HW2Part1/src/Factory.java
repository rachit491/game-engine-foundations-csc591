import java.util.Random;

/*
 * Class Responsible to create objects from Components
 */

public class Factory {
	
	public GameObject createPlayer() {
		GameObject obj = new GameObject();
		Transform transform = new Transform(obj);
		Box box = new Box(obj);
		
        box.setSize(30, 35);
        box.setColor(this.getRandomInt(256));
        
        transform = obj.getTransform();
        transform.setPosition(0, 0);	//default spawn
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
