package Level2;

import java.io.*;
import java.net.*;

/**
 * Level 2 - Multithreaded TCP Server
 * Course: Network Programming
 * Student ID: 2320936
 * Port: 20936
 *
 * This server accepts multiple clients at the same time.
 * Each client gets its own Thread (ClientHandler).
 * java Level2.Server
 */
public class Server {

    private static final int PORT = 20936;

    // Counter to give each client a unique ID
    private static int clientCount = 0;

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  Network Programming - Level 2 Server    ");
        System.out.println("  Multithreaded - Multiple Clients         ");
        System.out.println("===========================================");
        System.out.println("Server started on port: " + PORT);
        System.out.println("Waiting for clients...");
        System.out.println("-------------------------------------------");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            // This loop never stops — always ready for new clients
            while (true) {
                // Wait for a new client to connect
                Socket clientSocket = serverSocket.accept();

                // Give this client a unique number
                clientCount++;
                System.out.println("[+] Client #" + clientCount + " connected from: "
                        + clientSocket.getInetAddress().getHostAddress());

                // Create a new thread to handle this client
                // So the server is FREE to accept the next client immediately
                ClientHandler handler = new ClientHandler(clientSocket, clientCount);
                Thread thread = new Thread(handler);
                thread.start();

                System.out.println("[*] Thread started for Client #" + clientCount);
                System.out.println("-------------------------------------------");
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
