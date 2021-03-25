package client.display;

import client.general.ClientConfig;
import client.logic.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Displays graphic user interface in the window
 *
 * @author Team: "Steve and Issack" - Steve Khanna 10153930, Issack John 30031053
 * @version 2.0 (Iteration 2)
 * @since 01-29-2021
 */
public class GUI extends JPanel implements ActionListener {
    private JButton sendButton;
    private JTextField inputField;

    private Client client;
    private DefaultListModel<String> snippetList;

    /**
     * Constructor for gui to create gridlayout and init gui
     *
     * @param args arguemnts for the client
     */
    public GUI(String[] args) {
        super(new GridLayout(ClientConfig.GRID_LAYOUT_ROWS,
                ClientConfig.GRID_LAYOUT_COLS,
                ClientConfig.GRID_LAYOUT_HGAP,
                ClientConfig.GRID_LAYOUT_VGAP));
        initGUI(args);
    }

    /**
     * Initialize client for gui
     *
     * @param args arguments for the client
     */
    public void initGUI(String[] args) {
        setFocusable(true);
        try {
            if (args.length != 3) {
                System.out.println("Using Default Constructor with: 127.0.0.1:1245");
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
            System.out.println("Problem creating client");
        }
        createLabels();
    }

    /**
     * creating the labels for the panel
     */
    private void createLabels() {
        //Title at the top
        JLabel label = new JLabel("<html>News Feed<br/><html>");
        add(label);

        JLabel label2 = new JLabel(client.getTeamName());
        add(label2);

        //list of snippets
        snippetList = new DefaultListModel<>();
        JList<String> list = new JList<>(snippetList);
        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane);

        scrollPane.getVerticalScrollBar().addAdjustmentListener(
                e -> e.getAdjustable().setValue(e.getAdjustable().getMaximum())
        );

        //Input field
        inputField = new JTextField(20);
        add(inputField);

        //button
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        add(sendButton);
    }

    /**
     * listening for when a button is clicked
     *
     * @param e action event that was triggered
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            System.out.printf("%s\n", inputField.getText());
            client.sendSnippet(inputField.getText());
            inputField.setText("");
        }
    }

    /**
     * add snippet to our list model
     *
     * @param snippet the snippet to be added
     */
    public void updateSnippetList(String snippet) {
        snippetList.addElement(snippet);
    }

    public Client getClient() {
        return client;
    }
}
