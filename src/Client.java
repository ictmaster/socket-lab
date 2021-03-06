import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {

        String ip = "127.0.0.1";
        int port = 10008;

        if (args.length > 0) {
            ip = args[0];
        }
        System.out.println ("Attemping to connect to host " +
                ip + " on port "+port);

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Couldn't find host: " + ip);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;

        System.out.println ("Type Message (\"disconnect.\" to quit)");
        while ((userInput = stdIn.readLine()) != null)
        {
            out.println(userInput);
            String reply = in.readLine();
            // end loop
            if (userInput.equals("disconnect") || reply.equals("server_shutting_down"))
                break;

            System.out.println(">>>Server responds: " + reply);
        }

        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }
}
