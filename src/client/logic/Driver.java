package client.logic;

import client.Client;
import client.common.ClientConfig;
import client.display.GUI;

import javax.swing.JFrame;
import java.awt.Color;
import java.io.IOException;

public class Driver extends JFrame {

    public Driver() {
    }
    
    public Driver(Client client){
        initFrame(client);
    }

    private void initFrame(Client client){
        setSize(ClientConfig.WIDTH, ClientConfig.HEIGHT);
        setResizable(false);

        getContentPane().setBackground(Color.white);
        setTitle(ClientConfig.TITLE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new GUI(client));
//        pack();
    }

    /**
     * Starts the client server. If a port number is provided as a runtime argument,
     * it will be used to start the peer.
     * @param args optional server ip as first argument and port number as second argument.
     */
    public static void main(String[] args){

        Client client = null;
        try {
            if (args.length != 3) {
                System.out.println("No Server IP, port and team name provided. Using Default Constructor with: localhost:12345");
                client = new Client();
            }
            else{
                client = new Client(args[0], Integer.parseInt(args[1]), args[2]);
            }
            Client finalClient = client;
            Thread t = new Thread(){
                public void run() {
                    try{
                        finalClient.start();
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
        
        Driver driver = new Driver(client);
        driver.setVisible(true);

        
    }
}
