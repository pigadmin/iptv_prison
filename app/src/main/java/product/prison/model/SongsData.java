package product.prison.model;

public class SongsData {
    private int id;

    private String name;

    private String autho;

    private String pic;

    private String songFile;

    private int sid;

    private int albumId;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAutho(String autho) {
        this.autho = autho;
    }

    public String getAutho() {
        return this.autho;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return this.pic;
    }

    public void setSongFile(String songFile) {
        this.songFile = songFile;
    }

    public String getSongFile() {
        return this.songFile;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getSid() {
        return this.sid;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getAlbumId() {
        return this.albumId;
    }

}
