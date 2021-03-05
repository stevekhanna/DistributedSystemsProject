package client.general;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ClientConfig {

    /** Client defaults */
    public static final String DEFAULT_TEAM_NAME = "Steve and Issack\n";

    public static final String DEFAULT_SERVER_IP = "127.0.0.1";
    public static final int DEFAULT_PORT_NUMBER = 1245;
    public static final int DEFAULT_UDP_PORT_NUMBER = 0;
    public static final String DEFAULT_CLIENT_IP = "127.0.0.1";
    public static final String DEFAULT_LAN_IP = "192.168.1.65";

    public static final int DEFAULT_PACKET_LENGTH = 64;

    public static final int THREAD_POOL_SIZE = 10;
    public static final int KEEP_ALIVE_INTERVAL = 60 * 1000;
    public static final int INACTIVITY_INTERVAL = 3 * 60 * 1000;

    public static final String SOURCE_DIR = "src/client";
    public static final List<Path> CODE_FILES = new ArrayList<>();

    /** JFrame dimensions */
    public static final int HEIGHT = 420;
    public static final int WIDTH = 420;

    public static final int GRID_LAYOUT_ROWS = 5;
    public static final int GRID_LAYOUT_COLS = 1;
    public static final int GRID_LAYOUT_HGAP = 5;
    public static final int GRID_LAYOUT_VGAP = 5;


    /** JFrame title */
    public static final String TITLE = "OnlySnippets";

}