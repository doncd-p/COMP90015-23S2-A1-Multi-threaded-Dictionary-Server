/**
 * Author: Chenyang Dong
 * Student ID: 1074314
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * The type Gui.
 */
public class GUI extends JFrame {

    private JPanel panelMain;
    private JButton searchButton;
    private JButton updateButton;
    private JButton addButton;
    private JTextField textSearchField;
    private JButton searchEnterButton;
    private JButton removeButton;
    private JButton addEnterButton;
    private JPanel functionPanel;
    private CardLayout cardLayout;
    private JPanel searchPanel;
    private JPanel addPanel;
    private JPanel updatePanel;
    private JPanel cardPanel;
    private JButton updateEnterButton;
    private JTextField textAddField;
    private JTextField textUpdateField;
    private JTextArea textAddArea;
    private JTextArea textUpdateArea;

    /**
     * Instantiates a new Gui.
     *
     * @param dictionaryClient the dictionary client
     */
    public GUI(DictionaryClient dictionaryClient) {
        setContentPane(panelMain);
        setTitle("Dictionary");
        setSize(500, 500);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        // Add panels to the cardPanel with unique names
        cardPanel.add(searchPanel, "search");
        cardPanel.add(addPanel, "add");
        cardPanel.add(updatePanel, "update");

        // Set the initial visible panel
        cardLayout.show(cardPanel, "search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "search");
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "add");
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "update");
            }
        });
        searchEnterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = textSearchField.getText();
                dictionaryClient.search(word);
            }
        });

        addEnterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = textAddField.getText();
                String[] meanings = textAddArea.getText().split(";");
                meanings = Arrays.stream(meanings)
                        .map(String::trim) // Remove leading and trailing spaces
                        .filter(s -> !s.isEmpty()) // Remove empty strings
                        .toArray(String[]::new);
                dictionaryClient.add(word, meanings);
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = textSearchField.getText();
                dictionaryClient.remove(word);
            }
        });

        updateEnterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = textUpdateField.getText();
                String[] meanings = textUpdateArea.getText().split(";");
                meanings = Arrays.stream(meanings)
                        .map(String::trim) // Remove leading and trailing spaces
                        .filter(s -> !s.isEmpty()) // Remove empty strings
                        .toArray(String[]::new);
                dictionaryClient.update(word, meanings);
            }
        });
    }

}
