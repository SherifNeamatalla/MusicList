package TCP;

import Client.controller.ClientController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer extends Thread{

    private static ArrayList<String> users = new ArrayList<>(  );
    private TCPClientHandler tcpClientHandler;

    private ArrayList<ClientController> s;



    public void run() {

        try (ServerSocket server = new ServerSocket( 5020 )) {
            int connections = 0;
            while(true) {
                try {
                    Socket socket = server.accept();
                    System.out.println(" connection with socket: " + socket.toString());
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
    public static ArrayList<String> getUsers() {
        return users;
    }

}

class TCPClientHandler extends Thread {
    private String standardPassword = "TCPAccept" ;
    private String serviceName = "RMI";
    private String username;
    private String password;

    Socket socket;

    TCPClientHandler( Socket s) {
        this.socket = s;
    }


    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            username = in.readLine();
            password = in.readLine();


            synchronized (TCPServer.getUsers()) {
                if(password.equals( standardPassword ) && !username.equals( serviceName ) && !TCPServer.getUsers().contains( username )) {
                    TCPServer.getUsers().add( username );
                    out.println(serviceName);
                }
                else {
                    String errorRespond = new String();
                    errorRespond+= "Error ";
                    if(TCPServer.getUsers().contains( username ) || username.equals( serviceName )) errorRespond+="- User exists already! ";
                    if(!password.equals( standardPassword )) errorRespond += "- Wrong password! \n";
                    System.out.printf( errorRespond );
                    out.println(errorRespond);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}