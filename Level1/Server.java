package Level1;

import java.io.*;
import java.net.*;

/**
 * Level 1 - Basic TCP Server
 * Course: Network Programming
 * Student ID: 2320936
 * Port: 20936 (last 5 digits of academic ID)
 * Terminal=>java Server
 */
public class Server {

    // Port number = last 5 digits of academic ID: 2320936 → 20936
    private static final int PORT = 20936;

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   Network Programming - Level 1 Server   ");
        System.out.println("===========================================");
        System.out.println("Server starting on port: " + PORT);

        // Create ServerSocket to listen for incoming connections
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server is running and waiting for a client...");
            System.out.println("-------------------------------------------");

            // Accept one client connection (Level 1: only 1 client)
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected from: " + clientSocket.getInetAddress().getHostAddress());
            System.out.println("-------------------------------------------");

            // Set up input and output streams
            BufferedReader input  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter    output = new PrintWriter(clientSocket.getOutputStream(), true);

            String messageFromClient;

            // Keep receiving messages until client sends "exit"
            while ((messageFromClient = input.readLine()) != null) {

                System.out.println("[Client] " + messageFromClient);

                // Check if client wants to disconnect
                if (messageFromClient.equalsIgnoreCase("exit")) {
                    System.out.println("Client requested to disconnect.");
                    output.println("[Server] Goodbye! Connection closed.");
                    break;
                }

                // Build a response and send it back
                String response = "[Server] Message received: \"" + messageFromClient + "\"";
                output.println(response);
                System.out.println("[Server sent] " + response);
                System.out.println("-------------------------------------------");
            }

            System.out.println("Connection closed.");

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
