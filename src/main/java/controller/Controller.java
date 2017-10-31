package controller;

import javafx.event.Event;
import model.Model;
import model.Song;
import view.View;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Controller {
    private static Model model;
    private static View view;
    private static long songId;
    private static int selected;


    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        songId = 0;
        this.link(model,view);
    }

    public void link(Model model, View view) {
        //Setting the Library of the View to be identical with the library in the Model
        view.setLibrary(model.getLibrary());
        view.setPlaylist(model.getPlaylist());

    }

    public static void commitHandle(Event event) throws RemoteException {

       model.getLibrary().add(new Song("1",view.getTextTitle().getText(),view.getTextAlbum().getText(),view.getTextInterpret().getText(),songId));

       songId++;

    }

    public static void addAllHandle(Event event) throws RemoteException {

        for(interfaces.Song s : model.getLibrary().getList()){
            if(model.getPlaylist().findSongByID(s.getId()) == null)
            model.getPlaylist().add(s);
        }







    }

}
