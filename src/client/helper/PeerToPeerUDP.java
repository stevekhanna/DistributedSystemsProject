package client.helper;

import client.general.ClientConfig;
import client.logic.Client;
import client.peer.PeerPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class PeerToPeerUDP implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(PeerToPeerUDP.class.getName());

    private final Client client;
    private final PeerPacket packet;

    /**
     *
     * @param client
     * @param packet
     */
    public PeerToPeerUDP(Client client, PeerPacket packet) {
        this.client = client;
        this.packet = packet;
    }

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "Sending Ack " + packet.getMessage());
        byte[] msg = packet.getMessage().getBytes();
        try {
            DatagramPacket packet = new DatagramPacket(msg, msg.length, this.packet.getSocketAddress());
            client.getUDPSocket().send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
