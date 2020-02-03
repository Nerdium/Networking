package event;

public class MessageEvent extends Event {

	private String message;
	
	public MessageEvent(String message) {
		super(EVENT_TYPE.MESSAGE);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
