package client.helper;

import client.Client;

public class Inactive implements Runnable{

    private final Client client;
    private final String target;

    public Inactive(Client client, String target){
        this.client = client;
        this.target = target;
    }
    @Override
    public void run() {
        client.expired(target);
    }
}
