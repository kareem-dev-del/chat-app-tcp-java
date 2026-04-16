package Level3;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Level 3 - Advanced Multithreaded Server
 * Features:
 *   ✔ Online Users List
 *   ✔ Broadcast Messaging (clean format)
 *   ✔ Thread-safe handling (ConcurrentHashMap)
 */
public class Server {

    private static final int PORT = 20936;

    // Thread-safe map
    public static Map<String, PrintWriter> onlineUsers = new ConcurrentHashMap<>();

    private static int clientCount = 0;

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  Network Programming - Level 3 Server    ");
        System.out.println("  Features: Users List + Broadcast + Bots  ");
        System.out.println("===========================================");
        System.out.println("Server started on port: " + PORT);
        System.out.println("Waiting for clients...");
        System.out.println("-------------------------------------------");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientCount++;

                System.out.println("[+] New connection #" + clientCount
                        + " from: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler handler = new ClientHandler(clientSocket, clientCount);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            System.err.println("[SERVER ERROR] " + e.getMessage());
        }
    }

    /**
     * BROADCAST METHOD (Improved)
     * Sends message to all clients with clean format
     */
    public static void broadcast(String message, String senderName) {

        // Log on server
        if (senderName.equals("Server")) {
            System.out.println("[SERVER] " + message);
        } else {
            System.out.println("[" + senderName + "] " + message);
        }

        // Send to all clients
        for (Map.Entry<String, PrintWriter> entry : onlineUsers.entrySet()) {

            PrintWriter clientOut = entry.getValue();

            if (senderName.equals("Server")) {
                clientOut.println("[Server] " + message);
            } else {
                clientOut.println("[" + senderName + "]: " + message);
            }
        }
    }

    /**
     * ONLINE USERS LIST (Improved UI)
     */
    public static String getOnlineUsers() {

        if (onlineUsers.isEmpty()) {
            return "[Server] No users currently online.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[Online Users] (").append(onlineUsers.size()).append("): ");

        int i = 0;
        for (String user : onlineUsers.keySet()) {
            sb.append(user);
            if (i < onlineUsers.size() - 1) sb.append(", ");
            i++;
        }

        return sb.toString();
    }


}