import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class Server extends Thread {
    protected Socket socket;
    private static int connected_clients = 0;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        int port = 10008;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println ("Socket object created...");
            try {
                while (true){
                    System.out.println ("Waiting for client connection...");
                    new Server (serverSocket.accept());
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
        socket = client;
        start();
    }

    public void run() {
        System.out.println ("Creating thread for client "+(++connected_clients)+"...");

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader( socket.getInputStream()));

            String il;

            //Main loop
            while ((il = in.readLine()) != null){
                System.out.println ("Server(this): " + il);

                //Break the loop and close the socket if client disconnects...
                if (il.startsWith("disconnect"))
                    break;

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

    //TODO: write this
    protected String getEmailsByName(String[] args){
        if (args.length != 2){
            return "Error! expected exactly, "+args.length+" was given...\n":
        }
        return "not yet implemented";
    }

    //Function for parsing userinput
    protected String parseInput(String input){
        String[] args = input.split(" ");
        String command = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);
        if(command.equals("test")){
            return "AREE YOU TESTING? args: " + String.join(",",args);
        }
        if (command.equals("getmail")){
            return getEmailsByName(args);
        }

        return input;
    }
}