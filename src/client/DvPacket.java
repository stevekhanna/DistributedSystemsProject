package client;

import java.io.Serializable;
import java.net.DatagramPacket;

public class DvPacket implements Serializable {

    private String type;
    private String message;

    public DvPacket() {
        this.type = "";
        this.message = "";
    }

    public DvPacket(DatagramPacket datagram) {
        this.type = new String(datagram.getData(), 0, 4);
        this.message = new String(datagram.getData(), 5, datagram.getLength());
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }
}
