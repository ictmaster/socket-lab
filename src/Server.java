import java.net.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Server extends Thread {
    protected Socket socket;
    private static ArrayList<Server> serverInstances;
    UUID id;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        //For keeping track of running threads
        serverInstances = new ArrayList<>();

        int port = 10008;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println ("Socket object created...");
            try {
                while (true){
                    System.out.println ("Waiting for client connection...");
                    serverInstances.add(new Server (serverSocket.accept()));
                }
            }
            catch (IOException e){
                System.err.println("Couldn't connect to client.");
                System.exit(1);
            }
        }
        catch (IOException e) {
            System.err.println("Could not listen for port: "+port);
            System.exit(1);
        }
        finally {
            try {
                serverSocket.close();
            }
            catch (IOException e) {
                System.err.println("Could not close port: "+port);
                System.exit(1);
            }
        }
    }

    private Server (Socket client) {
        this.id = UUID.randomUUID();
        this.socket = client;
        this.start();

    }

    public void run() {
        System.out.println ("Creating thread for client "+(this.id)+"...");

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader( socket.getInputStream()));

            String il;

            //Main loop
            while ((il = in.readLine()) != null){
                System.out.println ("Server(this): " + il);

                //Break the loop and close the socket if client disconnects...
                if (il.startsWith("disconnect")){
                    System.out.println("Server(this): Disconnecting client "+ this.id );
                    serverInstances.remove(this);
                    break;
                }

                String output = parseInput(il);
                out.println(output);
            }

            out.close();
            in.close();
            socket.close();
        }
        catch (IOException e){
            System.err.println("Problem with Communication Server");
            System.exit(1);
        }
    }


    //Function for parsing userinput
    protected String parseInput(String input){
        String[] args = input.split(" ");
        String command = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        switch(command){
            case "test":
                return "AREE YOU TESTING? args: " + String.join(",",args);
            case "getmail":
                return DbHelper.getEmail(args);
            case "getnumber":
                return DbHelper.getNumber(args);
            case "listdep":
                return DbHelper.getList(args);
        }

        return input;
    }
}