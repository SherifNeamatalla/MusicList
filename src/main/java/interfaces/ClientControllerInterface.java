package interfaces;

import model.Model;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientControllerInterface extends Remote {

    void modelUpdater(Model model) throws RemoteException;
}
