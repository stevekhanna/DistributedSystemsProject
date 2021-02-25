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

    /**
     * Grabs all the stored peers
     *
     * @return string of peers with newline after each one
     */
    public String getPeersReport() {
        StringBuilder sb = new StringBuilder();
        client.getPeerTable().values().forEach(peerList -> {
            peerList.forEach(peer -> {
                sb.append(peer.toString());
            });
        });
        return sb.toString();
    }

    /**
     * Grabs all the peers for the source provides
     *
     * @param source the source IP:port where the peers came from
     * @return a string of peers with newline after each one
     */
    private String getPeersReport(Peer source) {
        StringBuilder sb = new StringBuilder();
        client.getPeerTable().get(source).forEach(peer -> {
            sb.append(peer.toString());
        });
        return sb.toString();
    }

    /**
     * Grab all the known sources as well as their peers
     * TODO: consider using stringify or something
     * <source location><newline><date><newline><numOfPeers><newline><peers>
     *
     * @return String, the sources as a string, the number of sources as well as the peers
     */
    public String getSourcesReport() {
        StringBuilder sb = new StringBuilder();
        client.getPeerTable().keySet().forEach(source -> {
            sb.append(source.toString())
                    .append(source.getTimeReceived())
                    .append("\n")
                    .append(client.getPeerTable().get(source).size())
                    .append("\n")
                    .append(getPeersReport(source));
        });
        return (sb.toString().equals("") ? "\n" : sb.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(client.countPeers()).append("\n"); // The reporting peer has a list of x peers
        sb.append(getPeersReport()); //iteratively add each peer

        sb.append(client.getPeerTable().size()).append("\n"); //number of sources
        sb.append(getSourcesReport()); //iteratively add each source

        sb.append(peersReceived.size()).append("\n"); //There were x peers that we received via UDP
        peersReceived.forEach(sb::append); //iteratively add each peer received

        sb.append(peersSent.size()).append("\n"); //There were x peer messages sent via UDP
        peersSent.forEach(sb::append); //append each peer
        sb.append(client.getSnippetList().size()).append("\n"); // This peer is aware of x snippets

        client.getSnippetList().forEach(sb::append); //append each snippet

        return sb.toString();
    }
}
