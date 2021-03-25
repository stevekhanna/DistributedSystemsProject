package client.peer;

/**
 * Represents the snippets that are received in our system
 *
 * @author Team: "Steve and Issack" - Steve Khanna 10153930, Issack John 30031053
 * @version 2.0 (Iteration 2)
 * @since 01-29-2021
 */
public class Snippet {

    private final int timestamp;
    private final String content;
    private final String sourcePeer;


    public Snippet(int timestamp, PeerPacket packet) {
        this.timestamp = timestamp;
        this.content = packet.getMessage();
        this.sourcePeer = packet.getSource();
    }

    @Override
    public String toString() {
        return timestamp + " " + content + " " + sourcePeer + "\n";
    }
}
