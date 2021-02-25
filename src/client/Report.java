package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * handle the get report request, getting the sources and their peers as a string
 */
public class Report {

    private final List<String> peersSent;
    private final List<String> peersReceived;
    private final Client client;


    public Report(Client client){
        this.peersSent = Collections.synchronizedList(new ArrayList<>());
        this.peersReceived = Collections.synchronizedList(new ArrayList<>());
        this.client = client;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(client.countPeers()).append("\n"); // The reporting peer has a list of x peers
        sb.append(client.getPeers()); //iteratively add each peer

        sb.append(client.getPeerTable().size()).append("\n"); //number of sources
        sb.append(client.getSources()); //iteratively add each source

        sb.append(peersReceived.size()).append("\n"); //There were x peers that we received via UDP
        peersReceived.forEach(sb::append); //iteratively add each peer received

        sb.append(peersSent.size()).append("\n"); //There were x peer messages sent via UDP
        peersSent.forEach(sb::append); //append each peer
        sb.append(client.getSnippetList().size()).append("\n"); // This peer is aware of x snippets

        client.getSnippetList().forEach(sb::append); //append each snippet

        return sb.toString();
    }
}
