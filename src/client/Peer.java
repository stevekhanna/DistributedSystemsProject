package client;
public class Peer {

	private String address;
	private int port;

	/**
	 * <peer> ::= <ip><colon><port><newline>
	 *  <ip> ::= <num><dot><num><dot><num><dot><num>
	 *  <port> ::= <num><num> ::= [0-9]+
	 *  <dot> ::= ‘.’
	 *  <colon> ::= ‘:’
	 * @param peer
	 */
	public Peer(String peer){

		//breakdown string
		this.address = peer.split(":")[0];
		this.port = Integer.parseInt(peer.split(":")[1]);
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public String toString() {
		return address + ":" + port;
	}
}
