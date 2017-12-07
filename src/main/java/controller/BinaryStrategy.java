package controller;

import interfaces.Playlist;
import interfaces.SerializableStrategy;
import interfaces.Song;
import java.io.*;

public class BinaryStrategy implements SerializableStrategy{

    private FileOutputStream fos = null;
    private FileInputStream fis = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;

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
            File f = new File("Library.ser");
            if(f.exists())
            fis = new FileInputStream( "Library.ser" );
            if(fis != null)
            ois = new ObjectInputStream( this.fis );

        } catch (Exception e) {
            System.out.println("File not found");
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
            File f = new File("Playlist.ser");
            if(f.exists())
            fis = new FileInputStream( "Playlist.ser" );
            if( fis != null)
            ois = new ObjectInputStream( this.fis );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void writeSong(Song s) throws IOException {

            oos.writeObject(s);
    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        model.Song s = null;
        if(ois != null) {
            s = (model.Song) ois.readObject();
        }
        s.setMedia( s.getPath() );


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
        if (ois == null)
            return null;
        Playlist playlist = new model.Playlist();

        if (fis != null)
        {
            while (fis.available() > 100)
            {


                    Song s = this.readSong();
                    if (s != null)
                        playlist.addSong(s);


            }
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
        if (ois == null)
            return null;
            Playlist playlist = new model.Playlist();
        if(fis != null) {
            while (fis.available() > 100) {


                    Song s = this.readSong();

                    if (s != null) {
                        s = Controller.getModel().getLibrary().findSongByID(s.getId());
                        playlist.addSong(s);
                    }


            }
        }

        return playlist;
    }

    @Override
    public void closeWritableLibrary()  {

        try {
            if(oos != null) {
                oos.close();
            }
            if(fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void closeReadableLibrary() {
        try {
            if(oos != null) {
                oos.close();
            }
            if(fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeWritablePlaylist() {
        try {
            if(oos != null) {
                oos.close();
            }
            if(fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeReadablePlaylist() {
        try {
            if(oos != null) {
                oos.close();
            }
            if(fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
