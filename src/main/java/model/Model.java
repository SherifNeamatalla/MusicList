package model;


import java.io.Serializable;
import java.rmi.RemoteException;


public class Model implements Serializable{
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
