package Client;

import java.net.DatagramPacket;

public class RequestProcessor implements Runnable{

    private DatagramPacket pkt;

    public RequestProcessor(Client client, DatagramPacket pkt) {
        this.pkt = pkt;
    }


    @Override
    public void run() {
        try{
            DvPacket data = new DvPacket(pkt);
            String source = data.getSource();
            System.out.println(source +"!!!!!!!!!!!!!!!\n");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
