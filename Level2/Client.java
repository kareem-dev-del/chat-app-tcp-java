package Level2;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Level 2 - TCP Client
 * Same as Level 1 but now multiple instances can run at the same time.
 * Each instance connects as a separate client to the server.
 * java Level2.Client
 */
public class Client {

    private static final String SERVER_HOST = "localhost";
    private static final int    PORT        = 20936;

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   Network Programming - Level 2 Client   ");
        System.out.println("===========================================");
        System.out.println("Connecting to server at " + SERVER_HOST + ":" + PORT + "...");

        try (Socket socket = new Socket(SERVER_HOST, PORT)) {

            System.out.println("Connected successfully!");
            System.out.println("-------------------------------------------");

            BufferedReader serverInput  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter    serverOutput = new PrintWriter(socket.getOutputStream(), true);
            Scanner        userInput    = new Scanner(System.in);

            // Print welcome messages from server
            System.out.println(serverInput.readLine());
            System.out.println(serverInput.readLine());
            System.out.println("-------------------------------------------");

            // Thread to listen for server messages in background
            // So we can send and receive at the same time
            Thread listenerThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = serverInput.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });
            listenerThread.setDaemon(true);
            listenerThread.start();

            // Main loop: read from user and send to server
            while (true) {
                System.out.print("[You] ");
                String message = userInput.nextLine();
                serverOutput.println(message);

                if (message.equalsIgnoreCase("exit")) {
                    Thread.sleep(300); // wait for goodbye message
                    break;
                }
            }

            System.out.println("Disconnected. Goodbye!");

        } catch (ConnectException e) {
            System.err.println("Cannot connect! Make sure the server is running on port " + PORT);
        } catch (IOException | InterruptedException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
