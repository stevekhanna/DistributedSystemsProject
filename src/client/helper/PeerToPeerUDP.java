package client.helper;

import client.logic.Client;
import client.peer.PeerPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class PeerToPeerUDP implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(PeerToPeerUDP.class.getName());

    private final Client client;
    private final PeerPacket packet;
    private final String teamName;

    /**
     *
     * @param client
     * @param packet
     */
    public PeerToPeerUDP(Client client, PeerPacket packet, String teamName) {
        this.client = client;
        this.packet = packet;
        this.teamName = teamName;
    }

    @Override
    public void run() {
        String message = "ack" + teamName;
        LOGGER.log(Level.INFO, "Sending Ack " + message);
        byte[] msg = message.getBytes();
        try {
            DatagramPacket packet = new DatagramPacket(msg, msg.length, this.packet.getSocketAddress());
            client.getUDPSocket().send(packet);
            client.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
