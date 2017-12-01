package controller;

import interfaces.Playlist;
import interfaces.SerializableStrategy;
import interfaces.Song;
import java.io.*;

public class BinaryStrategy implements SerializableStrategy{

    private FileOutputStream fos;
    private FileInputStream fis;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    @Override
    public void openWritableLibrary() throws IOException {
        try{
            fos = new FileOutputStream( "Library.ser" );
            oos = new ObjectOutputStream( this.fos );
        }
        catch ( Exception e ){
            e.printStackTrace();
        }
    }

    @Override
    public void openReadableLibrary() throws IOException {
        try {
            fis = new FileInputStream( "Library.ser" );
            ois = new ObjectInputStream( this.fis );
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void openWritablePlaylist() throws IOException {
        try{
            fos = new FileOutputStream( "Playlist.ser" );
            oos = new ObjectOutputStream( this.fos );
        }
        catch ( Exception e ){
            e.printStackTrace();
        }

    }

    @Override
    public void openReadablePlaylist() throws IOException {
        try {
            fis = new FileInputStream( "Playlist.ser" );
            ois = new ObjectInputStream( this.fis );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void writeSong(Song s) throws IOException {
         oos.writeObject(s);
        ((model.Song) s).writeExternal(oos);

    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {

        model.Song s = (model.Song) ois.readObject();
        s.readExternal(ois);
        return s;

    }

    @Override
    public void writeLibrary(Playlist p) throws IOException {

        for (Song i : p.getList()){
            this.writeSong(i);
            oos.reset();
        }
    }

    @Override
    public Playlist readLibrary() throws IOException, ClassNotFoundException {
        Playlist playlist = new model.Playlist();
        while(fis.available() > 100){
            Song s = this.readSong();
            playlist.addSong(s);
        }
        return playlist;

    }

    @Override
    public void writePlaylist(Playlist p) throws IOException {
        for (Song i : p.getList()){
            this.writeSong(i);
            oos.reset();
        }
    }

    @Override
    public Playlist readPlaylist() throws IOException, ClassNotFoundException {
        Playlist playlist = new model.Playlist();
        while(fis.available() > 100){
            Song s = this.readSong();
            s = Controller.getModel().getLibrary().findSongByID(s.getId());
            playlist.addSong(s);
        }
        return playlist;
    }

    @Override
    public void closeWritableLibrary()  {

        try {
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void closeReadableLibrary() {
        try {
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeWritablePlaylist() {
        try {
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeReadablePlaylist() {
        try {
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
