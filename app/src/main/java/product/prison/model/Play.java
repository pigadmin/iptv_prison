package product.prison.model;

import java.io.Serializable;

public class Play implements Serializable {
    private int id;

    private String sname;

    private int stype;

    private int sid;

    private String surl;

    private int type;


    private Material material;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSname() {
        return this.sname;
    }

    public void setStype(int stype) {
        this.stype = stype;
    }

    public int getStype() {
        return this.stype;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getSid() {
        return this.sid;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getSurl() {
        return this.surl;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    private Sources source;

    public void setSource(Sources source) {
        this.source = source;
    }

    public Sources getSource() {
        return this.source;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return this.material;
    }

}


