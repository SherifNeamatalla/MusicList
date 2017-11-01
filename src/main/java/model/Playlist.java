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

        //If this playlist doesn't already have this song
        if(this.indexOf(s) == -1)
        return this.add(s);

        return false;
    }

    @Override
    public boolean deleteSong(Song s) throws RemoteException {

        return this.remove(s);
    }

    @Override
    public boolean deleteSongByID(long id) throws RemoteException {

        Song s = findSongByID(id);

        if(s != null)
        return this.remove(s);

        return false;
    }

    @Override
    public void setList(ArrayList<interfaces.Song> s) throws RemoteException {


        for(int i = 0;i<s.size();i++)
        {
            //If this playlist doesn't already have this song
            if(this.indexOf(s.get(i)) == -1) {
                this.add(s.get(i));
            }
            else {
                this.set(this.indexOf(s.get(i)), s.get(i));
            }


        }



    }

    @Override
    public ArrayList<Song> getList() throws RemoteException {
        return this.songs;
    }

    @Override
    public void clearPlaylist() throws RemoteException {
        this.clear();

    }

    @Override
    public int sizeOfPlaylist() throws RemoteException {
        return this.size();
    }

    @Override
    public Song findSongByPath(String name) throws RemoteException {

        for(Song s : this.songs)
        {
            if(s.getPath() == name)
                return s;
        }
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

        //If this index is out of the bounds of already set items
        if(index == this.size())
            throw new IndexOutOfBoundsException();

        return this.songs.get(index);
    }

    @Override
    public int size() {

        return this.songs.size();
    }

    @Override
    protected void doAdd(int index, Song element)throws IndexOutOfBoundsException {

        //If the index is out of bounds of the array
        if(index > this.songs.size())
            throw new IndexOutOfBoundsException();

        this.songs.add(index,element);

    }

    @Override
    protected Song doSet(int index, Song element)throws IndexOutOfBoundsException {

        //If the index is out of bounds of the set elements
        if(index == this.songs.size())
            throw new IndexOutOfBoundsException();

        this.songs.set(index,element);

        return element;
    }

    @Override
    protected Song doRemove(int index) throws IndexOutOfBoundsException {

        //If the index is out of bounds of the set elements
        if(index == this.songs.size())
            throw new IndexOutOfBoundsException();

        Song s = this.songs.get(index);

        this.songs.remove(index);

        return s;
    }

}
