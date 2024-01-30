/**
 * Author: Chenyang Dong
 * Student ID: 1074314
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Arrays;

/**
 * The type Dictionary client.
 */
public class DictionaryClient {

    private String ip;
    private int port;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Please provide the required arguments with <server address> <port>.");
            System.exit(1);
        }
        int port = 4444;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }
        new DictionaryClient(args[0], port);
    }

    /**
     * Instantiates a new Dictionary client.
     *
     * @param ip   the ip
     * @param port the port
     */
    public DictionaryClient(String ip, int port) {
        this.ip = ip;
        this.port = port;

        // Initialize the GUI
        new GUI(this);
    }

    /**
     * Add the word.
     *
     * @param word     the word
     * @param meanings the meanings
     */
    public void add(String word, String[] meanings) {
        JSONObject request = new JSONObject();
        JSONArray meaningsList = new JSONArray();
        meaningsList.addAll(Arrays.asList(meanings));

        request.put("method", "add");
        request.put("word", word);
        request.put("meanings", meaningsList);

        try {
            JSONObject jsonResponse = sendRequestAndParseResponse(request);
            JSONObject responseObj = (JSONObject) jsonResponse.get("response");
            handleResponse(responseObj, "Word Added Successfully");
        } catch (ConnectException e) {
            e.printStackTrace();
            System.out.println("ConnectException: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Server not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Search the word.
     *
     * @param word the word
     */
    public void search(String word) {
        JSONObject request = new JSONObject();
        request.put("method", "search");
        request.put("word", word);

        try {
            JSONObject jsonResponse = sendRequestAndParseResponse(request);
            JSONObject responseObj = (JSONObject) jsonResponse.get("response");
            String status = (String) responseObj.get("status");

            // Check the status of the response and show dialog accordingly
            if ("success".equals(status)) {
                JSONArray meanings = (JSONArray) responseObj.get("data");
                StringBuilder meaningsText = new StringBuilder("Meanings for " + word + ":\n\n");
                for (Object meaning : meanings) {
                    meaningsText.append("- ").append(meaning.toString()).append("\n");
                }
                JOptionPane.showMessageDialog(null, meaningsText.toString(), "Meanings",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                handleResponse(responseObj, null);
            }
        } catch (ConnectException e) {
            e.printStackTrace();
            System.out.println("ConnectException: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Server not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Remove the word.
     *
     * @param word the word
     */
    public void remove(String word) {
        JSONObject request = new JSONObject();
        request.put("method", "remove");
        request.put("word", word);

        try {
            JSONObject jsonResponse = sendRequestAndParseResponse(request);
            handleResponse((JSONObject) jsonResponse.get("response"), "Word deleted");
        } catch (ConnectException e) {
            System.out.println("ConnectException: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Server not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Update the word.
     *
     * @param word     the word
     * @param meanings the meanings
     */
    public void update(String word, String[] meanings) {
        JSONObject request = new JSONObject();
        request.put("method", "update");
        request.put("word", word);
        JSONArray meaningsList = new JSONArray();
        meaningsList.addAll(Arrays.asList(meanings));
        request.put("meanings", meaningsList);

        try {
            JSONObject jsonResponse = sendRequestAndParseResponse(request);
            handleResponse((JSONObject) jsonResponse.get("response"), "Word Updated");
        } catch (ConnectException e) {
            e.printStackTrace();
            System.out.println("ConnectException: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Server not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private JSONObject sendRequestAndParseResponse(JSONObject request)
            throws IOException, ConnectException, ParseException {
        String response = communicate(request.toJSONString()); // Get the response
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(response); // Parse the response
        return jsonResponse;
    }

    private void handleResponse(JSONObject responseObj, String successMessage) {
        String status = (String) responseObj.get("status");
        String errorMessage = (String) responseObj.get("msg"); // Get the error message

        // Check the status of the response and show dialog accordingly
        if ("success".equals(status)) {
            if (successMessage != null) {
                JOptionPane.showMessageDialog(null, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Communicates with the server by sending data and receiving a response.
     *
     * @param sendData The data to be sent to the server.
     * @return The response received from the server.
     * @throws IOException the io exception
     */
    public String communicate(String sendData) throws IOException {
        Socket socket = new Socket(ip, port);

        // Get the input/output streams for reading/writing data from/to the socket
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        System.out.println("Sent Data: " + sendData);
        output.write(sendData);
        output.newLine(); // Add a newline character to indicate the end of the data
        output.flush(); // Flush the writer to ensure data is sent

        // Read the response
        String response = input.readLine();
        System.out.println("Received Response: " + response);

        socket.close();
        return response;
    }

}
