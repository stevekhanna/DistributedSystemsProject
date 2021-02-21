package Client;

import java.io.Serializable;
import java.net.DatagramPacket;

public class DvPacket implements Serializable {

    private String message;

    public DvPacket() {
        this.message = "";
    }

    public DvPacket(String source) {
        this.message = source;
    }

    public DvPacket(DatagramPacket datagram) {
        System.out.println("before stuck");
        this.message = new String(datagram.getData(), 0, datagram.getLength());
        System.out.println("stuck");
    }

    public String getMessage() {
        return message;
    }
}
