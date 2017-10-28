package view;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import static java.lang.Character.UnicodeBlock.MISCELLANEOUS_TECHNICAL;

public class View extends BorderPane{
   // private BorderPane window = new BorderPane();
    private FlowPane subWindow = new FlowPane(Orientation.VERTICAL);
    private ComboBox box = new ComboBox();
    private Button load = new Button ("Load");
    private Button save = new Button("Save");
    private ListView<String> library = new ListView<>();
    private ListView<String> playlist = new ListView<>();
    private Label title = new Label("Title");
    private Label interpret = new Label("Interpret");
    private Label album = new Label ("Album");
    private TextField textTitle = new TextField();
    private TextField textInterpret = new TextField();
    private TextField textAlbum = new TextField();
    private Button play = new Button("►");
    private Button pause = new Button();
    private Button next = new Button();
    private Button commit = new Button("Commit");
    private Button add = new Button("Add to Playlist");
    private Button addAll = new Button ("Add All");

    public View (){
        //create an Hbox to hold the upper components
        HBox top = new HBox();
        HBox bottom = new HBox();
        HBox controller = new HBox();


        //set Size for all the components
        setPrefSize(800,600);
        top.setPrefSize(800,50);
        box.setPrefSize(250,30);
        load.setPrefSize(75,30);
        save.setPrefSize(75,30);
        bottom.setPrefSize(800,50);
        library.setPrefSize(300,300);
        playlist.setPrefSize(290,300);
        playlist.setMaxWidth(300);
        play.setPrefSize(30,30);
        pause.setPrefSize(30,30);
        next.setPrefSize(30,30);
        commit.setPrefHeight(30);



        //Positioning every Element
        load.setTranslateX(20);
        save.setTranslateX(40);
        addAll.setTranslateY(10);
        subWindow.setVgap(5);
        subWindow.setTranslateX(-40);
        playlist.setTranslateX(-30);

        controller.getChildren().addAll(play,pause,next,commit);
        subWindow.getChildren().addAll(title,textTitle,interpret,textInterpret,album,textAlbum,controller,add);
        top.getChildren().addAll(box,load,save);
        bottom.getChildren().add(addAll);
        setTop(top);
        setLeft(library);
        setCenter(playlist);
        setRight(subWindow);
        setBottom(bottom);






















    }





}
