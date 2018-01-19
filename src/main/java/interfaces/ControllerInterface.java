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
    void commit(String title, String interpret, String album, long id) throws RemoteException, MalformedURLException, NotBoundException;
    void add(long id) throws RemoteException, MalformedURLException, NotBoundException;
    void remove(long id) throws RemoteException, MalformedURLException, NotBoundException;
    void addAll() throws RemoteException, MalformedURLException, NotBoundException;
    void removeAll() throws RemoteException, MalformedURLException, NotBoundException;
    void update() throws RemoteException, NotBoundException, MalformedURLException;
    void logOut(String user) throws RemoteException;
    Model getModel () throws RemoteException;

}
