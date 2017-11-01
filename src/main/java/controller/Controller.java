package controller;


import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import model.Model;
import model.Song;
import view.View;
import java.rmi.RemoteException;


public class Controller {

    private static Model model;
    private static View view;
    // When selected = -1, that means that nothing is selected, else this represents Id of the selected song
    private static long selected = -1;



    public Controller(Model model, View view) {

        this.model = model;
        this.view = view;
        //Links the Model with the View
        this.link(model,view);
        //Initializes the Actionlisteners of each respective Listview.
        setSelectedItemLibrary();

    }

    public void link(Model model, View view) {
        //Links the Library and Playlist of the View with the Library and Playlist in the Model.
        view.setLibrary(model.getLibrary());
        view.setPlaylist(model.getPlaylist());

    }

    public static void commitHandle(Event event) throws RemoteException {

        if(selected != -1) //If something is selected
        {

            interfaces.Song temp = model.getLibrary().findSongByID(selected);

            //Indices of the Song in both playlist and library, to be able to access it and change it later.
            int libraryIndex = model.getLibrary().indexOf(temp);
            int playlistIndex = model.getPlaylist().indexOf(temp);

            long id = temp.getId();
            //The data of the song to be changed
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
    public static void addHandle(Event event) throws RemoteException

    {

        if(selected != -1)
        {
            interfaces.Song temp = model.getLibrary().findSongByID(selected);


        model.getPlaylist().addSong(temp);

        }


    }


    //ActionListener of play Button.
    public static void playHandle(Event event) throws RemoteException
    {
        if(selected != -1)
        {
           Song s = (Song)model.getLibrary().findSongByID(selected);

            s.getMediaPlayer().play();
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
                selected = view.getLibrary().getSelectionModel().getSelectedItem().getId();

                if(selected !=-1){ //If item is selected
                    try {

                        view.getTextTitle().setText(model.getLibrary().findSongByID(selected).getTitle());
                        view.getTextInterpret().setText(model.getLibrary().findSongByID(selected).getInterpret());
                        view.getTextAlbum().setText(model.getLibrary().findSongByID(selected).getAlbum());

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    }


