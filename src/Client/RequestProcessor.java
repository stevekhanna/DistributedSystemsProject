package Client;

import java.net.DatagramPacket;

public class RequestProcessor implements Runnable{

    private DatagramPacket pkt;
    private Client client;

    public RequestProcessor(Client client, DatagramPacket pkt) {
        this.client = client;
        this.pkt = pkt;
    }

    @Override
    public void run() {
        System.out.println("GOT SOMETHING!!!!!!");
        try{
            DvPacket data = new DvPacket(pkt);
            System.out.println(data.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
