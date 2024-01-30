/**
 * Author: Chenyang Dong
 * Student ID: 1074314
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

/**
 * The type Dictionary server.
 */
public class DictionaryServer {
    private static final int POOL_SIZE = 10;
    private volatile boolean shouldExit = false;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // Check if the correct number of arguments are provided
        if (args.length < 2) {
            System.out.println("Please provide the required arguments with <port> <dictionary filename>.");
            System.exit(1);
        }

        int port = 4444;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }

        String filename = args[1];
        DictionaryServer server = new DictionaryServer();

        try (ServerSocket listeningSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + listeningSocket.getLocalPort() + " for a connection");

            Dictionary dict = new Dictionary(filename);
            WorkerPool workerPool = WorkerPool.getInstance();
            workerPool.initializePool(POOL_SIZE, dict);

            // Listens for user input to exit gracefully
            Thread userInputThread = new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (!server.shouldExit) {
                    String userInput = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(userInput)) {
                        server.shouldExit = true;
                        System.out.println("Exiting gracefully...");
                        Thread.currentThread().interrupt();
                        System.exit(0);
                    }
                }
                scanner.close();
            });
            userInputThread.start();

            while (!server.shouldExit) {
                if (workerPool.hasFreeWorker()) {
                    workerPool.assignWork(listeningSocket.accept());
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }

    }

}