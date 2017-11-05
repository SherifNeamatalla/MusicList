package controller;


import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import model.Model;
import model.Song;
import view.View;

import java.rmi.Remote;
import java.rmi.RemoteException;


public class Controller {

    private static Model model;
    private static View view;
    // When selected = -1, that means that nothing is selected, else this represents Id of the selected song
    private static long selected = -1;



    public Controller(Model model, View view) {

        this.model = model;
        this.view = view;
        //Links the Model with the View
        this.link(model,view);
        //Initializes the Actionlisteners of each respective Listview.
        setSelectedItemLibrary();

    }

    public void link(Model model, View view) {
        //Links the Library and Playlist of the View with the Library and Playlist in the Model.
        view.setLibrary(model.getLibrary());
        view.setPlaylist(model.getPlaylist());

    }

    public static void commitHandle(Event event) throws RemoteException {

        if(selected != -1) //If something is selected
        {

            interfaces.Song temp = model.getLibrary().findSongByID(selected);

            //Indices of the Song in both playlist and library, to be able to access it and change it later.
            int libraryIndex = model.getLibrary().indexOf(temp);
            int playlistIndex = model.getPlaylist().indexOf(temp);

            long id = temp.getId();
            //The data of the song to be changed
            String title = view.getTextTitle().getText();
            String album = view.getTextAlbum().getText();
            String interpret = view.getTextInterpret().getText();

            //make sure that the string is not empty
            if(!title.trim().isEmpty())
            {
                temp.setTitle(title);
            }
            if(!album.trim().isEmpty())
            {

                temp.setAlbum(album);
            }
            if(!interpret.trim().isEmpty())
            {

                temp.setInterpret(interpret);
            }

            // Change the song with the new Song with the new data.
            model.getLibrary().set(libraryIndex,temp);

            //If this song is in the playlist change it too to be up to date with the Library
            if(playlistIndex != -1)
            model.getPlaylist().set(playlistIndex,temp);



        }

    }

    //ActionListener of addAll Button.
    public static void addAllHandle(Event event) throws RemoteException {

        model.getPlaylist().setList(model.getLibrary().getList());

    }



    //ActionListener of add Button.
    public static void addHandle(Event event) throws RemoteException

    {

        if(selected != -1)
        {
            interfaces.Song temp = model.getLibrary().findSongByID(selected);
            model.getPlaylist().addSong(temp);

        }


    }

    public static void nextHandle(Event event) throws RemoteException
    {
        if(selected != -1)
        {
            selected++;
            Song s = (Song)model.getLibrary().findSongByID(selected);

            if(s != null) {
                s.getMediaPlayer().stop();



                playHelper();

            }


        }
    }

    //ActionListener of play Button.
    public static void playHandle(Event event) throws RemoteException {


        for(interfaces.Song s : model.getLibrary().getList())
        {
            if(s.getId() != selected) {
                Song temp = (Song) s;
                temp.getMediaPlayer().stop();
            }
        }
        if(selected != -1) {
            playHelper();
        }

    }

    public static void pauseHandle(Event event) throws RemoteException
    {

        if(selected != -1) {

            Song s = (Song) model.getLibrary().findSongByID(selected);
            System.out.println(s.getTitle());
            s.getMediaPlayer().pause();


        }
    }

    public static void playHelper() throws RemoteException{
        Song s = (Song)model.getLibrary().findSongByID(selected);
        MediaPlayer temp = s.getMediaPlayer();
        temp.play();
        temp.setOnEndOfMedia( () -> {
            selected ++;

            try {
                playHelper();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

    }



    //ActionListener of the Library inside view.
    public static void setSelectedItemLibrary()
    {

        view.getLibrary().setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                //Id of selected song.
                if(view.getLibrary().getSelectionModel().getSelectedItem() != null)
                    selected = view.getLibrary().getSelectionModel().getSelectedItem().getId();

                if(selected !=-1){ //If item is selected
                    try {

                        view.getTextTitle().setText(model.getLibrary().findSongByID(selected).getTitle());
                        view.getTextInterpret().setText(model.getLibrary().findSongByID(selected).getInterpret());
                        view.getTextAlbum().setText(model.getLibrary().findSongByID(selected).getAlbum());

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    }


