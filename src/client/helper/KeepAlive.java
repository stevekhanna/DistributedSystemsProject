package client.helper;

import client.Client;

public class KeepAlive implements Runnable{

    Client client;

    public KeepAlive(Client client){
        this.client = client;
    }
    @Override
    public void run() {
        client.keepAlive();
    }
}
