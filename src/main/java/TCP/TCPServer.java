package TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer {

    ArrayList<String> users ;

    public TCPServer(ArrayList<String> users) {
        this.users = users;
    }


    public void execute() {

        try (ServerSocket server = new ServerSocket( 5020 )) {
            int connections = 0;
            while(true) {
                System.out.println("waiting.....");
                try {
                    Socket socket = server.accept();
                    System.out.println("connection started...");
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
    String username;
    String password;

    ArrayList<String> users ;
    Socket socket;

    public TCPClientHandler(ArrayList<String> users , Socket s ) {
        this.socket = s;
        this.users = users;
    }

    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {


            username = in.readLine();
            password = in.readLine();

            System.out.println("in the TCP server\n " + username + " \n " + password);
            synchronized (users) {

                if(password.equals( standardPassword ) && !username.equals( serviceName ) && !users.contains( username )) {
                    users.add( username );
                    System.out.println( "Saved " + username + " to the list of Users which its size now is : "+users.size());
                    out.write(serviceName);
                }
                else {
                    String errorRespond = new String();
                    errorRespond+="Error\n";
                    if(users.contains( username ) || username.equals( serviceName )) errorRespond+="User exists already!\n";
                    if(!password.equals( standardPassword )) errorRespond += "Wrong password! \n";
                    out.write( errorRespond );
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(username);
        System.out.println(password);
    }
}