package client;

import java.io.Serializable;
import java.net.DatagramPacket;

public class DvPacket implements Serializable {

    private final String type;
    private final String message;
    private final String source;

    public DvPacket(DatagramPacket datagram) {
        this.type = new String(datagram.getData(), 0, 4);
        this.message = new String(datagram.getData(), 5, datagram.getLength());
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
}
