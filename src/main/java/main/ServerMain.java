package main;
import TCP.TCPServer;
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

        TCPServer tcpServer = new TCPServer();
        tcpServer.start();

        Remote controller = new ServerController( model);
        Naming.rebind( "RMI" , controller );

        System.out.println("server Started..");

    }


}