package controller;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import model.Model;
import model.Playlist;
import model.Song;
import view.View;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Controller {
    private static Model model;
    private static View view;
    private static long songId;
    private static long selected = -1;



    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        songId = 0;
        setSelectedItem();
        this.link(model,view);
    }

    public void link(Model model, View view) {
        //Setting the Library of the View to be identical with the library in the Model
        view.setLibrary(model.getLibrary());
        view.setPlaylist(model.getPlaylist());

    }

    public static void commitHandle(Event event) throws RemoteException {

        if(selected != -1)
        {

            interfaces.Song temp = model.getLibrary().findSongByID(selected);
            int libraryIndex = model.getLibrary().indexOf(temp);
            int playlistIndex = model.getPlaylist().indexOf(temp);
            long id = temp.getId();
            String title = view.getTextTitle().getText();
            String album = view.getTextAlbum().getText();
            String interpret = view.getTextInterpret().getText();
            if(title.length() != 0)
            {
                temp.setTitle(title);
            }
            if(album.length() != 0)
            {

                temp.setAlbum(album);
            }
            if(interpret.length() != 0)
            {

                temp.setInterpret(interpret);
            }

            model.getLibrary().set(libraryIndex,temp);
            if(playlistIndex != -1)
            model.getPlaylist().set(playlistIndex,temp);



        }

    }

    public static void addAllHandle(Event event) throws RemoteException {

        model.getPlaylist().setList(model.getLibrary().getList());

    }



    public static void addHandle(Event event) throws RemoteException

    {

        if(selected != -1)
        {
            interfaces.Song temp = model.getLibrary().findSongByID(selected);


        model.getPlaylist().addSong(temp);

        }


    }

    public static void playHandle(Event event) throws RemoteException
    {
        if(selected != -1)
        {
           Song s = (Song)model.getLibrary().findSongByID(selected);

            s.getMediaPlayer().play();
        }


    }

    public static void setSelectedItem()
    {

        view.getLibrary().setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                selected = view.getLibrary().getSelectionModel().getSelectedItem().getId();
            }
        });
    }

    }


