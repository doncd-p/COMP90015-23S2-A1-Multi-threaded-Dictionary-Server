/**
 * Author: Chenyang Dong
 * Student ID: 1074314
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.Socket;

/**
 * The type Worker thread.
 */
public class WorkerThread extends Thread {
    private Socket clientSocket = null;
    private final Dictionary dictionary;

    /**
     * Instantiates a new Worker thread.
     *
     * @param name       the name
     * @param dictionary the dictionary
     */
    public WorkerThread(String name, Dictionary dictionary) {
        super(name);
        this.dictionary = dictionary;
    }

    @Override
    public synchronized void run() {
        System.out.println(this.getName() + " is running!");

        while (!Thread.currentThread().isInterrupted()) {

            while (clientSocket == null) {
                try {
                    this.wait();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                JSONParser parser = new JSONParser();

                // Get the input/output streams for reading/writing data from/to the socket
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String receivedData = input.readLine(); // Read a line of text (assumes JSON is sent in one line)

                // Attempt to convert received data to JSON
                JSONObject request = (JSONObject) parser.parse(receivedData);
                System.out.println("Request from client: " + request.toJSONString());
                handleRequest(request);

                output.write(request.toJSONString());
                output.newLine(); // Add a newline character to indicate the end of the data
                output.flush(); // Flush the writer to ensure data is sent
                System.out.println("Response: " + request.toJSONString());

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IOException: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception: " + e.getMessage());
            } finally {
                clientSocket = null;
                WorkerPool.getInstance().addWorker(this);
            }
        }
    }

    /**
     * Handle request.
     *
     * @param request the request from the client
     */
    public void handleRequest(JSONObject request) {
        Object method = request.get("method");

        JSONObject response = new JSONObject();
        if (request.get("word").equals("")) {
            response.put("status", "error");
            response.put("msg", "Please enter a word");
        } else {

            synchronized (dictionary) {
                if (method.equals("add")) {
                    // Check if the word already exists in the dictionary
                    if (dictionary.search((String) request.get("word")) != null) {
                        response.put("status", "error");
                        response.put("msg", "Word already exists");
                    } else {
                        // Add the word into the dictionary
                        try {
                            JSONArray meanings = (JSONArray) request.get("meanings");
                            if (meanings.size() == 0) {
                                response.put("status", "error");
                                response.put("msg", "Please provide at least one meaning for the word");
                            } else {
                                dictionary.add((String) request.get("word"), meanings);
                                response.put("status", "success");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            response.put("status", "error");
                            response.put("msg", "An error occurred. Please try again later.");
                        }
                    }
                } else if (method.equals("search")) {
                    JSONArray meanings = dictionary.search((String) request.get("word"));
                    if (meanings == null) {
                        response.put("status", "error");
                        response.put("msg", "Word does not exist");
                    } else {
                        response.put("status", "success");
                        response.put("data", meanings);
                    }
                } else if (method.equals("update")) {
                    // Check if the word already exists in the dictionary
                    if (dictionary.search((String) request.get("word")) == null) {
                        response.put("status", "error");
                        response.put("msg", "Word does not exist");
                    } else {
                        // Update the meanings for the word
                        try {
                            JSONArray meanings = (JSONArray) request.get("meanings");
                            if (meanings.size() == 0) {
                                response.put("status", "error");
                                response.put("msg", "Please provide at least one meaning for the word");

                            } else {
                                dictionary.update((String) request.get("word"), meanings);
                                response.put("status", "success");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            response.put("status", "error");
                            response.put("msg", "An error occurred. Please try again later.");
                        }
                    }
                } else if (method.equals("remove")) {
                    // Check if the word already exists in the dictionary
                    if (dictionary.search((String) request.get("word")) == null) {
                        response.put("status", "error");
                        response.put("msg", "Word does not exist");
                    } else {
                        // Remove the word
                        try {
                            dictionary.remove((String) request.get("word"));
                            response.put("status", "success");
                        } catch (IOException e) {
                            e.printStackTrace();
                            response.put("status", "error");
                            response.put("msg", "An error occurred. Please try again later.");
                        }
                    }
                }
            }
        }
        request.put("response", response);
    }

    public void setConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
}