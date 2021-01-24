package Client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Client {
    private String serverIP = "localhost";
    private int port = 12345;

    BufferedReader reader;
    BufferedWriter writer;

    ConcurrentHashMap<String, Set<String>> peerTable;



    public Client(String serverIP, int port){
        this.serverIP = serverIP;
        this.port = port;
        this.peerTable = new ConcurrentHashMap<String, Set<String>>();
    }


    private String handleGetCode(){

        String response = "";
        String language = "java\n";

        Path path = FileSystems.getDefault().getPath("src");
        String s = path.toAbsolutePath().toString();
        if (s.contains("/")){s+="/";}else{s+="\\";}
        path = FileSystems.getDefault().getPath(s+"Client");
        s = path.toAbsolutePath().toString();
        if (s.contains("/")){s+="/";}else{s+="\\";}
        path = FileSystems.getDefault().getPath(s+"Client.java");
        s = path.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);

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

    private void handleRequest(Socket sock){

        try{
            reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            String request;
            while ((request = reader.readLine()) != null){
                System.out.println(request);
                String response = "";

                switch(request) {
                    case "get team name":
                        response = "Steve and Issack\n";
                        System.out.println("Writing response now: " + response);
                        writer.write(response);
                        writer.flush();
                    break;
                    case "get code":
                        response = handleGetCode();
                        writer.write(response);
                        writer.flush();
                    break;
                    case "receive peers":
                        int numberOfPeers = Integer.parseInt(reader.readLine());
                        System.out.println(numberOfPeers);
                        Set<String> peerList = Collections.synchronizedSet(new HashSet<String>());
                        while(numberOfPeers > 0){
                            String peer = reader.readLine();
                            peerList.add(peer);
                            System.out.println("Added: "+peer);
                            numberOfPeers--;
                        }
                        String source = sock.getRemoteSocketAddress().toString();
                        if(!peerTable.containsKey(source)){
                            peerTable.put(source, peerList);
                        }
                        else{
                            Set<String> temp = peerTable.get(source);
                            temp.addAll(peerList);
                        }

                        System.out.println(peerTable.toString());
                    break;
                    case "get report":

                    break;
                    default:
                        System.out.println("Request not recognized");
                }
            }
            reader.close();
            writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    public void start() throws IOException{
        Socket sock = new Socket(serverIP, port);
        handleRequest(sock);
        sock.close();
    }
}
