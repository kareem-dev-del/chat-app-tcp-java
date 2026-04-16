package Level2;

import java.io.*;
import java.net.*;

/**
 * ClientHandler - Runs in its own Thread
 * Handles all communication with ONE specific client.
 *
 * Implements Runnable so it can be used with Thread class.
 */
public class ClientHandler implements Runnable {

    private Socket socket;       // The connection to this client
    private int clientId;        // Unique ID for this client

    public ClientHandler(Socket socket, int clientId) {
        this.socket   = socket;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        System.out.println("[Thread-" + clientId + "] Handler started.");

        try {
            // Set up streams for this client
            BufferedReader input  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter    output = new PrintWriter(socket.getOutputStream(), true);

            // Welcome message to the client
            output.println("[Server] Welcome! You are Client #" + clientId);
            output.println("[Server] Type your messages. Type 'exit' to disconnect.");

            String message;

            // Keep reading messages from this client
            while ((message = input.readLine()) != null) {

                System.out.println("[Client #" + clientId + "] " + message);

                // Handle exit command
                if (message.equalsIgnoreCase("exit")) {
                    output.println("[Server] Goodbye, Client #" + clientId + "!");
                    System.out.println("[-] Client #" + clientId + " requested disconnect.");
                    break;
                }

                // Echo the message back with server stamp
                String response = "[Server → Client #" + clientId + "] You said: " + message;
                output.println(response);
            }

        } catch (IOException e) {
            // Client disconnected unexpectedly — server keeps running
            System.out.println("[!] Client #" + clientId + " disconnected unexpectedly: " + e.getMessage());
        } finally {
            // Always close the socket when done
            try {
                socket.close();
                System.out.println("[-] Connection closed for Client #" + clientId);
                System.out.println("-------------------------------------------");
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}
