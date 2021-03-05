package client.helper;

import client.general.ClientConfig;
import client.logic.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * @author Team: "Steve and Issack" - Steve Khanna 10153930, Issack John 30031053
 * @version 2.0 (Iteration 2)
 * @since 01-29-2021
 */
public class UDPBroadcast implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(UDPBroadcast.class.getName());

    private final Client client;
    private final String type;
    private final String message;

    public UDPBroadcast(Client client, String type, String message) {
        this.client = client;
        this.type = type;
        this.message = message;
    }

    /**
     *
     */
    @Override
    public void run() {
        //enumerate all known peers
        LOGGER.log(Level.INFO, "Broadcasting " + message);
        LOGGER.log(Level.INFO, "active peers " + client.getActivePeers());
        byte[] msg = message.getBytes();
        client.getActivePeers().forEach(peer -> {
            try {
                DatagramPacket packet = new DatagramPacket(msg, msg.length, InetAddress.getByName(peer.getAddress().equals(client.getClientIP()) ?
                        ClientConfig.DEFAULT_CLIENT_IP : peer.getAddress()), peer.getPort());
                client.getUDPSocket().send(packet);
                if (type.equals("peer")) {
                    client.getReport().addSentPeerToReport(peer, message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
