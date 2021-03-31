package client.peer;

import client.logic.Client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Used to store all data for the report, getting the sources and their peers as a string
 *
 * @author Team: "Steve and Issack" - Steve Khanna 10153930, Issack John 30031053
 * @version 2.0 (Iteration 2)
 * @since 01-29-2021
 */
public class Report {

    private final PeerTable peerTable;
    private final List<String> peersSent;
    private final List<String> peersReceived;
    private final Client client;


    public Report(Client client) {
        this.peersSent = Collections.synchronizedList(new ArrayList<>());
        this.peersReceived = Collections.synchronizedList(new ArrayList<>());
        this.client = client;
        this.peerTable = new PeerTable();
    }

    public PeerTable getPeerTable() {
        return peerTable;
    }

    /**
     * adding a peer that was sent to our report to be sent later
     *
     * @param peer    the peer that was
     * @param message
     */
    public void addSentPeerToReport(Peer peer, String message) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(InetAddress.getByName(peer.getAddress()).toString().substring(1))
                    .append(":")
                    .append(peer.getPort()).append(" ")
                    .append(message.substring(4)).append(" ")
                    .append(DateTimeFormatter
                            .ofPattern("yyyy-MM-dd HH:mm:ss")
                            .withZone(ZoneId.from(ZoneOffset.UTC))
                            .format(Instant.now()))
                    .append("\n");
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host");
        }
        this.peersSent.add(sb.toString());
    }

    /**
     * Grabs all the stored peers
     *
     * @return string of peers with newline after each one
     */
    public String getPeersReport() {
        StringBuilder sb = new StringBuilder();
        peerTable.values().forEach(peerList -> {
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
        peerTable.get(source).forEach(peer -> {
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
        peerTable.keySet().forEach(source -> {
            sb.append(source.toString())
                    .append(source.getTimeReceived())
                    .append("\n")
                    .append(peerTable.get(source).size())
                    .append("\n")
                    .append(getPeersReport(source));
        });
        return sb.toString();
    }

    /**
     * count number of peers in our peertable
     *
     * @return the amount of peers in our peertable
     */
    public int countPeers() {
        int totalPeers = 0;
        for (Set<Peer> setOfPeers : peerTable.values()) {
            totalPeers += setOfPeers.size();
        }
        return totalPeers;
    }

    /**
     * generate the report to be sent to registry
     *
     * @return the report as a string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(countPeers()).append("\n"); // The reporting peer has a list of x peers
        sb.append(getPeersReport()); //iteratively add each peer

        sb.append(peerTable.size()).append("\n"); //number of sources
        sb.append(getSourcesReport()); //iteratively add each source

        sb.append(peersReceived.size()).append("\n"); //There were x peers that we received via UDP
        peersReceived.forEach(sb::append); //iteratively add each peer received

        sb.append(peersSent.size()).append("\n"); //There were x peer messages sent via UDP
        peersSent.forEach(sb::append); //append each peer
        sb.append(client.getSnippetList().size()).append("\n"); // This peer is aware of x snippets

        client.getSnippetList().forEach(sb::append); //append each snippet

        return sb.toString();
    }

    public List<String> getPeersSent() {
        return peersSent;
    }

    public List<String> getPeersReceived() {
        return peersReceived;
    }
}
