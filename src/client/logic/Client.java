package client.logic;

import client.display.GUI;
import client.general.ClientConfig;
import client.helper.*;
import client.peer.Peer;
import client.peer.PeerPacket;
import client.peer.Report;
import client.peer.Snippet;
import client.synchronization.LamportClock;
import client.util.GeneralUtil;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * NOTES FOR CLEANING UP CODE
 * <p>
 * 1) USE X.length() == 0 INSTEAD OF X.equals("")
 * 2) Use vectors instead of arraylist for things modified by multiple threads
 * because they are already synchronised, Vector<T> v = new Vector<T>();
 * 3) Logging instead of println
 */

/**
 * A peer process that can receive peers from the registry as well as send a report on it's known sources and peers
 * <p>
 * TODO: CREATE ENUM CLASS FOR THE TYPE OF REQUESTS RECEIVED, PACKAGES
 * TODO: checking for bad peers
 *
 * @author Team: "Steve and Issack" - Steve Khanna 10153930, Issack John 30031053
 * @version 2.0 (Iteration 2)
 * @since 01-29-2021
 */
public class Client {

    private final static Logger LOGGER = Logger.getLogger(Client.class.getName());

    /**
     * Server ip of registry
     */
    private final String serverIP;

    private final String clientIP;

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
    private final Set<Peer> activePeers;

    private final List<Snippet> snippetList;
    /**
     *
     */
    private final Map<String, Future> futures;
    /**
     *
     */
    private final ScheduledExecutorService pool;
    /**
     *
     */
    private final LamportClock lamportClock;
    /**
     *
     */
    private final Report report;
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


    /**
     * Default class constructor
     * Initializes port, peerTable
     */
    public Client() {
        this.serverIP = ClientConfig.DEFAULT_SERVER_IP;
        this.port = ClientConfig.DEFAULT_PORT_NUMBER;
        this.teamName = ClientConfig.DEFAULT_TEAM_NAME;
        this.activePeers = Collections.synchronizedSet(new HashSet<>());
        this.snippetList = Collections.synchronizedList(new ArrayList<>());
        this.shutdown = false;
        this.futures = new ConcurrentHashMap<>();
        this.pool = Executors.newScheduledThreadPool(ClientConfig.THREAD_POOL_SIZE);
        this.lamportClock = new LamportClock();
        this.report = new Report(this);
        this.clientIP = ClientConfig.DEFAULT_CLIENT_IP;
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
        this.activePeers = Collections.synchronizedSet(new HashSet<>());
        this.snippetList = Collections.synchronizedList(new ArrayList<>());
        this.shutdown = false;
        this.gui = gui;
        this.futures = new ConcurrentHashMap<>();
        this.pool = Executors.newScheduledThreadPool(ClientConfig.THREAD_POOL_SIZE);
        this.lamportClock = new LamportClock();
        this.report = new Report(this);
        this.clientIP = serverIP.equals("127.0.0.1") ? ClientConfig.DEFAULT_LAN_IP : GeneralUtil.getMyIP();
    }

    /**
     * receive a list of peers from the registry and store them
     *
     * @param reader socket to read the peers from
     * @param source for storing the peers with their source
     * @throws IOException if there is a problem communicating with the registry
     */
    private void receivePeers(BufferedReader reader, Peer source) throws IOException {
        int numberOfPeers = Integer.parseInt(reader.readLine());
        Set<Peer> peerSet = Collections.synchronizedSet(new HashSet<>());

        while (numberOfPeers > 0) {
            Peer peer = new Peer(reader.readLine());
            if (isValidPeer(peer)) {
                peerSet.add(peer);
                LOGGER.log(Level.INFO, "(valid) Peer received is " + peer.toString());
            } else {
                LOGGER.log(Level.INFO, "(invalid) Peer received is " + peer.toString());
            }
            activePeers.add(peer);
            futures.put(peer.toString().replace("\n", ""), pool.schedule(new Inactive(this, peer),
                    ClientConfig.INACTIVITY_INTERVAL, TimeUnit.MILLISECONDS));
            numberOfPeers--;
        }

        //adding source peer to the report
        report.getPeerTable().put(source, peerSet);
    }

    /**
     * Checking to see if port number receieved is between the valid range
     *
     * @param peer
     * @return boolean if port number is valid
     */
    public boolean isValidPeer(Peer peer) {
        return peer.getPort() >= 1 && peer.getPort() <= 65535;
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
                        System.out.printf("Peers received: {\n%s\n}\n", report.getPeerTable().toString());
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
                        response.append(this.clientIP)
                                .append(":")
                                .append(udpSocket.getLocalPort())
                                .append("\n");
                        writer.write(response.toString());
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
     * Try to connect to the registry and log if any errors occur
     */
    public void connectToRegistry() {
        try {
            Socket socket = new Socket(serverIP, port);
            handleRequest(socket);
            socket.close();
            LOGGER.log(Level.INFO, "Socket successfully closed.");
        } catch (BindException be) {
            LOGGER.log(Level.SEVERE, "Unable to start TCP socket at port " + port);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Trouble connecting, connection refused");
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
            udpSocket = new DatagramSocket(ClientConfig.DEFAULT_UDP_PORT_NUMBER);
            udpSocket.setBroadcast(true);
            LOGGER.log(Level.INFO, "Client UDP port started at " + udpSocket.getLocalPort());
        } catch (SocketException e) {
            LOGGER.log(Level.SEVERE, "Unable to initialize udp socket");
            System.exit(1);
        }

        //First connection to Registry
        connectToRegistry();

        activePeers.add(new Peer(this.clientIP, this.udpSocket.getLocalPort()));

        //start keepalive timer
        futures.put(teamName, pool.schedule(new KeepAlive(this), ClientConfig.KEEP_ALIVE_INTERVAL,
                TimeUnit.MILLISECONDS));

        while (!shutdown) {
            byte[] msg = new byte[ClientConfig.DEFAULT_PACKET_LENGTH];
            DatagramPacket pkt = new DatagramPacket(msg, msg.length);

            try {
                udpSocket.receive(pkt);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Issue receiving from UDP socket");
            }

            if (!shutdown) {
                pool.execute(new RequestProcessor(this, new PeerPacket(pkt)));

                //restart KeepAlive timer
                futures.get(teamName).cancel(true);
                futures.put(teamName, pool.schedule(new KeepAlive(this), ClientConfig.KEEP_ALIVE_INTERVAL,
                        TimeUnit.MILLISECONDS));
            }
        }

        futures.forEach((key, value) -> value.cancel(true));
        LOGGER.log(Level.INFO, "Cleaned up futures");

        pool.shutdown();
        LOGGER.log(Level.INFO, "Shutting down pool");

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
        LOGGER.log(Level.INFO, "Pool Shutdown successful");

        // Second Connection to Registry
        connectToRegistry();

        System.out.println("Sent updated report to registry, now terminating");
        System.exit(0);
    }

    /**
     * Send snippet method after cleaning up payload
     *
     * @param snippet
     */
    public void sendSnippet(String snippet) {
        new Thread(new UDPBroadcast(this, "snip", "snip" + lamportClock.getTimestamp() + " " + snippet)).start();
    }

    /**
     * Shutdown boolean to close UDP socket
     */
    public void shutdown() {
        this.shutdown = true;
        LOGGER.log(Level.INFO, "Closing UDP socket");
        udpSocket.close();
    }

    /**
     * Let peers know we are alive
     */
    public void keepAlive() {
        System.out.println("Running keepalive");
        String randomPeer = getRandomPeer();
        //snip newline and send peer
        new Thread(new UDPBroadcast(this, "peer", "peer" + randomPeer.replace("\n", ""))).start();
        //restart keepalive
        futures.get(teamName).cancel(true);
        futures.put(teamName, pool.schedule(new KeepAlive(this), ClientConfig.KEEP_ALIVE_INTERVAL,
                TimeUnit.MILLISECONDS));
    }

    /**
     * Get and return 1 random peer
     *
     * @return the chosen peer
     */
    public String getRandomPeer() {
        return activePeers.toArray()[new Random().nextInt(activePeers.size())].toString();
    }

    /**
     * Removes target peer from active peers
     *
     * @param target the peer to be removed
     */
    public void expired(Peer target) {
        //remove target from known active peers
        if (activePeers.contains(target)) {
            for (Peer peer : activePeers) {
                if (peer.equals(target)) {
                    peer.setAlive(false);
                    break;
                }
            }
            activePeers.remove(target);
        }
    }
    public void sendAckAndShutDown(PeerPacket packet) {
        new Thread(new PeerToPeerUDP(this, packet, this.getTeamName())).start();
    }

    public List<Snippet> getSnippetList() {
        return snippetList;
    }

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

    public Set<Peer> getActivePeers() {
        return activePeers;
    }

    public String getClientIP() {
        return clientIP;
    }


}