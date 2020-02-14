package io;

import java.io.IOException;

public class Main {
  public static void main (String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Server or client: ");
		if(sc.nextLine().equals("s")) {
			Server server = new Server(6969, 2);
			server.start();
			System.out.println("Enter stuff:");
			for(;;) {
				MessageEvent event = new MessageEvent(sc.nextLine());
				server.writeAll(event);
			}
		} else {
			Client client = new Client("localhost", 6969);
			client.start();
			System.out.println("Enter stuff:");
			for(;;) {
				MessageEvent event = new MessageEvent(sc.nextLine());
				client.write(event);
			}
		}
  }
}