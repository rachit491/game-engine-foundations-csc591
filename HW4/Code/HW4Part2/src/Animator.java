import java.io.Serializable;

public class Animator implements IComponent, Serializable{
	
	private Box box;
	private int xVel;
	private int yVel;
	private int oldX;
	private int oldY;
	private int xlower;
	private int xupper;
	private int ylower;
	private int yupper;
	
	public Animator(Box b, int xVel, int yVel) {
		this.box = b;
		this.xVel = xVel;
		this.yVel = yVel;
		
		if(b != null && b.getObject() != null) {
			b.getObject().addComponent(this.getID(), this);
		}
	}
	
	public void setupInitialValues(int xl, int xu, int yl, int yu) {
		this.oldX = this.box.getObject().getTransform().getPositionX();
		this.oldY = this.box.getObject().getTransform().getPositionY();
		this.xlower = xl;
		this.xupper = xu;
		this.ylower = yl;
		this.yupper = yu;
	}
	
	public GameObject getObject() {
		return this.box.getObject();
	}

	public Components getID() {
		return Components.Animator;
	}
	
	public void playX(boolean active) {
		int x = this.box.getObject().getTransform().getPositionX();
		
		if(xVel != 0) {
			x += this.xVel;
			this.box.getObject().getTransform().setPositionX(x);
		}
		
		if((x < this.oldX-this.xlower || x > this.oldX+this.xupper) && active) {
			this.xVel *= -1;
	    }
	}
	
	public void playY(boolean active) {
		int y = this.box.getObject().getTransform().getPositionY();
				
		if(yVel != 0) {
			y += this.yVel;
			this.box.getObject().getTransform().setPositionY(y);
		}
		
		//CheckCollision
		if((y > this.oldY+this.yupper || y < this.oldY-this.ylower) && active) {
			this.yVel *= -1;
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
