package event;

import java.io.Serializable;

public class Event implements Serializable {
	private static final long serialVersionUID = -4125922486277157740L;
	
	private EVENT_TYPE eventType;
	
	public Event(EVENT_TYPE eventType) {
		this.eventType = eventType;
	}
	
	public EVENT_TYPE type() {
		 return eventType;
	}
	
	@Override
	public String toString() {
		return "YEET!";
	}
	
}
