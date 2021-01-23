package Client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client {
    private String serverIP = "localhost";
    private int port = 12345;

    BufferedReader reader;
    BufferedWriter writer;
    public Client(String serverIP, int port){
        this.serverIP = serverIP;
        this.port = port;
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

                        Path p = Paths.get("Client.java");
                        Path folder = FileSystems.getDefault().getPath(new String("./")).toAbsolutePath().getParent();

                        System.out.println(folder.toString());

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

                        String code = Files.readString(path)+"\n";
                        System.out.println(code);

                        String endOfCode = "...\n";
                        response = language+code+endOfCode;
                        writer.write(response);
                        writer.flush();

                    break;
                    case "receive peers":

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
