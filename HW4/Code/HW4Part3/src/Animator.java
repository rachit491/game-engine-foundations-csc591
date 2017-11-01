import java.io.Serializable;

public class Animator implements IComponent, Serializable{
	
	private Box box;
	private int xVel;
	private int yVel;
	
	public Animator(Box b, int xVel, int yVel) {
		this.box = b;
		this.xVel = xVel;
		this.yVel = yVel;
		
		if(b != null && b.getObject() != null) {
			b.getObject().addComponent(this.getID(), this);
		}
	}
	
	public GameObject getObject() {
		return this.box.getObject();
	}

	public Components getID() {
		return Components.Animator;
	}
	
	public void play() {
		int x = this.box.getObject().getTransform().getPositionX();
		int y = this.box.getObject().getTransform().getPositionY();
				
		if(xVel != 0) {
			x += this.xVel;
			this.box.getObject().getTransform().setPositionX(x);
		}
		
		if(yVel != 0) {
			y += this.yVel;
			this.box.getObject().getTransform().setPositionY(y);
		}
		
		if( (x+this.box.getWidth() > 320) || (x <= 0) ) {
	      this.xVel *= -1;
	    }
		
	}
	
	public void setXVelocity(int x) {
		this.xVel = x;
	}
	
	public int getXVelocity() {
		return this.xVel;
	}
	
	public void setYVelocity(int y) {
		this.yVel = y;
	}
	
	public int getYVelocity() {
		return this.yVel;
	}

}
