package controller;


import interfaces.SerializableStrategy;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private static MediaPlayer mediaPlayer;
    private ArrayList<interfaces.Song> uploadedSongs = new ArrayList<>();
    //This variable holds the value of the chosen Serializing mode
    private static String serMode = null;




    public Controller(Model model, View view) throws RemoteException {

        this.model = model;
        importSongs();
        this.view = view;
        //Initializes the Actionlisteners of each respective Listview.
        setLists();
        setActionListeners();

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
                Song i = new Song(audio.toURI().toString(), tags);
                //if id == -1, that means we reached maximum number of songs.
                if (i.getId() != -1) {
                    uploadedSongs.add(i);
                }
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Passing the made List of Songs to the Library
        model.getLibrary().setList(uploadedSongs);
    }



    public void setActionListeners()
    {
        setPlayAction();
        setPauseAction();
        setNextAction();
        setCommitAction();
        setAddPlayListAction();
        setRemoveAction();
        setAddAllAction();
        setRemoveAllAction();
        setSaveAction();
        setLoadAction();
        setBoxAction();
    }


    public void setLists() throws RemoteException {
        setSelectedItemLibrary();
        setSelectedItemPlaylist();
    }

    //ActionListener of play Button.
    // to play the Song we must stop the played song first then play the new Song
    public void setPlayAction()
    {
        view.getPlay().setOnAction(e-> {
            // if there is a Song that already played, it must be stopped first
            if (playedSongPlaylist != null && playedSongPlaylist != selectedSongPlaylist){
                mediaPlayer.stop();
                mediaPlayer = null;
            }

            if(selectedSongPlaylist != null) {
                playedSongPlaylist = selectedSongPlaylist;
                try {
                    playHelper();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }


    /* Method that is responsible for playing the songs
     * it calls itself once the played Song finishes
     */
    public void playHelper() throws RemoteException {
        Song s = playedSongPlaylist;
        if (s != null) {
            if (mediaPlayer == null)
                mediaPlayer = new MediaPlayer(playedSongPlaylist.getMedia());

            mediaPlayer.play();

            mediaPlayer.setOnEndOfMedia(() -> {
                try {
                    autoChange();
                    playHelper();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });

        }
    }


    /*change to the next Song
     * this Method is called whenever the song Changes(Next Button, the current Song finishes)
     */
    public void autoChange(){

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
    public void setPauseAction()
    {
        view.getPause().setOnAction(e->{
            if(mediaPlayer != null)
                mediaPlayer.pause();
        });
    }

    //Action listener for the Next button
    /*first it gets the actual Song
     *change to the next with autoChange method
     * play the next Song with playHelper
     */
    public void setNextAction() {

        view.getNext().setOnAction(e -> {
            if (playedSongPlaylist != null) {
                Song s = (Song) playedSongPlaylist;
                autoChange();
                mediaPlayer.stop();
                mediaPlayer = null;
                try {
                    playHelper();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }


    /*Action listener to the Remove button
    * if the removed Song is the played one, stop it first than remove it from the model
    */
    public void setRemoveAction()
    {
        view.getRemove().setOnAction(e->{
            if (playedSongPlaylist == selectedSongPlaylist && selectedSongPlaylist != null){
                mediaPlayer.stop();
                model.getPlaylist().remove(playedSongPlaylist);
                mediaPlayer = null;
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
        });
    }

    public void setCommitAction() {
        view.getCommit().setOnAction(e->{
            if(selectedIdLibrary != -1) //If something is selected
            {
                try {
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
                    if (!title.trim().isEmpty())
                        temp.setTitle(title);
                    if (!album.trim().isEmpty())
                        temp.setAlbum(album);
                    if (!interpret.trim().isEmpty())
                        temp.setInterpret(interpret);

                    // Change the song with the new Song with the new data.
                    model.getLibrary().set(libraryIndex, temp);

                    //If this song is in the playlist change it too to be up to date with the Library
                    if (playlistIndex != -1)
                        model.getPlaylist().set(playlistIndex, temp);
                }
                catch(RemoteException e1)
                {
                    e1.printStackTrace();
                }
            }

        });
    }



    //ActionListener of addAll Button.
    public void setAddAllAction()
    {
        view.getAddAll().setOnAction(e-> {
                try {
                    model.getPlaylist().setList(model.getLibrary().getList());
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
        });
    }

    //ActionListener of removeAll Button.
    public void setRemoveAllAction() {
        view.getRemoveAll().setOnAction( event -> {
            //if the playlist isn't empty then removes all songs
            if (!model.getPlaylist().isEmpty()) {
                //check if there is a song being played then stop it
                if(playedSongPlaylist != null){
                    mediaPlayer.stop();
                }
                try {
                    playedSongPlaylist = null;
                    selectedSongPlaylist = null;
                    mediaPlayer = null;
                    model.getPlaylist().clearPlaylist();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    //ActionListener of add Button.
    public void setAddPlayListAction()
    {
        view.getAdd().setOnAction(e-> {
                if(selectedIdLibrary != -1) {
                    try {
                        Song temp = (Song) model.getLibrary().findSongByID(selectedIdLibrary);
                        model.getPlaylist().addSong(temp);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
        });
    }


    public void setSaveAction() {
        view.getSave().setOnAction( event -> {
            SerializableStrategy strategy;


            if(serMode != null) {
                switch (serMode) {
                    case "Binary":
                        strategy = new BinaryStrategy();
                        break;
                    case "XML":
                        strategy = new XML();
                        break;
                    case "JDBC":
                        strategy = new JDBC();
                        break;
                    case "OpenJPA":
                        strategy = new OpenJPA();
                        break;
                    default:
                        strategy = null;
                }
            }
            else {

                strategy = null;
            }
            if(strategy != null) {
                try {

                    strategy.openWritableLibrary();
                    strategy.writeLibrary(model.getLibrary());
                    strategy.closeWritableLibrary();

                    strategy.openWritablePlaylist();
                    strategy.writePlaylist(model.getPlaylist());
                    strategy.closeWritablePlaylist();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } );
    }

    public  void setLoadAction() {
        view.getLoad().setOnAction( event -> {
            SerializableStrategy strategy;


            if(serMode != null) {
                switch (serMode) {
                    case "Binary":
                        strategy = new BinaryStrategy();
                        break;
                    case "XML":
                        strategy = new XML();
                        break;
                    case "JDBC":
                        strategy = new JDBC();
                        break;
                    case "OpenJPA":
                        strategy = new OpenJPA();
                        break;
                    default:
                        strategy = null;
                }
            }
            else {
                strategy = null;
            }
            if(strategy != null) {
                try {
                    if(mediaPlayer != null) mediaPlayer.stop();
                    strategy.openReadableLibrary();
                    interfaces.Playlist s = strategy.readLibrary();
                    //If s == null, then that means we did not save.
                    if(s != null) {
                        model.getLibrary().clearPlaylist();
                        model.getLibrary().setList(s.getList());
                    }
                    strategy.closeReadableLibrary();

                    strategy.openReadablePlaylist();
                    interfaces.Playlist sP = strategy.readPlaylist();
                    //If sP == null, then that means we did not save.
                    if(sP != null) {
                        model.getPlaylist().clearPlaylist();
                        model.getPlaylist().setList(sP.getList());
                    }
                    strategy.closeReadablePlaylist();


                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } );
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



    public void setBoxAction()
    {
        view.getBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {

                serMode = (String)view.getBox().getSelectionModel().getSelectedItem();

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
    public static Model getModel() {
        return model;
    }


    public static View getView() {
        return view;
    }
}