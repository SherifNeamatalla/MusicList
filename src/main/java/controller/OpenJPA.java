package controller;

import interfaces.Playlist;
import interfaces.SerializableStrategy;
import interfaces.Song;
//import org.apache.openjpa.persistence.OpenJPAPersistence;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.IOException;
import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;

public class OpenJPA implements SerializableStrategy {
    private EntityManager e = null;
    private EntityTransaction trans ;
    private EntityManagerFactory factory = null;
    private Connection connection;


    @Override
    public void openWritableLibrary() throws IOException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            PreparedStatement pstmt = connection.prepareStatement("DROP TABLE IF EXISTS Library;");
            pstmt.executeUpdate();
            pstmt = connection.prepareStatement(" CREATE TABLE IF NOT EXISTS Library  (id long, path text, title text, album text, interpret text );");
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

//        Map<String, String> map = new HashMap<>( );
//        map.put("openjpa.ConnectionURL","jdbc:sqlite:music.db");
//        map.put("openjpa.ConnectionDriverName", "org.sqlite.JDBC");
//        map.put("openjpa.jdbc.SynchronizeMappings", "false");
//        map.put("openjpa.RuntimeUnenhancedClasses", "supported");
//        map.put("openjpa.MetaDataFactory", "jpa(Types=" + model.Song.class.getName() + ")");
//        factory = OpenJPAPersistence.getEntityManagerFactory( map );
        factory = Persistence.createEntityManagerFactory( "openjpa" );
        e = factory.createEntityManager( );
        trans = e.getTransaction();
        trans.begin();
    }

    @Override
    public void openReadableLibrary() throws IOException {
        factory = Persistence.createEntityManagerFactory( "openjpa" );
        e = factory.createEntityManager( );
        trans = e.getTransaction();
        trans.begin();

    }

    @Override
    public void openWritablePlaylist() throws IOException {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            PreparedStatement pstmt = connection.prepareStatement("DROP TABLE IF EXISTS Playlist;");
            pstmt.executeUpdate();
            pstmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Playlist (id long, path text, title text, album text, interpret text );");
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        e.persist(s);
    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public void writeLibrary(Playlist p) throws IOException {
        for (Song s : p){
            writeSong(s);
        }
    }

    @Override
    public Playlist readLibrary() throws IOException, ClassNotFoundException {
        Playlist playlist  = new model.Playlist();
        List<model.Song> list = e.createQuery("SELECT x FROM Song x").getResultList();
        for (model.Song s : list){
            model.Song x = s;
            x.setTitle(s.getTitle2());
            x.setMedia(s.getPath());
            playlist.addSong(x);
        }

        return playlist;
    }

    @Override
    public void writePlaylist(Playlist p) throws IOException {
        for (Song s : p.getList()) {
            if (s != null) {
                try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Playlist  (id,path,title,album,interpret) VALUES (?,?,?,?,?)")) {

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

    }

    @Override
    public Playlist readPlaylist() throws IOException, ClassNotFoundException {
        Playlist playlist = new model.Playlist();
        try {
            Statement stmt = connection.createStatement();
            DatabaseMetaData db = connection.getMetaData();
            ResultSet rs = db.getTables(null, null, "Playlist", null);
            if (!rs.next()) {
                return null;
            }
            rs = stmt.executeQuery("SELECT id,path,title,album,interpret FROM Playlist");
            while (rs.next()) {
                model.Song s = new model.Song();
                s.setId(rs.getInt("id"));
                s.setPath(rs.getString("path"));
                s.setTitle(rs.getString("title"));
                s.setAlbum(rs.getString("album"));
                s.setInterpret(rs.getString("interpret"));
                s.setMedia(s.getPath());

                if (s != null) {
                    playlist.addSong(Controller.getModel().getLibrary().findSongByID(s.getId()));
                }
            }
            stmt.close();
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return playlist;
    }

    @Override
    public void closeWritableLibrary() {
        trans.commit();
        if(e!=null) {
            e.close();
        }
        if (factory != null)
            factory.close();
    }

    @Override
    public void closeReadableLibrary() {
        trans.commit();
        if(e!=null) {
            e.close();
        }
        if (factory != null)
            factory.close();
    }

    @Override
    public void closeWritablePlaylist() {
        try {
            connection.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void closeReadablePlaylist() {
        try {
            connection.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}
