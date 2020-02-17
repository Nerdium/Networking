package io;

import java.io.IOException;
import java.util.Scanner;

import io.network.client.Client;
import io.network.event.MessageEvent;
import io.network.server.Server;

public class Main {
  public static void main (String[] args) throws IOException {
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