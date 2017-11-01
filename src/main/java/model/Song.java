package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Song implements interfaces.Song {


    private SimpleStringProperty path ;
    private SimpleStringProperty title ;
    private SimpleStringProperty album ;
    private SimpleStringProperty interpret ;
    private long id ;
    private Media media;
    private MediaPlayer mediaPlayer;

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }





    public Song(String path, String tags, long id)// tags : (String title, String album, String interpret, long id)
    {
        this.media = new Media(path);

        this.path= new SimpleStringProperty(path);

        this.title= new SimpleStringProperty(tags.substring(3,32));

        this.interpret = new SimpleStringProperty(tags.substring(33,62));

        this.album = new SimpleStringProperty(tags.substring(63,92));

        this.id = id+1;

        mediaPlayer = new MediaPlayer(media);

    }


    @Override
    public String getAlbum() {

        return this.album.get();
    }

    @Override
    public void setAlbum(String album) {

        this.album.set(album);

    }

    @Override
    public String getInterpret() {

        return this.interpret.get();
    }

    @Override
    public void setInterpret(String interpret) {

        this.interpret.set(interpret);

    }

    @Override
    public String getPath() {

        return this.path.get();
    }

    @Override
    public void setPath(String path) {

        this.path.set(path);

    }

    @Override
    public String getTitle() {

        return this.title.get();
    }

    @Override
    public void setTitle(String title) {
        this.title.set(title);

    }

    @Override
    public long getId() {


        return this.id;
    }

    @Override
    public void setId(long id) {

        this.id = id;

    }

    @Override
    public ObservableValue<String> pathProperty() {
        return this.path;
    }

    @Override
    public ObservableValue<String> albumProperty() {
        return this.album;
    }

    @Override
    public ObservableValue<String> interpretProperty() {

        return this.interpret;
    }

    @Override
    public ObservableValue<String> titleProperty() {
        return this.title;
    }


    @Override
    public String toString()
    {
        return this.title.toString();
    }
}
