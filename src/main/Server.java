package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;

public class Server extends Thread {
    private static final String MSG_HEARTBEAT_PING = "msg_hb_ping";
    private static final String MSG_HEARTBEAT_PONG = "msg_hb_pong";

    static int counter = 0;
    static Timer timer;

    public Server(int port) {

        try (
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {

            String inputLine, outputLine;

            // Initiate conversation with client
            Protocol kkp = new Protocol();
            outputLine = kkp.processInput(null);
            out.println(outputLine);

//            //create thread to print counter value
//            Thread t = new Thread(() -> {
//                while (true) {
//                    try {
//                        System.out.println("Thread reading counter is: " + counter);
//                        if (counter == 3) {
//                            System.out.println("Counter has reached 3 now will terminate");
//                            timer.cancel();//end the timer
//                            break;//end this loop
//                        }
//                        Thread.sleep(1000);
//                    } catch (InterruptedException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            });
//
//            timer = new Timer("MyTimer");//create a new timer
//            timer.scheduleAtFixedRate(new TimerTask(){
//                @Override
//                public void run(){
//                    System.out.println("Send: " + MSG_HEARTBEAT_PING);
//                    out.println(MSG_HEARTBEAT_PING);
//                    counter++;
//                }
//            }, 30, 200);
//
//            t.start();//start thread to display counter

            while ((inputLine = in.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye."))
                    break;
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}