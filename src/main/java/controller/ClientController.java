package controller;

import interfaces.ControllerInterface;
import model.Model;
import view.ClientView;


import java.rmi.RemoteException;

public class ClientController {


    Model model;
    ClientView view;
    String username,password;
    ControllerInterface testing;

    public ClientController(Model model, ClientView view, ControllerInterface testing) throws RemoteException {

        this.model = model;
        this.view = view;
        link(model,view);
        //Initializes the Actionlisteners of Login and Clear buttons in ClientView
        setActionListeners();
        this.testing = testing;

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
        setPlay();

    }

    public void setLoginAction() {

        view.getLoginButton().setOnAction(e -> {

            username = view.getUsernameField().getText();
            password = view.getPasswordField().getText();


        });
    }

    public void setClearAction() {

        view.getClearButton().setOnAction(e -> {
            view.getUsernameField().setText(" ");
            view.getPasswordField().setText(" ");

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


    public void setPlay() {
        view.getPlay().setOnAction( event -> {
            try {
                System.out.println("h");
                testing.play( (int) view.getPlaylist().getSelectionModel().getSelectedItem().getId() );
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        } );
    } 
}
