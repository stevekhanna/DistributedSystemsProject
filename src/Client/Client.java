package Client;

import java.io.*;
import java.net.Socket;

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
                /*
                *
                * TODO: handle request
                *
                * */
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
