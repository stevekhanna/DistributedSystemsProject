package client;

import java.io.Serializable;
import java.net.DatagramPacket;

public class PeerPacket implements Serializable {

    private final String type;
    private final String message;
    private final String source;
    private final int timeReceived;

    public PeerPacket(DatagramPacket datagram) {
        String[] content = new String(datagram.getData(), 0, datagram.getLength()).split(" ");
        this.type = content[0].substring(0,4);
        int timeReceived = 0;
        switch (type) {
            case "snip" -> {
                timeReceived = Integer.parseInt(content[0].substring(4));
                this.message = content[1];
            }
            case "peer" -> this.message = content[0].substring(4);
            case "stop" -> this.message = "";
            default -> {
                this.message = "";
                System.out.printf("datagram type not recognized: %s\n", type);
            }
        }
        this.timeReceived = timeReceived;
        this.source = datagram.getSocketAddress().toString().substring(1);
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public int getTimeReceived() {
        return timeReceived;
    }
}
