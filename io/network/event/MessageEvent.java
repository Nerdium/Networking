package io.network.event;

import java.io.Serializable;

public class MessageEvent implements Serializable {
	private static final long serialVersionUID = -5541902066531761648L;
	private String message;
	
	public MessageEvent(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
