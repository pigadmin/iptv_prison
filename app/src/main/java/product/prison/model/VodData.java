package product.prison.model;

import java.io.Serializable;
import java.util.List;

public class VodData implements Serializable {
    private int id;

    private int typeId;

    private String name;

    private String act;

    private String des;

    private String icon;

    private long createDate;

    private String vv;

    private Ad ad;

    private int fileType;

    private int types;

    private double cost;

    private int position;

    private String sources;

    private List<Details> details;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return this.typeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getAct() {
        return this.act;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDes() {
        return this.des;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getCreateDate() {
        return this.createDate;
    }

    public void setVv(String vv) {
        this.vv = vv;
    }

    public String getVv() {
        return this.vv;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public Ad getAd() {
        return this.ad;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getFileType() {
        return this.fileType;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public int getTypes() {
        return this.types;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getCost() {
        return this.cost;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public String getSources() {
        return this.sources;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }

    public List<Details> getDetails() {
        return this.details;
    }

}