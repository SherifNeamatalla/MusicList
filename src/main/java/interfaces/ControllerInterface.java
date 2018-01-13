package interfaces;

import model.Model;
import view.ClientView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControllerInterface extends Remote{

    void play(int id) throws RemoteException;
    void pause() throws RemoteException;
    void next() throws RemoteException;
    void commit() throws RemoteException;
    void add() throws RemoteException;
    void remove() throws RemoteException;
    void addAll() throws RemoteException;
    void removeAll() throws RemoteException;


}
