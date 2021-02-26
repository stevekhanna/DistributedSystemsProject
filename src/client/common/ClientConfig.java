package client.common;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ClientConfig {

    /** Client defaults */
    public static final String DEFAULT_TEAM_NAME = "Steve and Issack\n";

    public static final String DEFAULT_SERVER_IP = "localhost";
    public static final int DEFAULT_PORT_NUMBER = 1245;
    public static final int UDP_DEFAULT_PORT = 0;

    public static final int THREAD_POOL_SIZE = 10;
    public static final int KEEP_ALIVE_INTERVAL = 10 * 1000;
    public static final int INACTIVITY_INTERVAL = 2 * 60 * 1000;

    public static final String SOURCE_DIR = "src/client";
    public static final List<Path> CODE_FILES = new ArrayList<>();

    /** JFrame dimensions */
    public static final int HEIGHT = 420;
    public static final int WIDTH = 420;

    /** JFrame title */
    public static final String TITLE = "OnlySnippets";

}