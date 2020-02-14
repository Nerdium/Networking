package io.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import io.network.event.MessageEvent;


public class ClientReader extends Thread {
	private Socket client;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	public ClientReader(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			output = new ObjectOutputStream(client.getOutputStream());
	    	input = new ObjectInputStream(client.getInputStream());
	    	while(client.isConnected()) {
	    		read();
	    	}

	    } catch (IOException | ClassNotFoundException e) {
	    	e.printStackTrace();
	    }
	  }

	  private void read() throws ClassNotFoundException, IOException {
			Object event = input.readObject();
			if(event instanceof MessageEvent) {
				MessageEvent msgEvent = (MessageEvent) event;
				System.out.println(msgEvent.getMessage());
			} else {
				System.out.println(event);
			}
		}

	  public Socket getClient() {
	    return client;
	  }
	  
	  public ObjectOutputStream getOutput() {
		  return output;
	  }
	  
}
