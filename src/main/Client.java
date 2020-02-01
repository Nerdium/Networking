package main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client extends Thread {
    private final static String HOSTNAME = "localhost";
    private final static int PORT = 6969;

    SocketChannel channel;

    @Override
    public void run() {
    	try {
    	channel = SocketChannel.open();
        // we open this channel in non blocking mode
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(HOSTNAME, PORT));

        while (!channel.finishConnect()) {
            // System.out.println("still connecting");
        }
        while (true) {
            String message = read();

            if (message.length() > 0) {
                System.out.println(message);
                write("Hello Server!");
                message = "";
            }
        }
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private void write(String message) throws IOException {
    	CharBuffer buffer = CharBuffer.wrap(message);
        while (buffer.hasRemaining()) {
            channel.write(Charset.defaultCharset().encode(buffer));
        }
    }
    
    private String read() throws IOException {
    	
    	String message = "";
    	ByteBuffer buffer = ByteBuffer.allocate(20);
    	
    	while(channel.read(buffer) > 0) {
    		buffer.flip();
    		
    		message += Charset.defaultCharset().decode(buffer);
    	}
    	
    	return message;
    }
    
    
}