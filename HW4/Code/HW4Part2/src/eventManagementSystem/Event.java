package eventManagementSystem;

import java.io.Serializable;
import java.util.ArrayList;

public class Event extends Object implements Serializable {
	
	EventType type;
	int priority, id;
	long timeStamp;
	ArrayList<Object> args = new ArrayList<Object>();
	
	public Event(EventType type, ArrayList<Object> args) {
		this.type = type;
		this.priority = type.getValue();
		this.timeStamp = (long) args.remove(0);
		this.setClientID((int) args.remove(0));
		this.args = args;
	}
	
	public EventType getEventType() {
		return this.type;
	}
	
	public long getTimeStamp() {
		return this.timeStamp;
	}
	
	public ArrayList<Object> getEventArgs() {
		return this.args;
	}
	
	public void setClientID(int index) {
		this.id = index;
	}
	
	public int getClientID() {
		return this.id;
	}
}
