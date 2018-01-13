package controller;

import interfaces.ControllerInterface;
import model.Model;
import view.ClientView;


import java.rmi.RemoteException;

public class ClientController {


    Model model;
    ClientView view;
    String username,password;
    ControllerInterface controllerInter;

    public ClientController(Model model, ClientView view, ControllerInterface controllerInter) throws RemoteException {

        this.model = model;
        this.view = view;
        link(model,view);
        //Initializes the Actionlisteners of Login and Clear buttons in ClientView
        setActionListeners();
        this.controllerInter = controllerInter;

    }
    public void link(Model model, ClientView view) {
        //Links the Library and Playlist of the View with the Library and Playlist in the Model.
        view.setLibrary(model.getLibrary());
        view.setPlaylist(model.getPlaylist());

    }

    public void setActionListeners()
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

    public void setLoginAction() {

        view.getLoginButton().setOnAction(e -> {

            username = view.getUsernameField().getText();
            password = view.getPasswordField().getText();


        });
    }

    public void setClearAction() {

        view.getClearButton().setOnAction(e -> {
            view.getUsernameField().setText("");
            view.getPasswordField().setText("");

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
                controllerInter.update();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }

    public void setPauseAction() {
        view.getPause().setOnAction( event -> {
            try {
                controllerInter.pause();
                controllerInter.update();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }

    public  void setNextAction() {
        view.getNext().setOnAction( event -> {
            try {
                controllerInter.next();
                controllerInter.update();

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
                controllerInter.update();

            } catch (RemoteException e) {
                e.printStackTrace();
            }


        } );
    }

    public void setAddAction() {
        view.getAdd().setOnAction( event -> {
            try {
                controllerInter.add( view.getLibrary().getSelectionModel().getSelectedItem().getId() );
                controllerInter.update();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }

    public void setRemoveAction() {
        view.getRemove().setOnAction( event -> {
            try {

                controllerInter.remove( view.getPlaylist().getSelectionModel().getSelectedItem().getId() );
                controllerInter.update();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }

    public void setAddAllAction() {
        view.getAddAll().setOnAction( event -> {
            try {
                controllerInter.addAll();
                controllerInter.update();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }
    public void setRemoveAllAction() {
        view.getRemoveAll().setOnAction( event -> {
            try {
                controllerInter.removeAll();
                controllerInter.update();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } );
    }



}
