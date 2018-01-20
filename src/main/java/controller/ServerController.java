package controller;

import TCP.TCPServer;
import UDP.UDPServer;
import interfaces.ClientControllerInterface;
import interfaces.ControllerInterface;
import javafx.scene.media.MediaPlayer;
import model.Model;
import model.Song;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServerController extends UnicastRemoteObject implements ControllerInterface {

    private Model model ;
    private Song playedSongPlaylist = null;
    private Song selectedSongPlaylist = null;
    private MediaPlayer mediaPlayer = null;
    private ArrayList<interfaces.Song> uploadedSongs = new ArrayList<>( );
    private long selectedIdLibrary = -1;
    private ArrayList<String> connectedClients ;
    private ClientControllerInterface clientUpdater ;
    UDPServer udp = null;

    public ServerController(Model model) throws RemoteException {
        this.model = model;
        importSongs();
    }

    private void importSongs() throws RemoteException {
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
        model.getLibrary().setList( uploadedSongs );
    }


    @Override
    public void play(long id) throws RemoteException {
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
        //new UDPServer(mediaPlayer).start();

    }

    private void playHelper() throws RemoteException {
        model.Song s = playedSongPlaylist;
        if (s != null) {
            if (mediaPlayer == null)
                mediaPlayer = new MediaPlayer(playedSongPlaylist.getMedia());

            mediaPlayer.play();
            if (udp == null) {
                udp = new UDPServer(mediaPlayer);
                udp.start();
            }
            else
                udp.setMp(mediaPlayer);

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

    private void autoChange() throws RemoteException {

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
        if(mediaPlayer != null)
            mediaPlayer.pause();

    }

    @Override
    public void next() throws RemoteException {
        if (playedSongPlaylist != null) {
//                Song s = (Song) playedSongPlaylist;
            autoChange();
            //               mediaPlayer.stop();
            //               mediaPlayer = null;
            try {
                playHelper();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }

    }

    @Override
    public void commit(String title, String interpret, String album, long id) throws RemoteException, MalformedURLException, NotBoundException {
        selectedIdLibrary = id;
        if(selectedIdLibrary != -1) //If something is selected
        {
            try {
                //get the selected Song
                Song temp = (Song) model.getLibrary().findSongByID(selectedIdLibrary);

                //Indices of the Song in both playlist and library, to be able to access it and change it later.
                int libraryIndex = model.getLibrary().indexOf(temp);
                int playlistIndex = model.getPlaylist().indexOf(temp);


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
        this.update();

    }

    @Override
    public void add(long id) throws RemoteException, MalformedURLException, NotBoundException {
        selectedIdLibrary = id;
        if(selectedIdLibrary != -1) {
            try {
                Song temp = (Song) model.getLibrary().findSongByID(selectedIdLibrary);
                model.getPlaylist().addSong(temp);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }
        this.update();

    }

    @Override
    public void remove(long id) throws RemoteException, MalformedURLException, NotBoundException {
        selectedSongPlaylist = (Song) model.getPlaylist().findSongByID( id );

        if (playedSongPlaylist == selectedSongPlaylist && selectedSongPlaylist != null){
            mediaPlayer.stop();
            model.getPlaylist().remove(playedSongPlaylist);
            mediaPlayer = null;
            selectedSongPlaylist = null;
            playedSongPlaylist = null;
        }
        else if(selectedSongPlaylist != null)
        {
            model.getPlaylist().remove(selectedSongPlaylist);
            selectedSongPlaylist = playedSongPlaylist;
        }
        //update all clients with the new view for the playlist
        this.update();
    }



    @Override
    public void addAll() throws RemoteException, MalformedURLException, NotBoundException {

        try {
            model.getPlaylist().setList(model.getLibrary().getList());
            this.update();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void removeAll() throws RemoteException, MalformedURLException, NotBoundException {
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
        this.update();

    }

    @Override
    public void update() throws RemoteException, NotBoundException, MalformedURLException {
        connectedClients = TCPServer.getUsers();
        for (String s: connectedClients) {
            clientUpdater = (ClientControllerInterface) Naming.lookup( s );
            clientUpdater.modelUpdater( this.model );
        }

    }

    @Override
    public void logOut(String user) throws RemoteException {
        if(TCPServer.getUsers().contains( user ) && user!=null) {
            TCPServer.getUsers().remove( user );
        }
    }

    @Override
    public Model getModel() throws RemoteException {
        return model;
    }

}
