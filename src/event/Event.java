package event;

public class Event {
	private EVENT_TYPE eventType;
	
	public Event(EVENT_TYPE eventType) {
		this.eventType = eventType;
	}
	
	public EVENT_TYPE type() {
		 return eventType;
	}
}
