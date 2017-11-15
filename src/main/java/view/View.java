package view;

import controller.Controller;
import interfaces.Song;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.rmi.RemoteException;

public class View extends BorderPane {
    // private BorderPane window = new BorderPane();
    private FlowPane subWindow = new FlowPane(Orientation.VERTICAL);
    private ComboBox box = new ComboBox();
    private Button load = new Button("Load");
    private Button save = new Button("Save");
    private ListView<Song> library = new ListView<Song>();
    private ListView<Song> playlist = new ListView<Song>();
    private Label title = new Label("Title");
    private Label interpret = new Label("Interpret");
    private Label album = new Label("Album");
    private TextField textTitle = new TextField();
    private TextField textInterpret = new TextField();
    private TextField textAlbum = new TextField();



    private Button play = new Button("\u25B6");
    private Button pause = new Button("||");
    private Button next = new Button("\u2192");
    private Button remove = new Button("Remove");
    private Button commit = new Button("Commit");
    private Button add = new Button("Add to Playlist");
    private Button addAll = new Button("Add All");


    public View() throws RemoteException {

        //create an Hbox to hold the upper components
        HBox top = new HBox();
        HBox bottom = new HBox();
        HBox controller = new HBox();


        //set Size for all the components
        setPrefSize(800, 600);
        top.setPrefSize(800, 50);
        box.setPrefSize(250, 30);
        load.setPrefSize(75, 30);
        save.setPrefSize(75, 30);
        bottom.setPrefSize(800, 50);
        library.setPrefSize(300, 300);
        playlist.setPrefSize(290, 300);
        playlist.setMaxWidth(300);
        commit.setPrefHeight(40);

        //Styling the Buttons
        play.setStyle("-fx-font-size : 20px; -fx-padding : 5px 10px");
        pause.setStyle("-fx-font-size : 20px; -fx-padding : 5px 10px");
        next.setStyle("-fx-font-size : 20px; -fx-padding : 5px 5px; ");

        //Positioning every Element
        load.setTranslateX(20);
        save.setTranslateX(40);
        addAll.setTranslateY(10);
        subWindow.setVgap(5);
        subWindow.setTranslateX(-40);
        playlist.setTranslateX(-30);

        controller.getChildren().addAll(play, pause, next, commit);
        subWindow.getChildren().addAll(title, textTitle, interpret, textInterpret, album, textAlbum, controller, add,remove);
        top.getChildren().addAll(box, load, save);
        bottom.getChildren().add(addAll);
        setTop(top);
        setLeft(library);
        setCenter(playlist);
        setRight(subWindow);
        setBottom(bottom);




        //Initialize the ActionListeners and the ListViews
        initializeListViews();
        initializeActionListeners();

    }



    //Initializes Buttons ActionListeners.
    public void initializeActionListeners()
    {
        commit.setOnAction(event -> {
            try {
                Controller.commitHandle(event);
            } catch (RemoteException e) {


            }
        });

        next.setOnAction(event -> {
            try {
                Controller.nextHandle(event);
            } catch (RemoteException e) {


            }
        });

        pause.setOnAction(event -> {
            try {
                Controller.pauseHandle(event);
            } catch (RemoteException e) {


            }
        });

        addAll.setOnAction(event -> {

            try {
                Controller.addAllHandle(event);

            } catch (RemoteException e) {

            }


        });

        add.setOnAction(event -> {
            try {
                Controller.addHandle(event);
            } catch (RemoteException e) {

            }
        });

        play.setOnAction(event -> {
            try {
                Controller.playHandle(event);
            } catch (RemoteException e) {

            }
        });

        remove.setOnAction(event -> {
            try {
                Controller.removeHandle(event);
            } catch (RemoteException e) {

            }
        });

    }

    public void initializeListViews()
    {
        library.setCellFactory(c -> {
            return new ListCell<Song>() {
                @Override
                public void updateItem(Song item, boolean x) {
                    super.updateItem(item, x);
                    if (x)
                        setText(" ");
                    else {

                        setText(String.format("%02d",item.getId())+ " "+getName(item));

                    }

                }
            };
        });

        playlist.setCellFactory(c1 -> {
            return new ListCell<Song>() {
                @Override
                public void updateItem(Song item, boolean x) {
                    super.updateItem(item, x);

                    if (x) {
                        setText(" ");

                    } else {


                        setText(String.format("%02d",item.getId())+ " "+getName(item));

                    }

                }
            };


        });
    }
    // a Static method so we can pass the title to the List in line 85
    public static String getName (Song song){
        return song.getTitle();
    }

    public void setLibrary(model.Playlist library1){
        library.setItems(library1);
    }
    public ListView<Song> getLibrary() {
        return library;
    }

    public void setPlaylist(model.Playlist pList){
        playlist.setItems(pList);
    }
    public ListView<Song> getPlaylist() {
        return playlist;
    }

    public Label getTitle() {
        return title;
    }
    public void setTitle(Label title) {
        this.title = title;
    }

    public Label getInterpret() {
        return interpret;
    }
    public void setInterpret(Label interpret) {
        this.interpret = interpret;
    }

    public Label getAlbum() {
        return album;
    }
    public void setAlbum(Label album) {
        this.album = album;
    }

    public TextField getTextTitle() {
        return textTitle;
    }
    public void setTextTitle(TextField textTitle) {
        this.textTitle = textTitle;
    }

    public TextField getTextInterpret() {
        return textInterpret;
    }
    public void setTextInterpret(TextField textInterpret) {
        this.textInterpret = textInterpret;
    }

    public TextField getTextAlbum() {
        return textAlbum;
    }
    public void setTextAlbum(TextField textAlbum) {
        this.textAlbum = textAlbum;
    }


}
