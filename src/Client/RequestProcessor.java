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
            String request = packet.getMessage();
            switch (request){
                case "stop":
                    System.out.println("Stop request received, terminating program");
                    client.shutdown();
                    break;
                case "snip":
                    System.out.println("Snip request received");
                    break;
                case "peer":
                    System.out.println("Peer request received");
                    break;
                default:
                    System.out.printf("Request not recognized: %s\n", request);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
