package Level3;

import java.io.*;
import java.net.*;
import java.util.Map;

/**
 * ClientHandler - Runs in its own Thread for each connected client.
 *
 * Features:
 *   ✔ Unique usernames
 *   ✔ Online users list
 *   ✔ Broadcast messaging (clean format)
 *   ✔ Ignore empty messages
 */
public class ClientHandler implements Runnable {

    private Socket socket;
    private int clientId;
    private String clientName;
    private PrintWriter output;

    public ClientHandler(Socket socket, int clientId) {
        this.socket = socket;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            // ── STEP 1: Get client name ──────────────────────────
            output.println("[Server] Welcome! Please enter your name:");
            clientName = input.readLine();

            if (clientName == null || clientName.trim().isEmpty()) {
                clientName = "Client#" + clientId;
            }
            clientName = clientName.trim();

            // ✅ Fix duplicate names
            while (Server.onlineUsers.containsKey(clientName)) {
                clientName = clientName + "_" + clientId;
            }

            // ── STEP 2: Add to online users ─────────────────────
            Server.onlineUsers.put(clientName, output);

            System.out.println("[+] \"" + clientName + "\" joined. Online: "
                    + Server.onlineUsers.size());

            Server.broadcast(clientName + " has joined the chat!", "Server");

            // Send commands
            output.println("[Server] Hello, " + clientName + "! Commands:");
            output.println("[Server]   /users     → see who is online");
            output.println("[Server]   /broadcast <msg> → send to everyone");
            output.println("[Server]   exit        → disconnect");
            output.println("-------------------------------------------");

            // ── STEP 3: Message loop ────────────────────────────
            String message;
            while ((message = input.readLine()) != null) {

                if (message.trim().isEmpty()) continue;

                System.out.println("[" + clientName + "] " + message);

                // exit
                if (message.equalsIgnoreCase("exit")) {
                    output.println("[Server] Goodbye, " + clientName + "!");
                    break;
                }

                // /users
                else if (message.equalsIgnoreCase("/users")) {
                    output.println(Server.getOnlineUsers());
                }

                // /broadcast
                else if (message.toLowerCase().startsWith("/broadcast ")) {
                    String broadcastMsg = message.substring(11);

                    if (broadcastMsg.trim().isEmpty()) {
                        output.println("[Server] Usage: /broadcast <your message>");
                    } else {
                        Server.broadcast(broadcastMsg, clientName);
                    }
                }

                // ✅ Normal message → Broadcast
                else {
                    Server.broadcast(message, clientName);
                }
            }

        } catch (IOException e) {
            System.out.println("[!] \"" + clientName + "\" lost connection: " + e.getMessage());
        } finally {

            // ── STEP 4: Cleanup ────────────────────────────────
            if (clientName != null) {
                Server.onlineUsers.remove(clientName);

                System.out.println("[-] \"" + clientName + "\" left. Online: "
                        + Server.onlineUsers.size());

                Server.broadcast(clientName + " has left the chat.", "Server");
            }

            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }

            System.out.println("-------------------------------------------");
        }
    }
}