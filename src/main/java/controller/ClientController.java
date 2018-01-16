package controller;

import TCP.TCPClient;
import interfaces.ClientControllerInterface;
import interfaces.ControllerInterface;
import javafx.application.Platform;
import model.Model;
import view.ClientView;


import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class ClientController extends UnicastRemoteObject implements ClientControllerInterface {


    private Model clientModel;
    private ClientView view;
    private String username,password;
    private String servicename ;
    private ControllerInterface controllerInter ;


    public ClientController(Model model, ClientView view) throws RemoteException {

        this.clientModel = model;
        this.view = view;
        link(model,view);
        //Initializes the Actionlisteners of Login and Clear buttons in ClientView
        setActionListeners();


    }
    private void link(Model model, ClientView view) {
        //Links the Library and Playlist of the View with the Library and Playlist in the Model.
        view.setLibrary(model.getLibrary());
        view.setPlaylist(model.getPlaylist());

    }

    private void setActionListeners()
    {
        setLoginAction();
        setClearAction();
        setPlayAction();
        setPauseAction();
        setNextAction();
        setCommitAction();
        setAddAction();
        setRemoveAction();
        setAddAllAction();
        setRemoveAllAction();
    }

    private void setLoginAction() {

        view.getLoginButton().setOnAction(e -> {

            username = view.getUsernameField().getText();
            password = view.getPasswordField().getText();
            TCPClient tcpClient = new TCPClient( username, password );
            tcpClient.start();
            try {
                tcpClient.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            servicename = tcpClient.getServiceName();


            System.out.println("Now the user name is: " + username + " The password is: " + password);
            System.out.println("Now the service name is " + servicename);

            try {
                // TODO: 15.01.2018  used to solve the current issue temporarily
                //servicename = "RMI";
                controllerInter = (ControllerInterface) Naming.lookup( servicename );
                System.out.println("connected to TCP and got STUB with service name " + servicename);

                clientModel.getLibrary().setList( controllerInter.getModel().getLibrary().getList() );
                clientModel.getPlaylist().setList( controllerInter.getModel().getPlaylist().getList() );
                Remote updater = this;
                Naming.rebind( username , updater);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            } catch (NotBoundException e1) {
                e1.printStackTrace();
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
            this.clear();
        });
    }


    public void setClearAction() {

        view.getClearButton().setOnAction(e -> {
            this.clear();

        });
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setPlayAction() {
        view.getPlay().setOnAction( event -> {
            try {
                controllerInter.play( view.getPlaylist().getSelectionModel().getSelectedItem().getId() );
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }

    public void setPauseAction() {
        view.getPause().setOnAction( event -> {
            try {
                controllerInter.pause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }

    public  void setNextAction() {
        view.getNext().setOnAction( event -> {
            try {
                controllerInter.next();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }

    public void setCommitAction() {
        view.getCommit().setOnAction( event -> {
            String title = view.getTitle().getText();
            String interpret = view.getInterpret().getText();
            String album = view.getAlbum().getText();
            long id = view.getLibrary().getSelectionModel().getSelectedItem().getId();
            try {
                controllerInter.commit( title, interpret, album, id );

            } catch (RemoteException e) {
                e.printStackTrace();
            }

        } );
    }

    public void setAddAction() {
        view.getAdd().setOnAction( event -> {
            try {
                controllerInter.add( view.getLibrary().getSelectionModel().getSelectedItem().getId() );

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }

    public void setRemoveAction() {
        view.getRemove().setOnAction( event -> {
            try {

                controllerInter.remove( view.getPlaylist().getSelectionModel().getSelectedItem().getId() );

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }

    public void setAddAllAction() {
        view.getAddAll().setOnAction( event -> {
            try {
                controllerInter.addAll();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void setRemoveAllAction() {
        view.getRemoveAll().setOnAction( event -> {
            try {
                controllerInter.removeAll();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }

    void clear () {
        view.getUsernameField().setText("");
        view.getPasswordField().setText("");
    }


    @Override
    public void modelUpdater(Model model) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    clientModel.getLibrary().clear();
                    clientModel.getLibrary().setList( model.getLibrary().getList() );
                    System.out.printf( "updating lists" );
                    clientModel.getPlaylist().setList( model.getPlaylist().getList() );
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });

//        this.model.getLibrary().setList( model.getLibrary().getList() );
//        System.out.printf( "updating lists" );
//        this.model.getPlaylist().setList( model.getPlaylist().getList() );

    }
}


