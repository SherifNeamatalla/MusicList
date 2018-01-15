package main;

import TCP.TCPClient;
import controller.ClientController;
import controller.ServerController;
import interfaces.ControllerInterface;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import view.ClientView;

import java.rmi.Naming;

public class ClientMain extends Application  {



    @Override
    public void start(Stage primaryStage) throws Exception {
        ClientView cv = new ClientView();
        Model cm = new Model();

        ClientController cc = new ClientController(cm,cv);


        String username = cc.getUsername();
        String password = cc.getPassword();

//        TCPClient tcp = new TCPClient( username,password );

        Scene s = new Scene( cv );
        primaryStage.setScene( s );
        primaryStage.show();
    }


}