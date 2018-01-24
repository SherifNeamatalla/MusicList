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

    public static ArrayList<String> getUsers() {
        return users;
    }

    public void run() {

        try (ServerSocket server = new ServerSocket( 5020 )) {
            int connections = 0;
            while (true) {
                try {
                    Socket socket = server.accept();
                    System.out.println( " connection with socket: " + socket.toString() );
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
            username = in.readLine();
            password = in.readLine();


            synchronized (TCPServer.getUsers()) {
                if (password.equals( standardPassword ) && !username.equals( serviceName ) && !TCPServer.getUsers().contains( username )) {
                    TCPServer.getUsers().add( username );
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