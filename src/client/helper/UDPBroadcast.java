package client.helper;

import client.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class UDPBroadcast implements Runnable {

    private final Client client;
    private final String type;
    private final String message;

    public UDPBroadcast(Client client, String type, String message) {
        this.client = client;
        this.type = type;
        this.message = message;
    }

    //TODO send to only peers that are still alive
    @Override
    public void run() {
        //enumerate all known peers
        System.out.println("broadcasting " + message);
        System.out.println(client.getActivePeers().toString());
        byte[] msg = message.getBytes();
        client.getActivePeers().forEach(peer -> {
            try {
                DatagramPacket packet = new DatagramPacket(msg, msg.length, InetAddress.getByName(peer.getAddress()), peer.getPort());
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
