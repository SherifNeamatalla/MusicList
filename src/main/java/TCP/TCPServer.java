package TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer extends Thread {

    private static ArrayList<String> users = new ArrayList<>(  );
    private TCPClientHandler tcpClientHandler;


    @Override
    public void run() {

        try (ServerSocket server = new ServerSocket( 5020 )) {
            int connections = 0;
            while(true) {
                try {
                    System.out.println("The count of connections in the Users list:" + users.size());
                    System.out.println("The count of connections in the connections parameter :" + connections);
                    System.out.println("TCP server waiting for connections! ");
                    Socket socket = server.accept();
                    System.out.println("socket: " + socket.toString());
                    connections++;
                    tcpClientHandler = new TCPClientHandler( users, socket );
                    tcpClientHandler.start();
                    // TODO: 15.01.2018  what is happening after this start !!!

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static ArrayList<String> getUsers() {
        return users;
    }

}

class TCPClientHandler extends Thread {
    String standardPassword = "TCPAccept" ;
    String serviceName = "RMI";

    ArrayList<String> users ;
    Socket socket;

    public TCPClientHandler(ArrayList<String> users , Socket s ) {
        this.socket = s;
        this.users = users;
    }


    @Override
    public void run() {
        System.out.println("started the thread of the TCPHandler");

        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {
            ObjectInputStream oin = new ObjectInputStream( in );
            ObjectOutputStream oout = new ObjectOutputStream( out );
            System.out.println("TCPHandler is waiting for the username and the password");
            // TODO: 15.01.2018 check if we need try-resource
            String username = oin.readUTF();
            System.out.println("go the Username, it's " + username);
            String password = oin.readUTF();

            System.out.println("in the TCP server\n " + username + " \n " + password);
            synchronized (users) {
                if(password.equals( standardPassword ) && !username.equals( serviceName ) && !users.contains( username )) {
                    users.add( username );
                    System.out.println( "Saved " + username + "to the list of Users");
                    oout.writeUTF( serviceName );
                    oout.flush();
                }
                else {
                    String errorRespond = new String();
                    errorRespond+="Error\n";
                    if(users.contains( username ) || username.equals( serviceName )) errorRespond+="User exists already!\n";
                    if(!password.equals( standardPassword )) errorRespond += "Wrong password! \n";
                    oout.writeUTF( errorRespond );
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}