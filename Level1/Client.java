package Level1;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Level 1 - Basic TCP Client
 * Course: Network Programming
 * Student ID: 2320936
 * Port: 20936 (last 5 digits of academic ID)
 * Terminal=>java Client
 */
public class Client {

    private static final String SERVER_HOST = "localhost";
    private static final int    PORT        = 20936;       // must match Server.java

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   Network Programming - Level 1 Client   ");
        System.out.println("===========================================");
        System.out.println("Connecting to server at " + SERVER_HOST + ":" + PORT);

        // Connect to the server
        try (Socket socket = new Socket(SERVER_HOST, PORT)) {

            System.out.println("Connected to server successfully!");
            System.out.println("Type a message and press Enter to send.");
            System.out.println("Type 'exit' to disconnect.");
            System.out.println("-------------------------------------------");

            // Set up input and output streams
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter    serverOutput = new PrintWriter(socket.getOutputStream(), true);
            Scanner        userInput    = new Scanner(System.in);

            String userMessage;

            // Read messages from the user and send to server
            while (true) {
                System.out.print("[You] ");
                userMessage = userInput.nextLine();

                // Send message to server
                serverOutput.println(userMessage);

                // Wait for server's response
                String response = serverInput.readLine();
                if (response != null) {
                    System.out.println(response);
                }

                // If user typed exit, stop the loop
                if (userMessage.equalsIgnoreCase("exit")) {
                    break;
                }

                System.out.println("-------------------------------------------");
            }

            System.out.println("Disconnected from server.");

        } catch (ConnectException e) {
            System.err.println("Cannot connect to server! Make sure the server is running on port " + PORT);
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
