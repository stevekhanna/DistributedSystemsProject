package client;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PeerTable extends ConcurrentHashMap<Peer, Set<Peer>> {

    public PeerTable(){super();}

    public PeerTable(Map<Peer, Set<Peer>> peerTable){super(peerTable);}

    public PeerTable(PeerTable peerTable){super(peerTable);}

}