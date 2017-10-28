package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class Song implements interfaces.Song {


    private SimpleStringProperty Pfad ;
    private SimpleStringProperty Titel ;
    private SimpleStringProperty Album ;
    private SimpleStringProperty Interpreten ;

    public Song(SimpleStringProperty pfad, SimpleStringProperty titel, SimpleStringProperty album, SimpleStringProperty interpreten)
    {
        Pfad = pfad;

        Titel = titel;

        Album = album;

        Interpreten = interpreten;

    }



    @Override
    public String getAlbum() {
        return null;
    }

    @Override
    public void setAlbum(String album) {

    }

    @Override
    public String getInterpret() {
        return null;
    }

    @Override
    public void setInterpret(String interpret) {

    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void setPath(String path) {

    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long id) {

    }

    @Override
    public ObservableValue<String> pathProperty() {
        return null;
    }

    @Override
    public ObservableValue<String> albumProperty() {
        return null;
    }

    @Override
    public ObservableValue<String> interpretProperty() {
        return null;
    }

    @Override
    public ObservableValue<String> titleProperty() {
        return null;
    }
}
