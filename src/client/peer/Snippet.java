package client.peer;

import client.peer.PeerPacket;

public class Snippet {

    private final int timestamp;
    private final String content;
    private final String sourcePeer;


    public Snippet(int timestamp, PeerPacket packet){
        this.timestamp = timestamp;
        this.content = packet.getMessage();
        this.sourcePeer = packet.getSource();
    }

    @Override
    public String toString() {
        return timestamp + " " + content + " " + sourcePeer + "\n";
    }
}
