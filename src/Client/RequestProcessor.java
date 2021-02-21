package Client;


public class RequestProcessor implements Runnable{

    private DvPacket packet;
    private Client client;

    public RequestProcessor(Client client, DvPacket pkt) {
        this.client = client;
        this.packet = pkt;
    }

    @Override
    public void run() {
        processPacket();
    }

    public void processPacket(){
        try{
            System.out.println(packet.getMessage());
            if(packet.getMessage().equals("stop")){
                System.out.println("Terminating program");
                client.shutdown();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
