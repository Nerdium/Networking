package io.network;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class WriteTask implements Runnable {
	private ObjectOutputStream output;
	  private Object event;

	  public WriteTask(ObjectOutputStream output, Object event) throws IOException {
	    this.output = output;
	    this.event = event;
	  }

	@Override
	public void run() {
		try {
			output.writeObject(event);
	    } catch(IOException e) {
	      e.printStackTrace();
	    }
	}
}
