package controller;

import interfaces.ControllerInterface;
import javafx.scene.media.MediaPlayer;
import model.Model;
import model.Song;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServerController extends UnicastRemoteObject implements ControllerInterface {

    private Model model ;
    private Song playedSongPlaylist = null;
    private Song selectedSongPlaylist = null;
    private MediaPlayer mediaPlayer = null;
    private ArrayList<interfaces.Song> uploadedSongs = new ArrayList<>(  );


    public ServerController(Model model) throws RemoteException {
        this.model = model;
        importSongs();

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


    @Override
    public void play(int id) throws RemoteException {
        System.out.println("hhh");
        selectedSongPlaylist = (model.Song) model.getPlaylist().findSongByID( id );

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

    }

    public void playHelper() throws RemoteException {
        model.Song s = playedSongPlaylist;
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

    public void autoChange() throws RemoteException {

        int indexOfNext = model.getPlaylist().indexOf(playedSongPlaylist)+1;
        // if the Song is the last one -> change it to the First one
        if (indexOfNext == model.getPlaylist().size()) {
//            view.getPlaylist().getSelectionModel().selectFirst();
//            playedSongPlaylist = (Song) view.getPlaylist().getSelectionModel().getSelectedItem();
            playedSongPlaylist = (Song) model.getPlaylist().findSongByID( 0 );
            selectedSongPlaylist = playedSongPlaylist;

        }
        else {
//            view.getPlaylist().getSelectionModel().select(model.getPlaylist().get(indexOfNext));
//            playedSongPlaylist = (Song) view.getPlaylist().getSelectionModel().getSelectedItem();
            playedSongPlaylist = (Song) model.getPlaylist().findSongByID( indexOfNext );
            selectedSongPlaylist = playedSongPlaylist;
        }
        mediaPlayer.stop();
        mediaPlayer = null;
    }

    @Override
    public void pause() throws RemoteException {

    }

    @Override
    public void next() throws RemoteException {

    }

    @Override
    public void commit() throws RemoteException {

    }

    @Override
    public void add() throws RemoteException {

    }

    @Override
    public void remove() throws RemoteException {

    }

    @Override
    public void addAll() throws RemoteException {

    }

    @Override
    public void removeAll() throws RemoteException {

    }

    @Override
    public Model getModel() throws RemoteException {
        return model;
    }

}
