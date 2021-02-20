package Client;

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

    @Test
    void testNonDefaultConstructorIP(){
        Client client = new Client("testing", 9999);
        assertEquals("testing", client.getServerIP());
    }

    @Test
    void testNonDefaultConstructorPort(){
        Client client = new Client("testing", 9999);
        assertEquals(9999, client.getPort());
    }
}