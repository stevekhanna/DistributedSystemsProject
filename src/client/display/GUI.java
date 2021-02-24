package client.display;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUI extends JPanel implements ActionListener {
    private JButton sendButton;
    private JTextField inputField;
    private Client client;
    private DefaultListModel<String> snippetList;

    public GUI(String[] args) {
        super(new GridLayout(4, 1, 5, 5));
        initGUI(args);
    }

    public void initGUI(String[] args) {
        setFocusable(true);
        createLabels();
        try {
            if (args.length != 3) {
                System.out.println("No Server IP, port and team name provided. Using Default Constructor with: localhost:12345");
                client = new Client();
            } else {
                client = new Client(args[0], Integer.parseInt(args[1]), args[2], this);
            }
            Client finalClient = client;
            Thread t = new Thread(() -> {
                try {
                    finalClient.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            });
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createLabels() {
        //Title at the top
        JLabel label = new JLabel("<html>News Feed<br/><html>");
        add(label);

        //list of snippets
        snippetList = new DefaultListModel<>();
        add(new JList<>(snippetList));
        snippetList.addElement("hey");

        //Input field
        inputField = new JTextField(20);
        add(inputField);

        //button
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        add(sendButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            System.out.printf("%s\n", inputField.getText());
            client.sendSnippet(inputField.getText());
        }
    }

    public void updateSnippetList(String snippet) {
        snippetList.addElement(snippet);
    }
}
