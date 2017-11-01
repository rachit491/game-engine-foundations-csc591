import java.io.Serializable;
import java.util.ArrayList;

import eventManagementSystem.*;

public class UserInputs implements IComponent, Serializable, EventHandler {
	int up;
	boolean onPlatform;
	boolean left, right, down;
	boolean draw;
	EventManager em;
	Timeline tl;
	ReplaySystem replay;
	
	public UserInputs(EventManager em, Timeline tl, ReplaySystem replay) {
		this.em = em;
		this.replay = replay;
		this.tl = tl;
		em.addDispatcher(EventType.INPUT_EVENT, this);
		em.addDispatcher(EventType.INPUT_EVENT_RELEASE, this);
	}
	
	public void setFlagDefault() {
		up = 0;
		draw = false;
		onPlatform = true;
	}
	
	@Override
	public void onEvent(Event e) {
		
		if(e.getEventType() == EventType.INPUT_EVENT) {
			this.handleUserInputs(e.getClientID(), e.getEventArgs());
			//System.out.println("INPUT_1");
		}
		
		if(e.getEventType() == EventType.INPUT_EVENT_RELEASE) {
			this.handleUserInputsReleased(e.getClientID(), e.getEventArgs());
			//System.out.println("INPUT_2");
		}
		
	}
	
	public void handleUserInputs(int cid, ArrayList<Object> args) {
		String str = (String) args.get(0);
		
		if(str.compareTo("keyPressed") == 0) {
			if((int)args.get(1) == 37) {
				left = true;
			}
			
			if((int)args.get(1) == 39) {
				right = true;
			}
			
			if((int)args.get(1) == 999) {
				if(up == 0 && onPlatform) {
					up = -1;
					down = false;
				}
				if(!this.replay.getIsReplaying())
					this.em.trigger(new Event(EventType.JUMP_EVENT, new ArrayList<Object>(){{
						add(tl.getTimeCycles());
						add(cid);
						add("jump");
					}}));
			}
		}
	}
	
	public void handleUserInputsReleased(int cid, ArrayList<Object> args) {
		String str = (String) args.get(0);
		
		if(str.compareTo("keyReleased") == 0) {
			if((int)args.get(1) == 37) {
				left = false;
			}
			
			if((int)args.get(1) == 39) {
				right = false;
			}
			
			if((int)args.get(1) == 999) {
				up = 0;
				down  = true;
				onPlatform = false;
			}
		}
	}

	@Override
	public GameObject getObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Components getID() {
		// TODO Auto-generated method stub
		return Components.UserInputs;
	}
}
