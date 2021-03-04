package client.util;

import client.common.ClientConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class GeneralUtil {

    /**
     * Convert all source code for peer process to string
     * TODO: Read from a list
     *
     * @return String response, all the source code as a string
     */
    public static String getCode() {
        StringBuilder response = new StringBuilder();
        String language = "java\n";
        response.append(language);
        ClientConfig.CODE_FILES.forEach(path -> {
                    try {
                        String code = Files.readString(path, StandardCharsets.UTF_8) + "\n";
                        response.append(code);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        String endOfCode = "...\n";
        response.append(endOfCode);
        return response.toString();
    }

    /**
     * Getting the IP address
     *
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ip;
    }
}
