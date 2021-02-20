package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

public class RequestProcessor implements Runnable{

    private Socket peerSocket = null;
    private BufferedWriter out;
    private BufferedReader in;


    @Override
    public void run() {
        assert(peerSocket != null);
    }
}
