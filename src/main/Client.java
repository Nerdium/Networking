package main;

import java.io.ObjectOutputStream;
import java.net.Socket;

import event.MessageEvent;

public class Client extends Thread {
	
	private Socket socket;
	private ObjectOutputStream oos;
	
	public Client() {
		start();
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket("localhost", 6969);
			oos = new ObjectOutputStream(socket.getOutputStream());
			while(socket.isConnected()) {
				oos.writeObject(new MessageEvent("Howdy!"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
