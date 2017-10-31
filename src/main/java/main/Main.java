package main;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import view.View;

import java.rmi.RemoteException;

public class Main extends Application {

    public static void Main(String[]args) throws RemoteException {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Instanciating the Model, View and the Controller
        Model model = new Model();
        View ourView = new View();
        Controller cont = new Controller(model,ourView);

        //Linking the Model with the View









        Stage window = primaryStage;
        Scene x= new Scene(ourView );
        window.setScene(x);
        window.show();

    }
}
