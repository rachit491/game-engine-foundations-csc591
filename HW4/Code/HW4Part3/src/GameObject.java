import java.io.Serializable;
import java.util.HashMap;

public class GameObject implements Serializable {
	private int handle;
	private static int guid = 1;
	private int factor = 1000;
	private int pid;
	private HashMap<Components, IComponent> componentsList;
	private String name;
	
	public GameObject(int clientID) {
		this.handle = (this.factor*clientID) + GameObject.guid;
		GameObject.guid++;
		this.pid = (this.handle/this.factor);	//clientID
		this.componentsList = new HashMap<Components, IComponent>();
	}
	
	public void copy(GameObject go) {
		this.handle = go.getID();
		this.pid = go.getPID();
	}

	public void addComponent(Components id, IComponent comp) {
		this.componentsList.put(id, comp);
	}

	public HashMap<Components, IComponent> getComponents() {
		return this.componentsList;
	}
	
	public int getID() {
		return this.handle;
	}
	
	public int getPID() {
		return this.pid;
	}
	
	public void setPID(int pid) {
		this.pid = pid;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Transform getTransform() {
		return (Transform) this.componentsList.get(Components.Transform);
	}
	
	public Box getBox() {
		return (Box) this.componentsList.get(Components.Box);
	}

	public Animator getAnimator() {
		return (Animator) this.componentsList.get(Components.Animator);
	}

}
