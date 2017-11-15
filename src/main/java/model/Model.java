package model;

import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Model {
    //This has the Library songs
    private Playlist library ;
    //This has the Playlist songs
    private Playlist playlist;

    public Model() throws RemoteException {
        this.library = new Playlist();
        this.playlist = new Playlist();

    }

    //Returns songs of the Library
    public Playlist getLibrary() {
        return library;
    }
  
    //Returns songs of the playlist
    public Playlist getPlaylist() {
        return playlist;
    }
}
