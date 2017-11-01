import java.io.Serializable;

public class Transform implements IComponent, Serializable {
	private int[] position;
	private GameObject obj;
	private boolean visible;
	
	public Transform(GameObject o) {
		this.visible = true;
		this.obj = o;
		this.position = new int[]{0, 0};
		if(o != null) {
			o.addComponent(this.getID(), this);
		}
	}
	
	@Override
	public Components getID() {
		return Components.Transform;
	}

	@Override
	public GameObject getObject() {
		return this.obj;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean getVisible() {
		return this.visible;
	}
	
	public void setPosition(int x, int y) {
		this.position[0] = x;
		this.position[1] = y;
	}
	
	public void setPositionX(int x) {
		this.position[0] = x;
	}
	
	public void setPositionY(int y) {
		this.position[1] = y;
	}
	
	public int getPositionX() {
		return this.position[0];
	}
	
	public int getPositionY() {
		return this.position[1];
	}
	
}
