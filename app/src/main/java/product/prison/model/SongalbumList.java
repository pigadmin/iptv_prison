package product.prison.model;

public class SongalbumList {
    private int id;

    private int songTypeId;

    private String name;

    private String path;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setSongTypeId(int songTypeId) {
        this.songTypeId = songTypeId;
    }

    public int getSongTypeId() {
        return this.songTypeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

}
