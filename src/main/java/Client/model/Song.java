package Client.model;

import controller.IDGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import org.apache.openjpa.persistence.Persistent;
import org.apache.openjpa.persistence.jdbc.Strategy;

import javax.persistence.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@Entity
@Table(name = "Library")
public class Song implements interfaces.Song,Externalizable  {


    @Id
    @Column(name = "id")
    private long id ;

    @Persistent
    @Strategy("helper.StringPropertyValueHandler")
    @Column(name = "path")
    private SimpleStringProperty path = new SimpleStringProperty("");

//    @Persistent
//    @Strategy("helper.StringPropertyValueHandler")
//    @Column(name = "title")
    private SimpleStringProperty title = new SimpleStringProperty("");


//Due to unknown Error
    @Persistent
    @Strategy("helper.StringPropertyValueHandler")
    @Column(name = "title")
    private SimpleStringProperty title2 = title;

    @Persistent
    @Strategy("helper.StringPropertyValueHandler")
    @Column(name = "album")
    private SimpleStringProperty album = new SimpleStringProperty("");

    @Persistent
    @Strategy("helper.StringPropertyValueHandler")
    @Column(name = "interpret")
    private SimpleStringProperty interpret = new SimpleStringProperty("");

    @Transient
    private transient Media media;



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
        //Due to title bug
        this.title2.set(title);
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

    public String getTitle2() {
        return title2.get();
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

    //Overrides the writeExternal from Externalizable
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(this.getId());
        out.writeUTF(path.get());
        out.writeUTF(title.get());
        out.writeUTF(album.get());
        out.writeUTF(interpret.get());
        out.flush();
    }

    //Overrides the readExternal from Externalizable
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setId(in.readLong());
        this.setPath((String) in.readUTF());
        this.setTitle((String) in.readUTF());
        this.setAlbum((String) in.readUTF());
        this.setInterpret((String) in.readUTF());
    }


}
