package main;
import com.sun.javafx.application.PlatformImpl;
import controller.ServerController;
import interfaces.ControllerInterface;
import model.Model;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class ServerMain {
    public static void main(String args []) throws RemoteException, MalformedURLException {
        PlatformImpl.startup(() -> {});
        Model model = new Model();

        LocateRegistry.createRegistry(1099);
        Remote controller = new ServerController( model );

        Naming.rebind( "RMI" , controller );

        System.out.println("server Started..");

    }


}