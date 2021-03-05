package client.helper;

import client.logic.Client;
import client.peer.Peer;

public class Inactive implements Runnable{

    private final Client client;
    private final Peer target;

    public Inactive(Client client, Peer target){
        this.client = client;
        this.target = target;
    }

    @Override
    public void run() {
        client.expired(target);
    }
}
