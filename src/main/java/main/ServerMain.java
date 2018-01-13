package main;
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

        Model model = new Model();

        LocateRegistry.createRegistry(1099);
        Remote controller = new ServerController( model );

        Naming.rebind( "//127.0.0.1:1099/RMI" , controller );



    }


}