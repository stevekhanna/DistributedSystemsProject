package client;

import java.io.Serializable;
import java.net.DatagramPacket;

public class PeerPacket implements Serializable {

    private final String type;
    private final String message;
    private final String source;

    public PeerPacket(DatagramPacket datagram) {
        this.type = new String(datagram.getData(), 0, 4);
        if(this.type.equals("peer")){
            this.message = new String(datagram.getData(), 4, datagram.getLength()-4);
        }else if(this.type.equals("stop")){
            this.message = "";
        }else{
            this.message = new String(datagram.getData(), 5, datagram.getLength()-5);
        }
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
