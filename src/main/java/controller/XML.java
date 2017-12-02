package controller;

import interfaces.Playlist;
import interfaces.SerializableStrategy;
import interfaces.Song;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class XML implements SerializableStrategy {
    FileOutputStream fos = null ;
    FileInputStream fis = null;
    XMLEncoder xmlEnc = null;
    XMLDecoder xmlDec = null;



    @Override
    public void openWritableLibrary() throws IOException {
        try {
            fos = new FileOutputStream("Library.xml");
            xmlEnc = new XMLEncoder(fos);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void openReadableLibrary() throws IOException {
        try {
            File f = new File("Library.xml");
            if(f.exists())
            fis = new FileInputStream("Library.xml");
            if(fis != null)
            xmlDec = new XMLDecoder(fis);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void openWritablePlaylist() throws IOException {
        try {
            fos = new FileOutputStream( "Playlist.xml" );
            xmlEnc = new XMLEncoder( fos );
        } catch ( Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void openReadablePlaylist() throws IOException {
        try {
            File f = new File("Playlist.xml");
            if(f.exists())
            fis = new FileInputStream( "Playlist.xml" );
            if(fis != null)
            xmlDec = new XMLDecoder( fis );
        } catch ( Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void writeSong(Song s) throws IOException {
        xmlEnc.writeObject(s);
        xmlEnc.flush();
    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        model.Song s = null;

        try {
            if(xmlDec != null) {
                s = (model.Song) xmlDec.readObject();
                if(s != null)
                s.setMedia(s.getPath());
            }
        }
        catch(java.lang.ArrayIndexOutOfBoundsException e)
        {
            return null;
        }

        return s;

    }

    @Override
    public void writeLibrary(Playlist p) throws IOException {

        if(xmlEnc != null) {
            for (Song s : p.getList()) {

                this.writeSong(s);
            }
        }
    }

    @Override
    public Playlist readLibrary() throws IOException, ClassNotFoundException {
        if(xmlDec == null)
            return null;
        Playlist playlist = new model.Playlist();
        Song s;
        do {


            s = this.readSong();
            if(s != null) {

                playlist.addSong(s);
            }

        }
        while(s!= null);



        return playlist;

    }

    @Override
    public void writePlaylist(Playlist p) throws IOException {
        for( Song song : p.getList()){
            if(xmlEnc != null) {
                this.writeSong(song);
            }
        }
    }

    @Override
    public Playlist readPlaylist() throws IOException, ClassNotFoundException {
        if(xmlDec == null)
            return null;
        Playlist playlist = new model.Playlist();
        Song s;
        do {

            s = this.readSong();
            if(s != null) {

                playlist.addSong( Controller.getModel().getLibrary().findSongByID(s.getId()) );
            }
        }while( s!= null);


        return playlist;
    }

    @Override
    public void closeWritableLibrary() {
        try {
            if (xmlEnc != null) {
                xmlEnc.close();
            }
            if (fos != null) {
                fos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void closeReadableLibrary() {
        try {
            if(fis != null) {
                fis.close();
            }
            if(xmlDec != null) {
                xmlDec.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeWritablePlaylist() {
        try {
            if (xmlEnc != null) {
                xmlEnc.close();
            }
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeReadablePlaylist() {
        try {
            if(fis != null) {
                fis.close();
            }
            if(xmlDec != null) {
                xmlDec.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
