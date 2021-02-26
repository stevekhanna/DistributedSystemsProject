package client.helper;

import client.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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
        byte[] msg = message.getBytes();
        client.getActivePeers().forEach(peer -> {
            try {
                DatagramPacket packet = new DatagramPacket(msg, msg.length, InetAddress.getByName(peer.getAddress()), peer.getPort());
                client.getUDPSocket().send(packet);
                if (type.equals("peer")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(InetAddress.getByName(peer.getAddress()).toString().substring(1))
                            .append(":")
                            .append(peer.getPort()).append(" ")
                            .append(message.substring(4)).append(" ")
                            .append(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.from(ZoneOffset.UTC)).format(Instant.now()))
                            .append("\n");
                    client.getReport().getPeersSent().add(sb.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
