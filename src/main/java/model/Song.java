package model;

import controller.IDGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;


public class Song implements interfaces.Song,Serializable,Externalizable  {



    private transient SimpleStringProperty path = new SimpleStringProperty("") ;
    private transient SimpleStringProperty title = new SimpleStringProperty("") ;
    private transient SimpleStringProperty album = new SimpleStringProperty("");
    private transient SimpleStringProperty interpret = new SimpleStringProperty("");
    private long id ;
    private Media media;
    
    public Song(){

    }
    //Constructor for song, this constructor initiates the data of the song
    public Song(String path, String tags)// tags : (String title, String album, String interpret, long id)
    {
        if (path.substring(path.length()-3).equals("mp3")){
            if (tags.substring(0, 3).equals("TAG")){
                this.title.set(tags.substring(3,32));

                this.interpret.set(tags.substring(33,62));

                this.album.set(tags.substring(63,92));
            }
            else {
                int x = path.indexOf("songs");
                String s = path.substring(x+6 , path.length()-4);
                this.title.set(s.replace("%20", " "));
            }
            this.media = new Media(path);
            this.path.set(path);
            this.id = IDGenerator.getNextID();
        }else
            this.id = -1;
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

    public Media getMedia() {
        return media;
    }


    public void setMedia(String media) {
        this.media = new Media(media);
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
        if(title != null) {
            return this.title.toString();
        }
        return "";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(path.get());
        out.writeObject(title.get());
        out.writeObject(interpret.get());
        out.writeObject(album.get());
        out.writeObject(getMedia().getSource());
        out.writeLong(this.getId());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setPath((String) in.readObject());
        this.setTitle((String) in.readObject());
        this.setInterpret((String) in.readObject());
        this.setAlbum((String) in.readObject());
        this.setMedia((String) in.readObject());
        this.setId(in.readLong());
    }
}
