package client.peer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a peer in our system
 *
 * @author Team: "Steve and Issack" - Steve Khanna 10153930, Issack John 30031053
 * @version 2.0 (Iteration 2)
 * @since 01-29-2021
 */
public class Peer {

    private final String address;
    private final int port;
    private final String timeReceived =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.from(ZoneOffset.UTC)).format(Instant.now());

    /**
     * constructor for when a string reprsentation of the peer is received
     *
     * @param peer string representation of peer
     */
    public Peer(String peer) {
        this.address = peer.split(":")[0];
        this.port = Integer.parseInt(peer.split(":")[1]);
    }

    public Peer(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getTimeReceived() {
        return this.timeReceived;
    }

    public void setAlive(boolean alive) {
    }

    @Override
    public String toString() {
        return address + ":" + port + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Peer peer = (Peer) o;
        return getPort() == peer.getPort() && Objects.equals(getAddress(), peer.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddress(), getPort());
    }
}
