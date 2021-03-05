package client.helper;


import client.general.ClientConfig;
import client.logic.Client;
import client.peer.*;

import javax.swing.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestProcessor implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(RequestProcessor.class.getName());

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
                case "snip" -> {
                    LOGGER.log(Level.INFO, "Snip request received by " + client.getUDPSocket().getLocalSocketAddress().toString());
                    handleSnipRequest();
                }
                case "peer" -> {
                    LOGGER.log(Level.INFO, "Peer request received");
                    handlePeerRequest();
                }
                case "stop" -> {
                    LOGGER.log(Level.INFO, "Stop request received, terminating program");
                    client.shutdown();
                }
                default -> LOGGER.log(Level.INFO, "Request not recognized: " + request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //If received snippet from someone not in our list, add them
    public void handleSnipRequest() {
        int timestamp = Math.max(client.getLamportClock().getTimestamp(), packet.getTimeReceived()) + 1;
        client.getLamportClock().setTimestamp(timestamp);
        client.getSnippetList().add(new Snippet(timestamp, packet));
        LOGGER.log(Level.INFO, "snip message is "+ packet.getMessage());
        SwingUtilities.invokeLater(() -> client.getGui().updateSnippetList(timestamp + packet.getMessage()));
    }

    public void handlePeerRequest() {

        String packetSource = packet.getSource();

        Report report = client.getReport();
        report.getPeersReceived().add(packet.toString());
        Peer source = new Peer(packetSource);

        Map<String, Future> futures = client.getFutures();
        if(futures.containsKey(packetSource)){
            futures.get(packetSource).cancel(true);
        }
        futures.put(packetSource, client.getPool().schedule(new Inactive(client, new Peer(packetSource)), ClientConfig.INACTIVITY_INTERVAL, TimeUnit.MILLISECONDS));

        Set<Peer> peerList = Collections.synchronizedSet(new HashSet<>());
        Peer peerReceived = new Peer(packet.getMessage());
        peerList.add(peerReceived);

        PeerTable peerTable = report.getPeerTable();
        if (!peerTable.containsKey(source)) {
            peerTable.put(source, peerList);
        } else {
            Set<Peer> temp = peerTable.get(source);
            temp.addAll(peerList);
        }
        Set<Peer> activePeers = client.getActivePeers();

        LOGGER.log(Level.INFO, "source ip "+ source.toString() + "peer ip " + peerReceived.toString());
        activePeers.add(source);
        activePeers.add(peerReceived);

    }
}
