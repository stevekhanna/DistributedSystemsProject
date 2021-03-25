package client.peer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Peer Table is a concurrent hashmap that keeps track of all peers regardless of live-ness
 *
 * @author Team: "Steve and Issack" - Steve Khanna 10153930, Issack John 30031053
 * @version 2.0 (Iteration 2)
 * @since 01-29-2021
 */
public class PeerTable extends ConcurrentHashMap<Peer, Set<Peer>> {

    /**
     * Default constructor Source: Peers
     */
    public PeerTable() {
        super();
    }

    public PeerTable(Map<Peer, Set<Peer>> peerTable) {
        super(peerTable);
    }

    public PeerTable(PeerTable peerTable) {
        super(peerTable);
    }

}