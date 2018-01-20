package interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface Playlist extends Remote, Serializable, Iterable<Song>{
    boolean addSong(Song s) throws RemoteException;
    boolean deleteSong(Song s) throws RemoteException;
    boolean deleteSongByID(long id) throws RemoteException;
    void setList(ArrayList<Song> s) throws RemoteException;
    ArrayList<Song> getList() throws RemoteException;
    void clearPlaylist() throws RemoteException;
    int sizeOfPlaylist() throws RemoteException;
    Song findSongByPath(String name) throws RemoteException;
    Song findSongByID(long id) throws RemoteException;
}
