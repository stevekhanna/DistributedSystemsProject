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
 * 1 ) TODO Convert all string to string builder
 * 2 ) TODO Extract any constants and store them as static final at the top of the file
 * 3 ) TODO JavaDoc on all functions and class
 * 4 ) TODO Testcases, Junit
 * 5 ) TODO Look for any places where exceptions might occur
 * 6 ) TODO Run with multiple peers
 * 7 ) TODO Getters and setters if needed
 * 8 ) TODO Move variables that are shared to instance level and others to local level
 * 9 ) TODO Separate cases into function and figure out proper name for methods
 * 10 ) TODO ******use her peer class*****
 * 11 ) TODO change handle to get for all except the handleRequest method
 * 12 ) TODO Add local debugger instead of standard output.
 */

/**
 * Insert class summary here, *say what class does but not how it does it*
 * @author Steve and Issack - Steve Khanna 10153930, Issack John 30031053
 * @version 1.0
 * @since 01-20-2020
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

    private BufferedReader reader;
    private BufferedWriter writer;

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
        this.peerTable = new ConcurrentHashMap<String, Set<String>>();
        this.timeTable = new ConcurrentHashMap<String, String>();
    }

    /**
     * Overloaded class constructor
     * @param serverIP
     * @param port
     */
    public Client(String serverIP, int port){
        this.serverIP = serverIP;
        this.port = port;
        this.peerTable = new ConcurrentHashMap<String, Set<String>>();
        this.timeTable = new ConcurrentHashMap<String, String>();
    }


    /**
     *
     * @return
     */
    private String handleGetCode(){

        String response = "";
        String language = "java\n";

        // Simplify
        Path path = FileSystems.getDefault().getPath("src");
        String s = path.toAbsolutePath().toString();
        if (s.contains("/")){s+="/";}else{s+="\\";}
        path = FileSystems.getDefault().getPath(s+"Client");
        s = path.toAbsolutePath().toString();
        if (s.contains("/")){s+="/";}else{s+="\\";}
        path = FileSystems.getDefault().getPath(s+"Client.java");
        s = path.toAbsolutePath().toString();

        try{
            String code = Files.readString(path, StandardCharsets.UTF_8)+"\n";
            String endOfCode = "...\n";
            response = language+code+endOfCode;
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return response;
    }

    /**
     *
     * @return
     */
    private String handleGetPeers(){
        StringBuilder sb = new StringBuilder();
        peerTable.values().forEach(peerList -> {
            peerList.forEach(peer -> {
                sb.append(peer).append("\n");
            });
        });
        return sb.toString();
    }

    /**
     *
     * @param source
     * @return
     */
    private String handleGetPeers(String source){
        StringBuilder sb = new StringBuilder();
        peerTable.get(source).forEach(peer -> {
                sb.append(peer).append("\n");
        });
        return sb.toString();
    }

    /**
     *
     * @return
     */
    private int handleGetNumOfSources(){
        return peerTable.size();
    }

    /**
     *
     * @param source
     * @return
     */
    private int handleGetNumOfPeers(String source){
        return peerTable.get(source).size();
    }

    /**
     *
     * @return
     */
    private String handleGetSources(){
        StringBuilder sb = new StringBuilder();
        peerTable.keySet().forEach(source -> {
            sb.append(source)
                    .append("\n")
                    .append(timeTable.get(source))
                    .append("\n")
                    .append(handleGetNumOfPeers(source))
                    .append("\n")
                    .append(handleGetPeers(source));
        });

        return sb.toString();
    }

    /**
     *
     * @return
     */
    private int handleGetNumOfPeers(){
        return peerTable.values().size();
    }

    /**
     *
     * @param sock
     */
    private void handleRequest(Socket sock){

        try{
            reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            String request;
            boolean done = false;
            while ((request = reader.readLine()) != null && !done){
                System.out.println(request);
                String response = "";

                switch(request) {
                    case "get team name":
                        response = TEAM_NAME;
                        System.out.printf("Writing response: {\n%s}\n" ,response);
                        writer.write(response);
                        writer.flush();
                    break;
                    case "get code":
                        response = handleGetCode();
                        writer.write(response);
                        writer.flush();
                        System.out.println("Code Written Successfully.");
                    break;
                    case "receive peers":
                        int numberOfPeers = Integer.parseInt(reader.readLine());
                        String dateAcquired = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
                        Set<String> peerList = Collections.synchronizedSet(new HashSet<String>());

                        while(numberOfPeers > 0){
                            String peer = reader.readLine();
                            peerList.add(peer);
                            numberOfPeers--;
                        }
                        String source = sock.getInetAddress().getHostAddress();
                        timeTable.put(source, dateAcquired);
                        if(!peerTable.containsKey(source)){
                            peerTable.put(source, peerList);
                        }
                        else{
                            Set<String> temp = peerTable.get(source);
                            temp.addAll(peerList);
                        }

                        System.out.printf("Peers received: {\n%s\n}\n",peerTable.toString());
                    break;
                    case "get report":
                        int numOfPeers = 0;
                        //newline peer peer
                        String peers = "";
                        int numOfSources = 0;
                        String sources = "";

                        if(peerTable.isEmpty()){
                            response = numOfPeers + "\n" + peers + "\n" + numOfSources + "\n" + sources +"\n";
                        }
                        else{
                            numOfPeers = handleGetNumOfPeers();
                            peers = handleGetPeers();
                            numOfSources = handleGetNumOfSources();
                            sources = handleGetSources();

                            response = numOfPeers + "\n" + peers + numOfSources + "\n" + sources;
                        }
                        System.out.printf("Writing response:\n{%s}\n", response);
                        writer.write(response);
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
     *
     * @throws IOException
     */
    public void start() throws IOException{
        Socket sock = new Socket(serverIP, port);
        handleRequest(sock);
        sock.close();
        System.out.println("Socket successfully closed.");
    }

    /**
     * Starts the client server
     * @param args
     */
    public static void main(String[] args) {
        //Server IP = 136.159.5.22
        //Port: 55921
        if (args.length != 2) {
            System.out.println("Number of arguments is not valid. Usage: Server IP, port");
            System.exit(1);
        }
        try {
            Client client = new Client(args[0], Integer.parseInt(args[1]));
            client.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
