package main;
import TCP.TCPServer;
import UDP.UDPServer;
import com.sun.javafx.application.PlatformImpl;
import controller.ServerController;
import interfaces.ControllerInterface;
import model.Model;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;


public class ServerMain {
    public static void main(String args []) throws RemoteException, MalformedURLException, AlreadyBoundException {
        PlatformImpl.startup(() -> {});
        Model model = new Model();
        LocateRegistry.createRegistry(1099);
        //instantiate a new tcp server
        TCPServer tcpServer = new TCPServer();
        tcpServer.start();
        //declaring a Remote object which will here represent the skeleton
        Remote controller = new ServerController( model);
        //registering the Remote Object in the rmiregistery
        Naming.rebind( "RMI" , controller );

        //System.out.println("server Started..");
    }


}