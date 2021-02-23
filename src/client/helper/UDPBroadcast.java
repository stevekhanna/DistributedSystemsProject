package client.helper;

import client.Client;
import client.DvPacket;

public class UDPBroadcast implements Runnable{

    private Client client;
    private DvPacket packet;

    public UDPBroadcast(Client client, DvPacket packet){
        this.client = client;
        this.packet = packet;
    }


    @Override
    public void run() {
        //enumerate all known peers
    }
}
