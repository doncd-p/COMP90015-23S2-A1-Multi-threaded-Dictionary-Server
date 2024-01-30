/**
 * Author: Chenyang Dong
 * Student ID: 1074314
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * The type Dictionary.
 */
public class Dictionary {
    private String filename;
    private JSONObject dictionary;

    /**
     * Instantiates a new Dictionary.
     *
     * @param filename the filename
     */
    public Dictionary(String filename) {
        try {
            this.filename = filename;
            FileReader reader = new FileReader(filename);
            JSONParser parser = new JSONParser();
            dictionary = (JSONObject) parser.parse(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException: " + e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("ParseException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Add the word.
     *
     * @param word     the word
     * @param meanings the meanings
     * @throws IOException the io exception
     */
    public synchronized void add(String word, JSONArray meanings) throws IOException {
        dictionary.put(word, meanings);
        FileWriter writer = new FileWriter(filename, false);
        writer.write(dictionary.toJSONString());
        writer.flush();
        writer.close();
    }

    /**
     * Search the word.
     *
     * @param word the word
     * @return The JSON array of meanings of with the word.
     * @throws NoSuchElementException the no such element exception
     */
    public synchronized JSONArray search(String word) throws NoSuchElementException {
        return (JSONArray) dictionary.get(word);
    }

    /**
     * Update the word.
     *
     * @param word     the word
     * @param meanings the meanings
     * @throws IOException the io exception
     */
    public synchronized void update(String word, JSONArray meanings) throws IOException {
        dictionary.put(word, meanings);
        FileWriter writer = new FileWriter(filename, false);
        writer.write(dictionary.toJSONString());
        writer.flush();
        writer.close();
    }

    /**
     * Remove the word.
     *
     * @param word the word
     * @throws IOException the io exception
     */
    public synchronized void remove(String word) throws IOException {
        dictionary.remove(word);
        FileWriter writer = new FileWriter(filename, false);
        writer.write(dictionary.toJSONString());
        writer.flush();
        writer.close();
    }
}
