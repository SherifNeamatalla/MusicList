package interfaces;

import model.Model;
import view.ClientView;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControllerInterface extends Remote{

    void play(long id) throws RemoteException;
    void pause() throws RemoteException;
    void next() throws RemoteException;
    void commit(String title, String interpret, String album, long id) throws RemoteException;
    void add(long id) throws RemoteException;
    void remove(long id) throws RemoteException;
    void addAll() throws RemoteException;
    void removeAll() throws RemoteException;
    void update() throws RemoteException, NotBoundException, MalformedURLException;
    Model getModel () throws RemoteException;

}
