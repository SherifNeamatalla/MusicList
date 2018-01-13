package interfaces;

import model.Model;
import view.ClientView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControllerInterface extends Remote{

    void setActionListeners () throws RemoteException;
    void setLists() throws RemoteException;
    void link(Model model, ClientView clientView) throws RemoteException;

}
