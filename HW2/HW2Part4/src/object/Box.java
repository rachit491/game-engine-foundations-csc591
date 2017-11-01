package object;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Random;

public class Box implements IComponent, Serializable {
	
	private GameObject obj;
	private int width;
	private int height;
	private int[] color;
	private Transform transform;
	private Rectangle rect;
	
	public Box(GameObject obj) {
		this.obj = obj;
		this.transform = obj.getTransform();
		this.width = 0;
		this.color = new int[]{0, 0, 0};
		this.height = 0;
		this.rect = new Rectangle(0, 0, 0, 0);
		
		if(obj != null) {
			obj.addComponent(this.getID(), this);
		}
	}
	
	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
		
	}
	
	public void setWidth(int w) {
		this.width = w;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public void setHeight(int h) {
		this.height = h;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public boolean getVisible() {
		return this.transform.getVisible();
	}
	
	@Override
	public GameObject getObject() {
		return this.obj;
	}

	@Override
	public Components getID() {
		return Components.Box;
	}

	public void setColor(int[] color) {
		this.color = color;
	}
	
	public int[] getColor() {
		return this.color;
	}
	
	public Rectangle getRectangle() {
		int x = this.transform.getPositionX();
		int y = this.transform.getPositionY();
		
		this.rect.setBounds(x, y, this.width, this.height);
		
		return this.rect;
	}

}
