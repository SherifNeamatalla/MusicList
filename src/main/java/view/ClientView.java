package view;

import interfaces.Song;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.rmi.RemoteException;

public class ClientView extends BorderPane {
    private FlowPane subWindow = new FlowPane( Orientation.VERTICAL);

    private ListView<Song> library = new ListView<Song>();
    private ListView<Song> playlist = new ListView<Song>();
    private Label title = new Label("Title");
    private Label interpret = new Label("Interpret");
    private Label album = new Label("Album");
    private TextField textTitle = new TextField();
    private TextField textInterpret = new TextField();
    private TextField textAlbum = new TextField();
    private Label duration = new Label( "Duration:" );
    private Label actualTime = new Label( "must show actual time" );

    private Button play = new Button("\u25B6");
    private Button pause = new Button("\u23F8");
    private Button next = new Button("\u2192");
    private Button remove = new Button("Remove");
    private Button commit = new Button("Commit");
    private Button add = new Button("Add to Playlist");
    private Button addAll = new Button("Add All");
    private Button removeAll = new Button("Remove All");


    public ClientView() throws RemoteException {

        //create an Hbox to hold the upper components
        HBox top = new HBox();
        HBox bottom = new HBox();
        HBox controller = new HBox();
        HBox time = new HBox(  );

        //set Size for all the components
        setPrefSize(900, 600);
        top.setPrefSize(800, 50);
        bottom.setPrefSize(800, 50);
        library.setPrefSize(300, 300);
        playlist.setPrefSize(290, 300);
        playlist.setMaxWidth(300);
        commit.setPrefHeight(40);

        //setting spaces between the buttons of each HBox
        top.setSpacing( 5 );
        bottom.setSpacing( 5 );
        controller.setSpacing( 5 );

        //Styling the Buttons
        play.setStyle("-fx-font-size : 20px; -fx-padding : 5px 10px");
        pause.setStyle("-fx-font-size : 20px; -fx-padding : 5px 10px");
        next.setStyle("-fx-font-size : 20px; -fx-padding : 5px 5px; ");

        //Positioning every Element

        addAll.setTranslateY(10);
        removeAll.setTranslateY(10);
        subWindow.setVgap(5);
        subWindow.setTranslateX(-40);
        playlist.setTranslateX(-30);

        controller.getChildren().addAll(play, pause, next, commit);
        time.getChildren().addAll( duration , actualTime );
        time.setSpacing( 10 );
        subWindow.getChildren().addAll(title, textTitle, interpret, textInterpret, album, textAlbum, controller, add,remove , time);
        bottom.getChildren().addAll(addAll,removeAll);
        setTop(top);
        setLeft(library);
        setCenter(playlist);
        setRight(subWindow);
        setBottom(bottom);




        //Initialize the ActionListeners and the ListViews
        initializeListViews();

    }

    public Button getPlay() {
        return play;
    }

    public Button getPause() {
        return pause;
    }

    public Button getNext() {
        return next;
    }

    public Button getRemove() {
        return remove;
    }

    public Button getCommit() {
        return commit;
    }

    public Button getAdd() {
        return add;
    }

    public Button getAddAll() {
        return addAll;
    }

    public Button getRemoveAll() {
        return removeAll;
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