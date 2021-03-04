package client.logic;

import client.common.ClientConfig;
import client.display.GUI;

import javax.swing.JFrame;
import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Driver extends JFrame {

    private GUI gui;

    public Driver(String[] args) {
        populateConfigFile();
        initFrame(args);
    }

    private void initFrame(String[] args) {
        setSize(ClientConfig.WIDTH, ClientConfig.HEIGHT);
        setResizable(false);

        getContentPane().setBackground(Color.white);
        setTitle(ClientConfig.TITLE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.gui = new GUI(args);
        add(this.gui);
        pack();
    }


    private static void populateConfigFile() {

        try {
            Files.find(Paths.get(ClientConfig.SOURCE_DIR),
                    Integer.MAX_VALUE,
                    (filePath, fileAttr) -> fileAttr.isRegularFile())
                    .forEach(ClientConfig.CODE_FILES::add);
        } catch (IOException e) {
            System.out.println("Path not found. Could not populate config file with source code paths");
        }
    }

    public GUI getGui() {
        return gui;
    }

    /**
     * Starts the client server. If a port number is provided as a runtime argument,
     * it will be used to start the peer.
     *
     * @param args optional server ip as first argument and port number as second argument.
     */
    public static void main(String[] args) {

        Driver driver = new Driver(args);
        driver.setVisible(true);
    }
}
