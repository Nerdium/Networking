package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import event.EVENT_TYPE;
import event.Event;
import event.MessageEvent;

public class ServerSocketThread extends Thread {
	
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private int port;
	private ObjectInputStream ois;
	
	public ServerSocketThread(int port) throws IOException {
		this.port = port;
		serverSocket = new ServerSocket(port);
		start();
	}
	
	@Override
	public void run() {
		try {
			//Blocking call
			clientSocket = serverSocket.accept();
			
			ois = new ObjectInputStream(clientSocket.getInputStream());
			
			while(clientSocket.isConnected()) {
				read();
			}
			
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void read() throws ClassNotFoundException, IOException {
		Event test = (Event) ois.readObject();
		if(test.type() == EVENT_TYPE.MESSAGE) {
			MessageEvent event = (MessageEvent) test;
			System.out.println(event.getMessage());
		} else {
			System.out.println(test);
		}
	}
	
}
