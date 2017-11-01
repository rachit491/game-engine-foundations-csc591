package eventManagementSystem;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class EventManager implements Serializable {
	
	boolean wildCard;
	ObjectOutputStream out;
	HashMap<EventType, EventHandler> hmap = new HashMap<EventType, EventHandler>();
	PriorityQueue<Event> eventQueue = new PriorityQueue<Event>(50000, new Comparator<Event>() {
        public int compare(Event e1, Event e2) {
        	if(e1.timeStamp < e2.timeStamp) {
        		return -1;
        	}
        	else if(e1.timeStamp == e2.timeStamp) {
        		return e1.priority < e2.priority ? -1: 1;
        	}
        	else {
        		return 1;
        	}
        }
    });

	public EventManager() {
		this.wildCard = false;
	}
	
	public void addDispatcher(EventType type, EventHandler obj) {
		hmap.put(type, obj);
	}
	
	public void removeDispatcher(EventType type, EventHandler obj) {
		hmap.remove(type);
	}
	
	public void trigger(Event e) {
		
		if(e.getEventType() == EventType.REPLAY_FF || e.getEventType() == EventType.REPLAY_SM) {
			EventHandler ob = hmap.get(e.getEventType());
			ob.onEvent(e);
		}
		else {
			//insert any trigger event in priority queue first 
			//and then handle events as per the order
			this.eventQueue.add(e);
			e = this.eventQueue.poll();
			
			if(hmap.containsKey(e.getEventType())) {
				/*try {			
					out.writeObject(e);
					out.reset();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				if(wildCard) {
					//send a copy of the event to Recorder
					EventHandler ob2 = hmap.get(EventType.REPLAY_RECORD);
					ob2.onEvent(e);
				}
				
				EventHandler ob = hmap.get(e.getEventType());
				ob.onEvent(e);
				
				if(e.getEventType() == EventType.REPLAY_RECORD) {
					this.setWildCard(true);
				}
				
				if(e.getEventType() == EventType.REPLAY_STOP) {
					this.setWildCard(false);
				}
			}
		}
	}

	public void setWildCard(boolean b) {
		this.wildCard = b;
	}

	/*public void setOutputStream(OutputStream outputStream) {
		try {
			out = new ObjectOutputStream(outputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
