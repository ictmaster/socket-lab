import java.net.*;
import java.io.*;
import java.util.Arrays;

public class Server extends Thread {
    protected Socket socket;

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
        System.out.println ("Creating thread for client");

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader( socket.getInputStream()));

            String il;

            while ((il = in.readLine()) != null){
                System.out.println ("Server(this): " + il);

                if (il.equals("disconnect"))
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
    protected String parseInput(String input){
        String[] args = input.split(" ");
        String command = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);
        if(command.equals("test")){
            return "AREE YOU TESTING? args: " + String.join(",",args);
        }

        return input;
    }
}