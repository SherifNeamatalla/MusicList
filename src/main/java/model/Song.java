package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class Song implements interfaces.Song {


    private SimpleStringProperty path ;
    private SimpleStringProperty title ;
    private SimpleStringProperty album ;
    private SimpleStringProperty interpret ;
    private long id;

    public Song(String path, String title, String album, String interpret, long id)
    {
        this.path.set(path);

        this.title.set(title);

        this.album.set(album);

        this.interpret.set(interpret);

        this.id = id;


    }


    @Override
    public String getAlbum() {

        return this.album.toString();
    }

    @Override
    public void setAlbum(String album) {

        this.album.set(album);

    }

    @Override
    public String getInterpret() {

        return this.interpret.toString();
    }

    @Override
    public void setInterpret(String interpret) {

        this.interpret.set(interpret);

    }

    @Override
    public String getPath() {

        return this.path.toString();
    }

    @Override
    public void setPath(String path) {

        this.path.set(path);

    }

    @Override
    public String getTitle() {

        return this.title.toString();
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
