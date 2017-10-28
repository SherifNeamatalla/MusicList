package model;

public class Model {
    private Playlist library ;
    private Playlist playlist;


    public Model()
    {
        this.library = new Playlist();

        this.playlist = new Playlist();
    }

    public Playlist getLibrary() {
        return library;
    }

    public void setLibrary(Playlist library) {
        this.library = library;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }




}
