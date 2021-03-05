package client.synchronization;

public class LamportClock {

    private int timestamp;

    public LamportClock(){
        timestamp = 0;
    }

    public void setTimestamp(int timestamp){
        this.timestamp = timestamp;
    }

    public int getTimestamp() {
        return timestamp;
    }
}
