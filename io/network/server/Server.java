package io.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import io.network.WriteTask;

public class Server extends Thread {
	private SocketWriter socketWriter;
	private BlockingQueue<Runnable> writeTasks;
	private ArrayList<ClientReader> clients;
	private ServerSocket serverSocket;
	
	private int playerSlots;
	private int port;
		
	public Server(int port, int playerSlots) {
		this.port = port;
		this.playerSlots = playerSlots;
		writeTasks = new ArrayBlockingQueue<Runnable>(12);
		socketWriter = new SocketWriter(2, 4, 5000, TimeUnit.MILLISECONDS, writeTasks);

	    clients = new ArrayList<ClientReader>();
			
			//socketWriter.execute(new Task());
			
	}
	
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			listenForClient();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void clientConnected() throws IOException {
		if(clients.size() < playerSlots) {
			listenForClient();
		}
	}
	
	public void listenForClient() throws IOException {
		Socket client = serverSocket.accept();
		ClientReader newConnection = new ClientReader(client);
		clients.add(newConnection);
		newConnection.start();
    	System.out.println("Client connected: " + client);
		clientConnected();
	}
	
	public void write(ClientReader client, Object event) throws IOException {
		socketWriter.execute(new WriteTask(client.getOutput(), event));
	}

	public void writeAll(Object event) throws IOException {
		for(ClientReader client : clients) {
			write(client, event);
		}
	}
}

