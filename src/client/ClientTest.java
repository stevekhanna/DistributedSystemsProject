package client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    public static final int DEFAULT_PORT_NUMBER = 1245;
    public static final String DEFAULT_SERVER_IP = "localhost";

    @Test
    void start() {

    }

    @Test
    void testDefaultConstructorPort(){
        Client client = new Client();
        assertEquals(DEFAULT_PORT_NUMBER, client.getPort());
    }

    @Test
    void testDefaultConstructorIP(){
        Client client = new Client();
        assertEquals(DEFAULT_SERVER_IP, client.getServerIP());
    }
}