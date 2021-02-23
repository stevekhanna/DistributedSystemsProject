package client;

public class Snippet {

    private final int timestamp;
    private final String content;
    private final String sourcePeer;


    public Snippet(int timestamp, DvPacket packet){
        this.timestamp = timestamp;
        this.content = packet.getMessage();
        this.sourcePeer = packet.getSource();
    }

    @Override
    public String toString() {
        return timestamp + " " + content + " " + sourcePeer + "\n";
    }
}
