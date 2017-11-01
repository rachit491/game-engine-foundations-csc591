import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import eventManagementSystem.Event;
import eventManagementSystem.EventHandler;
import eventManagementSystem.EventManager;
import eventManagementSystem.EventType;

public class ReplaySystem implements EventHandler {
	
	EventManager em;
	Timeline tl, replayTL;
	boolean normal, replaying, ff;
	Record record;
	int clientID;
	
	Queue<Event> q = new LinkedList<Event>();
	
	public ReplaySystem(EventManager em, Timeline tl) {
		this.em = em;
		this.tl = tl;
		//this.normal = false;
		this.ff = false;
		record = new Record(em, tl);
		this.em.addDispatcher(EventType.REPLAY_NORMAL, this);
		this.em.addDispatcher(EventType.REPLAY_FF, this);
		this.em.addDispatcher(EventType.REPLAY_SM, this);
	}
	
	@Override
	public void onEvent(Event e) {
		if(this.getIsReplaying()) {
			if(e.getEventType() == EventType.REPLAY_SM) {
				this.replayTL.setTickSize(30);
				this.ff = false;
				System.out.println("SlowMo");
			}
			
			if(e.getEventType() == EventType.REPLAY_FF) {
				this.replayTL.setTickSize(120);	
				this.ff = true;
				System.out.println("Fast Forward");
			}
		}
		
		if(e.getEventType() == EventType.REPLAY_NORMAL) {
			this.clientID = e.getClientID();
			//pause the game time
			this.tl.setIsPaused(true);
			this.ff = false;
			//copy of old TL
			if(this.replayTL == null) {
				this.replayTL = new Timeline(this.record.getStartTime());
				this.replayTL.setTickSize(60);
			}
			//this.normal = true;
			this.replaying = true;
			System.out.println("REPLAY_NORMAL");
			
			this.replayStarted();
		}
	}
	
	public void replayStarted() {
		this.q = record.getEventQueue();
		if(!this.q.isEmpty()) {
			Event ev = this.q.peek();
			//System.out.println(ev.getEventType() + " " + ev.getClientID() + " " + ev.getTimeStamp());
			//System.out.println(this.tl.getTimeCycles() + " " + this.replayTL.getTimeCycles());
			if(ev.getTimeStamp() <= this.replayTL.getTimeCycles()) {
				this.q.poll();
				this.em.trigger(ev);
				if(this.ff)
					replayStarted();
			}
		}
		else {
			this.replayTL.setIsPaused(true);
			this.tl.setIsPaused(false);
			this.replaying = false;
			this.em.trigger(new Event(EventType.REPLAY_RESTORE_DATA, new ArrayList<Object>() {{
				add(tl.getTimeCycles());
				add(clientID);
				add("replayRestoreData");
			}}));
		}
	}
	
	public boolean getIsReplaying() {
		return this.replaying;
	}
}
