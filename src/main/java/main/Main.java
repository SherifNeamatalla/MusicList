package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.View;

public class Main extends Application {

    public static void Main(String[]args){
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage window = primaryStage;
        View ourView = new View();
        Scene x= new Scene(ourView );
        window.setScene(x);
        window.show();

    }
}
