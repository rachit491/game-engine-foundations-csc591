import java.io.Serializable;

import eventManagementSystem.Event;
import eventManagementSystem.EventHandler;
import eventManagementSystem.EventManager;
import eventManagementSystem.EventType;
import processing.core.PVector;

public class Physics implements IComponent, Serializable, EventHandler {
	float gravity;
	PVector velocity;
	int bounceY, speed;
	int maxYVel;
	ReplaySystem replay;
	GameObject obj;
	
	public Physics(GameObject obj, ReplaySystem replay) {
		this.obj = obj;
		//default values
		gravity = 0;
		velocity = PVector.random2D();
		while(velocity.y < velocity.x) {
			velocity = PVector.random2D();
		}
		velocity.mult(7);
		bounceY = 0;
		speed = 0;
		maxYVel = 0;
		this.replay = replay;
		
		if(obj != null) {
			obj.addComponent(this.getID(), this);
		}
	}
	
	public void addEventListener(EventManager em) {
		em.addDispatcher(EventType.JUMP_EVENT, this);
		em.addDispatcher(EventType.UPDATE_POSITION_EVENT, this);
	}
	
	public void setGravity(int gravity) {
		this.gravity = gravity;
	}
	
	public void setBounceY(int bounceY) {
		this.bounceY = bounceY;
	}
	
	public void setMaxYVel(int maxYVel) {
		this.maxYVel = maxYVel;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void setVelocityX(float x) {
		this.velocity.x = x;
	}
	
	public void setVelocityY(float f) {
		this.velocity.y = f;
	}
	
	public float getGravity() {
		if(this.replay.getIsReplaying()) {
			this.gravity = (float) 0.5;
		} else {
			this.gravity = 1;
		}
		return this.gravity;
	}
	
	public int getBounceY() {
		if(this.replay.getIsReplaying()) {
			this.bounceY = -15;
		} else {
			this.bounceY = -15;
		}
		return this.bounceY;
	}
	
	public int getMaxYVel() {
		if(this.replay.getIsReplaying()) {
			this.bounceY = 20;
		} else {
			this.bounceY = 10;
		}
		return this.maxYVel;
	}
	
	public int getSpeed() {
		if(this.replay.getIsReplaying()) {
			this.speed = 5;
		} else {
			this.speed = 10;
		}
		return this.speed;
	}
	
	public PVector getVelocity() {
		return this.velocity;
	}

	@Override
	public void onEvent(Event e) {
		/*if(this.replay.getIsReplaying()) {
			System.out.println("Replay-Jump");
		}
		else {*/
			String str = (String) e.getEventArgs().get(0);
			if(e.getEventType() == EventType.JUMP_EVENT && str.compareTo("jump") == 0) {
				setVelocityY(getBounceY());
				//System.out.println("JUMP_EVENT");
			}
			
			if(e.getEventType() == EventType.UPDATE_POSITION_EVENT && str.compareTo("update_position") == 0) {
				//setVelocityY(getVelocity().y + getGravity());
				
				//setVelocityY(Math.min(getMaxYVel(), getVelocity().y));
				//setVelocityY(Math.max(-getMaxYVel(), getVelocity().y));
				//System.out.println("UPDATE_POSITION_EVENT");
			}
		//}
	}

	@Override
	public GameObject getObject() {
		return this.obj;
	}

	@Override
	public Components getID() {
		return Components.Physics;
	}
	
}
