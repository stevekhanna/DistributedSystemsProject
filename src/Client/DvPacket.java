package Client;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;

public class DvPacket implements Serializable {

    private String message;

    public DvPacket(){
        this.message = "";
    }

    public DvPacket(String source){
        this.message = source;
    }

    public DvPacket(DatagramPacket datagram) throws IOException, ClassNotFoundException {
        this.message = new String(datagram.getData(), 0, datagram.getLength());
    }

    public String getMessage() {
        return message;
    }
}
