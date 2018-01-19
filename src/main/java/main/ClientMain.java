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
        ClientView cv = new ClientView();
        Model cm = new Model();

        ClientController cc = new ClientController(cm,cv);

        primaryStage.setOnCloseRequest( event -> { cc.setLogOutAction();
            System.exit( 0 );
        } );


        Scene s = new Scene( cv );
        primaryStage.setScene( s );
        primaryStage.show();
    }


}