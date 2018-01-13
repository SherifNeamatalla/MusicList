package main;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import view.ClientView;

public class ClientMain extends Application  {



    @Override
    public void start(Stage primaryStage) throws Exception {
        ClientView cv = new ClientView();

        Scene s = new Scene( cv );
        primaryStage.setScene( s );
        primaryStage.show();
    }


}
