import java.util.LinkedList;
import java.util.Queue;

import eventManagementSystem.Event;
import eventManagementSystem.EventHandler;
import eventManagementSystem.EventManager;
import eventManagementSystem.EventType;

public class Record implements EventHandler {
	
	EventManager em;
	boolean isRecording;
	Timeline tl;
	long startTime;
	Queue<Event> q = new LinkedList<Event>();
	
	public Record(EventManager em, Timeline tl) {
		this.em = em;
		this.tl = tl;
		this.isRecording = false;
		this.em.addDispatcher(EventType.REPLAY_RECORD, this);
		this.em.addDispatcher(EventType.REPLAY_STOP, this);
	}
	
	@Override
	public void onEvent(Event e) {
		if(e.getEventType() == EventType.REPLAY_RECORD) {
			this.isRecording = true;
			this.startTime = (long) (this.tl.getTimeCycles()/this.tl.getTickSize());
		}
		else if(e.getEventType() == EventType.REPLAY_STOP) {
			this.isRecording = false;
		}
		else {
			this.q.add(e);
		}
	}
	
	public boolean getIsRecording() {
		return this.isRecording;
	}
	
	public long getStartTime() {
		return this.startTime;
	}

	public Queue<Event> getEventQueue() {
		return this.q;
	}

}
