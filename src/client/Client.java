package client;

import client.common.ClientConfig;
import client.display.GUI;
import client.helper.Inactive;
import client.helper.KeepAlive;
import client.helper.RequestProcessor;
import client.helper.UDPBroadcast;
import client.util.GeneralUtil;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * A peer process that can receive peers from the registry as well as send a report on it's known sources and peers
 *
 * @author Team: "Steve and Issack" - Steve Khanna 10153930, Issack John 30031053
 * @version 2.0 (Iteration 2)
 * @since 01-29-2021
 */
public class Client {
    /**
     * Server ip of registry
     */
    private final String serverIP;

    /**
     * Port number of registry
     */
    private final int port;

    /**
     * The client team name
     */
    private final String teamName;

    /**
     * Contains the sources and peers we know about, no duplicate sources
     */
    private final PeerTable peerTable;

    private final List<Snippet> snippetList;

    /**
     * Socket used for UDP communication with peers
     */
    private DatagramSocket udpSocket;

    /**
     * Controls whether udp socket continues to receive req
     */
    private boolean shutdown;

    /**
     * GUI for the client
     */
    private GUI gui;

    private final Map<String, Future> futures; //future tasks

    private final ScheduledExecutorService pool;

    private final LamportClock lamportClock;

    private final Report report;

    /**
     * Default class constructor
     * Initializes port, peerTable
     */
    public Client() {
        this.serverIP = ClientConfig.DEFAULT_SERVER_IP;
        this.port = ClientConfig.DEFAULT_PORT_NUMBER;
        this.teamName = ClientConfig.DEFAULT_TEAM_NAME;
        this.peerTable = new PeerTable();
        this.snippetList = Collections.synchronizedList(new ArrayList<>());
        this.shutdown = false;
        this.futures = new ConcurrentHashMap<>();
        this.pool = Executors.newScheduledThreadPool(ClientConfig.THREAD_POOL_SIZE);
        this.lamportClock = new LamportClock();
        this.report = new Report(this);
    }

    /**
     * Overloaded class constructor
     *
     * @param serverIP the Ip for the registry
     * @param port     the port for the registry
     * @param teamName the teamName of this client
     */
    public Client(String serverIP, int port, String teamName, GUI gui) {
        this.serverIP = serverIP;
        this.port = port;
        this.teamName = teamName + "\n";
        this.peerTable = new PeerTable();
        this.snippetList = Collections.synchronizedList(new ArrayList<>());
        this.shutdown = false;
        this.gui = gui;
        this.futures = new ConcurrentHashMap<>();
        this.pool = Executors.newScheduledThreadPool(ClientConfig.THREAD_POOL_SIZE);
        this.lamportClock = new LamportClock();
        this.report = new Report(this);
    }

    /**
     * receive a list of peers from the registry and store them
     *
     * @param reader socket to read the peers from
     * @param source for storing the peers with their source
     * @throws IOException if there is a problem communicating with the registry
     *                     TODO change to UTC Time
     */
    private void receivePeers(BufferedReader reader, Peer source) throws IOException {
        int numberOfPeers = Integer.parseInt(reader.readLine());
        Set<Peer> peerList = Collections.synchronizedSet(new HashSet<>());

        while (numberOfPeers > 0) {
            Peer peer = new Peer(reader.readLine());
            peerList.add(peer);
            futures.put(peer.toString().replace("\n", ""), pool.schedule(new Inactive(this, peer), ClientConfig.INACTIVITY_INTERVAL, TimeUnit.MILLISECONDS));
            numberOfPeers--;
        }

        String key = source.toString().replace("\n", "");
        if (!peerTable.containsKey(source)) {
            peerTable.put(source, peerList);
            if(futures.containsKey(key)){
                futures.get(key).cancel(true);
            }
        } else {
            Set<Peer> temp = peerTable.get(source);
            temp.addAll(peerList);
        }
        futures.put(key, pool.schedule(new Inactive(this, source), ClientConfig.INACTIVITY_INTERVAL, TimeUnit.MILLISECONDS));
    }

    public int countPeers() {
        int totalPeers = 0;
        for (Set<Peer> setOfPeers : peerTable.values()) {
            totalPeers += setOfPeers.size();
        }
        return totalPeers;
    }


    /**
     * handling the communication between the peer process and the registry server
     * based on the communication protocol
     *
     * @param sock the socket where the client is connected to the registry for all necessary communication
     */
    private void handleRequest(Socket sock) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

            String request;
            boolean done = false;
            while ((request = reader.readLine()) != null && !done) {
                System.out.println(request);
                StringBuilder response = new StringBuilder();

                switch (request) {
                    case "get team name" -> {
                        response.append(teamName);
                        System.out.printf("Writing response: {\n%s}\n", response.toString());
                        writer.write(response.toString());
                        writer.flush();
                    }
                    case "get code" -> {
                        response.append(GeneralUtil.getCode());
                        writer.write(response.toString());
                        writer.flush();
                        System.out.println("Code Written Successfully.");
                    }
                    case "receive peers" -> {
                        Peer peer = new Peer(sock.getInetAddress().getHostAddress(), sock.getPort());
                        receivePeers(reader, peer);
                        System.out.printf("Peers received: {\n%s\n}\n", peerTable.toString());
                    }
                    case "get report" -> {
                        response.append(report.toString());
                        System.out.printf("Writing response:\n{%s}\n", response.toString());
                        writer.write(response.toString());
                        writer.flush();
                    }
                    case "close" -> {
                        System.out.println("Close Received");
                        done = true;
                    }
                    case "get location" -> {
                        System.out.println("get location Received");
                        String ip = GeneralUtil.getMyIP();
                        if (ip.equals("Error")) {
                            writer.write("Garbage\n");
                        } else {
                            response.append((serverIP.equals("localhost") ? "127.0.0.1" : ip))
                                    .append(":")
                                    .append(udpSocket.getLocalPort())
                                    .append("\n");
                            writer.write(response.toString());
                        }
                        writer.flush();
                    }
                    default -> System.out.printf("Request not recognized: %s\n", request);
                }
            }
            reader.close();
            writer.close();
            System.out.println("Reader and Writer successfully closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the peer and accepts requests from registry.
     *
     * @throws IOException if there are problems starting this peer or
     *                     if there are problems communicating with the registry.
     */
    public void start() throws IOException {

        //setup UDP broadcast socket
        try {
            udpSocket = new DatagramSocket(ClientConfig.UDP_DEFAULT_PORT);
            udpSocket.setBroadcast(true);
        } catch (Exception e) {
            System.out.println("Problem initializing broadcast socket");
            e.printStackTrace();
        }

        connectToRegistry();

        //start keepalive timer
        futures.put(teamName, pool.schedule(new KeepAlive(this), ClientConfig.KEEP_ALIVE_INTERVAL, TimeUnit.MILLISECONDS));

        while (!shutdown) {
            byte[] msg = new byte[64];
            DatagramPacket pkt = new DatagramPacket(msg, msg.length);
            try {
                udpSocket.receive(pkt);
            } catch (Exception e) {
                break;
            }
            PeerPacket packet = new PeerPacket(pkt);
            if (packet.getType().equals("stop")) {
                System.out.println("Stop request received, terminating program");
                shutdown();
            } else {
                pool.execute(new RequestProcessor(this, packet));

                //restart KeepAlive timer
                futures.get(teamName).cancel(true);
                futures.put(teamName, pool.schedule(new KeepAlive(this), ClientConfig.KEEP_ALIVE_INTERVAL, TimeUnit.MILLISECONDS));
            }
        }
        System.out.println("Closing UDP socket");
        udpSocket.close();

        //cleanup futures
        System.out.println("Cleaning up futures");
        for (Map.Entry<String, Future> elem : futures.entrySet()) {
            elem.getValue().cancel(true);
        }

        System.out.println("Shutting down pool");
        pool.shutdown();
        //https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html#shutdown()
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        System.out.println("Executor Shutdown successful");

        connectToRegistry();
        System.out.println("Sent updated report to registry, now terminating");
        System.exit(0);
    }

    public void connectToRegistry() {
        try {
            Socket socket = new Socket(serverIP, port);
            handleRequest(socket);
            socket.close();
            System.out.println("Socket successfully closed.");
        } catch (BindException be) {
            System.out.println("Trouble binding to port");
            be.printStackTrace();
        } catch (IOException ce) {
            System.out.println("Trouble connecting, connection refused");
            ce.printStackTrace();
        }
    }

    public void sendSnippet(String snippet) {
        new Thread(new UDPBroadcast(this, "snip", "snip" + lamportClock.getTimestamp() + " " + snippet)).start();
    }

    public void shutdown() {
        this.shutdown = true;
    }

    public void keepAlive() {
        System.out.println("Running keepalive");
        String randomPeer = getRandomPeer();
        //snip newline and send peer
        new Thread(new UDPBroadcast(this, "peer", "peer" + randomPeer.substring(0, randomPeer.length() - 1))).start();
        //restart keepalive
        futures.get(teamName).cancel(true);
        futures.put(teamName, pool.schedule(new KeepAlive(this), ClientConfig.KEEP_ALIVE_INTERVAL, TimeUnit.MILLISECONDS));
    }

    //TODO actually send a random peer, maybe add complexity to this.
    public String getRandomPeer() {

        //get first source's set of peers
        Set<Peer> peers = peerTable.entrySet().iterator().next().getValue();
        return peers.iterator().next().toString();
    }

    //TODO one liner this
    public void expired(Peer target) {
        //remove target from known active peers
        for (Map.Entry<Peer, Set<Peer>> entry : peerTable.entrySet()) {
            Peer k = entry.getKey();
            if(k.equals(target)){
                k.setAlive(false);
            }
            Set<Peer> v = entry.getValue();
            if(v.contains(target)){
                for (Peer peer : v) {
                    if (peer.equals(target)) {
                        peer.setAlive(false);
                        break;
                    }
                }
            }
        }
    }

    public Collection<Set<Peer>> getAllPeers() {
        return peerTable.values();
    }

    public List<Snippet> getSnippetList() {
        return snippetList;
    }

    // TODO: Research Synchronized method to see if necessary
    public DatagramSocket getUDPSocket() {
        return udpSocket;
    }

    public GUI getGui() {
        return gui;
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getPort() {
        return port;
    }

    public PeerTable getPeerTable() {
        return peerTable;
    }

    public Map<String, Future> getFutures() {
        return futures;
    }

    public ScheduledExecutorService getPool() {
        return pool;
    }

    public LamportClock getLamportClock() {
        return lamportClock;
    }

    public String getTeamName() {
        return teamName;
    }

    public Report getReport() {
        return report;
    }
}
