package main;

import java.io.IOException;
import java.util.ArrayList;

public class Server extends Thread {
	
	private int openSlots;
	private int port;
	private ArrayList<ServerSocketThread> sockets = new ArrayList<ServerSocketThread>();
	
	public Server(int port, int slots) {
		this.port = port;
		openSlots = slots;
		start();
	}
	
	@Override
	public void run() {
		try {
			addSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void addSocket() throws IOException {
		sockets.add(new ServerSocketThread(port));
	}
	
}
