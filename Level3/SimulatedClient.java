package Level3;

import java.io.*;
import java.net.*;

/**
 * SimulatedClient (BOT)
 * ----------------------
 * This class simulates real users automatically.
 *
 * Each bot will:
 * 1. Connect to the server
 * 2. Send its name
 * 3. Send automatic messages
 * 4. Call /users command
 * 5. Send broadcast message
 * 6. Disconnect
 */
public class SimulatedClient implements Runnable {

    private static final String HOST = "localhost";
    private static final int PORT = 20936;

    private String name;
    private String[] messages;

    public SimulatedClient(String name, String[] messages) {
        this.name = name;
        this.messages = messages;
    }

    @Override
    public void run() {
        System.out.println("[BOT STARTED] " + name);

        try (Socket socket = new Socket(HOST, PORT)) {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            // Listen to server messages (background)
            Thread listener = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println("[BOT " + name + " RECEIVED] " + msg);
                    }
                } catch (IOException e) {
                    System.out.println("[BOT " + name + "] Disconnected from server.");
                }
            });

            listener.setDaemon(true);
            listener.start();

            Thread.sleep(500);

            // 1. Send bot name
            out.println(name);
            System.out.println("[BOT] Name sent: " + name);

            Thread.sleep(500);

            // 2. Send normal messages
            for (String msg : messages) {
                out.println(msg);
                System.out.println("[BOT SENT] " + name + ": " + msg);
                Thread.sleep(800);
            }

            // 3. Check online users
            out.println("/users");
            Thread.sleep(500);

            // 4. Broadcast message
            out.println("/broadcast Hello from " + name + " (simulated bot)");
            Thread.sleep(500);

            // 5. Exit
            out.println("exit");

            System.out.println("[BOT FINISHED] " + name);

        } catch (ConnectException e) {
            System.out.println("[ERROR] Cannot connect server for bot: " + name);
        } catch (Exception e) {
            System.out.println("[ERROR] Bot " + name + ": " + e.getMessage());
        }
    }

    /**
     * MAIN - Run 3 bots in parallel
     */
    public static void main(String[] args) throws InterruptedException {

        System.out.println("=================================");
        System.out.println("   Starting Simulated Clients   ");
        System.out.println("=================================");

        SimulatedClient bot1 = new SimulatedClient("Mohamed", new String[]{
                "Hi there!",
                "I am bot 1",
                "Testing chat system"
        });

        SimulatedClient bot2 = new SimulatedClient("Omar", new String[]{
                "Hello everyone",
                "Bot 2 here",
                "System is working"
        });

        SimulatedClient bot3 = new SimulatedClient("Ibrahim", new String[]{
                "Hey!",
                "I am bot 3",
                "Final test message"
        });

        Thread t1 = new Thread(bot1);
        Thread t2 = new Thread(bot2);
        Thread t3 = new Thread(bot3);

        t1.start();
        Thread.sleep(300);

        t2.start();
        Thread.sleep(300);

        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("=================================");
        System.out.println("   All Bots Finished ✔          ");
        System.out.println("=================================");
    }
}