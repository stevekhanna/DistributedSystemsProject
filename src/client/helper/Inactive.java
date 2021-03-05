package client.helper;

import client.logic.Client;
import client.peer.Peer;

/**
 * Runnable class for inactive thread timeout
 *
 * @author Team: "Steve and Issack" - Steve Khanna 10153930, Issack John 30031053
 * @version 2.0 (Iteration 2)
 * @since 01-29-2021
 */
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
