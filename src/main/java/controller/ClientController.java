package controller;

import TCP.TCPClient;
import UDP.UDPClient;
import interfaces.ClientControllerInterface;
import interfaces.ControllerInterface;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import model.Model;
import view.ClientView;
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

    // when the user clicks any button, the relevant method in the server controller will be called over the Remote Object
    public ClientController(Model model, ClientView view) throws RemoteException {

        this.clientModel = model;
        this.view = view;
        link( model, view );

        //Initializes the Actionlisteners of Login and Clear buttons in ClientView
        setActionListeners();
        setSelectedItemLibrary();

    }

    // to constantly ask the server to get the duration of the actual song
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
            // create a tcp client with the username and the password the user has entered
            TCPClient tcpClient = new TCPClient( username, password );
            tcpClient.start();

            //wait for the tcp thread to finish
            try {
                tcpClient.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            //get the servicename
            serviceName = tcpClient.getServiceName();

//            System.out.println( "Now the username is: " + username + " The password is: " + password );
//            System.out.println( "Now the service name is " + serviceName );

            // TODO: 16.01.2018 check if the service name an Error has or not , and act upon !
            if (serviceName!= null && !serviceName.startsWith( "Error" )) {
                try {
                    // get the reference of the Remote Object which will here represent the Stub
                    controllerInter = (ControllerInterface) Naming.lookup( serviceName );
//                  System.out.println( "connected to TCP and got STUB with service name " + serviceName );
                    if(controllerInter != null){
                        // make the model of the client identical to the Server Model
                        clientModel.getLibrary().setList( controllerInter.getModel().getLibrary().getList() );
                        clientModel.getPlaylist().setList(controllerInter.getModel().getPlaylist().getList());

                        // Register the client as a Remote object with the name of the user
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

            // remove the client from the registry if its window was closed
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

        // using this statement to change something in the view from outside the application thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    clientModel.getLibrary().clear();
                    clientModel.getLibrary().setList( model.getLibrary().getList() );
                    //System.out.printf( "updating lists" );
                    clientModel.getPlaylist().clear();
                    clientModel.getPlaylist().setList( model.getPlaylist().getList() );
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });

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