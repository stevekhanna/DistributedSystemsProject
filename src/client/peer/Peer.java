package client.peer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Peer {

	private final String address;
	private final int port;
	private final String timeReceived = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.from(ZoneOffset.UTC)).format(Instant.now());

	/**
	 *
	 * @param peer
	 */
	public Peer(String peer){
		this.address = peer.split(":")[0];
		this.port = Integer.parseInt(peer.split(":")[1]);
	}

	public Peer(String address, int port){
		this.address = address;
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public String getTimeReceived(){
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
