package controller;

import interfaces.Playlist;
import interfaces.SerializableStrategy;
import interfaces.Song;

import java.io.IOException;
import java.sql.*;

public class JDBC implements SerializableStrategy {
    private Connection con;
    private ResultSet rs ;
    @Override
    public void openWritableLibrary() throws IOException {
        try{
            con = DriverManager.getConnection("jdbc:sqlite:Library.db");
            PreparedStatement stmt = con.prepareStatement("CREATE TABLE IF NOT EXISTS Library (title text, interpret text, album text," +
                    "id long , media text) ;");
                stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void openReadableLibrary() throws IOException {
        try{
            con = DriverManager.getConnection("jdbc:sqlite:Library.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void openWritablePlaylist() throws IOException {

    }

    @Override
    public void openReadablePlaylist() throws IOException {

    }

    @Override
    public void writeSong(Song s) throws IOException {
        try (PreparedStatement stmt1 = con.prepareStatement("INSERT INTO Library (title, interpret,album,id,media)" +
                     "VALUES (?,?,?,?,?);") ){
            stmt1.setString(1,s.getTitle());
            stmt1.setString(2,s.getInterpret());
            stmt1.setString(3,s.getAlbum());
            stmt1.setLong(4,s.getId());
            stmt1.setString(5,((model.Song) s).getMedia().getSource());
            stmt1.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        Song s = new model.Song();
        try{
            s.setTitle(rs.getString("title"));
            s.setInterpret(rs.getString("interpret"));
            s.setAlbum(rs.getString("album"));
            s.setId(rs.getLong("id"));
            ((model.Song) s ).setMedia(rs.getString("media"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public void writeLibrary(Playlist p) throws IOException {
        for (Song s : p){
            writeSong(s);
        }
    }

    @Override
    public Playlist readLibrary() throws IOException, ClassNotFoundException {
        Playlist playlist = new model.Playlist();
        try{
            PreparedStatement stmt1 = con.prepareStatement("SELECT * FROM library ;");
            rs = stmt1.executeQuery();
            while(rs.next()){
                Song s = readSong();
                playlist.addSong(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return playlist;
    }

    @Override
    public void writePlaylist(Playlist p) throws IOException {

    }

    @Override
    public Playlist readPlaylist() throws IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public void closeWritableLibrary() {
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeReadableLibrary() {
        try{
            if (con != null)
                con.close();
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeWritablePlaylist() {

    }

    @Override
    public void closeReadablePlaylist() {

    }
}
