package model;

import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Model {
    private Playlist library ;
    private Playlist playlist;


    // Making a hardcoded Songs to test the Model
    Song i = new Song("csdsfs","3 Da2at ;)","untiteled","Abou",555);
    Song y = new Song("csdsdfsfd","el3ab yala","untiteled","mo7y",556);

    public Model() throws RemoteException {
        //Making a List of Songs and adding a Songs to it
        ArrayList<interfaces.Song> x = new ArrayList<>();
        x.add(i);
        x.add(y);


        this.library = new Playlist();
        this.playlist = new Playlist();

        //Passing the made List of Songs to the Library
        library.setList(x);
        playlist.setList(library.getList());

    }

    public Playlist getLibrary() {
        return library;
    }

    public void setLibrary(Playlist library) {
        this.library = library;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }



}
