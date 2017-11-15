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
        //Making a List of Songs
        ArrayList<interfaces.Song> uploadedSongs = new ArrayList<>();

        this.library = new Playlist();
        this.playlist = new Playlist();


        FileInputStream file = null;
        File folder = new File("songs");
        File[] files = folder.listFiles();

        for (File audio : files) {
            try {
                //Gets the audio file in the file variable
                file = new FileInputStream(audio);
                //Skips till the last 120 bytes that contain the meta data
                file.skip(audio.length() - 128);
                //These two lines putes the last 128 bytes into the tagsStart variable
                byte[] tagsStart = new byte[128];
                file.read(tagsStart);
                //tags variable has the meta data
                String tags = new String(tagsStart);


                if (tags.substring(0, 3).equals("TAG")) {
                    Song i = new Song(audio.toURI().toString(), tags, uploadedSongs.size());
                    uploadedSongs.add(i);
                } else{//if the Song does not have Metaata
                    String path = audio.getPath();
                    //read only mp3 files
                    if (path.substring(path.length()-3).equals("mp3")){
                        //Initializes the song with the meta data using the meta data constructor
                        Song i = new Song(audio.toURI().toString(), uploadedSongs.size(), path.substring(6,path.length()-4));
                        uploadedSongs.add(i);
                    }
                }
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Passing the made List of Songs to the Library to link them
        library.setList(uploadedSongs);


    }

    //Returns songs of the Library
    public Playlist getLibrary() {
        return library;
    }
    public void setLibrary(Playlist library) {
        this.library = library;
    }

    //Returns songs of the playlist
    public Playlist getPlaylist() {
        return playlist;
    }
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }



}
