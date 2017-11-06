package model;

import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Model {
    private Playlist library ;
    private Playlist playlist;

    public Model() throws RemoteException {
        //Making a List of Songs
        ArrayList<interfaces.Song> uploadedSongs = new ArrayList<>();

        this.library = new Playlist();
        this.playlist = new Playlist();

        FileInputStream file = null;
        File folder = new File("songs");
        File[] files = folder.listFiles();
        for (File audio : files) {
            try {
                file = new FileInputStream(audio);
                file.skip(audio.length() - 128);
                byte[] tagsStart = new byte[128];
                file.read(tagsStart);
                String tags = new String(tagsStart);

                if (tags.substring(0, 3).equals("TAG")) {
                    Song i = new Song(audio.toURI().toString(), tags, uploadedSongs.size());
                    uploadedSongs.add(i);
                } else{//if the Song does not have Metaata
                    String path = audio.getPath();
                    //read only mp3 files
                    if (path.substring(path.length()-3).equals("mp3")){
                        Song i = new Song(audio.toURI().toString(), uploadedSongs.size(), path.substring(6,path.length()-4));
                        uploadedSongs.add(i);
                    }
                }
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Passing the made List of Songs to the Library
        library.setList(uploadedSongs);


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
