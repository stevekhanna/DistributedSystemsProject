package Client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GeneralUtil {
    
    /**
     * Getting the IP address
     * @return String IPv4 address
     */
    public static String getMyIP() {
        //https://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
        URL whatismyip = null;
        String ip = "Error";
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            ip = in.readLine(); //you get the IP as a String
            System.out.println(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ip;
    }
}
