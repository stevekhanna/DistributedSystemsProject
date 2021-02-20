package Client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.DatagramPacket;

public class DvPacket implements Serializable {

    private String source;

    public DvPacket(){
        this.source = "";
    }

    public DvPacket(String source){
        this.source = source;
    }

    public DvPacket(DatagramPacket datagram) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(datagram.getData());
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);

        source = (String) objectStream.readObject();
    }

    public String getSource() {
        return source;
    }
}
