package model;

import interfaces.Song;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;

public class Playlist extends  ModifiableObservableListBase<Song> implements interfaces.Playlist {

   private ArrayList<interfaces.Song> songs = new ArrayList<Song>();

    public Playlist() {

    }

    @Override
    public boolean addSong(interfaces.Song s) throws RemoteException {

        return songs.add(s);
    }

    @Override
    public boolean deleteSong(Song s) throws RemoteException {

        return songs.remove(s);
    }

    @Override
    public boolean deleteSongByID(long id) throws RemoteException {

        Song s = findSongByID(id);

        if(s != null)
        return this.songs.remove(s);

        return false;
    }

    @Override
    public void setList(ArrayList<interfaces.Song> s) throws RemoteException {
        this.songs = s;

    }

    @Override
    public ArrayList<Song> getList() throws RemoteException {
        return this.songs;
    }

    @Override
    public void clearPlaylist() throws RemoteException {
        this.songs.clear();

    }

    @Override
    public int sizeOfPlaylist() throws RemoteException {
        return songs.size();
    }

    @Override
    public Song findSongByPath(String name) throws RemoteException {

        return null;
    }

    @Override
    public Song findSongByID(long id) throws RemoteException {

        for(Song s : this.songs)
        {
            if(s.getId() == id)
                return s;
        }
        return null;
    }


    @Override
    public Song get(int index) throws IndexOutOfBoundsException {
        if(index == this.songs.size())
            throw new IndexOutOfBoundsException();

        return this.songs.get(index);
    }

    @Override
    public int size() {

        return this.songs.size();
    }

    @Override
    protected void doAdd(int index, Song element)throws IndexOutOfBoundsException {

        if(index > this.songs.size())
            throw new IndexOutOfBoundsException();

        this.songs.add(index,element);

    }

    @Override
    protected Song doSet(int index, Song element)throws IndexOutOfBoundsException {

        if(index == this.songs.size())
            throw new IndexOutOfBoundsException();

        this.songs.set(index,element);

        return element;
    }

    @Override
    protected Song doRemove(int index) throws IndexOutOfBoundsException {

        if(index == this.songs.size())
            throw new IndexOutOfBoundsException();

        Song s = this.songs.get(index);

        this.songs.remove(index);

        return s;
    }

}
