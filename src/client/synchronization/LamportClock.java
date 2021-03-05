package client.synchronization;

/**
 *
 *
 * @author Team: "Steve and Issack" - Steve Khanna 10153930, Issack John 30031053
 * @version 2.0 (Iteration 2)
 * @since 01-29-2021
 */
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
