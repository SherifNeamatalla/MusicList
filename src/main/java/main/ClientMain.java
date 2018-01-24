package main;


import controller.ClientController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import view.ClientView;


public class ClientMain extends Application  {



    @Override
    public void start(Stage primaryStage) throws Exception {
        // initializing a new View & Model for each Client
        ClientView cv = new ClientView();
        Model cm = new Model();
        // to let the Client interact with the view
        ClientController cc = new ClientController(cm,cv);
        // when the window is closed
        primaryStage.setOnCloseRequest( event -> { cc.setLogOutAction();
            System.exit( 0 );
        } );


        Scene s = new Scene( cv );
        primaryStage.setScene( s );
        primaryStage.show();
    }


}