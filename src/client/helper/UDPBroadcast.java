package client.helper;

import client.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class UDPBroadcast implements Runnable{

    private Client client;
    private String snippet;

    public UDPBroadcast(Client client, String snippet){
        this.client = client;
        this.snippet = "snip " + snippet;
    }

    @Override
    public void run() {
        //enumerate all known peers
        byte[] msg = snippet.getBytes();
        client.getAllPeers().forEach(peerList -> {
            peerList.forEach(peer -> {
                try{
                    DatagramPacket packet = new DatagramPacket(msg, msg.length, InetAddress.getByName(peer.getAddress()), peer.getPort());
                    client.getUDPSocket().send(packet);
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
        });
    }
}
