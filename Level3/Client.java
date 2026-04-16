package Level3;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Level 3 - Human Client (Improved)
 * Features:
 *   ✔ Async listener thread
 *   ✔ Clean exit handling
 *   ✔ Better user experience
 */
public class Client {

    private static final String SERVER_HOST = "localhost";
    private static final int PORT = 20936;

    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println("   Network Programming - Level 3 Client   ");
        System.out.println("===========================================");
        System.out.println("Connecting to " + SERVER_HOST + ":" + PORT + "...");

        try (Socket socket = new Socket(SERVER_HOST, PORT)) {

            System.out.println("Connected!");
            System.out.println("-------------------------------------------");

            BufferedReader serverInput = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter serverOutput = new PrintWriter(
                    socket.getOutputStream(), true);

            Scanner keyboard = new Scanner(System.in);

            // ── Listener Thread ───────────────────────────────
            Thread listener = new Thread(() -> {
                try {
                    String line;
                    while ((line = serverInput.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("[!] Server disconnected.");
                }
            });

            listener.setDaemon(true);
            listener.start();

            // ── Send messages ────────────────────────────────
            while (true) {

                String input = keyboard.nextLine().trim();

                if (input.isEmpty()) {
                    continue; // ignore empty input
                }

                serverOutput.println(input);

                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Disconnecting...");
                    break;
                }
            }

        } catch (ConnectException e) {
            System.err.println("[ERROR] Cannot connect to server on port " + PORT);
        } catch (IOException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }

        System.out.println("Goodbye!");
    }
}