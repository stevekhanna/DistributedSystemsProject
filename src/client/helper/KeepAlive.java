package client.helper;

import client.logic.Client;

/**
 * Runnable class used to keep client alive
 *
 * @author Team: "Steve and Issack" - Steve Khanna 10153930, Issack John 30031053
 * @version 2.0 (Iteration 2)
 * @since 01-29-2021
 */
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
