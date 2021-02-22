package client;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PeerTable extends ConcurrentHashMap<String, Set<Peer>> {

    public PeerTable(){super();}

    public PeerTable(Map<String, Set<Peer>> peerTable){super(peerTable);}

    public PeerTable(PeerTable peerTable){super(peerTable);}
}
