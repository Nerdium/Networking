package io.network.client;

import java.io.IOException;

import io.network.WriteTask;

public class Client {
	private ServerConnection serverConnection;
	//private String address;
	//private int port;
	
	public Client(String address, int port) {
		//this.address = address;
		//this.port = port;
		
		serverConnection = new ServerConnection(address, port);
		
	}
	
	public void start() {
		serverConnection.start();
	}
	
	
	public void write(Object event) throws IOException {
		WriteTask task = new WriteTask(serverConnection.getOutput(), event);
		task.run();
	}
}
