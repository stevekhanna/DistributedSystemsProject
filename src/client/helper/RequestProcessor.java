package client.helper;


import client.*;
import client.common.ClientConfig;

import javax.swing.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RequestProcessor implements Runnable {

    private final PeerPacket packet;
    private final Client client;

    public RequestProcessor(Client client, PeerPacket pkt) {
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
        System.out.printf("snip message is %s\n", packet.getMessage());
        SwingUtilities.invokeLater(() -> client.getGui().updateSnippetList(packet.getMessage()));
    }

    public void handlePeerRequest() {
        PeerTable peerTable = client.getPeerTable();

        Peer source = new Peer(packet.getSource());

        client.getFutures().get(packet.getSource()).cancel(true);
        client.getFutures().put(packet.getSource(), client.getPool().schedule(new Inactive(client, packet.getSource()), ClientConfig.INACTIVITY_INTERVAL, TimeUnit.MILLISECONDS));

        Set<Peer> peerList = Collections.synchronizedSet(new HashSet<>());
        peerList.add(new Peer(packet.getMessage()));

        if (!peerTable.containsKey(source)) {
            peerTable.put(source, peerList);
        } else {
            Set<Peer> temp = peerTable.get(source);
            temp.addAll(peerList);
        }
    }
}
