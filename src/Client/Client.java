package Client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A peer process that can receive peers from the registry as well as send a report on it's known sources and peers
 * @author Team: "Steve and Issack" - Steve Khanna 10153930, Issack John 30031053
 * @version 1.0 (Iteration 1)
 * @since 01-29-2021
 */
public class Client {
    /** Server ip of registry */
    private final String serverIP;
    /** Port number of registry */
    private final int port;
    /** Our team name */
    private static final String TEAM_NAME = "Steve and Issack\n";
    /** If no port number is provided when running the client, this port number will be used */
    public static final int DEFAULT_PORT_NUMBER = 1245;
    /** If no server ip is provided when running the client, this server ip will be used*/
    public static final String DEFAULT_SERVER_IP = "localhost";

    /** Contains the sources and peers we know about, no duplicate sources*/
    private final ConcurrentHashMap<String, Set<String>> peerTable;
    /** Contains the time when the peerTable was acquire from a source, no duplicates allowed */
    private final ConcurrentHashMap<String, String> timeTable;

    /**
     * Default class constructor
     * Initializes port, peerTable, and timeTable
     */
    public Client(){
        this.serverIP = DEFAULT_SERVER_IP;
        this.port = DEFAULT_PORT_NUMBER;
        this.peerTable = new ConcurrentHashMap<>();
        this.timeTable = new ConcurrentHashMap<>();
    }

    /**
     * Overloaded class constructor
     * @param serverIP the Ip for the registry
     * @param port the port for the registry
     */
    public Client(String serverIP, int port){
        this.serverIP = serverIP;
        this.port = port;
        this.peerTable = new ConcurrentHashMap<>();
        this.timeTable = new ConcurrentHashMap<>();
    }


    /**
     * Convert all source code for peer process to string
     * @return String response, all the source code as a string
     */
    private String getCode(){
        StringBuilder response = new StringBuilder();
        String language = "java\n";

        Path path = FileSystems.getDefault().getPath("src/Client/Client.java");

        try{
            String code = Files.readString(path, StandardCharsets.UTF_8)+"\n";
            String endOfCode = "...\n";
            response.append(language).append(code).append(endOfCode);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return response.toString();
    }

    /**
     * Grabs all the stored peers
     * @return string of peers with newline after each one
     */
    private String getPeers(){
        StringBuilder sb = new StringBuilder();
        peerTable.values().forEach(peerList -> {
            peerList.forEach(peer -> {
                sb.append(peer).append("\n");
            });
        });
        return sb.toString();
    }

    /**
     * Grabs all the peers for the source provides
     * @param source the source IP:port where the peers came from
     * @return a string of peers with newline after each one
     */
    private String getPeers(String source){
        StringBuilder sb = new StringBuilder();
        peerTable.get(source).forEach(peer -> {
                sb.append(peer).append("\n");
        });
        return sb.toString();
    }

    /**
     * Grab all the known sources as well as their peers
     * @return String, the sources as a string, the number of sources as well as the peers
     */
    private String getSources(){
        StringBuilder sb = new StringBuilder();
        peerTable.keySet().forEach(source -> {
            sb.append(source)
                    .append("\n")
                    .append(timeTable.get(source))
                    .append("\n")
                    .append(peerTable.get(source).size())
                    .append("\n")
                    .append(getPeers(source));
        });

        return sb.toString();
    }

    /**
     * receive a list of peers from the registry and store them
     * @param reader socket to read the peers from
     * @param source for storing the peers with their source
     * @throws IOException if there is a problem communicating with the registry
     */
    private void receivePeers(BufferedReader reader, String source) throws IOException {
        int numberOfPeers = Integer.parseInt(reader.readLine());
        String dateAcquired = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        Set<String> peerList = Collections.synchronizedSet(new HashSet<>());

        while(numberOfPeers > 0){
            String peer = reader.readLine();
            peerList.add(peer);
            numberOfPeers--;
        }

        timeTable.put(source, dateAcquired);
        if(!peerTable.containsKey(source)){
            peerTable.put(source, peerList);
        }
        else{
            Set<String> temp = peerTable.get(source);
            temp.addAll(peerList);
        }
    }

    /**
     * handle the get report request, getting the sources and their peers as a string
     * @return String, information on the sources, peers and how many peers their are
     */
    private String getReport(){
        StringBuilder report = new StringBuilder();
        String sources = (getSources().equals("") ? "\n" : getSources());

        int totalPeers = 0;
        for(Set<String> setOfPeers : peerTable.values()){
            totalPeers += setOfPeers.size();
        }

        report.append(totalPeers) //numOfPeers
                .append("\n")
                .append(getPeers())
                .append(peerTable.size()) //numOfSources
                .append("\n")
                .append(sources);

        return report.toString();
    }


    /**
     * handling the communication between the peer process and the registry server
     * based on the communication protocol
     * @param sock the socket where the client is connected to the registry for all necessary communication
     */
    private void handleRequest(Socket sock){

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

            String request;
            boolean done = false;
            while ((request = reader.readLine()) != null && !done){
                System.out.println(request);
                StringBuilder response = new StringBuilder();

                switch(request) {
                    case "get team name":
                        response.append(TEAM_NAME);
                        System.out.printf("Writing response: {\n%s}\n" ,response.toString());
                        writer.write(response.toString());
                        writer.flush();
                        break;
                    case "get code":
                        response.append(getCode());
                        writer.write(response.toString());
                        writer.flush();
                        System.out.println("Code Written Successfully.");
                        break;
                    case "receive peers":
                        receivePeers(reader, sock.getInetAddress().getHostAddress()+":"+sock.getPort());
                        System.out.printf("Peers received: {\n%s\n}\n",peerTable.toString());
                        break;
                    case "get report":
                        response.append(getReport());
                        System.out.printf("Writing response:\n{%s}\n", response.toString());
                        writer.write(response.toString());
                        writer.flush();
                        break;
                    case "close":
                        System.out.println("Close Received");
                        done = true;
                        break;
                    default:
                        System.out.println("Request not recognized");
                }
            }
            reader.close();
            writer.close();
            System.out.println("Reader and Writer successfully closed.");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * get the server ip
     * @return string server ip
     */
    public String getServerIP() {
        return serverIP;
    }

    /**
     * get the port
     * @return int port
     */
    public int getPort() {
        return port;
    }


    /**
     * Starts the peer and accepts requests from registry.
     * @throws IOException if there are problems starting this peer or
     * if there are problems communicating with the registry.
     */
    public void start() throws IOException{
        Socket sock = new Socket(serverIP, port);
        handleRequest(sock);
        sock.close();
        System.out.println("Socket successfully closed.");
    }

    /**
     * Starts the client server. If a port number is provided as a runtime argument,
     * it will be used to start the peer.
     * @param args optional server ip as first argument and port number as second argument.
     */
    public static void main(String[] args) {

        try {
            Client client;
            if (args.length != 2) {
                System.out.println("No Server IP and port provided. Using Default Constructor with: localhost:12345");
                client = new Client();
            }
            else{
                client = new Client(args[0], Integer.parseInt(args[1]));
            }
            client.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
