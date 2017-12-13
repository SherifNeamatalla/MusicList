package model;

import interfaces.Song;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javafx.collections.ModifiableObservableListBase;


public class Playlist extends  ModifiableObservableListBase<Song> implements interfaces.Playlist,Serializable {

   private ArrayList<interfaces.Song> songs = new ArrayList<Song>();

    public Playlist() {

    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    @Override
    public boolean addSong(interfaces.Song s) throws RemoteException {

        //If this playlist doesn't already have this song
        if(this.indexOf(s) == -1)
        return add(s);

        return false;
    }

    @Override
    public boolean deleteSong(Song s) throws RemoteException {

        return remove(s);
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
        return songs.size();
    }

    @Override
    public Song findSongByPath(String name) throws RemoteException {

        for(Song s : this.songs)
        {
            if(s.getPath() .equals(name))
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
        return songs.get(index);
    }

    @Override
    public int size() {

        return this.songs.size();
    }

    @Override
    protected void doAdd(int index, Song element)throws IndexOutOfBoundsException {

       songs.add( index, element );

    }

    @Override
    protected Song doSet(int index, Song element)throws IndexOutOfBoundsException {

       return songs.set( index, element );
    }

    @Override
    protected Song doRemove(int index) throws IndexOutOfBoundsException {

        return songs.remove( index );
    }

}
