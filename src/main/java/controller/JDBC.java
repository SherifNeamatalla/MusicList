package controller;

import interfaces.Playlist;
import interfaces.SerializableStrategy;
import interfaces.Song;

import java.io.IOException;
import java.sql.*;

public class JDBC implements SerializableStrategy {

    private Connection connection;
    private String table;
    private ResultSet rs = null;

    @Override
    public void openWritableLibrary() throws IOException {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            PreparedStatement pstmt = connection.prepareStatement("DROP TABLE IF EXISTS Library;");
            pstmt.executeUpdate();
            pstmt = connection.prepareStatement(" CREATE TABLE IF NOT EXISTS Library  (id long, path text, title text, album text, interpret text );");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table = "Library";


    }

    @Override
    public void openReadableLibrary() throws IOException {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openWritablePlaylist() throws IOException {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            PreparedStatement pstmt = connection.prepareStatement("DROP TABLE IF EXISTS Playlist;");
            pstmt.executeUpdate();
            pstmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Playlist (id long, path text, title text, album text, interpret text );");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table = "Playlist";

    }

    @Override
    public void openReadablePlaylist() throws IOException {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void writeSong(Song s) throws IOException {

        if (s != null) {
            try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO " + table + "  (id,path,title,album,interpret) VALUES (?,?,?,?,?)")) {

                pstmt.setInt(1, (int) s.getId());
                pstmt.setString(2, s.getPath());
                pstmt.setString(3, s.getTitle());
                pstmt.setString(4, s.getAlbum());
                pstmt.setString(5, s.getInterpret());

                pstmt.executeUpdate();


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        try {
            model.Song s = new model.Song();
            s.setId(rs.getInt("id"));
            s.setPath(rs.getString("path"));
            s.setTitle(rs.getString("title"));
            s.setAlbum(rs.getString("album"));
            s.setInterpret(rs.getString("interpret"));
            s.setMedia(s.getPath());
            return s;
        } catch (SQLException e) {
            return null;
        }

    }

    @Override
    public void writeLibrary(Playlist p) throws IOException {
        for (Song s : p.getList()) {
            this.writeSong(s);
        }

    }

    @Override
    public Playlist readLibrary() throws IOException, ClassNotFoundException {
        Playlist playlist = new model.Playlist();
        try {
            Statement stmt = connection.createStatement();
            DatabaseMetaData db = connection.getMetaData();
            rs = db.getTables(null, null, "Library", null);
            if (!rs.next()) {
                return null;
            }
            rs = stmt.executeQuery("SELECT id,path,title,album,interpret FROM Library");
            while (rs.next()) {
                Song s = readSong();
                if (s != null)
                    playlist.addSong(s);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return playlist;
    }

    @Override
    public void writePlaylist(Playlist p) throws IOException {
        for (Song s : p.getList()) {
            this.writeSong(s);
        }
    }

    @Override
    public Playlist readPlaylist() throws IOException, ClassNotFoundException {
        Playlist playlist = new model.Playlist();
        try {
            Statement stmt = connection.createStatement();
            DatabaseMetaData db = connection.getMetaData();
            rs = db.getTables(null, null, "Playlist", null);
            if (!rs.next()) {
                return null;
            }
            rs = stmt.executeQuery("SELECT id,path,title,album,interpret FROM Playlist");
            while (rs.next()) {
                Song s = readSong();
                if (s != null) {
                    playlist.addSong(Controller.getModel().getLibrary().findSongByID(s.getId()));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return playlist;
    }

    @Override
    public void closeWritableLibrary() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void closeReadableLibrary() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void closeWritablePlaylist() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeReadablePlaylist() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
