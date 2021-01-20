package Client;

public class Driver {
    public static void main(String[] args) {
        //Server IP = 136.159.5.22
        //Port: 55921
        if(args.length != 2 ){
            System.out.println("Number of arguments is not valid. Usage: Server IP, port");
            System.exit(1);
        }
        try{
            Client client = new Client(args[0], Integer.parseInt(args[1]));
            client.start();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
