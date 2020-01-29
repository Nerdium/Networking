package main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class Server extends Thread{
	private Selector selector;
	private ServerSocketChannel serverChannel;
	private int port;
	
	public Server(int port) {
		this.port = port;
		try {
			selector = Selector.open();
			serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			serverChannel.bind(new InetSocketAddress("localhost", port));
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			while(!Thread.currentThread().isInterrupted()) {
				selector.select();
				
				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while(keys.hasNext()) {
					SelectionKey key = keys.next();
					keys.remove();
					
					if(!key.isValid()) {
						continue;
					}
					
					if(key.isAcceptable()) {
						System.out.println("Accepting connection");
			            accept(key);
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void accept(SelectionKey key) {
		
	}
}
