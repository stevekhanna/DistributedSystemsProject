package Client.helper;


import Client.Client;
import Client.DvPacket;

public class RequestProcessor implements Runnable{

    private final DvPacket packet;
    private final Client client;

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
            String request = packet.getType();
            switch (request){
                case "stop":
                    System.out.println("Stop request received, terminating program");
                    client.shutdown();
                    break;
                case "snip":
                    System.out.println("Snip request received");
                    handleSnipRequest();
                    break;
                case "peer":
                    System.out.println("Peer request received");
                    handlePeerRequest();
                    break;
                default:
                    System.out.printf("Request not recognized: %s\n", request);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleSnipRequest(){

    }

    public void handlePeerRequest(){

    }
}
