package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * handle the get report request, getting the sources and their peers as a string
 */
public class Report {

    private final List<String> peersSent;
    private final Client client;


    public Report(Client client){
        this.peersSent = Collections.synchronizedList(new ArrayList<>());
        this.client = client;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(client.getPeerTable().size());
        sb.append(peersSent.size()); //There were x peer messages sent via UDP
        peersSent.forEach(sb::append); //append each peer
        client.getSnippetList().forEach(sb::append); //append each snippet
        return sb.toString();
    }
}
