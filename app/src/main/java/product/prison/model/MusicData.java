package product.prison.model;

import java.util.List;

public class MusicData {
    private int id;

    private String name;

    private String icon;

    private List<SongalbumList> songalbumList;

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

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setSongalbumList(List<SongalbumList> songalbumList) {
        this.songalbumList = songalbumList;
    }

    public List<SongalbumList> getSongalbumList() {
        return this.songalbumList;
    }

}