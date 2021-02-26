package client;

import java.io.Serializable;
import java.net.DatagramPacket;

public class PeerPacket implements Serializable {

    private final String type;
    private final String message;
    private final String source;
    private final int timeReceived;

    //TODO: something weird happens with empty snippet
    public PeerPacket(DatagramPacket datagram) {
        String content = new String(datagram.getData(), 0, datagram.getLength());
        this.type = content.substring(0,4);
        int timeReceived = 0;
        String tempMessage = "";
        switch (type) {
            case "snip" -> {
                String [] parts = content.split(" ", 2);
                timeReceived = Integer.parseInt(parts[0].substring(4));
                if(parts.length == 2) {
                    tempMessage = parts[1];
                }
            }
            case "peer" -> tempMessage = content.substring(4);
            case "stop" -> tempMessage = "";
            default -> {
                tempMessage = "";
                System.out.printf("datagram type not recognized: %s\n", type);
            }
        }
        this.message = tempMessage;
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
