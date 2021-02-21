package Client.logic;

import Client.Client;
import Client.common.ClientConfig;
import Client.display.GUI;

import javax.swing.JFrame;
import java.awt.Color;
import java.io.IOException;

public class Driver extends JFrame {

    public Driver() {
        initFrame();
    }

    private void initFrame(){
        setSize(ClientConfig.WIDTH, ClientConfig.HEIGHT);
        setResizable(false);

        getContentPane().setBackground(Color.white);
        setTitle(ClientConfig.TITLE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new GUI());
//        pack();
    }

    /**
     * Starts the client server. If a port number is provided as a runtime argument,
     * it will be used to start the peer.
     * @param args optional server ip as first argument and port number as second argument.
     */
    public static void main(String[] args){
        Driver driver = new Driver();
        driver.setVisible(true);

        try {
            Client client;
            if (args.length != 2) {
                System.out.println("No Server IP and port provided. Using Default Constructor with: localhost:12345");
                client = new Client();
            }
            else{
                client = new Client(args[0], Integer.parseInt(args[1]));
            }
//            client.start();
            Thread t = new Thread(){
                public void run() {
                    try{
                        client.start();
                    }catch (IOException e){
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            };
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
