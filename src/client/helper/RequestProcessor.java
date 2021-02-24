package client.helper;


import client.Client;
import client.DvPacket;
import client.Snippet;

import javax.swing.*;

public class RequestProcessor implements Runnable {

    private final DvPacket packet;
    private final Client client;

    public RequestProcessor(Client client, DvPacket pkt) {
        this.client = client;
        this.packet = pkt;
    }

    @Override
    public void run() {
        processPacket();
    }

    public void processPacket() {
        try {
            String request = packet.getType();
            switch (request) {
                case "snip":
                    System.out.println("Snip request received");
                    handleSnipRequest();
                    break;
                case "peer":
                    System.out.println("Peer request received");
                    handlePeerRequest();
                    break;
                default:
                    System.out.printf("Request not recognized: %s\n", request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSnipRequest() {
        client.getSnippetList().add(new Snippet(0, packet));
        SwingUtilities.invokeLater(() -> client.getGui().updateSnippetList(packet.getMessage()));
    }

    //TODO
    public void handlePeerRequest() {

    }
}
