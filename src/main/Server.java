package main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class Server {
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private InetAddress ip;
	private int port;
	
	public Server() {
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			ip = InetAddress.getByName("localhost");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void init(int port) {
		try {
			this.port = port;
			serverSocketChannel.bind(new InetSocketAddress(ip, port));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
