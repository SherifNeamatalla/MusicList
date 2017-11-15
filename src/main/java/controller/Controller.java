package controller;


import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import model.Model;
import model.Song;
import view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;


public class Controller {

    private static Model model;
    private static View view;
    // When selected = -1, that means that nothing is selected, else this represents Id of the selected song
    private static long selectedIdLibrary = -1;
    private static Song selectedSongPlaylist = null;
    private static Song playedSongPlaylist = null;
     private ArrayList<interfaces.Song> uploadedSongs = new ArrayList<>();



    public Controller(Model model, View view) throws RemoteException {

        this.model = model;
        importSongs();
        this.view = view;
        //Initializes the Actionlisteners of each respective Listview.
        setSelectedItemLibrary();
        setSelectedItemPlaylist();

    }

    public void link(Model model, View view) {
        //Links the Library and Playlist of the View with the Library and Playlist in the Model.
        view.setLibrary(model.getLibrary());
        view.setPlaylist(model.getPlaylist());

    }

    public void importSongs() throws RemoteException {
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
        model.getLibrary().setList(uploadedSongs);
    }
    public static void commitHandle(Event event) throws RemoteException {

        if(selectedIdLibrary != -1) //If something is selected
        {
            //get the selected Song
            Song temp = (Song) model.getLibrary().findSongByID(selectedIdLibrary);

            //Indices of the Song in both playlist and library, to be able to access it and change it later.
            int libraryIndex = model.getLibrary().indexOf(temp);
            int playlistIndex = model.getPlaylist().indexOf(temp);

            //get the id of the selected Song
            long id = temp.getId();

            //get the new Data from the text fields in the view
            String title = view.getTextTitle().getText();
            String album = view.getTextAlbum().getText();
            String interpret = view.getTextInterpret().getText();

            //make sure that the string is not empty
            if(!title.trim().isEmpty())
                temp.setTitle(title);
            if(!album.trim().isEmpty())
                temp.setAlbum(album);
            if(!interpret.trim().isEmpty())
                temp.setInterpret(interpret);

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
    public static void addHandle(Event event) throws RemoteException {

        //Check if the user selects an Empty Cell
        if(selectedIdLibrary != -1) {
            Song temp = (Song) model.getLibrary().findSongByID(selectedIdLibrary);
            model.getPlaylist().addSong(temp);
        }
    }


    //ActionListener of play Button.
    // to play the Song we must stop the played song first then play the new Song
    public static void playHandle(Event event) throws RemoteException {

        // if there is a Song that already played, it must be stopped first
        if (playedSongPlaylist != null && playedSongPlaylist != selectedSongPlaylist){
            playedSongPlaylist.getMediaPlayer().stop();
        }

        if(selectedSongPlaylist != null) {
            playedSongPlaylist = selectedSongPlaylist;
            playHelper();
        }
    }

    /* Method that is responsible for playing the songs
     * it calls itself once the played Song finishes
     */
    public static void playHelper() throws RemoteException {
        Song s = playedSongPlaylist;
        if (s != null) {
            MediaPlayer temp = s.getMediaPlayer();
            temp.play();

            temp.setOnEndOfMedia(() -> {
                try {
                    autoChange();
                    playHelper();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });

        }
    }

    //Action listener for the Next button
    /*first it gets the actual Song
     *change to the next with autoChange method
     * play the next Song with playHelper
     */
    public static void nextHandle(Event event) throws RemoteException
    {
        if(playedSongPlaylist != null)
        {
            Song s = (Song)playedSongPlaylist;
            autoChange();
            s.getMediaPlayer().stop();
            playHelper();
        }
    }


    /*change to the next Song
     * this Method is called whenever the song Changes(Next Button, the current Song finishes)
     */
    public static void autoChange (){

        int indexOfNext = model.getPlaylist().indexOf(playedSongPlaylist)+1;
        // if the Song is the last one -> change it to the First one
        if (indexOfNext == model.getPlaylist().size()) {
            view.getPlaylist().getSelectionModel().selectFirst();
            playedSongPlaylist = (Song) view.getPlaylist().getSelectionModel().getSelectedItem();
            selectedSongPlaylist = playedSongPlaylist;

        }
        else {
            view.getPlaylist().getSelectionModel().select(model.getPlaylist().get(indexOfNext));
            playedSongPlaylist = (Song) view.getPlaylist().getSelectionModel().getSelectedItem();
            selectedSongPlaylist = playedSongPlaylist;
        }
    }


    //Action listener to the Pause button
    public static void pauseHandle(Event event) throws RemoteException
    {
        if(playedSongPlaylist != null)
            playedSongPlaylist.getMediaPlayer().pause();
    }


    /*Action listener to the Remove button
     * if the removed Song is the played one, stop it first than remove it from the model
     */
    public static void removeHandle(Event event) throws RemoteException {
        if (playedSongPlaylist == selectedSongPlaylist && selectedSongPlaylist != null){
            playedSongPlaylist.getMediaPlayer().stop();
            model.getPlaylist().remove(playedSongPlaylist);
            selectedSongPlaylist = null;
            playedSongPlaylist = null;
            view.getPlaylist().getSelectionModel().select(null);
        }
        else if(selectedSongPlaylist != null)
        {
            model.getPlaylist().remove(selectedSongPlaylist);
            selectedSongPlaylist = playedSongPlaylist;
            view.getPlaylist().getSelectionModel().select(playedSongPlaylist);
        }

    }

    //ActionListener of the Library inside view.
    public static void setSelectedItemLibrary()
    {
        view.getLibrary().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Id of selected song.
                if(view.getLibrary().getSelectionModel().getSelectedItem() != null)
                    selectedIdLibrary = view.getLibrary().getSelectionModel().getSelectedItem().getId();

                // if an item is selected, show its data in the Text fields in the view
                if(selectedIdLibrary !=-1){
                    try {

                        view.getTextTitle().setText(model.getLibrary().findSongByID(selectedIdLibrary).getTitle());
                        view.getTextInterpret().setText(model.getLibrary().findSongByID(selectedIdLibrary).getInterpret());
                        view.getTextAlbum().setText(model.getLibrary().findSongByID(selectedIdLibrary).getAlbum());

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    public static void setSelectedItemPlaylist() throws RemoteException
    {
        view.getPlaylist().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (view.getPlaylist().getSelectionModel().getSelectedItem() != null)
                    selectedSongPlaylist = (Song) view.getPlaylist().getSelectionModel().getSelectedItem();
            }
        });
    }
}