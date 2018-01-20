package Client.controller;

import Client.client.TCP.TCPClient;
import Client.client.UDP.UDPClient;
import interfaces.ClientControllerInterface;
import interfaces.ControllerInterface;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import model.Model;
import Client.client.view.ClientView;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientController extends UnicastRemoteObject implements ClientControllerInterface {


    private Model clientModel;
    private ClientView view;
    private String username, password;
    private String serviceName;
    private ControllerInterface controllerInter;


    public ClientController(Model model, ClientView view) throws RemoteException {

        this.clientModel = model;
        this.view = view;
        link( model, view );

        //Initializes the Actionlisteners of Login and Clear buttons in ClientView
        setActionListeners();
        setSelectedItemLibrary();

    }

    private void setupDurationThread() {
        new Thread(()->{
            while (true) {
                new UDPClient( this.view.getActualTime() ).start();
                try {
                    Thread.sleep( 1000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void link(Model model, ClientView view) {
        //Links the Library and Playlist of the View with the Library and Playlist in the Model.
        view.setLibrary( model.getLibrary() );
        view.setPlaylist( model.getPlaylist() );

    }

    private void setActionListeners() {
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

        view.getLoginButton().setOnAction( e -> {

            this.setUsername(view.getUsernameField().getText() );
            this.setPassword( view.getPasswordField().getText() );
            TCPClient tcpClient = new TCPClient( username, password );
            tcpClient.start();


            try {
                tcpClient.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            serviceName = tcpClient.getServiceName();

            System.out.println( "Now the username is: " + username + " The password is: " + password );
            System.out.println( "Now the service name is " + serviceName );

            // TODO: 16.01.2018 check if the service name an Error has or not , and act upon !
            if (serviceName!= null && !serviceName.startsWith( "Error" )) {
                try {

                        controllerInter = (ControllerInterface) Naming.lookup( serviceName );
                        System.out.println( "connected to TCP and got STUB with service name " + serviceName );
                    if(controllerInter != null){
                        //clientModel.getLibrary().clear();
                        clientModel.getLibrary().setList( controllerInter.getModel().getLibrary().getList() );
                        clientModel.getPlaylist().setList(controllerInter.getModel().getPlaylist().getList());
                        Remote updater = this;
                        Naming.rebind( username, updater );
                    }} catch (RemoteException e1) {
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
                this.clear();
                this.view.getNameOfUser().setText( view.getNameOfUser().getText()+" " + username );
                setupDurationThread();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, serviceName, ButtonType.OK);
                alert.showAndWait();
                System.out.println(serviceName);
                try {
                    // TODO: 15.01.2018  used to solve the current issue temporarily
                    //servicename = "RMI";
                    if(controllerInter != null){

                        controllerInter = (ControllerInterface) Naming.lookup(serviceName);
                        System.out.println("connected to TCP and got STUB with service name " + serviceName);

                        modelUpdater(controllerInter.getModel());
                        //clientModel.getPlaylist().setList(controllerInter.getModel().getPlaylist().getList());

                        Remote updater = this;
                        Naming.rebind(username, updater);
                    }} catch (RemoteException e1) {
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }


    private void setClearAction() {

        view.getClearButton().setOnAction( e -> {
            this.clear();
        } );
    }



    private void setPlayAction() {
        view.getPlay().setOnAction( event -> {
            try {
                if(controllerInter != null)
                    if(view.getPlaylist().getSelectionModel().getSelectedItem() != null)
                        controllerInter.play( view.getPlaylist().getSelectionModel().getSelectedItem().getId() );
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }

    private void setPauseAction() {
        view.getPause().setOnAction( event -> {
            try {
                if(controllerInter != null)

                    controllerInter.pause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }

    private void setNextAction() {
        view.getNext().setOnAction( event -> {
            try {
                if(controllerInter != null)

                    controllerInter.next();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }

    private void setCommitAction() {
        view.getCommit().setOnAction( event -> {
            String title = view.getTextTitle().getText();
            String interpret = view.getTextInterpret().getText();
            String album = view.getTextAlbum().getText();
            long id = -1;
            if(view.getLibrary().getSelectionModel().getSelectedItem()!= null)
                id = view.getLibrary().getSelectionModel().getSelectedItem().getId();
            try {
                if(controllerInter != null)

                    controllerInter.commit( title, interpret, album, id );

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        } );
    }

    private void setAddAction() {
        view.getAdd().setOnAction( event -> {
            try {
                if(controllerInter != null)
                    if(view.getLibrary().getSelectionModel().getSelectedItem() != null)
                        controllerInter.add( view.getLibrary().getSelectionModel().getSelectedItem().getId() );

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } );
    }

    private void setRemoveAction() {
        view.getRemove().setOnAction( event -> {
            try {

                if(controllerInter != null)
                    if(view.getPlaylist().getSelectionModel().getSelectedItem() != null)
                        controllerInter.remove( view.getPlaylist().getSelectionModel().getSelectedItem().getId() );

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } );
    }

    private void setAddAllAction() {
        view.getAddAll().setOnAction( event -> {
            try {
                if(controllerInter != null)

                    controllerInter.addAll();

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } );
    }

    private void setRemoveAllAction() {
        view.getRemoveAll().setOnAction( event -> {
            try {
                if(controllerInter != null)

                    controllerInter.removeAll();

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } );
    }

    private void clear() {
        view.getUsernameField().setText( "" );
        view.getPasswordField().setText( "" );
    }

    public void setLogOutAction() {
        try {


            if(controllerInter != null) {
                this.controllerInter.logOut(this.username);
                Naming.unbind(this.username);
            }


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void modelUpdater(Model model) throws RemoteException {

//        this.clientModel.getLibrary().setList( model.getLibrary().getList() );
//        this.clientModel.getPlaylist().setList( model.getPlaylist().getList() );

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    clientModel.getLibrary().clear();
                    clientModel.getLibrary().setList( model.getLibrary().getList() );
                    System.out.printf( "updating lists" );
                    clientModel.getPlaylist().clear();
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public  void setSelectedItemLibrary()
    {
        view.getLibrary().setOnMouseClicked(new EventHandler<MouseEvent>() {
            long selectedIdLibrary = -1;
            @Override
            public void handle(MouseEvent event) {
                //Id of selected song.
                if(view.getLibrary().getSelectionModel().getSelectedItem() != null)
                    selectedIdLibrary = view.getLibrary().getSelectionModel().getSelectedItem().getId();

                // if an item is selected, show its data in the Text fields in the view
                if(selectedIdLibrary !=-1){
                    try {

                        view.getTextTitle().setText(clientModel.getLibrary().findSongByID(selectedIdLibrary).getTitle());
                        view.getTextInterpret().setText(clientModel.getLibrary().findSongByID(selectedIdLibrary).getInterpret());
                        view.getTextAlbum().setText(clientModel.getLibrary().findSongByID(selectedIdLibrary).getAlbum());

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}