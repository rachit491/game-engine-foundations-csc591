import java.util.HashMap;

public class GameObject {
	private int handle;
	private static int guid = 1;
	private HashMap<Components, IComponent> componentsList;
	
	public GameObject() {
		this.handle = GameObject.guid++;
		this.componentsList = new HashMap<Components, IComponent>();
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
	
	public Transform getTransform() {
		return (Transform) this.componentsList.get(Components.Transform);
	}
	
	public Box getBox() {
		return (Box) this.componentsList.get(Components.Box);
	}

}
