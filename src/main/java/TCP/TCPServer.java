package TCP;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer extends Thread {

    private static ArrayList<String> users = new ArrayList<>();
    private TCPClientHandler tcpClientHandler;

    public void run() {
        // a Socket to connect the server with the client
        try (ServerSocket server = new ServerSocket( 5020 )) {
            int connections = 0;
            while (true) {
                try {
                    //waitin for the Client to connect
                    Socket socket = server.accept();
                    //System.out.println( " connection with socket: " + socket.toString() );
                    connections++;
                    tcpClientHandler = new TCPClientHandler( socket );
                    tcpClientHandler.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // get the list of the users
    public static ArrayList<String> getUsers() {
        return users;
    }

}

class TCPClientHandler extends Thread {
    private String standardPassword = "TCPAccept";
    private String serviceName = "RMI";
    private String username;
    private String password;

    private Socket socket;

    TCPClientHandler(Socket s) {
        this.socket = s;
    }


    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
             PrintWriter out = new PrintWriter( socket.getOutputStream(), true )) {
            //read from the client the username and the password
            username = in.readLine();
            password = in.readLine();

            // the array must be synchronized so that no more than one user can register at a time
            synchronized (TCPServer.getUsers()) {
                // make sure that the password correct and the username is not already token
                if (password.equals( standardPassword ) && !username.equals( serviceName ) && !TCPServer.getUsers().contains( username )) {
                    // add the user to the list of users
                    TCPServer.getUsers().add( username );
                    //respond to the client with the servicename
                    out.println( serviceName );
                } else {
                    String errorRespond = "";
                    errorRespond += "Error ";
                    if (TCPServer.getUsers().contains( username ) || username.equals( serviceName ))
                        errorRespond += "- User exists already! ";
                    if (!password.equals( standardPassword )) errorRespond += "- Wrong password! ";
                    System.out.println( errorRespond );
                    out.println( errorRespond );
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}