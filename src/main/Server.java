package main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Server extends Thread {
    private final static String CLIENT_CHANNEL = "clientChannel";
    private final static String SERVER_CHANNEL = "serverChannel";
    private final static String CHANNEL_TYPE = "channelType";

    private final static String HOSTNAME = "localhost";
    private final static int PORT = 6969;
    
    private ArrayList<SocketChannel> sockets = new ArrayList<SocketChannel>();

    /**
     * ServerSocketChannel represents a channel for sockets that listen to
     * incoming connections.
     */
    @Override
    public void run() {
    	try {
        // Create a new ServerSocketChannel. The channel is unbound.
        ServerSocketChannel channel = ServerSocketChannel.open();

        // Bind the channel to an address. The channel starts listening to
        // incoming connections.
        channel.bind(new InetSocketAddress(HOSTNAME, PORT));

        // Mark the ServerSocketChannel as non blocking
        channel.configureBlocking(false);

        /*
         * Create a selector that will by used for multiplexing. The selector
         * registers the ServerSocketChannel as well as all SocketChannels that are created
         */
        Selector selector = Selector.open();

        /*
         * Register the ServerSocketChannel with the selector. The OP_ACCEPT
         * option marks a selection key as ready when the channel accepts a new connection.
         * When the socket server accepts a connection this key is added to the list of
         * selected keys of the selector.
         * When asked for the selected keys, this key is returned and hence we
         * know that a new connection has been accepted.
         */
        SelectionKey socketServerSelectionKey = channel.register(selector, SelectionKey.OP_ACCEPT);
        // Set property in the key that identifies the channel
        Map<String, String> properties = new HashMap<>();
        properties.put(CHANNEL_TYPE, SERVER_CHANNEL);
        socketServerSelectionKey.attach(properties);
        // Wait for the selected keys
        while(true) {
            /*
             * The select method is a blocking method which returns when at least
             * one of the registered channel is selected.
             * In this example, when the socket accepts a new connection, this method
             * will return. Once a SocketClient is added to the list of registered
             * channels, then this method would also return when one of the clients
             * has data to be read or written.
             * It is also possible to perform a nonblocking select using the
             * selectNow() function.
             * We can also specify the maximum time for which a select function
             * can be blocked using the select(long timeout) function.
             */
            if (selector.select() == 0)
                continue;
            // The select method returns with a list of selected keys
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                /*
                 * the selection key could either by the socketserver informing
                 * that a new connection has been made, or a socket client that
                 * is ready for read/write we use the properties object attached
                 * to the channel to find out the type of channel.
                 */
                if (((Map<?, ?>) key.attachment()).get(CHANNEL_TYPE).equals(SERVER_CHANNEL)) {
                    // A new connection has been obtained. This channel is
                    // therefore a socket server.
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    // Accept the new connection on the server socket. Since the
                    // server socket channel is marked as non blocking
                    // this channel will return null if no client is connected.
                    SocketChannel clientSocketChannel = serverChannel.accept();

                    if (clientSocketChannel != null) {
                        // Set the client connection to be non blocking
                        clientSocketChannel.configureBlocking(false);
                        SelectionKey clientKey = clientSocketChannel.register(
                                selector,
                                SelectionKey.OP_READ,
                                SelectionKey.OP_WRITE
                        );
                        Map<String, String> clientProperties = new HashMap<>();
                        clientProperties.put(CHANNEL_TYPE, CLIENT_CHANNEL);
                        clientKey.attach(clientProperties);
                        
                        System.out.println(clientSocketChannel.getRemoteAddress());
                        
                        sockets.add(clientSocketChannel);
                        
                        // Write something to the new created client
                        write(clientSocketChannel, "Hello Client!");
                    }
                } else {
                    // Data is available for read (Buffer for reading)
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    if (key.isReadable()) {
                        System.out.println(read(clientChannel));
                    }
                }

                // Once a key is handled, it needs to be removed
                iterator.remove();
            }
        }
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private void write(SocketChannel clientSocketChannel, String message) throws IOException {
    	CharBuffer buffer = CharBuffer.wrap(message);
        while (buffer.hasRemaining()) {
            clientSocketChannel.write(Charset.defaultCharset().encode(buffer));
        }
        buffer.clear();
    }
    
    public void writeAll(String message) throws IOException {
    	for(SocketChannel clientChannel : sockets) {
    		write(clientChannel, message);
    	}
    }
    
    private String read(SocketChannel clientChannel) throws IOException {
    	String message = "";
    	ByteBuffer buffer = ByteBuffer.allocate(20);
    	
    	while(clientChannel.read(buffer) > 0) {
    		buffer.flip();
    		
    		message += Charset.defaultCharset().decode(buffer);
    	}
    	
    	return message;
    }
    
}