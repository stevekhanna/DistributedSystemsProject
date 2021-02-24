package MultiClientTester;

import client.logic.Driver;

public class MultiClient {
    private static final String HOST = "localhost";

    private static final String PORT = "55921";

    private static final int DEFAULT_NUM_OF_PORTS = 10;

    public static void main(String[] args) {
        int numOfPorts = DEFAULT_NUM_OF_PORTS;
        if (args.length > 0) {
            try {
                numOfPorts = Integer.parseInt(args[0]);
                if(numOfPorts < 2){
                    numOfPorts = DEFAULT_NUM_OF_PORTS;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Expected first argument to be a port number.  Argument ignored.");
            }
        }

        for(int i = 0; i< numOfPorts; i++){
            String teamName = "Client "+i;
            String [] argumentArr = {HOST, PORT, teamName};
            Driver driver = new Driver(argumentArr);
            driver.setVisible(true);
        }
    }
}
