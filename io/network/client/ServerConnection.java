package io.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import io.network.event.MessageEvent;

public class ServerConnection extends Thread {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private String address;
	private int port;
	
	public ServerConnection(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("test");
			socket = new Socket(address, port);
			//Connected
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			
			System.out.println("Connected to server...");
			
			while(socket.isConnected()) {
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
	
	public Socket getSocket() {
		return socket;
	}
	
	public ObjectOutputStream getOutput() {
		return output;
	}
}
