package client.util;

import client.general.ClientConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * General utilities that help with our system
 *
 * @author Team: "Steve and Issack" - Steve Khanna 10153930, Issack John 30031053
 * @version 2.0 (Iteration 2)
 * @since 01-29-2021
 */
public class GeneralUtil {

    /**
     * Convert all source code for peer process to string
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

    /** Title: getting-the-external-ip-address-in-java
     Author: Adelec Bakkal
     Date: May 30, 2010
     Code version: unknown
     Availability: https://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
     */
    /**
     * Method to return my current IPv4 Address
     *
     * @return my IP
     */
    public static String getMyIP() {
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
