package TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer extends Thread {

    ArrayList<String> users ;

    public TCPServer(ArrayList<String> users) {
        this.users = users;
    }

    @Override
    public void run() {

        try (ServerSocket server = new ServerSocket( 5020 )) {
            int connections = 0;
            while(true) {
                try {
                    Socket socket = server.accept();
                    connections++;
                    new TCPClientHandler( users , socket ).start();

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
        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream();
             ObjectInputStream oin = new ObjectInputStream( in );
             ObjectOutputStream oout = new ObjectOutputStream( out )) {
            String username = oin.readUTF();
            String password = oin.readUTF();

            System.out.println("in the TCP server\n " + username + " \n " + password);
            synchronized (users) {
                if(password.equals( standardPassword ) && !username.equals( serviceName ) && !users.contains( username )) {
                    users.add( username );
                    System.out.println( "Saved " + username + "to the list of Users");
                    oout.writeUTF( serviceName );
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